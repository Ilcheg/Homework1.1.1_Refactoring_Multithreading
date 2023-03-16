package ru.netology;

import java.util.List;

public class Main {
    private static final int PORT = 9999;

    public static void main(String[] args) {
        Server server = new Server();
        Handler handler = new HandlerImpl();
        final List<String> validPaths = List.of("/index.html", "/spring.svg", "/spring.png", "/resources.html", "/styles.css", "/app.js", "/links.html", "/forms.html", "/classic.html", "/events.html", "/events.js");
        for (String path : validPaths) {
            server.addHandler("GET", path, handler);
            server.addHandler("POST", path, handler);
        }
        server.start(PORT);
    }
}



