package com.ug.dsa.datastructures;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HashTableTest {

    private HashTable<String, Integer> map;

    @BeforeEach
    public void setUp() {
        map = new HashTable<>(4, 0.75f);
    }

    @Test
    public void testInitialState() {
        assertTrue(map.isEmpty());
        assertEquals(0, map.size());
        assertEquals(4, map.getCapacity());
    }

    @Test
    public void testPutAndGet() {
        assertNull(map.put("Apple", 10));
        assertNull(map.put("Banana", 20));
        assertNull(map.put("Cherry", 30));

        assertEquals(3, map.size());
        assertFalse(map.isEmpty());

        assertEquals(10, map.get("Apple"));
        assertEquals(20, map.get("Banana"));
        assertEquals(30, map.get("Cherry"));
        assertNull(map.get("Durian"));
    }

    @Test
    public void testUpdateKey() {
        assertNull(map.put("Apple", 10));
        assertEquals(10, map.put("Apple", 50)); // Update value

        assertEquals(1, map.size());
        assertEquals(50, map.get("Apple"));
    }

    @Test
    public void testRemoveKey() {
        map.put("Apple", 10);
        map.put("Banana", 20);

        assertEquals(20, map.remove("Banana"));
        assertEquals(1, map.size());
        assertNull(map.get("Banana"));
        assertFalse(map.containsKey("Banana"));

        assertNull(map.remove("NonExistent"));
    }

    @Test
    public void testContainsKeyAndValue() {
        map.put("One", 1);
        map.put("Two", 2);

        assertTrue(map.containsKey("One"));
        assertFalse(map.containsKey("Three"));

        assertTrue(map.containsValue(2));
        assertFalse(map.containsValue(99));
    }

    @Test
    public void testNullKeyHandling() {
        map.put(null, 100);
        assertEquals(1, map.size());
        assertTrue(map.containsKey(null));
        assertEquals(100, map.get(null));

        assertEquals(100, map.put(null, 200));
        assertEquals(200, map.get(null));

        assertEquals(200, map.remove(null));
        assertFalse(map.containsKey(null));
        assertNull(map.get(null));
    }

    @Test
    public void testDynamicResizing() {
        // Initial capacity is 4, load factor threshold is 3 (4 * 0.75)
        map.put("A", 1);
        map.put("B", 2);
        assertEquals(4, map.getCapacity());

        map.put("C", 3); // Triggers resize to 8
        assertTrue(map.getCapacity() >= 8);
        assertEquals(3, map.size());

        // Verify all elements are still accessible after resize
        assertEquals(1, map.get("A"));
        assertEquals(2, map.get("B"));
        assertEquals(3, map.get("C"));
    }

    @Test
    public void testCollisionResolution() {
        // Keys with potential same bucket index
        HashTable<Integer, String> intMap = new HashTable<>(4, 0.75f);
        intMap.put(0, "Zero");
        intMap.put(4, "Four");
        intMap.put(8, "Eight");

        assertEquals(3, intMap.size());
        assertEquals("Zero", intMap.get(0));
        assertEquals("Four", intMap.get(4));
        assertEquals("Eight", intMap.get(8));

        // Delete from middle of chain
        assertEquals("Four", intMap.remove(4));
        assertEquals(2, intMap.size());
        assertNull(intMap.get(4));
        assertEquals("Zero", intMap.get(0));
        assertEquals("Eight", intMap.get(8));
    }

    @Test
    public void testClear() {
        map.put("A", 1);
        map.put("B", 2);
        map.clear();

        assertTrue(map.isEmpty());
        assertEquals(0, map.size());
        assertNull(map.get("A"));
    }

    @Test
    public void testToString() {
        assertEquals("{}", map.toString());
        map.put("X", 100);
        assertTrue(map.toString().contains("X=100"));
    }

    @Test
    public void testInvalidConstructorArguments() {
        assertThrows(IllegalArgumentException.class, () -> new HashTable<>(0));
        assertThrows(IllegalArgumentException.class, () -> new HashTable<>(10, -0.5f));
    }
}
