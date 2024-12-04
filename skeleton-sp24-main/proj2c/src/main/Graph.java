package main;

import java.util.*;

public class Graph {
    private final Map<Integer, Set<Integer>> graph; // 存储向下的边（hyponym关系）
    private final Map<Integer, Set<Integer>> reverseGraph;  // 存储向上的边（hypernym关系）

    public Graph() {
        graph = new HashMap<>();
        reverseGraph = new HashMap<>();
    }

    // Add Node
    public void addNode(int v) {
        graph.putIfAbsent(v, new HashSet<>());
        reverseGraph.putIfAbsent(v, new HashSet<>());
    }

    // Add Edge
    public void addEdge(int from, int to) {
        if (!graph.containsKey(from) || !graph.containsKey(to)) {
            throw new IllegalArgumentException("Nodes do not exist!");
        }
        graph.get(from).add(to);
        reverseGraph.get(to).add(from);  // 添加反向边
    }

    // 获取子节点（hyponyms）
    public Set<Integer> getNeighbors(int v) {
        if (!graph.containsKey(v)) {
            throw new IllegalArgumentException("Node " + v + " does not exist");
        }
        return Collections.unmodifiableSet(graph.get(v));
    }

    // 获取父节点（hypernyms）
    public Set<Integer> getParents(int v) {
        if (!reverseGraph.containsKey(v)) {
            throw new IllegalArgumentException("Node " + v + " does not exist");
        }
        return Collections.unmodifiableSet(reverseGraph.get(v));
    }
}
