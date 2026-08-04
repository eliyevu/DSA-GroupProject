# Smart Service Operations Optimizer

## Week One – Individual Task Allocation

### Objective

The objective for Week One is to complete the **first working implementation** of the assigned **Data Structure** and **Algorithm**. By the end of the week, every member should have functional code committed to the GitHub repository.

---

# General Requirements (All Members)

Every member is expected to:

* Research and understand the assigned Data Structure and Algorithm.
* Design the implementation before coding.
* Implement both components from scratch.
* Follow Java coding standards and Object-Oriented Programming principles.
* Ensure the code compiles and runs successfully.
* Push all completed work to GitHub.
* Create a Pull Request to the `dev` branch.
* Be prepared to demonstrate and explain their implementation during the team meeting.

> **Note:** Unit testing, documentation, complexity analysis, trace tables, and performance evaluation will be completed in later phases of the project.

---

# Member 1 - JOHNSON, ELIZABETH

## Data Structure

**Dynamic Array**

* Implement a custom Dynamic Array.
* Implement:

    * Add
    * Remove
    * Get
    * Set
    * Resize

## Algorithm

**Linear Search**

* Implement Linear Search.
* Ensure it works with the Dynamic Array.

---

# Member 2 - OTSIWA, MAXWELL AGYAKWA

## Data Structure

**Linked List**


* Implement a Linked List.
* Support insertion, deletion and traversal.

## Algorithm

**Binary Search**

* Implement Binary Search.
* Ensure it works correctly on sorted data.

---

# Member 3 - 	KYEI, ENOCK

## Data Structure

**Stack**

* Implement:

    * Push
    * Pop
    * Peek
    * isEmpty

## Algorithm

**Selection Sort**

* Implement Selection Sort.
* Ensure it sorts correctly.

---

# Member 4 - RAFIU, ABDUL RAZAK

## Data Structure

**Queue**

* Implement:

    * Enqueue
    * Dequeue
    * Front
    * isEmpty

## Algorithm

**Insertion Sort**

* Implement Insertion Sort.

---

# Member 5 - 	SUKAH, JERRY ELI

## Data Structure

**Circular Queue**


* Implement a Circular Queue.
* Handle wrap-around correctly.

## Algorithm

**Merge Sort**

* Implement Merge Sort.

---

# Member 6 - 	ARMAH, FRANKLYN NII ARYEETEY

## Data Structure

**Deque**

* Implement insertion and deletion from both ends.

## Algorithm

**Quick Sort**

* Implement Quick Sort.

---

# Member 7 - AMOAH, EDWARD JUNIOR

## Data Structure

**Heap / Priority Queue**


* Implement a Min Heap.
* Support insertion and deletion.

## Algorithm

**Greedy Algorithm**

* Implement a Greedy solution for resource allocation or scheduling.

---

# Member 8 - ABIWU, KELVIN NUTIFAFA KWESI

## Data Structure

**Binary Search Tree (BST)**

* Implement insertion.
* Implement searching.
* Implement inorder traversal.

## Algorithm

**Breadth-First Search (BFS)**

* Implement BFS using the Graph data structure.

---

# Member 9 - 	Amartey Felix Laryea

## Data Structure

**Red-Black Tree**


* Implement node insertion.
* Implement balancing (rotations and recolouring).

## Algorithm

**Depth-First Search (DFS)**

* Implement DFS.

---

# Member 10 - Roselyn Francis

## Data Structure

**B-Tree**

* Implement insertion.
* Implement searching.

## Algorithm

**Dijkstra's Algorithm**

* Implement Dijkstra's shortest path algorithm.

---

# Member 11 - Irene Darah-Mensah

## Data Structure

**Hash Table**

* Implement a Hash Table.
* Support insertion, searching and deletion.
* Handle collisions.

## Algorithm

**Prim's Algorithm**

* Implement Prim's Minimum Spanning Tree algorithm.

---

# Member 12 - NANA KOFI AGYIN

## Data Structure

**Graph**

* Implement both Adjacency List and Adjacency Matrix representations.
* Support adding vertices and edges.

## Algorithm

**Kruskal's Algorithm**

* Implement Kruskal's Minimum Spanning Tree algorithm.

---

# Member 13 - ABIA-WILLIAMS, ALVIN

## Data Structure

**Disjoint Set (Union-Find)**


* Implement:

    * MakeSet
    * Find
    * Union

## Algorithm

**Dynamic Programming (Knapsack)**


* Implement the 0/1 Knapsack algorithm.

---

# Member 14 - MENSAH-EFFINBURG, NYNEL BUADU

## Data Structure

**Graph (Co-Implementation)**

* Assist in implementing the custom Graph data structure.
* Implement one of the graph representations (Adjacency Matrix or Adjacency List).
* Implement core graph operations:

  * Add Vertex
  * Remove Vertex
  * Add Edge
  * Remove Edge
  * Retrieve Neighbours
* Ensure the Graph implementation is compatible with all graph algorithms.

### Collaboration

Work closely with:

* **Nana Kofi Agyin** (Graph Lead)



# Component Dependencies & Collaboration

To ensure consistency across the project, **all algorithms must be implemented using the team's custom data structures**. Members **must not use Java's built-in data structures** (such as `ArrayList`, `LinkedList`, `HashMap`, `PriorityQueue`, `TreeMap`, etc.) for the core implementations unless explicitly approved by the Project Lead.

Each algorithm owner is expected to collaborate with the member(s) responsible for the required data structure(s).

---

## Data Structure Dependencies

### Dynamic Array

Used by:

* Linear Search
* Binary Search
* Selection Sort
* Insertion Sort
* Merge Sort
* Quick Sort

**Owner:** Johnson, Elizabeth

---

### Linked List

Used by:

* Any future features that require sequential storage.

**Owner:** Otsiwa, Maxwell Agyakwa

---

### Stack

Used by:

* Depth-First Search (DFS)

**Owner:** Kyei, Enock

---

### Queue

Used by:

* Breadth-First Search (BFS)

**Owner:** Rafiu, Abdul Razak

---

### Circular Queue

Used by:

* Shuttle dispatch and request scheduling modules during system integration.

**Owner:** Sukah, Jerry Eli

---

### Deque

Used by:

* Priority handling modules during system integration.

**Owner:** Armah, Franklyn Nii Aryeetey

---

### Heap / Priority Queue

Used by:

* Dijkstra's Algorithm
* Prim's Algorithm
* Greedy Algorithm

**Owner:** Amoah, Edward Junior

---

### Binary Search Tree (BST)

Used by:

* Fast searching and indexing features during system integration.

**Owner:** Abiwu, Kelvin Nutifafa Kwesi

---

### Red-Black Tree

Used by:

* Balanced indexing features during system integration.

**Owner:** Amartey Felix Laryea

---

### B-Tree

Used by:

* Database indexing simulation.

**Owner:** Roselyn Francis

---

### Hash Table

Used by:

* Fast record lookup modules.

**Owner:** Irene Darah-Mensah

---

### Graph

Used by:

* Breadth-First Search (BFS)
* Depth-First Search (DFS)
* Dijkstra's Algorithm
* Prim's Algorithm
* Kruskal's Algorithm

**Owner:** Nana Kofi Agyin

---

### Disjoint Set (Union-Find)

Used by:

* Kruskal's Algorithm

**Owner:** Abia-Williams, Alvin

---

# Collaboration Requirements

The following members must work together throughout Week One to ensure compatibility between their implementations.

| Algorithm                      | Required Data Structure(s) | Collaborate With                     |
| ------------------------------ | -------------------------- | ------------------------------------ |
| Linear Search                  | Dynamic Array              | Johnson, Elizabeth                   |
| Binary Search                  | Dynamic Array              | Johnson, Elizabeth                   |
| Selection Sort                 | Dynamic Array              | Johnson, Elizabeth                   |
| Insertion Sort                 | Dynamic Array              | Johnson, Elizabeth                   |
| Merge Sort                     | Dynamic Array              | Johnson, Elizabeth                   |
| Quick Sort                     | Dynamic Array              | Johnson, Elizabeth                   |
| Breadth-First Search (BFS)     | Graph, Queue               | Nana Kofi Agyin, Rafiu Abdul Razak   |
| Depth-First Search (DFS)       | Graph, Stack               | Nana Kofi Agyin, Kyei Enock          |
| Dijkstra's Algorithm           | Graph, Heap                | Nana Kofi Agyin, Amoah Edward Junior |
| Prim's Algorithm               | Graph, Heap                | Nana Kofi Agyin, Amoah Edward Junior |
| Kruskal's Algorithm            | Graph, Disjoint Set        | Nana Kofi Agyin, Abia-Williams Alvin |
| Greedy Algorithm               | Heap / Priority Queue      | Amoah Edward Junior                  |
| Dynamic Programming (Knapsack) | Dynamic Array              | Johnson, Elizabeth                   |

---

# API Design Requirement

Before implementing dependent algorithms, each Data Structure owner must publish the public methods (API) for their implementation. Algorithm owners must use these agreed method signatures when developing their solutions.

For example, the Queue implementation should expose methods such as:

* `enqueue()`
* `dequeue()`
* `peek()`
* `isEmpty()`
* `size()`

Similarly, the Graph implementation should provide methods such as:

* `addVertex()`
* `removeVertex()`
* `addEdge()`
* `removeEdge()`
* `getNeighbours()`
* `getVertices()`

---

## Git Workflow

1. Pull the latest changes from the `dev` branch.
2. Implement your assigned task.
3. Commit your changes with a meaningful commit message.
4. Push your work to the `dev` branch.
5. Open a Pull Request for review.
6. Address review comments if any.


# Week One Deliverables & Submission Requirements

By the end of Week One, every member must have completed the following:

* ✅ Implemented their assigned **Data Structure** from scratch.
* ✅ Implemented their assigned **Algorithm** using the team's agreed custom data structure(s).
* ✅ Ensured the code compiles and runs successfully.
* ✅ Added all completed code to their local Git repository.
* ✅ Pushed their code to the **`dev`** branch on GitHub.
* ✅ Opened a **Pull Request (PR)** for review.
* ✅ Ensured the Pull Request is ready for review and merging.
* ✅ Be prepared to demonstrate and explain both implementations during the Week One project review meeting.

> **Important:** All development for Week One must be committed to the **`dev`** branch. No member should commit or push directly to the **`main`** branch. All code changes must be submitted through a Pull Request for review before being merged.
