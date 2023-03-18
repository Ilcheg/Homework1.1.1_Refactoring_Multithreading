package ru.netology;

import java.io.OutputStream;
@FunctionalInterface
public interface Handler {
    void handle(Request request, OutputStream responseStream);
}
