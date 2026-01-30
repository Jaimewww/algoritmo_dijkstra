import io.FileSelector;
import io.GraphReader;
import structures.Edge;
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

    // --- COLORES ---
    public static class ANSI {
        public static final String RESET = "\u001B[0m";
        public static final String GREEN_BOLD = "\u001B[1;32m";
        public static final String YELLOW_BOLD = "\u001B[1;33m";
        public static final String BLUE = "\u001B[34m";
        public static final String BLUE_BOLD = "\u001B[1;34m";
        public static final String CYAN = "\u001B[36m";
        public static final String RED_BOLD = "\u001B[1;31m";
        public static final String WHITE_BOLD = "\u001B[1;37m";
        public static final String MAGENTA_BOLD = "\u001B[1;35m";
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
                    ejecutarDijkstraConTabla();
                    break;
                case 3:
                    mostrarTablaGeneralConexiones();
                    break;
                case 4:
                    System.out.println(ANSI.BLUE_BOLD + "Saliendo del sistema..." + ANSI.RESET);
                    running = false;
                    break;
                default:
                    System.out.println(ANSI.MAGENTA_BOLD + "Opción no válida." + ANSI.RESET);
            }
        }
        scanner.close();
    }

    // --- MENÚ PRINCIPAL ---
    private static void printMenu() {
        System.out.println("\n" + ANSI.BLUE_BOLD + "============================================================" + ANSI.RESET);
        System.out.println(ANSI.BLUE_BOLD + "          TALLER 10: ALGORITMO DE DIJKSTRA" + ANSI.RESET);
        System.out.println(ANSI.BLUE_BOLD + "============================================================" + ANSI.RESET);

        if (currentGraph != null) {
            System.out.println(" " + ANSI.GREEN_BOLD + "ESTADO: Grafo cargado (" + currentGraph.getNodes() + " nodos)." + ANSI.RESET);
        } else {
            System.out.println(" " + ANSI.YELLOW_BOLD + "ESTADO: Sin grafo." + ANSI.RESET);
        }

        System.out.println(ANSI.BLUE_BOLD + "------------------------------------------------------------" + ANSI.RESET);
        System.out.println(ANSI.WHITE_BOLD + " 1. " + ANSI.RESET + "CARGAR GRAFO");
        System.out.println(ANSI.WHITE_BOLD + " 2. " + ANSI.RESET + "CALCULAR RUTA");
        System.out.println(ANSI.WHITE_BOLD + " 3. " + ANSI.RESET + "VER TABLA GENERAL");
        System.out.println(ANSI.WHITE_BOLD + " 4. " + ANSI.RESET + "SALIR");
        System.out.println(ANSI.BLUE_BOLD + "============================================================" + ANSI.RESET);
    }

    // --- LÓGICA ---

    private static void cargarGrafoInteractivo(Path carpeta) {
        try {
            System.out.println(ANSI.BLUE + "--- CARGAR NUEVO DATASET ---" + ANSI.RESET);
            FileSelector selector = new FileSelector();
            Path archivoSeleccionado = selector.escogerArchivoDeCarpeta(carpeta);
            System.out.println("Leyendo archivo: " + ANSI.YELLOW_BOLD + archivoSeleccionado.getFileName() + ANSI.RESET + "...");

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

    private static void ejecutarDijkstraConTabla() {
        if (currentGraph == null) {
            System.out.println(ANSI.MAGENTA_BOLD + "Error: Cargue un grafo primero." + ANSI.RESET);
            return;
        }

        System.out.println(ANSI.BLUE + "--- CONFIGURAR RUTA ---" + ANSI.RESET);
        int maxNode = currentGraph.getNodes() - 1;

        int inputSource = getIntInput(">> Ingrese Nodo Origen (0-" + maxNode + "): ");
        if (inputSource < 0 || inputSource > maxNode) {
            System.out.println(ANSI.RED_BOLD + "Error: Nodo origen inválido." + ANSI.RESET);
            return;
        }

        int inputDest = getIntInput(">> Ingrese Nodo Destino (0-" + maxNode + "): ");
        if (inputDest < 0 || inputDest > maxNode) {
            System.out.println(ANSI.RED_BOLD + "Error: Nodo destino inválido." + ANSI.RESET);
            return;
        }

        System.out.print("Procesando... ");
        dijkstraSolver.execute(currentGraph.getAdjacencyList(), inputSource);

        sourceNode = inputSource;
        isDijkstraExecuted = true;
        System.out.println(ANSI.GREEN_BOLD + "Completado." + ANSI.RESET);
        mostrarTablaDeResultadosCalculados();
        mostrarCamino(inputDest);

        System.out.println(ANSI.CYAN + "\nRegresando al menú principal..." + ANSI.RESET);
    }

       private static void calcularRutaPuntoAPunto() {
        if (currentGraph == null) {
            System.out.println(ANSI.MAGENTA_BOLD + "Error: Primero cargue un grafo." + ANSI.RESET);
            return;
        }

        System.out.println(ANSI.BLUE + "--- CÁLCULO RÁPIDO ---" + ANSI.RESET);
        int maxNode = currentGraph.getNodes() - 1;

        int origen = getIntInput(">> Origen: ");
        if (origen < 0 || origen > maxNode) { System.out.println(ANSI.RED_BOLD + "Inválido." + ANSI.RESET); return; }

        int destino = getIntInput(">> Destino: ");
        if (destino < 0 || destino > maxNode) { System.out.println(ANSI.RED_BOLD + "Inválido." + ANSI.RESET); return; }

        dijkstraSolver.execute(currentGraph.getAdjacencyList(), origen);
        sourceNode = origen;
        isDijkstraExecuted = true;

        mostrarCamino(destino);
    }

    private static void mostrarTablaGeneralConexiones() {
        if (currentGraph == null) {
            System.out.println(ANSI.MAGENTA_BOLD + "Error: No hay grafo cargado." + ANSI.RESET);
            return;
        }

        System.out.println("\n" + ANSI.CYAN + "--- TABLA GENERAL DEL GRAFO (Topología) ---" + ANSI.RESET);
        System.out.printf("%-10s %-40s%n", "Nodo", "Conexiones [Destino : Peso]");
        System.out.println("-------------------------------------------------------");

        List<List<Edge>> lista = currentGraph.getAdjacencyList();

        for (int i = 0; i < lista.size(); i++) {
            List<Edge> adj = lista.get(i);
            StringBuilder sb = new StringBuilder();

            if (adj.isEmpty()) {
                sb.append(ANSI.RED_BOLD).append("Sin conexiones salientes").append(ANSI.RESET);
            } else {
                for (Edge e : adj) {
                    sb.append("[")
                            .append(ANSI.YELLOW_BOLD).append(e.getDestination()).append(ANSI.RESET)
                            .append(" : ")
                            .append(ANSI.GREEN_BOLD).append(e.getWeight()).append(ANSI.RESET)
                            .append("]  ");
                }
            }
            System.out.printf("Nodo %-5d %-40s%n", i, sb.toString());
        }
        System.out.println("-------------------------------------------------------");
    }

    // --- MÉTODOS AUXILIARES ---

    private static void mostrarTablaDeResultadosCalculados() {
        System.out.println("\n" + ANSI.CYAN + "RESULTADOS CALCULADOS DESDE NODO " + sourceNode + ":" + ANSI.RESET);
        System.out.printf("%-10s %-15s %-15s%n", "Destino", "Costo Acumulado", "Estado");
        System.out.println("-------------------------------------------");

        int[] distancias = dijkstraSolver.getDistances();
        for (int i = 0; i < distancias.length; i++) {
            String costoStr = (distancias[i] == Integer.MAX_VALUE) ? "INF" : String.valueOf(distancias[i]);
            String estado = (distancias[i] == Integer.MAX_VALUE) ? ANSI.RED_BOLD + "Inalcanzable" + ANSI.RESET : "Accesible";
            if (i == sourceNode) estado = ANSI.CYAN + "Origen" + ANSI.RESET;

            System.out.printf("Nodo %-5d %-15s %-15s%n", i, costoStr, estado);
        }
        System.out.println("-------------------------------------------");
    }

    private static void mostrarCamino(int target) {
        int[] distancias = dijkstraSolver.getDistances();

        if (target >= distancias.length || target < 0) {
            System.out.println(ANSI.RED_BOLD + "Nodo inválido." + ANSI.RESET);
            return;
        }

        int costo = distancias[target];

        System.out.println("\n" + ANSI.BLUE + "--- RUTA SELECCIONADA ---" + ANSI.RESET);
        if (costo == Integer.MAX_VALUE) {
            System.out.println(ANSI.RED_BOLD + "No hay camino posible de " + sourceNode + " a " + target + "." + ANSI.RESET);
        } else {
            List<Integer> path = dijkstraSolver.getShortestPath(target, dijkstraSolver.getPredecessors());

            System.out.println("Costo Total: " + ANSI.GREEN_BOLD + costo + ANSI.RESET);
            System.out.print("Ruta: ");
            for (int i = 0; i < path.size(); i++) {
                System.out.print(ANSI.YELLOW_BOLD + path.get(i) + ANSI.RESET);
                if (i < path.size() - 1) System.out.print(" -> ");
            }
            System.out.println();
        }
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