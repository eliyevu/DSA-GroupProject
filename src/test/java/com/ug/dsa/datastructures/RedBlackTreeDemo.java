package com.ug.dsa.datastructures;

public class RedBlackTreeDemo {

    public static void main(String[] args) {


        RedBlackTree<Integer> tree = new RedBlackTree<>();
        for (int value = 10; value <= 100; value += 10) {
            tree.insert(value);
        }

        System.out.println("Tree after insert(10..100):  " + tree);
        System.out.println("Size:                        " + tree.size());
        System.out.println("Height (edges):              " + tree.height());
        System.out.println("isEmpty:                      " + tree.isEmpty());
        System.out.println("Valid Red-Black tree?         " + tree.isValid());

        System.out.println();
        System.out.println("------------ TRAVERSAL ------------");
        System.out.print("In-order traversal (sorted): ");
        for (int key : tree.inorderKeys()) {
            System.out.print(key + " ");
        }
        System.out.println();

        System.out.println();
        System.out.println("------------ STRUCTURE (level order, key+color) ------------");
        int depth = 0;
        for (String line : tree.levelOrderByDepth()) {
            System.out.println("Depth " + depth++ + ": " + line);
        }
        System.out.println("Root key/color:              " + tree.getRootKeyColor());

        System.out.println();
        System.out.println("------------ SEARCH ------------");
        int[] searchKeys = {50, 999};
        for (int key : searchKeys) {
            System.out.println("contains(" + key + "):" + " ".repeat(Math.max(1, 16 - String.valueOf(key).length()))
                + tree.contains(key));
        }

        System.out.println();
        System.out.println("========== ROTATION / RECOLORING TRIGGERS (Integer) ==========");

        System.out.println();
        System.out.println("------------ Ascending inserts: 10, 20, 30 ------------");
        System.out.println("(forces a LEFT rotation at the root)");
        RedBlackTree<Integer> ascending = new RedBlackTree<>();
        for (int key : new int[]{10, 20, 30}) {
            ascending.insert(key);
            System.out.println("  insert(" + key + ")  ->  " + ascending
                + "   root=" + ascending.getRootKeyColor()
                + "   valid=" + ascending.isValid());
        }

        System.out.println();
        System.out.println("------------ Descending inserts: 30, 20, 10 ------------");
        System.out.println("(forces a RIGHT rotation at the root)");
        RedBlackTree<Integer> descending = new RedBlackTree<>();
        for (int key : new int[]{30, 20, 10}) {
            descending.insert(key);
            System.out.println("  insert(" + key + ")  ->  " + descending
                + "   root=" + descending.getRootKeyColor()
                + "   valid=" + descending.isValid());
        }

        System.out.println();
        System.out.println("------------ Triangle shape: 30, 10, 20 ------------");
        System.out.println("(child rotation, then grandparent rotation)");
        RedBlackTree<Integer> triangle = new RedBlackTree<>();
        for (int key : new int[]{30, 10, 20}) {
            triangle.insert(key);
            System.out.println("  insert(" + key + ")  ->  " + triangle
                + "   root=" + triangle.getRootKeyColor()
                + "   valid=" + triangle.isValid());
        }

        System.out.println();
        System.out.println("------------ Red uncle: 20, 10, 30, 5 ------------");
        System.out.println("(pure recoloring, no rotation needed)");
        RedBlackTree<Integer> redUncle = new RedBlackTree<>();
        for (int key : new int[]{20, 10, 30, 5}) {
            redUncle.insert(key);
            System.out.println("  insert(" + key + ")  ->  " + redUncle
                + "   root=" + redUncle.getRootKeyColor()
                + "   valid=" + redUncle.isValid());
        }

        System.out.println();
        System.out.println("========== GENERIC TYPE CHECK: RedBlackTree<String> ==========");
        RedBlackTree<String> words = new RedBlackTree<>();
        String[] toInsert = {"mango", "banana", "kiwi", "apple", "papaya", "zebra", "cherry"};
        for (String w : toInsert) {
            words.insert(w);
        }
        System.out.println("Inserted:                    " + java.util.Arrays.toString(toInsert));
        System.out.println("Sorted (in-order):           " + words);
        System.out.println("Size:                        " + words.size());
        System.out.println("Valid Red-Black tree?        " + words.isValid());
        System.out.println("contains(\"kiwi\"):             " + words.contains("kiwi"));
        System.out.println("contains(\"pineapple\"):        " + words.contains("pineapple"));

        System.out.println();
        System.out.println("========== GENERIC TYPE CHECK: RedBlackTree<Double> ==========");
        RedBlackTree<Double> decimals = new RedBlackTree<>();
        double[] toInsertD = {3.14, 1.41, 2.72, 0.58, 1.73};
        for (double d : toInsertD) {
            decimals.insert(d);
        }
        System.out.println("Sorted (in-order):           " + decimals);
        System.out.println("Valid Red-Black tree?        " + decimals.isValid());

    }
}