package com.ug.dsa.datastructures;

public class StackTest {
    public static void main(String[] args) {
        System.out.println("Testing with Integers");
        Stack<Integer> intStack = new Stack<>(5);
        intStack.push(10);
        intStack.push(20);
        intStack.push(30);

        intStack.printStack();
        System.out.println("Peek: " + intStack.peek()); // 30
        System.out.println("Pop: " + intStack.pop());   // 30
        System.out.println("Size: " + intStack.size()); // 2
        System.out.println("Is Empty: " + intStack.isEmpty()); // false
        System.out.println("\n");

        System.out.println("Testing with Strings");

        Stack<String> stringStack = new Stack<>(3);
        stringStack.push("Hello");
        stringStack.push("World");
        stringStack.printStack();
        System.out.println("Peek: " + stringStack.peek()); // World
    }
}
