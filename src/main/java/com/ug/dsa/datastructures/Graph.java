package com.ug.dsa.datastructures;

public class Graph {

    private DynamicArray[] adjacencyList;
    private int numVertices;

    public Graph(int numVertices) {
        this.numVertices = numVertices;
        adjacencyList = new DynamicArray[numVertices];
        for (int i = 0; i < numVertices; i++) {
            adjacencyList[i] = new DynamicArray();
        }
    }

    public void addEdge(int source, int destination) {
        adjacencyList[source].add(destination);
        adjacencyList[destination].add(source); // undirected graph
    }

    public void removeEdge(int source, int destination) {
        adjacencyList[source].remove(indexOf(adjacencyList[source], destination));
        adjacencyList[destination].remove(indexOf(adjacencyList[destination], source));
    }

    public DynamicArray getNeighbours(int vertex) {
        return adjacencyList[vertex];
    }

    public int getVertices() {
        return numVertices;
    }

    private int indexOf(DynamicArray array, int value) {
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i) == value) {
                return i;
            }
        }
        return -1;
    }
}