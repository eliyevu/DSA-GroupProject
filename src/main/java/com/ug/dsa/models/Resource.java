package com.ug.dsa.models;

/**
 * Minimal Resource model.
 *
 * NOTE: this was an empty stub. Added the minimum fields needed to
 * support Team 4's Indexing/Search service (findResource by id).
 * Whoever owns this model, feel free to extend with more fields
 * (type, capacity, etc.) - just keep the id field since
 * IndexingService depends on it.
 *
 * Added by: Amoah Edward Junior (Member 7)
 */
public class Resource {

    private String id;
    private String name;

    public Resource(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Resource{id='" + id + "', name='" + name + "'}";
    }
}
