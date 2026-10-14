# Documentation: Abiwu Kelvin Nutifafa

Project: Smart Service Operations Optimizer (DSA group project)

This document covers my contributions:

1. **Indexing / Search** (Team 4, with Edward Amoah Junior): `services/IndexingService.java`
2. **BFS and BST**: `algorithms/BFS.java`, `datastructures/BST.java`, with the supporting Graph and Queue work and their tests

All code reuses the project's custom data structures. No Java collection classes (`HashMap`, `TreeMap`, `ArrayList`) are used for the indexes, and no data structure was duplicated.

---

## 1. Indexing Service

**File:** `src/main/java/com/ug/dsa/services/IndexingService.java`

`IndexingService` is a search layer on top of the structures the data-structures team built. It stores nothing of its own beyond references to those structures. Each lookup is routed to the structure best suited to it.

### Which structure answers which query

| Operation | Structure used | Why |
|---|---|---|
| `findRequestById(int id)` | `BST` (membership check), then `HashTable<Integer, ServiceRequest>` | The hash table gives average O(1) lookup by unique ID. The BST also keeps the IDs ordered, so `getIndexedRequestIds()` can return them sorted. |
| `findRequestsByCategory(String category)` | `RedBlackTree<CategoryEntry>` | Keeps requests ordered by category and stays balanced as data grows. |
| `findLocation(int id)` | `BTree<Integer, Location>` | Edward's part. |
| `findResource(int id)` | `BTree<Integer, Resource>` | Edward's part. |

### How `findRequestById` works

`indexRequest(request)` inserts the request's ID into the BST and puts the request in the hash table, keyed by ID. `findRequestById(id)` first checks the BST (`search`). If the ID is not there it returns `null`. Otherwise it returns `requestById.get(id)`.

### How `findRequestsByCategory` works

The Red-Black Tree stores only keys and rejects duplicate keys, but many requests share one category. To handle this, the tree stores `CategoryEntry` objects, a small private wrapper that holds the category, the request ID, and the request itself. `CategoryEntry` implements `Comparable` by comparing the category first and the request ID second. Two requests in the same category therefore have different keys and both stay in the tree, sorted together.

`findRequestsByCategory` reads the tree in order with `inorderKeys()` and collects the entries whose category matches. Categories are normalised with `trim().toUpperCase()` on both insert and search, so `"plumbing"`, `" Plumbing "` and `"PLUMBING"` match each other.

If the category is `null`, the method returns an empty array. If the ID is not found, `findRequestById` returns `null`.

### Time complexity

| Operation | Cost | Notes |
|---|---|---|
| `indexRequest` | O(h) + O(1) avg + O(log n) | BST insert (h = BST height), hash table put, Red-Black Tree insert |
| `findRequestById` | O(h) + O(1) avg | BST membership check, then hash lookup |
| `findRequestsByCategory` | O(n) | In-order walk over all entries, filtered by category |

### Known limitations

- **The BST is not self-balancing.** If request IDs are inserted in sorted order (1, 2, 3, ...), the BST degenerates into a linked list, and its `search` becomes O(n) in the worst case. The hash table lookup itself stays O(1) on average. A fuller design would use the `RedBlackTree` for the ID index too.
- **`findRequestsByCategory` is O(n)**, not O(log n + k), because `RedBlackTree` offers `inorderKeys()` and `contains()` but no range search. Adding a range query to the tree would be the owning team's change, so I filtered the in-order list instead of writing a new structure.

### Division of work

- **Abiwu Kelvin Nutifafa:** `findRequestById`, `findRequestsByCategory`, the `CategoryEntry` design for the Red-Black Tree
- **Edward Amoah Junior:** `findLocation`, `findResource`, `indexLocation`, `indexResource` using the B-Tree, and the update of the request methods to the `int requestId` in the final `ServiceRequest` model

---

## 2. BFS and BST

### Breadth-First Search

**File:** `src/main/java/com/ug/dsa/algorithms/BFS.java`

`BFS.traverse(Graph graph, int startVertex)` returns the vertices in the order they are visited, starting from `startVertex` and moving outward level by level.

How it works:

1. Mark the start vertex visited and put it in a `Queue` (the project's own).
2. Remove a vertex from the queue and add it to the result.
3. For each of its neighbours (`graph.getNeighbours`), if it has not been visited, mark it visited and enqueue it.
4. Repeat until the queue is empty.

Validation: a `null` graph throws `IllegalArgumentException`. A start vertex outside `0 .. numVertices-1` throws `IndexOutOfBoundsException`.

**Complexity:** O(V + E) time when each vertex's neighbours are read from an adjacency list, and O(V) extra space for the `visited` array and the queue.

**Use in the project:** BFS finds the fewest-hops order of locations from a starting point. It does not use road distances, so shortest distance is handled by `Dijkstra`.

### Binary Search Tree

**File:** `src/main/java/com/ug/dsa/datastructures/BST.java`

An integer BST with `insert`, `search`, `inorderTraversal` and `isEmpty`. Duplicate values are ignored on insert. `inorderTraversal()` returns the values in ascending order using the project's `DynamicArray`. `IndexingService` uses it as the request-ID index (see section 1).

**Complexity:** `insert` and `search` are O(h), where h is the tree height. This is O(log n) for a balanced tree and O(n) in the worst case, for example when values are inserted in sorted order. The in-order traversal is O(n).

---

## 3. Testing

- `src/test/java/com/ug/dsa/algorithms/BfsTest.java` builds a 6-vertex graph with edges 0-1, 0-2, 1-3, 2-3, 3-4, 4-5, runs `BFS.traverse(graph, 0)` and prints the order. The result visits vertices level by level: 1 and 2 (one step from 0) before 3, then 4, then 5.
- `src/test/java/com/ug/dsa/datastructures/BSTTest.java` tests the BST.
- To run a test, run its `main` method (for example with the Run button in VS Code).
- `.\gradlew clean build` completes with `BUILD SUCCESSFUL` on the integrated `dev` branch.
