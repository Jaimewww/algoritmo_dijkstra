package io;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *Clase para establecer la ruta segun el archivo que el usuario escoja
 * */
public class FileSelector {

    public Path escogerArchivoDeCarpeta(Path carpeta) throws IOException {
        // lista solo archivos .txt
        List<Path> archivos;
        try (Stream<Path> stream = Files.list(carpeta)) {
            archivos = stream
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".txt"))
                    .collect(Collectors.toList());
        }

        if (archivos.isEmpty()) {
            throw new IOException("No hay archivos .txt en la carpeta: " + carpeta);
        }

        System.out.println("=========== DATASETS DISPONIBLES ===========");

        for (int i = 0; i < archivos.size(); i++) {
            System.out.println(i + " -> " + archivos.get(i).getFileName());
        }

        System.out.println("============================================");

        Scanner sc = new Scanner(System.in);
        int opcion;

        while (true) {
            System.out.print("Seleccione el número del archivo: ");

            // Validamos que se ingrese un numero entero
            if (!sc.hasNextInt()) {
                System.out.println("❌ Error: Debe ingresar un número.");
                sc.next(); // limpiamos las entrada incorrecta
                continue;
            }

            opcion = sc.nextInt();

            // Validamos que este en el rango permitido
            if (opcion < 0 || opcion >= archivos.size()) {
                System.out.println("❌ Opción inválida. Intente de nuevo.");
            } else {
                break;
            }
        }
        return archivos.get(opcion);
    }
}
