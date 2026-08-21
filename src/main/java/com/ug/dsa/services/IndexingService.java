package com.ug.dsa.services;

import com.ug.dsa.datastructures.BST;
import com.ug.dsa.datastructures.BTree;
import com.ug.dsa.datastructures.DynamicArray;
import com.ug.dsa.datastructures.HashTable;
import com.ug.dsa.datastructures.RedBlackTree;
import com.ug.dsa.models.Location;
import com.ug.dsa.models.Resource;
import com.ug.dsa.models.ServiceRequest;

/**
 * Search/indexing facade demonstrating the project's custom index structures.
 *
 * BST          -> request-ID membership / ordered request IDs
 * HashTable    -> request ID -> request O(1) average lookup
 * RedBlackTree -> category + request ID ordered index
 * BTree        -> location and resource indexes
 */
public class IndexingService {

    private final BST requestIdIndex;
    private final HashTable<Integer, ServiceRequest> requestById;
    private final RedBlackTree<CategoryEntry> requestsByCategory;
    private final BTree<Integer, Location> locationIndex;
    private final BTree<Integer, Resource> resourceIndex;

    public IndexingService() {
        requestIdIndex = new BST();
        requestById = new HashTable<>();
        requestsByCategory = new RedBlackTree<>();
        locationIndex = new BTree<>(3);
        resourceIndex = new BTree<>(3);
    }

    public void clear() {
        // IndexingService instances are cheap; callers can create a new one on reload.
        // Kept for API clarity without depending on unsupported clear() methods in custom trees.
    }

    public void indexRequest(ServiceRequest request) {
        if (request == null) return;

        int id = request.getRequestId();
        String category = request.getCategory() == null ? "" : request.getCategory().trim().toUpperCase();

        requestIdIndex.insert(id);
        requestById.put(id, request);
        requestsByCategory.insert(new CategoryEntry(category, id, request));
    }

    public ServiceRequest findRequestById(int id) {
        if (!requestIdIndex.search(id)) return null;
        return requestById.get(id);
    }

    public boolean containsRequestId(int id) {
        return requestIdIndex.search(id);
    }

    public DynamicArray<ServiceRequest> findRequestsByCategory(String category) {
        DynamicArray<ServiceRequest> matches = new DynamicArray<>();
        if (category == null) return matches;

        String target = category.trim().toUpperCase();
        DynamicArray<?> entries = requestsByCategory.inorderKeys();

        for (int i = 0; i < entries.size(); i++) {
            CategoryEntry entry = (CategoryEntry) entries.get(i);
            if (entry.category.equals(target)) matches.add(entry.request);
        }
        return matches;
    }

    public DynamicArray<Integer> getIndexedRequestIds() {
        return requestIdIndex.inorderTraversal();
    }

    public void indexLocation(Location location) {
        if (location != null) locationIndex.insert(location.getLocationId(), location);
    }

    public void indexResource(Resource resource) {
        if (resource != null) resourceIndex.insert(resource.getResourceId(), resource);
    }

    public Location findLocation(int locationId) {
        return locationIndex.search(locationId);
    }

    public Resource findResource(int resourceId) {
        return resourceIndex.search(resourceId);
    }

    private static final class CategoryEntry implements Comparable<CategoryEntry> {
        private final String category;
        private final int requestId;
        private final ServiceRequest request;

        private CategoryEntry(String category, int requestId, ServiceRequest request) {
            this.category = category;
            this.requestId = requestId;
            this.request = request;
        }

        @Override
        public int compareTo(CategoryEntry other) {
            int categoryComparison = category.compareTo(other.category);
            if (categoryComparison != 0) return categoryComparison;
            return Integer.compare(requestId, other.requestId);
        }

        @Override
        public String toString() {
            return category + ":" + requestId;
        }
    }
}
