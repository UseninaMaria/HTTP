package ru.netology;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        final Server server = new Server();

        server.addHandler("GET", "/messages", new Handler() {
            public void handle(Request request, BufferedOutputStream responseStream) {
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
            }
        });

        server.addHandler("POST", "/messages", new Handler() {
                    public void handle(Request request, BufferedOutputStream responseStream) {
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
                    }
                }
        );

        server.listen(9999);
    }
}

