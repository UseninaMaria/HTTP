package ru.netology;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class Main {
    private static final List<String> validPaths = List.of("/index.html", "/spring.svg",
            "/spring.png", "/resources.html", "/styles.css", "/app.js", "/links.html",
            "/forms.html", "/classic.html", "/events.html", "/events.js");

    public static void main(String[] args) {
        Server server = new Server();
        for (String validPath : validPaths) {
            server.addHandler("GET", validPath, (request, responseStream) -> {
                try {
                    final Path filePath = Path.of(".", "public", request.getPath());
                    final String mimeType;
                    mimeType = Files.probeContentType(filePath);
                    final long length = Files.size(filePath);
                    responseStream.write((
                            "HTTP/1.1 200 OK\r\n" +
                                    "Content-Type: " + mimeType + "\r\n" +
                                    "Content-Length: " + length + "\r\n" +
                                    "Connection: close\r\n" +
                                    "\r\n"
                    ).getBytes());
                    Files.copy(filePath, responseStream);
                    responseStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        for (String validPath : validPaths) {
            server.addHandler("POST", validPath, (request, responseStream) -> {
                final Path filePath = Path.of(".", "public", request.getPath());
                final String mimeType;
                try {
                    mimeType = Files.probeContentType(filePath);
                    final long length = Files.size(filePath);
                    responseStream.write((
                            "HTTP/1.1 200 OK\r\n" +
                                    "Content-Type: " + mimeType + "\r\n" +
                                    "Content-Length: " + length + "\r\n" +
                                    "Connection: close\r\n" +
                                    "\r\n"
                    ).getBytes());
                    Files.copy(filePath, responseStream);
                    responseStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        server.listen(9999);
    }
}

