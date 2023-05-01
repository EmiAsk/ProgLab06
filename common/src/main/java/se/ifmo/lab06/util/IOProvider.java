package se.ifmo.lab06.util;

import java.util.Scanner;

public class IOProvider {
    Printer printer;
    Scanner scanner;

    public IOProvider(Scanner scanner, Printer printer) {
        this.scanner = scanner;
        this.printer = printer;
    }

    public Printer getPrinter() {
        return printer;
    }

    public Scanner getScanner() {
        return scanner;
    }

    public void closeScanner() {scanner.close();}
}
