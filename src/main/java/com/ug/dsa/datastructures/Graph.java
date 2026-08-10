package com.ug.dsa.datastructures;

public class Graph {

    private int numVertices;
    private int numEdges;
    private final boolean isDirected;

    // Adjacency Matrix representation: matrix[u][v] stores edge weight (0 if no edge)
    private int[][] adjMatrix;

    // Adjacency List representation: adjList[u] contains a custom LinkedList of Edge objects
    @SuppressWarnings("unchecked")
    private LinkedList<Edge>[] adjList;

    public Graph(int numVertices) {
        this(numVertices, false);
    }

    @SuppressWarnings("unchecked")
    public Graph(int numVertices, boolean isDirected) {
        if (numVertices < 0) {
            throw new IllegalArgumentException("Number of vertices cannot be negative.");
        }
        this.numVertices = numVertices;
        this.numEdges = 0;
        this.isDirected = isDirected;

        this.adjMatrix = new int[numVertices][numVertices];
        this.adjList = new LinkedList[numVertices];

        for (int i = 0; i < numVertices; i++) {
            adjList[i] = new LinkedList<>();
        }
    }


    @SuppressWarnings("unchecked")
    public int addVertex() {
        int newNumVertices = numVertices + 1;

        // Resize Adjacency Matrix
        int[][] newMatrix = new int[newNumVertices][newNumVertices];
        for (int i = 0; i < numVertices; i++) {
            System.arraycopy(adjMatrix[i], 0, newMatrix[i], 0, numVertices);
        }
        this.adjMatrix = newMatrix;

        // Resize Adjacency List
        LinkedList<Edge>[] newList = new LinkedList[newNumVertices];
        for (int i = 0; i < numVertices; i++) {
            newList[i] = adjList[i];
        }
        newList[numVertices] = new LinkedList<>();
        this.adjList = newList;

        int newVertexIndex = numVertices;
        this.numVertices = newNumVertices;
        return newVertexIndex;
    }

    public void addEdge(int src, int dest) {
        addEdge(src, dest, 1);
    }

    public void addEdge(int src, int dest, int weight) {
        validateVertex(src);
        validateVertex(dest);
        if (weight <= 0) {
            throw new IllegalArgumentException("Edge weight must be positive: " + weight);
        }

        // Check if edge already exists to handle update vs new edge count
        boolean exists = (adjMatrix[src][dest] != 0);

        // Update Matrix
        adjMatrix[src][dest] = weight;
        if (!isDirected) {
            adjMatrix[dest][src] = weight;
        }

        // Update List - remove old edge if updating weight
        Edge newEdge = new Edge(src, dest, weight);
        removeEdgeFromList(src, dest);
        adjList[src].add(newEdge);

        if (!isDirected && src != dest) {
            Edge revEdge = new Edge(dest, src, weight);
            removeEdgeFromList(dest, src);
            adjList[dest].add(revEdge);
        }

        if (!exists) {
            numEdges++;
        }
    }


    public boolean removeEdge(int src, int dest) {
        validateVertex(src);
        validateVertex(dest);

        if (adjMatrix[src][dest] == 0) {
            return false;
        }

        adjMatrix[src][dest] = 0;
        removeEdgeFromList(src, dest);

        if (!isDirected && src != dest) {
            adjMatrix[dest][src] = 0;
            removeEdgeFromList(dest, src);
        }

        numEdges--;
        return true;
    }


    @SuppressWarnings("unchecked")
    public void removeVertex(int v) {
        validateVertex(v);

        // 1. Calculate edges to remove
        for (int i = 0; i < numVertices; i++) {
            if (adjMatrix[v][i] != 0) {
                removeEdge(v, i);
            } else if (isDirected && adjMatrix[i][v] != 0) {
                removeEdge(i, v);
            }
        }

        int newNumVertices = numVertices - 1;

        // 2. Rebuild Adjacency Matrix without row/column v
        int[][] newMatrix = new int[newNumVertices][newNumVertices];
        for (int i = 0, newI = 0; i < numVertices; i++) {
            if (i == v) continue;
            for (int j = 0, newJ = 0; j < numVertices; j++) {
                if (j == v) continue;
                newMatrix[newI][newJ] = adjMatrix[i][j];
                newJ++;
            }
            newI++;
        }
        this.adjMatrix = newMatrix;

        // 3. Rebuild Adjacency List without list v and shift vertex references in Edges
        LinkedList<Edge>[] newList = new LinkedList[newNumVertices];
        for (int i = 0, newI = 0; i < numVertices; i++) {
            if (i == v) continue;
            newList[newI] = new LinkedList<>();
            LinkedList<Edge> oldList = adjList[i];
            for (int k = 0; k < oldList.size(); k++) {
                Edge e = oldList.get(k);
                int newSrc = e.getSrc() > v ? e.getSrc() - 1 : e.getSrc();
                int newDest = e.getDest() > v ? e.getDest() - 1 : e.getDest();
                newList[newI].add(new Edge(newSrc, newDest, e.getWeight()));
            }
            newI++;
        }
        this.adjList = newList;
        this.numVertices = newNumVertices;
    }


    public Edge[] getNeighbours(int v) {
        validateVertex(v);
        LinkedList<Edge> list = adjList[v];
        Edge[] neighbours = new Edge[list.size()];
        for (int i = 0; i < list.size(); i++) {
            neighbours[i] = list.get(i);
        }
        return neighbours;
    }


    public Edge[] getAllEdges() {
        Edge[] allEdgesTemp = new Edge[numEdges];
        int count = 0;

        for (int i = 0; i < numVertices; i++) {
            LinkedList<Edge> list = adjList[i];
            for (int j = 0; j < list.size(); j++) {
                Edge e = list.get(j);
                if (isDirected || e.getSrc() <= e.getDest()) {
                    if (count < allEdgesTemp.length) {
                        allEdgesTemp[count++] = e;
                    }
                }
            }
        }

        // Return exact sized array
        if (count == allEdgesTemp.length) {
            return allEdgesTemp;
        }
        Edge[] result = new Edge[count];
        System.arraycopy(allEdgesTemp, 0, result, 0, count);
        return result;
    }


    public int[][] getAdjacencyMatrix() {
        int[][] copy = new int[numVertices][numVertices];
        for (int i = 0; i < numVertices; i++) {
            System.arraycopy(adjMatrix[i], 0, copy[i], 0, numVertices);
        }
        return copy;
    }


    public LinkedList<Edge>[] getAdjacencyList() {
        return adjList;
    }

    public int getNumVertices() {
        return numVertices;
    }

    public int getNumEdges() {
        return numEdges;
    }

    public boolean isDirected() {
        return isDirected;
    }

    /**
     * Displays the Adjacency Matrix to standard output.
     */
    public void displayAdjacencyMatrix() {
        System.out.println("--- Adjacency Matrix ---");
        System.out.print("    ");
        for (int i = 0; i < numVertices; i++) {
            System.out.printf("%4d", i);
        }
        System.out.println();
        System.out.print("    ");
        for (int i = 0; i < numVertices; i++) {
            System.out.print("----");
        }
        System.out.println();

        for (int i = 0; i < numVertices; i++) {
            System.out.printf("%2d |", i);
            for (int j = 0; j < numVertices; j++) {
                System.out.printf("%4d", adjMatrix[i][j]);
            }
            System.out.println();
        }
    }

    /**
     * Displays the Adjacency List to standard output.
     */
    public void displayAdjacencyList() {
        System.out.println("--- Adjacency List ---");
        for (int i = 0; i < numVertices; i++) {
            System.out.print("Vertex " + i + " -> ");
            LinkedList<Edge> list = adjList[i];
            if (list.isEmpty()) {
                System.out.println("Empty");
            } else {
                for (int j = 0; j < list.size(); j++) {
                    Edge e = list.get(j);
                    System.out.print("[" + e.getDest() + " (w=" + e.getWeight() + ")]");
                    if (j < list.size() - 1) {
                        System.out.print(" -> ");
                    }
                }
                System.out.println();
            }
        }
    }

    private void removeEdgeFromList(int src, int dest) {
        LinkedList<Edge> list = adjList[src];
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getDest() == dest) {
                list.removeAt(i);
                break;
            }
        }
    }

    private void validateVertex(int v) {
        if (v < 0 || v >= numVertices) {
            throw new IndexOutOfBoundsException("Vertex index " + v + " out of bounds for graph with " + numVertices + " vertices.");
        }
    }
}
