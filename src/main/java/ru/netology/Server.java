package ru.netology;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public final int NUMBER_THREADS = 64;
    private final ExecutorService threadPool;
    private final Map<String, Map<String, Handler>> handlers;

    public Server() {
        threadPool = Executors.newFixedThreadPool(NUMBER_THREADS);
        handlers = new HashMap<>();
    }

    public void start(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                final Socket socket = serverSocket.accept();
                threadPool.execute(() -> connectionHanding(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
        }
    }

    private void connectionHanding(Socket socket) {
        try (final InputStream is = socket.getInputStream();
             final BufferedReader in = new BufferedReader(new InputStreamReader(is));
             final OutputStream out = new BufferedOutputStream(socket.getOutputStream())) {
            final String requestLine = in.readLine();
            System.out.println(requestLine);
            final String[] parts = requestLine.split(" ");

            String method = parts[0];
            String path = parts[1];
            Request request = new Request(method, path, in);
            System.out.print("handled\n");
            handleRequest(request, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleRequest(Request request, OutputStream responseStream) throws IOException {
        boolean founded = false;
        Map<String, Handler> methodHandlers = handlers.getOrDefault(request.getMethod(), null);
        if (methodHandlers != null) {
            Handler handler = methodHandlers.getOrDefault(request.getPath(), null);
            if (handler != null) {
                founded = true;
                handler.handle(request, responseStream);
            }
        }
        if (!founded) {
            handlerNotFound(responseStream);
        }
    }

    private void handlerNotFound(OutputStream responseStream) throws IOException {
        responseStream.write((
                "HTTP/1.1 404 Not Found\r\n" +
                        "Content-Length: 0\r\n" +
                        "Connection: close\r\n" +
                        "\r\n"
        ).getBytes());
        responseStream.flush();
    }

    public void addHandler(String method, String path, Handler handler) {
        Map<String, Handler> methodHandlers;
        if (handlers.containsKey(method)) {
            methodHandlers = handlers.get(method);
        } else {
            methodHandlers = new HashMap<>();
            handlers.put(method, methodHandlers);
        }
        methodHandlers.put(path, handler);
    }
}
