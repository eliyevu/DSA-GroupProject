package com.ug.dsa.models;

/**
 * Minimal Location model.
 *
 * NOTE: this was an empty stub. Added the minimum fields needed to
 * support Team 4's Indexing/Search service (findLocation by id).
 * Whoever owns this model, feel free to extend with more fields
 * (name, coordinates, etc.) - just keep the id field since
 * IndexingService depends on it.
 *
 * Added by: Amoah Edward Junior (Member 7)
 */
public class Location {

    private String id;
    private String name;

    public Location(String id, String name) {
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
        return "Location{id='" + id + "', name='" + name + "'}";
    }
}
