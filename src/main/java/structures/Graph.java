package structures;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa un grafo ponderado utilizando una lista de adyacencia
 * @author Jaime Landázuri
 */

public class Graph {
    private final int nodes;
    private final List<List<Edge>> adjacencyList;
    private final boolean isDirected; // tipo de grafo (Dirigido, No dirigido)

    public Graph(int nodes, boolean isDirected) {
        this.nodes = nodes;
        this.isDirected = isDirected;
        this.adjacencyList = new ArrayList<>();
        for (int i = 0; i < nodes; i++) {
            this.adjacencyList.add(new ArrayList<>());
        }
    }

    public void addEdge(int source, int destination, int weight) {
        // Siempre se agrega la arista de origen a destino
        this.adjacencyList.get(source).add(new Edge(destination, weight));

        // Si NO es dirigido, se agrega la arista inversa con el mismo peso
        if (!isDirected) {
            this.adjacencyList.get(destination).add(new Edge(source, weight));
        }
    }

    public int getNodes() {
        return nodes;
    }

    public List<List<Edge>> getAdjacencyList() {
        return adjacencyList;
    }

    public boolean isDirected() {
        return isDirected;
    }
}