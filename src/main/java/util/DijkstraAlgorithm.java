package util;
import structures.Edge;
import java.util.*;

public class DijkstraAlgorithm {
    private static final int INF = Integer.MAX_VALUE;
    private int[] distances;
    private int[] predecessors;

    // Ejecuta Dijkstra y guarda distances y predecessors en campos de la clase
    public void execute(List<List<Edge>> graph, int source) {
        int n = graph.size();
        distances = new int[n];
        boolean[] visited = new boolean[n];
        predecessors = new int[n];

        Arrays.fill(distances, INF);
        Arrays.fill(predecessors, -1);

        distances[source] = 0;

        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.offer(new Node(source, 0));

        while (!pq.isEmpty()) {
            Node current = pq.poll();
            int u = current.vertex;

            if (visited[u]) continue;
            visited[u] = true;

            List<Edge> neighbors = graph.get(u);
            if (neighbors == null) continue;

            for (Edge e : neighbors) {
                int v = e.getDestination();
                int weight = e.getWeight();

                if (weight < 0) continue; // Dijkstra no soporta pesos negativos

                if (!visited[v] && distances[u] != INF && distances[u] + weight < distances[v]) {
                    distances[v] = distances[u] + weight;
                    predecessors[v] = u;
                    pq.offer(new Node(v, distances[v]));
                }
            }
        }
    }

    // Reconstruye el camino más corto desde el origen (implicado en predecessors) hasta target.
    // Si distances está disponible y target es inalcanzable, devuelve lista vacía.
    public List<Integer> getShortestPath(int target, int[] predecessors) {
        if (predecessors == null) return new ArrayList<>();

        // Si tenemos distances calculadas en el objeto, comprobar inalcanzable
        if (this.distances != null && target >= 0 && target < this.distances.length
                && this.distances[target] == INF) {
            return new ArrayList<>();
        }

        LinkedList<Integer> path = new LinkedList<>();
        int at = target;
        // Evitar bucle infinito si predecessors está mal formado
        int steps = 0;
        while (at != -1 && steps <= predecessors.length) {
            path.addFirst(at);
            at = predecessors[at];
            steps++;
        }

        // Si no llegamos a un origen (predecessor -1) y no hay distances para validar, devolvemos el path construido
        return new ArrayList<>(path);
    }

    // Getters para acceder a los resultados después de ejecutar execute
    public int[] getDistances() {
        return distances;
    }

    public int[] getPredecessors() {
        return predecessors;
    }

    // Nodo para la PriorityQueue
    private static class Node implements Comparable<Node> {
        final int vertex;
        final int distance;

        Node(int vertex, int distance) {
            this.vertex = vertex;
            this.distance = distance;
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.distance, other.distance);
        }
    }
}
