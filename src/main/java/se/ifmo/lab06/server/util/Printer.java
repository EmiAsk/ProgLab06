package se.ifmo.lab06.server.util;

public interface Printer {

    void print(String text);
    void printf(String format, Object... args);
}
