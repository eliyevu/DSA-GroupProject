package com.ug.dsa.datastructures;
public class RedBlackTree {
    private static final boolean RED = true;
    private  static final boolean BLACK = false;

    private  static class Node{
        int key;
        boolean color:
        Node left,right,parent;

        Node(int key){
            this.key = key;
            this.color = RED;
        }
    }
    private  final Node NIL = new Node(-1);
    private Node root;
    public RedBlackTree(){
        NIL.color = BLACK;
        NIL.left= NIL.right = NIL.parent = NIL;
        root = NIL;
    }

    private void leftRotate( Node x){
        Node y = x.right:
        x.right = y.right;
        if (y.left != NIL){
            y.left.parent = x;
        }
        y.parent = x.parent;
        if(x.parent == NIL){
            root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        }
        else{
            x.parent.right = y;
        }
        y.left = x;
        x.parent = y;
    }
    private void rightRotate(Node x){
        Node y = x.left;
        x.lleft = y.right;
        if (y.right != NIl){
            y.right.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == NIL){
            root = y;
        } else if (x == x.parent.right) {
            x.parent.right = y;
        }
        else{
            x.parent.left = y;
        }
        y.right =x;
        x.parent = y;
    }
    public  void insert(int key){
        node new_node = new Node(key);
        new_node.left = NIL;
        new_node.right = NIL;

        Node y = NIL;
        Node x = root;

        while (x != NIL){
            y = x;
            if (new_node.key < x.key){
                x =x.left;
            } else if (new_node.key > x.key)  {
                x = x.right;
            }
            else {
                return
            }
        }
        new_node.parent = y;
        if (y = NIL){
            root = new_node;
        } else if (new_node.key < y.key) {
            y.left = new_node;
        }
        else {
            y.right = new_node;
        }
        fixInsert(new_node)
    }
    private void fixInsert(Node z) {
        while (z.parent.color == RED) {
            if (z.parent == z.parent.parent.left) {
                Node uncle = z.parent.parent.right;

                if (uncle.color == RED) {
                    z.parent.color = BLACK;
                    uncle.color = BLACK;
                    z.parent.parent.color = RED;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.right) {
                        z = z.parent;
                        leftRotate(z);
                    }
                    z.parent.color = BLACK;
                    z.parent.parent.color = RED;
                    rightRotate(z.parent.parent);
                }
            } else {
                Node uncle = z.parent.parent.left;

                if (uncle.color == RED) {
                    z.parent.color = BLACK;
                    uncle.color = BLACK;
                    z.parent.parent.color = RED;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.left) {
                        z = z.parent;
                        rightRotate(z);
                    }
                    z.parent.color = BLACK;
                    z.parent.parent.color = RED;
                    leftRotate(z.parent.parent);
                }
            }

            if (z == root) {
                break;
            }
        }
        root.color = BLACK;

    }













}