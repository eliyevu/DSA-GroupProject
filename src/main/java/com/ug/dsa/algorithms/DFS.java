package com.ug.dsa.datastructures;

import java.util.*;

public class GraphDFS {

    private final Map<Integer, List<Integer>> adj = new LinkedHashMap<>();

    public void addVertex(int v) {
        adj.putIfAbsent(v, new ArrayList<>());
    }


    public void addEdge(int v, int w) {
        addVertex(v);
        addVertex(w);
        adj.get(v).add(w);
        adj.get(w).add(v);
    }

    public void addDirectedEdge(int v, int w) {
        addVertex(v);
        addVertex(w);
        adj.get(v).add(w);
    }

    public List<Integer> neighbors(int v) {
        return adj.getOrDefault(v, Collections.emptyList());
    }

    public Set<Integer> vertices() {
        return adj.keySet();
    }

    public int vertexCount() {
        return adj.size();
    }

    public int edgeCount() {
        int total = 0;
        for (List<Integer> list : adj.values()) total += list.size();
        return total; 
    }

    public List<Integer> dfsRecursive(int start) {
        List<Integer> order = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();
        dfsRecursiveHelper(start, visited, order);
        return order;
    }

    private void dfsRecursiveHelper(int v, Set<Integer> visited, List<Integer> order) {
        visited.add(v);
        order.add(v);
        for (int neighbor : neighbors(v)) {
            if (!visited.contains(neighbor)) {
                dfsRecursiveHelper(neighbor, visited, order);
            }
        }
    }

    public List<Integer> dfsIterative(int start) {
        List<Integer> order = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();
        Deque<Integer> stack = new ArrayDeque<>();

        stack.push(start);
        while (!stack.isEmpty()) {
            int v = stack.pop();
            if (visited.contains(v)) continue;
            visited.add(v);
            order.add(v);

            List<Integer> neighbors = neighbors(v);
            for (int i = neighbors.size() - 1; i >= 0; i--) {
                int neighbor = neighbors.get(i);
                if (!visited.contains(neighbor)) {
                    stack.push(neighbor);
                }
            }
        }
        return order;
    }


    public List<List<Integer>> connectedComponents() {
        List<List<Integer>> components = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();

        for (int v : adj.keySet()) {
            if (!visited.contains(v)) {
                List<Integer> component = new ArrayList<>();
                dfsRecursiveHelper(v, visited, component);
                components.add(component);
            }
        }
        return components;
    }

    public boolean hasPath(int start, int target) {
        return dfsRecursive(start).contains(target);
    }
}