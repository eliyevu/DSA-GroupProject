package com.ug.dsa.services;

import com.ug.dsa.datastructures.HashTable;
import com.ug.dsa.datastructures.RedBlackTree;
import com.ug.dsa.datastructures.DynamicArray;
import com.ug.dsa.datastructures.BTree;
import com.ug.dsa.models.ServiceRequest;
import com.ug.dsa.models.Location;
import com.ug.dsa.models.Resource;

/**
 * TEAM 4 - Indexing / Search
 * Members: Amoah Edward Junior, Abiwu Kelvin Nutifafa
 */
public class IndexingService {

    private final HashTable<Integer, ServiceRequest> requestById;
    private final RedBlackTree<CategoryEntry> requestsByCategory;

    private final BTree<Integer, Location> locationIndex;
    private final BTree<Integer, Resource> resourceIndex;

    public IndexingService() {
        this.requestById = new HashTable<>();
        this.requestsByCategory = new RedBlackTree<>();
        this.locationIndex = new BTree<>(3);
        this.resourceIndex = new BTree<>(3);
    }

    public void indexRequest(ServiceRequest request) {
        if (request == null) {
            return;
        }
        int id = request.getRequestId();
        String category = request.getCategory();
        requestById.put(id, request);
        requestsByCategory.insert(new CategoryEntry(category, id, request));
    }

    public ServiceRequest findRequestById(int id) {
        return requestById.get(id);
    }

    public DynamicArray findRequestsByCategory(String category) {
        DynamicArray matches = new DynamicArray();
        if (category == null) {
            return matches;
        }
        DynamicArray allEntries = requestsByCategory.inorderKeys();
        for (int i = 0; i < allEntries.size(); i++) {
            CategoryEntry entry = (CategoryEntry) allEntries.get(i);
            if (entry.category.equals(category)) {
                matches.add(entry.request);
            }
        }
        return matches;
    }

    private static class CategoryEntry implements Comparable<CategoryEntry> {
        final String category;
        final int id;
        final ServiceRequest request;

        CategoryEntry(String category, int id, ServiceRequest request) {
            this.category = category;
            this.id = id;
            this.request = request;
        }

        @Override
        public int compareTo(CategoryEntry other) {
            int categoryComparison = this.category.compareTo(other.category);
            if (categoryComparison != 0) {
                return categoryComparison;
            }
            return Integer.compare(this.id, other.id);
        }

        @Override
        public String toString() {
            return category + ":" + id;
        }
    }

    public void indexLocation(Location location) {
        if (location == null) {
            return;
        }
        locationIndex.insert(location.getLocationId(), location);
    }

    public void indexResource(Resource resource) {
        if (resource == null) {
            return;
        }
        resourceIndex.insert(resource.getResourceId(), resource);
    }

    public Location findLocation(int locationId) {
        return locationIndex.search(locationId);
    }

    public Resource findResource(int resourceId) {
        return resourceIndex.search(resourceId);
    }
}
