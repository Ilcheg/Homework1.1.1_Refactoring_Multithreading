package ru.netology;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

public class HandlerImpl implements Handler {
    @Override
    public void handle(Request request, OutputStream responseStream) {
        String path = request.getPath();
        final var filePath = Path.of(".", "public", path);
        final String mimeType;
        byte[] content;
        try {
            mimeType = Files.probeContentType(filePath);
            try {
                final String template = Files.readString(filePath);
                content = template.replace("{time}", LocalDateTime.now().toString()).getBytes();
            } catch (IOException e) {
                content = Files.readAllBytes(filePath);
            }

            responseStream.write((
                    "HTTP/1.1 200 OK\r\n" +
                            "Content-Type: " + mimeType + "\r\n" +
                            "Content-Length: " + content.length + "\r\n" +
                            "Connection: close\r\n" +
                            "\r\n"
            ).getBytes());
            responseStream.write(content);
            responseStream.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
