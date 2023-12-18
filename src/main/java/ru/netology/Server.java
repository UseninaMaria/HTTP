package ru.netology;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private final Map<String, Map<String, Handler>> handlers = new HashMap<>();


    public void addHandler(String method, String path, Handler handler) {
        handlers.computeIfAbsent(method, k -> new HashMap<>()).put(path, handler);
    }

    private Request parseRequest(BufferedReader in) throws IOException {

        String requestLine = in.readLine();

        if (requestLine == null) {
            in.close();
            return null;
        }

        String[] parts = requestLine.split(" ");

        String method = parts[0];
        String path = parts[1];
        String protocol = parts[2];

        Map<String, String> headers = new HashMap<>();
        headers = parseHeaders(in);

        if (parts.length == 3) {
            Request request = new Request(method, path, protocol, headers);
            return request;
        } else {
            InputStream body = parseBody(in);
            Request request = new Request(method, path, protocol, headers, body);
            return request;
        }
    }

    public static Map<String, String> parseHeaders(BufferedReader in) throws IOException {
        Map<String, String> headers = new HashMap<>();
        String headerLine;
        while ((headerLine = in.readLine()) != null && !headerLine.isEmpty()) {
            String[] headerParts = headerLine.split(": ");
            if (headerParts.length == 2) {
                headers.put(headerParts[0], headerParts[1]);
            }
        }
        return headers;
    }

    public static InputStream parseBody(BufferedReader in) throws IOException {
        String separator = "\r\n\r\n";
        StringBuilder requestBody = new StringBuilder();
        String line;
        boolean bodyStarted = false;
        while ((line = in.readLine()) != null) {
            if (bodyStarted) {
                requestBody.append(line).append("\n");
            } else if (line.equals(separator)) {
                bodyStarted = true;
            }
        }

        String requestBodyString = requestBody.toString();
        byte[] requestBodyBytes = requestBodyString.getBytes();
        InputStream body = new ByteArrayInputStream(requestBodyBytes);
        return body;
    }

    public void listen(int port) {
        System.out.println("Server running");
        try (final ServerSocket serverSocket = new ServerSocket(port)) {
            ExecutorService threadPool = Executors.newFixedThreadPool(64);
            while (true) {
                try {
                    final Socket socket = serverSocket.accept();
                    threadPool.execute(() -> handleConnection(socket));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleConnection(Socket socket) {
        try (
                final BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                final BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream())
        ) {

            Request request = parseRequest(in);
            Handler handler = handlers.getOrDefault(request.getMethod(), Collections.emptyMap()).get(request.getPath());

            if (handler != null) {
                handler.handle(request, out);
            } else {
                badRequest(out);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void badRequest(BufferedOutputStream out) throws IOException {
        out.write((
                "HTTP/1.1 400 Bad Request\r\n" +
                        "Content-Length: 0\r\n" +
                        "Connection: close\r\n" +
                        "\r\n"
        ).getBytes());
        out.flush();
    }
}
