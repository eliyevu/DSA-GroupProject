package com.ug.dsa.services;

import com.ug.dsa.datastructures.BTree;
import com.ug.dsa.models.Location;
import com.ug.dsa.models.Resource;

/**
 * TEAM 4 - Indexing / Search
 * Members: Amoah Edward Junior, Abiwu Kelvin Nutifafa
 *
 * This half of the task (B-Tree side) covers findLocation() and
 * findResource(). Kelvin's half (Hash Table / BST side) covers
 * findRequestById() and findRequestsByCategory() separately.
 *
 * Does NOT implement its own tree logic - reuses the team's existing
 * custom BTree<K, V> (owned by Roselyn Francis, modified to be generic
 * so it can store a value alongside each key).
 */
public class IndexingService {

    private final BTree<String, Location> locationIndex;
    private final BTree<String, Resource> resourceIndex;

    private static final int BTREE_DEGREE = 3; // minimum degree (t) for the B-Trees

    public IndexingService() {
        locationIndex = new BTree<>(BTREE_DEGREE);
        resourceIndex = new BTree<>(BTREE_DEGREE);
    }

    // ------------------------------------------------------------
    // Indexing (adding data in)
    // ------------------------------------------------------------

    /**
     * Add a location to the index so it can later be found by id.
     */
    public void indexLocation(Location location) {
        if (location == null || location.getId() == null) {
            throw new IllegalArgumentException("Location and its id must not be null");
        }
        locationIndex.insert(location.getId(), location);
    }

    /**
     * Add a resource to the index so it can later be found by id.
     */
    public void indexResource(Resource resource) {
        if (resource == null || resource.getId() == null) {
            throw new IllegalArgumentException("Resource and its id must not be null");
        }
        resourceIndex.insert(resource.getId(), resource);
    }

    // ------------------------------------------------------------
    // Lookups
    // ------------------------------------------------------------

    /**
     * Find a location by its id. Returns null if not found.
     */
    public Location findLocation(String id) {
        return locationIndex.search(id);
    }

    /**
     * Find a resource by its id. Returns null if not found.
     */
    public Resource findResource(String id) {
        return resourceIndex.search(id);
    }

    // ------------------------------------------------------------
    // Demo
    // ------------------------------------------------------------

    public static void main(String[] args) {
        IndexingService indexingService = new IndexingService();

        indexingService.indexLocation(new Location("L1", "Main Gate"));
        indexingService.indexLocation(new Location("L2", "Library"));
        indexingService.indexResource(new Resource("R1", "Shuttle Bus A"));
        indexingService.indexResource(new Resource("R2", "Maintenance Van"));

        System.out.println("Lookup L2: " + indexingService.findLocation("L2"));
        System.out.println("Lookup R1: " + indexingService.findResource("R1"));
        System.out.println("Lookup L99 (missing): " + indexingService.findLocation("L99"));
    }
}
