package io;

import structures.Graph;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class GraphReader {

    /**
     * Carga un grafo desde un archivo de texto.
     * @param archivo Ruta del archivo en formato String.
     * @return Objeto Graph poblado.
     * @throws IOException Si hay error de lectura.
     */
    public static Graph cargar(String archivo) throws IOException {
        List<String> lineas = Files.readAllLines(Path.of(archivo));
        lineas.removeIf(String::isBlank);
        if (lineas.isEmpty()) {
            throw new IllegalArgumentException("El archivo está vacío.");
        }
        String primeraLinea = lineas.get(0).trim();
        String[] cabecera = primeraLinea.split("\\s+"); // Separa por cualquier espacio en blanco

        int numNodos;
        try {
            numNodos = Integer.parseInt(cabecera[0]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("La primera línea debe contener el número de nodos.");
        }

        // true = Dirigido (Importante para Dijkstra, ya que A->B no implica B->A con el mismo costo)
        Graph grafo = new Graph(numNodos, true);

        // 3. Leer las aristas (Desde la segunda línea en adelante)
        for (int i = 1; i < lineas.size(); i++) {
            String linea = lineas.get(i).trim();
            String[] partes = linea.split("\\s+");

            // Validar que la línea tenga 3 partes: origen, destino, peso
            if (partes.length < 3) {
                System.err.println("Advertencia: Línea " + (i + 1) + " ignorada (formato incorrecto): " + linea);
                continue;
            }

            try {
                int origen = Integer.parseInt(partes[0]);
                int destino = Integer.parseInt(partes[1]);
                int peso = Integer.parseInt(partes[2]);

                if (origen < 0 || origen >= numNodos || destino < 0 || destino >= numNodos) {
                    throw new IllegalArgumentException("Nodo fuera de rango en línea " + (i + 1));
                }

                grafo.addEdge(origen, destino, peso);

            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Error de formato numérico en línea " + (i + 1));
            }
        }

        return grafo;
    }
}