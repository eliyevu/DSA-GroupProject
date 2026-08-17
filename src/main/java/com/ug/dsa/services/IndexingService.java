package com.ug.dsa.services;

import com.ug.dsa.datastructures.HashTable;
import com.ug.dsa.datastructures.RedBlackTree;
import com.ug.dsa.datastructures.DynamicArray;
import com.ug.dsa.models.ServiceRequest;

public class IndexingService {

    private final HashTable<String, ServiceRequest> requestById;
    private final RedBlackTree<CategoryEntry> requestsByCategory;

    public IndexingService() {
        this.requestById = new HashTable<>();
        this.requestsByCategory = new RedBlackTree<>();
    }

    public void indexRequest(ServiceRequest request) {
        if (request == null) {
            return;
        }
        String id = request.getId();
        String category = request.getCategory();
        requestById.put(id, request);
        requestsByCategory.insert(new CategoryEntry(category, id, request));
    }

    public ServiceRequest findRequestById(String id) {
        if (id == null) {
            return null;
        }
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
        final String id;
        final ServiceRequest request;

        CategoryEntry(String category, String id, ServiceRequest request) {
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
            return this.id.compareTo(other.id);
        }

        @Override
        public String toString() {
            return category + ":" + id;
        }
    }
}
