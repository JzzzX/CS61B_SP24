package main;

import java.util.*;

public class Graph {
    private final Map<Integer, Set<Integer>> graph;
    public Graph() {
        graph = new HashMap<>();
    }

    // Add Node
    public void addNode(int v) {
        if (!graph.containsKey(v)) {
            graph.put(v, new HashSet<>());
        }
        // Another simple solution
        // graph.putIfAbsent(v, new HashSet<>());
    }

    // Add Edge
    public void addEdge(int from, int to) {
        if (!graph.containsKey(from) || !graph.containsKey(to)) {
            throw new IllegalArgumentException("Nodes do not exist!");
        }
        graph.get(from).add(to);
    }

    // Get adjacent node
    public Set<Integer> getNeighbors(int v) {
        if (!graph.containsKey(v)) {
            throw new IllegalArgumentException("Node" + v + "does not exist");
        }
        return Collections.unmodifiableSet(graph.get(v));
    }
}
