package util;
import structures.Edge;
import java.util.*;


public class DijkstraAlgorithm {
    // Implementa la lógica de "relajación"
    public void execute(List<List<Edge>> graph, int source) {
        // Estructuras obligatorias por la guía
        int[] distances = new int[graph.size()];
        boolean[] visited = new boolean[graph.size()];
        int[] predecessors = new int[graph.size()];

        // Firma sugerida para el proceso principal
        // PriorityQueue<Node> pq = ...
    }

    public List<Integer> getShortestPath(int target, int[] predecessors) {
        // Lógica para retroceder desde el destino al origen
        // TODO: Implementar la lógica para obtener el camino más corto
        return new ArrayList<>();
    }
}
