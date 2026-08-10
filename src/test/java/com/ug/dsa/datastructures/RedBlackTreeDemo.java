package com.ug.dsa.datastructures;

public class RedBlackTreeDemo {

    public static void main(String[] args) {


        RedBlackTree tree = new RedBlackTree();
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
        System.out.println("========== ROTATION / RECOLORING TRIGGERS ==========");

        System.out.println();
        System.out.println("------------ Ascending inserts: 10, 20, 30 ------------");
        System.out.println("(forces a LEFT rotation at the root)");
        RedBlackTree ascending = new RedBlackTree();
        for (int key : new int[]{10, 20, 30}) {
            ascending.insert(key);
            System.out.println("  insert(" + key + ")  ->  " + ascending
                    + "   root=" + ascending.getRootKeyColor()
                    + "   valid=" + ascending.isValid());
        }

        System.out.println();
        System.out.println("------------ Descending inserts: 30, 20, 10 ------------");
        System.out.println("(forces a RIGHT rotation at the root)");
        RedBlackTree descending = new RedBlackTree();
        for (int key : new int[]{30, 20, 10}) {
            descending.insert(key);
            System.out.println("  insert(" + key + ")  ->  " + descending
                    + "   root=" + descending.getRootKeyColor()
                    + "   valid=" + descending.isValid());
        }

        System.out.println();
        System.out.println("------------ Triangle shape: 30, 10, 20 ------------");
        System.out.println("(child rotation, then grandparent rotation)");
        RedBlackTree triangle = new RedBlackTree();
        for (int key : new int[]{30, 10, 20}) {
            triangle.insert(key);
            System.out.println("  insert(" + key + ")  ->  " + triangle
                    + "   root=" + triangle.getRootKeyColor()
                    + "   valid=" + triangle.isValid());
        }

        System.out.println();
        System.out.println("------------ Red uncle: 20, 10, 30, 5 ------------");
        System.out.println("(pure recoloring, no rotation needed)");
        RedBlackTree redUncle = new RedBlackTree();
        for (int key : new int[]{20, 10, 30, 5}) {
            redUncle.insert(key);
            System.out.println("  insert(" + key + ")  ->  " + redUncle
                    + "   root=" + redUncle.getRootKeyColor()
                    + "   valid=" + redUncle.isValid());
        }

        System.out.println();
        System.out.println("------------ Larger mixed sequence (with a duplicate) ------------");
        RedBlackTree mixed = new RedBlackTree();
        int[] mixedKeys = {10, 18, 7, 15, 16, 30, 25, 40, 60, 2, 17, 3, -5, 100, 1, 15};
        for (int key : mixedKeys) {
            mixed.insert(key);
        }
        System.out.println("Inserted:                    " + java.util.Arrays.toString(mixedKeys));
        System.out.println("Resulting sorted tree:       " + mixed);
        System.out.println("Size (duplicate ignored):    " + mixed.size());
        System.out.println("Valid Red-Black tree?        " + mixed.isValid());

        System.out.println();

    }
}