
import io.FileSelector;
import io.GraphReader;
import structures.Graph;
import util.DijkstraAlgorithm;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static Graph currentGraph = null;
    private static DijkstraAlgorithm dijkstraSolver = new DijkstraAlgorithm();
    private static int sourceNode = -1;
    private static boolean isDijkstraExecuted = false;

    private static final Scanner scanner = new Scanner(System.in);

    // Colores para la consola
    public static class ANSI {
        public static final String RESET = "\u001B[0m";
        public static final String GREEN = "\u001B[32m";
        public static final String GREEN_BOLD = "\u001B[1;32m";
        public static final String YELLOW = "\u001B[33m";
        public static final String YELLOW_BOLD = "\u001B[1;33m";
        public static final String BLUE = "\u001B[34m";
        public static final String BLUE_BOLD = "\u001B[1;34m";
        public static final String CYAN = "\u001B[36m";
        public static final String MAGENTA_BOLD = "\u001B[1;35m";
        public static final String RED_BOLD = "\u001B[1;31m";
        public static final String WHITE_BOLD = "\u001B[1;37m";
    }

    public static void main(String[] args) {
        boolean running = true;
        Path rutaDatasets = Paths.get(".");

        while (running) {
            printMenu();
            int option = getIntInput(ANSI.YELLOW_BOLD + ">> Seleccione una opción: " + ANSI.RESET);

            System.out.println();

            switch (option) {
                case 1:
                    cargarGrafoInteractivo(rutaDatasets);
                    break;
                case 2:
                    ejecutarDijkstraInteractivo();
                    break;
                case 3:
                    buscarCaminoMinimo();
                    break;
                case 4:
                    mostrarTodasLasDistancias();
                    break;
                case 5:
                    mostrarInformacionGrafo();
                    break;
                case 6:
                    System.out.println(ANSI.BLUE_BOLD + "Saliendo del sistema..." + ANSI.RESET);
                    running = false;
                    break;
                default:
                    System.out.println(ANSI.MAGENTA_BOLD + "Opción no válida." + ANSI.RESET);
            }
        }
        scanner.close();
    }

    // --- MÉTODOS VISUALES ---
    private static void printMenu() {
        System.out.println("\n" + ANSI.BLUE_BOLD + "============================================================" + ANSI.RESET);
        System.out.println(ANSI.BLUE_BOLD + "          TALLER 10: ALGORITMO DE DIJKSTRA" + ANSI.RESET);
        System.out.println(ANSI.BLUE_BOLD + "============================================================" + ANSI.RESET);

        if (currentGraph != null) {
            System.out.println(" " + ANSI.GREEN_BOLD + "ESTADO: Grafo cargado." + ANSI.RESET);
            System.out.println("    " + ANSI.GREEN + "Nodos: " + currentGraph.getNodes() + ANSI.RESET);
        } else {
            System.out.println(" " + ANSI.YELLOW_BOLD + "ESTADO: Sin grafo." + ANSI.RESET);
        }

        if (isDijkstraExecuted) {
            System.out.println(" " + ANSI.CYAN + "DIJKSTRA: Ejecutado (Origen: " + sourceNode + ")" + ANSI.RESET);
        } else {
            System.out.println(" " + ANSI.YELLOW + "DIJKSTRA: Pendiente." + ANSI.RESET);
        }

        System.out.println(ANSI.BLUE_BOLD + "------------------------------------------------------------" + ANSI.RESET);
        System.out.println(ANSI.WHITE_BOLD + " 1. " + ANSI.RESET + "CARGAR GRAFO");
        System.out.println(ANSI.WHITE_BOLD + " 2. " + ANSI.RESET + "EJECUTAR DIJKSTRA");
        System.out.println(ANSI.WHITE_BOLD + " 3. " + ANSI.RESET + "CONSULTAR CAMINO");
        System.out.println(ANSI.WHITE_BOLD + " 4. " + ANSI.RESET + "VER DISTANCIAS");
        System.out.println(ANSI.WHITE_BOLD + " 5. " + ANSI.RESET + "VER INFO GRAFO");
        System.out.println(ANSI.WHITE_BOLD + " 6. " + ANSI.RESET + "SALIR");
        System.out.println(ANSI.BLUE_BOLD + "============================================================" + ANSI.RESET);
    }

    // --- LÓGICA ---

    private static void cargarGrafoInteractivo(Path carpeta) {
        try {
            System.out.println(ANSI.BLUE + "--- CARGAR NUEVO DATASET ---" + ANSI.RESET);

            FileSelector selector = new FileSelector();
            Path archivoSeleccionado = selector.escogerArchivoDeCarpeta(carpeta);

            System.out.println("Leyendo archivo: " + ANSI.YELLOW + archivoSeleccionado.getFileName() + ANSI.RESET + "...");

            currentGraph = GraphReader.cargar(archivoSeleccionado.toString());

            isDijkstraExecuted = false;
            sourceNode = -1;
            dijkstraSolver = new DijkstraAlgorithm();

            System.out.println(ANSI.GREEN_BOLD + "¡Grafo cargado correctamente!" + ANSI.RESET);

        } catch (IOException e) {
            System.err.println(ANSI.RED_BOLD + "Error de lectura: " + e.getMessage() + ANSI.RESET);
        } catch (Exception e) {
            System.err.println(ANSI.RED_BOLD + "Error: " + e.getMessage() + ANSI.RESET);
        }
    }

    private static void ejecutarDijkstraInteractivo() {
        if (currentGraph == null) {
            System.out.println(ANSI.MAGENTA_BOLD + "Error: Cargue un grafo primero." + ANSI.RESET);
            return;
        }

        System.out.println(ANSI.BLUE + "--- CONFIGURACIÓN DE DIJKSTRA ---" + ANSI.RESET);
        int maxNode = currentGraph.getNodes() - 1;

        int inputSource = getIntInput(">> Ingrese Nodo Origen (0-" + maxNode + "): ");

        if (inputSource < 0 || inputSource > maxNode) {
            System.out.println(ANSI.RED_BOLD + "Error: Nodo fuera de rango." + ANSI.RESET);
            return;
        }

        long startTime = System.nanoTime();
        dijkstraSolver.execute(currentGraph.getAdjacencyList(), inputSource);
        long endTime = System.nanoTime();

        System.out.println(ANSI.GREEN_BOLD + "Calculado en " + (endTime - startTime) / 1000 + " µs." + ANSI.RESET);

        sourceNode = inputSource;
        isDijkstraExecuted = true;
    }

    private static void buscarCaminoMinimo() {
        if (!validarEjecucionPrevia()) return;

        System.out.println(ANSI.BLUE + "--- CONSULTA DE RUTA ---" + ANSI.RESET);
        int target = getIntInput(">> Ingrese Nodo Destino: ");

        if (target < 0 || target >= currentGraph.getNodes()) {
            System.out.println(ANSI.RED_BOLD + "Error: Nodo destino no existe." + ANSI.RESET);
            return;
        }

        int[] distancias = dijkstraSolver.getDistances();
        int distanciaTotal = distancias[target];

        if (distanciaTotal == Integer.MAX_VALUE) {
            System.out.println(ANSI.RED_BOLD + "No hay camino de " + sourceNode + " a " + target + "." + ANSI.RESET);
        } else {
            List<Integer> path = dijkstraSolver.getShortestPath(target, dijkstraSolver.getPredecessors());

            System.out.println("Costo: " + ANSI.GREEN_BOLD + distanciaTotal + ANSI.RESET);
            System.out.print("Ruta: ");
            for (int i = 0; i < path.size(); i++) {
                System.out.print(ANSI.YELLOW_BOLD + path.get(i) + ANSI.RESET);
                if (i < path.size() - 1) System.out.print(" -> ");
            }
            System.out.println();
        }
    }

    private static void mostrarTodasLasDistancias() {
        if (!validarEjecucionPrevia()) return;

        System.out.println(ANSI.BLUE + "--- DISTANCIAS DESDE NODO " + sourceNode + " ---" + ANSI.RESET);
        System.out.printf("%-10s %-10s %-15s%n", "Destino", "Costo", "Estado");
        System.out.println("-------------------------------------");

        int[] distancias = dijkstraSolver.getDistances();
        for (int i = 0; i < distancias.length; i++) {
            String costoStr = (distancias[i] == Integer.MAX_VALUE) ? "INF" : String.valueOf(distancias[i]);
            String estado = (distancias[i] == Integer.MAX_VALUE) ? ANSI.RED_BOLD + "Inalcanzable" + ANSI.RESET : ANSI.GREEN + "Accesible" + ANSI.RESET;
            if (i == sourceNode) estado = ANSI.CYAN + "Origen" + ANSI.RESET;

            System.out.printf("Nodo %-5d %-10s %-15s%n", i, costoStr, estado);
        }
    }

    private static void mostrarInformacionGrafo() {
        if (currentGraph == null) {
            System.out.println(ANSI.MAGENTA_BOLD + "No hay grafo cargado." + ANSI.RESET);
            return;
        }
        System.out.println(ANSI.BLUE_BOLD + "--- INFO DEL GRAFO ---" + ANSI.RESET);
        System.out.println("Nodos: " + currentGraph.getNodes());
        System.out.println("Tipo: " + (currentGraph.isDirected() ? "Dirigido" : "No Dirigido"));

        var adj = currentGraph.getAdjacencyList();
        int edgesCount = 0;
        for (var list : adj) edgesCount += list.size();
        System.out.println("Aristas: " + edgesCount);
    }

    private static boolean validarEjecucionPrevia() {
        if (currentGraph == null) {
            System.out.println(ANSI.MAGENTA_BOLD + "Error: Primero cargue un grafo." + ANSI.RESET);
            return false;
        }
        if (!isDijkstraExecuted) {
            System.out.println(ANSI.MAGENTA_BOLD + "Error: Ejecute Dijkstra primero." + ANSI.RESET);
            return false;
        }
        return true;
    }

    private static int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.println(ANSI.MAGENTA_BOLD + "Entrada inválida." + ANSI.RESET);
            scanner.next();
            System.out.print(prompt);
        }
        int res = scanner.nextInt();
        scanner.nextLine();
        return res;
    }
}