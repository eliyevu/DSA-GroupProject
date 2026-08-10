import java.util.*;

public class GraphDFSDemo {

    public static void main(String[] args) {

        System.out.println("================ DEPTH-FIRST SEARCH ================");

        GraphDFS g = new GraphDFS();
        g.addEdge(1, 2);
        g.addEdge(1, 3);
        g.addEdge(2, 4);
        g.addEdge(2, 5);
        g.addEdge(3, 5);
        g.addEdge(6, 7);

        System.out.println("Graph edges added:            1-2, 1-3, 2-4, 2-5, 3-5, 6-7");
        System.out.println("(6-7 is a disconnected component)");

        System.out.println();
        System.out.println("------------ TRAVERSAL ------------");
        System.out.println("Recursive DFS from 1:        " + g.dfsRecursive(1));
        System.out.println("Iterative DFS from 1:        " + g.dfsIterative(1));
        System.out.println("Recursive DFS from 6:        " + g.dfsRecursive(6));

        System.out.println();
        System.out.println("------------ REACHABILITY ------------");
        int[][] pathChecks = {{1, 4}, {1, 5}, {1, 7}, {6, 3}};
        for (int[] check : pathChecks) {
            System.out.println("hasPath(" + check[0] + " -> " + check[1] + "):" + " "
                + g.hasPath(check[0], check[1]));
        }

        System.out.println();
        System.out.println("------------ CONNECTED COMPONENTS ------------");
        List<List<Integer>> components = g.dfsAllComponents();
        for (int i = 0; i < components.size(); i++) {
            System.out.println("Component " + (i + 1) + ":                 " + components.get(i));
        }
        System.out.println("Total components:            " + components.size());

        System.out.println();
        System.out.println("========== RECURSIVE vs ITERATIVE ORDER CHECK ==========");
        System.out.println("(Both should agree since iterative pushes neighbors in reverse)");
        boolean sameOrder = g.dfsRecursive(1).equals(g.dfsIterative(1));
        System.out.println("Orders match starting at 1:  " + sameOrder);

        System.out.println();
        System.out.println("------------ A SLIGHTLY LARGER GRAPH (tree-shaped) ------------");
        GraphDFS tree = new GraphDFS();
        tree.addEdge(1, 2);
        tree.addEdge(1, 3);
        tree.addEdge(2, 4);
        tree.addEdge(2, 5);
        tree.addEdge(3, 6);
        tree.addEdge(3, 7);
        tree.addEdge(4, 8);
        System.out.println("Edges: 1-2, 1-3, 2-4, 2-5, 3-6, 3-7, 4-8 (binary-tree shaped)");
        System.out.println("DFS from root 1:              " + tree.dfsRecursive(1));

        System.out.println();
        System.out.println("================ COMPLETE ================");
    }
}