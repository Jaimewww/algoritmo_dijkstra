package io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Clase encargada de leer una matriz desde un archivo utilizando Streams
 */
public class MatrixReader {

    //Metodo para cargar el archivo
    public static int[][] cargar(String archivo) throws IOException {
        int cantidadDatos = 0;
        List<String> lineas = Files.lines(Path.of(archivo))
                .map(String::trim)
                .filter(l -> !l.isEmpty())   // elimina líneas en blanco
                .toList();

        // Validamos que el archivo no este vacío
        if (lineas.isEmpty()) {
            throw new IllegalArgumentException("Error: El archivo está vacío o no tiene datos válidos.");
        }

        //Validamos que el archivo tenga al menos 2 hijos validos
        if(lineas.size() < 2) {
            throw new IllegalArgumentException("Error: El archivo no tiene los suficientes datos");
        }

        //Asignamos la primera linea de la matriz a una variable para comprobar que existan esa misma cantidad de filas
        try {
            cantidadDatos = Integer.parseInt(lineas.get(0));
        }catch (NumberFormatException e) {
            throw new NumberFormatException("La primera linea de el txt debe de ser un numero entero ya que va a describir la cantidad de nodos");
        }

        if(cantidadDatos != lineas.size() -1) {
            throw new NumberFormatException("La cantidad de nodos no coinciden ya que se esperan "+ cantidadDatos+ " y existen: "+ (lineas.size()-1));
        }

        int[][] matriz = new int[cantidadDatos][4];
        for(int i = 0; i < cantidadDatos; i++) {
            String[] IDnodos = lineas.get(i+1).split(";");
            if(IDnodos.length != 4) {
                throw new IllegalArgumentException("Fila: " + i +2 + " invalida, se espera 4 columnas en la fila: ");
            }

            for(int j = 0; j < 4; j++) {
                try {
                    matriz[i][j] = Integer.parseInt(IDnodos[j].trim());
                }catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Valor inválido en fila " + (i + 2) + ", columna " + (j + 1));
                }
            }
        }
        return matriz;
    }
}
