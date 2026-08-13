package com.ug.dsa.algorithms;

import java.util.*;


public class GraphDFS<T> {

    private final Map<T, List<T>> adj = new LinkedHashMap<>();

    public void addVertex(T v) {
        adj.putIfAbsent(v, new ArrayList<>());
    }

    public void addEdge(T v, T w) {
        addVertex(v);
        addVertex(w);
        adj.get(v).add(w);
        adj.get(w).add(v);
    }


    public void addDirectedEdge(T v, T w) {
        addVertex(v);
        addVertex(w);
        adj.get(v).add(w);
    }

    public List<T> neighbors(T v) {
        return adj.getOrDefault(v, Collections.emptyList());
    }

    public Set<T> vertices() {
        return adj.keySet();
    }

    public int vertexCount() {
        return adj.size();
    }

    public int edgeCount() {
        int total = 0;
        for (List<T> list : adj.values()) total += list.size();
        return total; 
    }

    public List<T> dfsRecursive(T start) {
        List<T> order = new ArrayList<>();
        Set<T> visited = new HashSet<>();
        dfsRecursiveHelper(start, visited, order);
        return order;
    }

    private void dfsRecursiveHelper(T v, Set<T> visited, List<T> order) {
        visited.add(v);
        order.add(v);
        for (T neighbor : neighbors(v)) {
            if (!visited.contains(neighbor)) {
                dfsRecursiveHelper(neighbor, visited, order);
            }
        }
    }

    public List<T> dfsIterative(T start) {
        List<T> order = new ArrayList<>();
        Set<T> visited = new HashSet<>();
        Deque<T> stack = new ArrayDeque<>();

        stack.push(start);
        while (!stack.isEmpty()) {
            T v = stack.pop();
            if (visited.contains(v)) continue;
            visited.add(v);
            order.add(v);

            List<T> neighbors = neighbors(v);
            for (int i = neighbors.size() - 1; i >= 0; i--) {
                T neighbor = neighbors.get(i);
                if (!visited.contains(neighbor)) {
                    stack.push(neighbor);
                }
            }
        }
        return order;
    }

    public List<List<T>> connectedComponents() {
        List<List<T>> components = new ArrayList<>();
        Set<T> visited = new HashSet<>();

        for (T v : adj.keySet()) {
            if (!visited.contains(v)) {
                List<T> component = new ArrayList<>();
                dfsRecursiveHelper(v, visited, component);
                components.add(component);
            }
        }
        return components;
    }

    public boolean hasPath(T start, T target) {
        return dfsRecursive(start).contains(target);
    }
}