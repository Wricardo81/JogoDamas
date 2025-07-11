package util;

import java.util.Scanner;

public class ConsoleInput {
    private static Scanner scanner = new Scanner(System.in);

    public static String lerString() {
        return scanner.nextLine();
    }

    public static int lerInt() {
        while (!scanner.hasNextInt()) {
            System.out.println("Entrada inválida. Por favor, digite um número inteiro.");
            scanner.next(); // Consumir a entrada inválida
        }
        int num = scanner.nextInt();
        scanner.nextLine(); // Consumir a nova linha restante
        return num;
    }

    public static void fecharScanner() {
        if (scanner != null) {
            scanner.close();
        }
    }
}