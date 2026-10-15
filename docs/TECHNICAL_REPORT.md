# UNIVERSITY OF GHANA
## DEPARTMENT OF COMPUTER SCIENCE

# GHANA SMART SERVICE OPERATIONS OPTIMIZER

### Technical Project Report

**Course:** DCIT 204/308 – Data Structures and Algorithms I & II  
**Project Context:** University of Ghana, Legon Campus Service Hub  
**Implementation Language:** Java  
**Database:** PostgreSQL using JDBC, with CSV fallback  
**Build and Testing Framework:** Gradle and JUnit 5

---

## Document Information

| Report item | Details |
|---|---|
| Institution | University of Ghana |
| Department | Department of Computer Science |
| Project title | Ghana Smart Service Operations Optimizer |
| Course | DCIT 204/308: Data Structures and Algorithms I & II |
| Programming language | Java |
| Database | PostgreSQL |
| Development approach | Object-oriented and modular design |

---

# 1. Project Summary

The Ghana Smart Service Operations Optimizer is a Java-based application designed to support the management of service operations within the University of Ghana, Legon campus. The system brings together service requests, operational resources, campus locations, and road connections to demonstrate how data structures and algorithms can be applied to practical scheduling, routing, searching, and resource-allocation problems.

The application uses a console-based interface through which users can load data, search for service requests, schedule tasks, determine routes between locations, construct minimum spanning networks, and compare different approaches to resource allocation. PostgreSQL provides relational data storage, while CSV files provide an alternative source of data when the database is unavailable.

The system uses several custom data structures, including dynamic arrays, linked lists, stacks, queues, circular queues, deques, heaps, binary search trees, red-black trees, B-trees, hash tables, disjoint sets, and graphs. These structures support the implementation of algorithms such as linear search, binary search, selection sort, insertion sort, merge sort, quicksort, breadth-first search, depth-first search, Dijkstra's shortest-path algorithm, Prim's algorithm, Kruskal's algorithm, greedy scheduling, and dynamic-programming knapsack optimisation.

The dataset contains 52 locations, 107 roads, 300 service requests, and 30 resources. These records provide a foundation for demonstrating the system's major operations and meet the stated minimum dataset sizes for these four categories.

The project demonstrates how algorithmic techniques can be used to organise service requests, identify routes, and allocate limited resources. It also provides an opportunity to compare algorithms with different computational characteristics and understand the trade-offs between simplicity, efficiency, and solution quality.

Further testing and evaluation are necessary to establish the system's performance under different workloads. In particular, the project requires a complete test run, measured benchmark results, performance graphs, database-operation evidence, and detailed algorithm traces to support its final evaluation.

# 2. Introduction and Local Problem Context

## 2.1 Background

The University of Ghana, Legon campus, contains lecture halls, hostels, laboratories, administrative offices, service centres, and other facilities connected by roads and pathways. The operation of such an environment involves activities such as maintenance, delivery, transportation, and responding to service requests.

Managing these activities can become challenging when several requests must be handled within limited time and resource constraints. Some requests may require urgent attention, while others may depend on the availability, location, or capacity of a particular resource. Identifying suitable routes between locations is also important when planning service operations.

Data structures and algorithms provide useful techniques for addressing these problems. Queues can organise requests according to arrival order, priority queues can support urgency-based scheduling, graphs can represent campus locations and roads, and shortest-path algorithms can determine routes according to defined travel costs. Optimisation algorithms can also help select a suitable combination of requests when resources have limited capacity.

The Ghana Smart Service Operations Optimizer applies these concepts to a campus service environment. It provides a practical demonstration of how theoretical concepts from data structures and algorithms can be combined in one application.

## 2.2 Problem Statement

Service operations require an organised way to store requests, determine their processing order, identify appropriate resources, and calculate routes between locations. When these activities are handled without systematic methods, it may become difficult to prioritise urgent work, manage limited capacity, and compare alternative operational decisions.

The main problem addressed by this project is how to use appropriate data structures and algorithms to support the efficient organisation and management of campus service operations.

The system is designed to address the following questions:

- How can service requests be stored and retrieved efficiently?
- How can requests be scheduled according to arrival order or urgency?
- How can the system determine whether two campus locations are connected?
- How can a route with the lowest configured travel cost be identified?
- How can a minimum spanning network be constructed from the campus road graph?
- How can limited resource capacity be used to select suitable service requests?
- How does the performance of different algorithms change as the input size increases?

## 2.3 Project Context

The project uses the University of Ghana, Legon campus, as its operational setting. Campus locations and road connections are represented as graph data, while service requests and resources provide the information needed for scheduling and optimisation.

The dataset contains location, road, service-request, and resource records. Where operational requests or resource details are simulated, they should be identified as such. This distinction is important because simulated records are useful for testing algorithms but should not be interpreted as evidence of actual campus demand.

The project does not depend on live GPS data, real-time traffic information, or a live campus dispatch service. Instead, it focuses on demonstrating the use of data structures and algorithms with a defined dataset.

## 2.4 Project Objectives

The main objective is to develop a Java application that applies data structures and algorithms to campus service operations.

The specific objectives are to:

1. Represent campus locations and their road connections using a weighted graph.
2. Store and retrieve operational data using PostgreSQL and JDBC.
3. Provide CSV-based data loading as an alternative when the database is unavailable.
4. Implement different data structures for storing, searching, scheduling, and processing records.
5. Apply searching and sorting algorithms to service-related data.
6. Implement queue-based and priority-based request scheduling.
7. Use graph traversal and shortest-path algorithms to analyse connectivity and routes.
8. Apply minimum spanning tree algorithms to construct low-weight networks.
9. Compare greedy and dynamic-programming approaches to resource allocation.
10. Evaluate algorithm correctness and performance using tests and measurable experiments.

## 2.5 Scope of the Project

The project covers data loading, request searching, scheduling, routing, resource optimisation, and the demonstration of data structures and algorithms through a console interface.

The system includes:

- Campus locations and weighted road connections.
- Service-request and resource records.
- Custom data structures and algorithm implementations.
- PostgreSQL database access through JDBC.
- CSV-based data loading.
- Search, sorting, scheduling, routing, and optimisation services.
- Support for algorithm-run and audit-event records.

The current scope does not establish a production-ready campus dispatch system. Live GPS tracking, real-time traffic updates, a mobile application, production authentication, and deployment for actual campus operations are outside the demonstrated functionality.

# 3. System Requirements

## 3.1 Functional Requirements

| ID | Requirement | Description |
|---|---|---|
| FR-01 | Data loading | Load locations, roads, service requests, and resources. |
| FR-02 | Data persistence | Store and retrieve records using PostgreSQL and JDBC. |
| FR-03 | Search operations | Search for requests and other supported records. |
| FR-04 | Request scheduling | Organise requests using FIFO, deque, and priority-based methods. |
| FR-05 | Graph traversal | Identify reachable locations using BFS and DFS. |
| FR-06 | Shortest-path routing | Calculate a minimum-weight route using Dijkstra's algorithm. |
| FR-07 | Network construction | Generate minimum spanning networks using Prim's and Kruskal's algorithms. |
| FR-08 | Resource allocation | Select eligible service requests using greedy and knapsack approaches. |
| FR-09 | Operational records | Support algorithm-run and audit-event records. |
| FR-10 | User interaction | Allow users to execute supported operations through a console menu. |

## 3.2 Non-Functional Requirements

**Correctness:** Algorithms should produce valid results for normal inputs and handle boundary conditions such as empty collections, duplicate values, disconnected graphs, and invalid inputs.

**Maintainability:** The system separates models, data structures, algorithms, services, and database operations into different packages.

**Portability:** Java and Gradle provide the application environment, while database connection settings can be supplied through environment variables.

**Data integrity:** Relational constraints should help maintain valid identifiers and relationships between locations, roads, requests, and resources.

**Transparency:** Algorithm traces, test results, benchmark measurements, and limitations should be documented so that the system's behaviour can be evaluated.

**Efficiency:** Appropriate data structures and algorithms should be selected according to the operations they support and their expected computational costs.

# 4. Input and Output Specification

| Operation | Input | Expected output |
|---|---|---|
| Data loading | CSV files or database records | Loaded models, indexes, and graph |
| FIFO scheduling | Service requests with submission times | Next request according to arrival order |
| Priority scheduling | Eligible requests and priority rules | Request selected according to priority |
| Request search | Request identifier or category | Matching request or an empty result |
| BFS/DFS | Graph and starting location | Traversal order and reachable locations |
| Dijkstra routing | Graph, source, and destination | Route and total configured weight |
| Minimum spanning network | Weighted graph | Selected edges forming a spanning tree or forest |
| Greedy allocation | Eligible requests and available resource | Requests accepted under the remaining capacity |
| Knapsack optimisation | Items, values, weights, and capacity | Selected items and maximum calculated value |
| Performance testing | Algorithm and input size | Timing and other measured results |

Binary search requires input ordered according to the same comparison rule used by the search algorithm. Dijkstra's algorithm requires non-negative edge weights. A disconnected graph may produce separate components rather than one spanning tree.

The exact meaning of resource capacity and operational cost must remain consistent throughout the implementation and evaluation. The report should explain whether capacity represents distance, workload, time, or another defined quantity.

# 5. Dataset and Data Dictionary

## 5.1 Dataset Summary

The application uses four main datasets.

| Dataset | Number of records | Purpose |
|---|---:|---|
| Locations | 52 | Represents campus locations and graph vertices |
| Roads | 107 | Represents connections between locations |
| Service requests | 300 | Provides records for searching, scheduling, and optimisation |
| Resources | 30 | Represents resources with types, locations, capacities, and availability |

The dataset sizes satisfy the specified minimums of 50 locations, 100 roads, 300 service requests, and 30 resources.

The system also supports algorithm-run records. A separate `algorithm_runs.csv` file is not available in the current dataset, so the required set of at least 30 recorded algorithm runs must be completed and verified.

## 5.2 Data Dictionary

| Entity | Main fields | Description |
|---|---|---|
| Location | `locationId`, `name`, `area`, `type`, `latitude`, `longitude` | Describes a campus place and graph vertex. |
| Road | `roadId`, `fromLocationId`, `toLocationId`, `distance`, `travelTime`, `roadConditionWeight` | Represents a weighted connection between locations. |
| ServiceRequest | `requestId`, `source`, `destination`, `category`, `urgency`, `timeSubmitted`, `deadline`, `status` | Describes work to be scheduled, routed, or allocated. |
| Resource | `resourceId`, `type`, `homeLocation`, `capacity`, `availabilityStatus` | Describes an operational resource and its availability. |
| AlgorithmRun | `runId`, `algorithmName`, `inputSize`, `timeNs`, `memoryKb`, `dateRun` | Stores measurements associated with an algorithm run. |
| AuditEvent | `eventId`, `eventType`, `requestId`, `timestamp`, `description` | Records an operational event. |

## 5.3 Data Relationships

The main relationships between entities are:

- A location can be the starting point or destination of several roads.
- A location can be the source or destination of several service requests.
- A location can serve as the home location of one or more resources.
- A service request may be associated with operational audit events.
- Algorithm-run records store measurements for particular algorithms and input sizes.

The database schema defines foreign-key relationships between roads and locations, service requests and locations, and resources and their home locations. The precise audit-event relationship should be confirmed against the schema used during execution.



# 6. System Architecture

## 6.1 Architectural Overview

The system follows a modular architecture in which the console interface communicates with the main application engine. The engine coordinates data loading, scheduling, indexing, routing, and optimisation services. These services use domain models, custom data structures, algorithms, and database access objects to perform their tasks.

The architecture separates the main responsibilities of the application to make the code easier to understand, test, and maintain.

### Main components

**Console interface:** Provides the menu through which users select operations.

**Application engine:** Coordinates the services and manages the overall workflow.

**Data-loading service:** Loads records from PostgreSQL or CSV files and prepares the in-memory data.

**Scheduling service:** Organises service requests according to the selected scheduling policy.

**Indexing service:** Provides access to records through search algorithms and indexes.

**Routing service:** Builds and analyses the campus graph using traversal, shortest-path, and minimum spanning tree algorithms.

**Optimisation service:** Selects eligible requests using greedy scheduling and dynamic programming.

**Database layer:** Provides JDBC connections, database initialisation, and data access objects.

**Model layer:** Defines the main entities and operational records.

## 6.2 Package Organisation

| Package | Responsibility | Examples |
|---|---|---|
| `com.ug.dsa` | Application entry point and console menu | `Main.java` |
| `models` | Domain and measurement objects | `Location`, `Road`, `ServiceRequest`, `Resource`, `AlgorithmRun`, `AuditEvent` |
| `datastructures` | Custom structures used by the application | `DynamicArray`, `LinkedList`, `Stack`, `Queue`, `CircularQueue`, `Deque`, `Heap`, `BST`, `RedBlackTree`, `BTree`, `HashTable`, `DisjointSet`, `Graph`, `Edge` |
| `algorithms` | Searching, sorting, graph, and optimisation algorithms | `LinearSearch`, `BinarySearch`, `MergeSort`, `QuickSort`, `BFS`, `DFS`, `Dijkstra`, `Prim`, `Kruskal`, `Knapsack` |
| `database` | Database connection, schema initialisation, and persistence | `DatabaseConnection`, `DatabaseInitializer`, DAO classes |
| `services` | Business operations and coordination | `DataLoaderService`, `SchedulingService`, `IndexingService`, `RoutingService`, `OptimizationService`, `SmartOperationsEngine` |

## 6.3 Main Execution Process

The main execution process follows these steps:

1. The user starts the application and selects the data-loading option.
2. The data-loading service attempts to connect to PostgreSQL and initialise the required tables.
3. Records are loaded from the database where possible. If database access or loading fails, the service can fall back to CSV files.
4. Location identifiers are mapped to graph indexes, and the graph is constructed from the road records.
5. The application engine prepares the scheduling, indexing, routing, and optimisation services.
6. The user selects an operation from the console menu.
7. The relevant service executes the requested operation and returns its result.
8. Where supported, algorithm-run and audit-event information can be stored through the database layer.

# 7. Database Design and Persistence

## 7.1 Database Technology

PostgreSQL is used as the relational database, with JDBC providing communication between the Java application and the database. The application includes database initialisation logic and data access objects for the main entity types.

The Docker Compose configuration defines a PostgreSQL 16 Alpine container, with the database named `ug_smart_service_db` and port `5432` exposed for local connections.

The CSV fallback provides an alternative way to load the application's data when PostgreSQL is unavailable.

## 7.2 Connection Configuration

The database connection reads the following environment variables:

- `DB_URL`
- `DB_USER`
- `DB_PASSWORD`

The default connection URL is:

`jdbc:postgresql://localhost:5432/ug_smart_service_db`

The default local username and password are both `postgres`. These defaults are suitable only for a controlled development environment and should be changed for shared or production use. Database credentials should not be committed to a public repository.

## 7.3 Database Tables

| Table | Primary key | Purpose |
|---|---|---|
| `locations` | `location_id` | Stores campus locations and coordinates. |
| `roads` | `road_id` | Stores road connections and associated weights. |
| `service_requests` | `request_id` | Stores requests, categories, priorities, and status information. |
| `resources` | `resource_id` | Stores resource types, locations, capacities, and availability. |
| `algorithm_runs` | `run_id` | Stores algorithm measurements. |
| `audit_events` | `event_id` | Stores operational event records. |

The database schema includes primary keys and foreign keys to maintain relationships between entities. Numeric constraints are used for selected fields, including non-negative road distance and travel time, valid resource capacity, and service-request urgency values.

## 7.4 Data Access Layer

Separate data access objects are provided for locations, roads, service requests, resources, algorithm runs, and audit events.

This approach separates SQL operations from the application logic. Services can request records through the DAO layer without embedding database queries directly into each algorithm.

The data-loading service connects the database or CSV input to the Java models, graph representation, and custom data structures.

## 7.5 Persistence Verification

The database layer should be evaluated through practical tests rather than relying only on the presence of database classes. The following checks are necessary:

1. Confirm that the required tables are created successfully.
2. Load the expected records and verify the resulting counts.
3. Insert a test record and retrieve it again.
4. Verify that duplicate identifiers and invalid foreign keys are handled correctly.
5. Record an algorithm run and confirm that it remains available after restarting the application.
6. Record and retrieve an audit event.
7. Confirm that CSV fallback works when database access is unavailable.

The results of these checks should be recorded using actual database queries, console output, or screenshots.

# 8. Custom Data Structures

Custom data structures are an important part of the project because they demonstrate how information can be organised to support different operations. Their effectiveness depends on both their implementation and the way they are used by the algorithms.

| Data structure | Application | Typical operation complexity | Important test or demonstration |
|---|---|---|---|
| Dynamic array | Stores resizable sequences of records and results | Index access: O(1); append: amortised O(1); middle insertion/removal: O(n) | Show resizing and boundary conditions. |
| Linked list | Stores elements in linked nodes | Head insertion: O(1); search: O(n) | Demonstrate insertion, traversal, and removal. |
| Stack | Supports last-in, first-out processing | Push/pop/peek: O(1) | Show push/pop order and empty-stack behaviour. |
| Queue | Supports first-in, first-out scheduling | Enqueue/dequeue: O(1) for suitable linked implementations | Demonstrate request arrival order. |
| Circular queue | Reuses a fixed-capacity buffer | Enqueue/dequeue: O(1) | Demonstrate wrap-around and full/empty conditions. |
| Deque | Allows insertion and removal at both ends | O(1) for end operations in suitable implementations | Show front and rear operations. |
| Heap/priority queue | Supports priority scheduling and priority-driven algorithms | Peek: O(1); insertion/extraction: O(log n) | Demonstrate priority ordering and heap property. |
| Binary search tree | Maintains ordered keys | Average search/insert: O(log n); worst case: O(n) | Show search paths and inorder traversal. |
| Red-black tree | Maintains a balanced ordered index | Search/insert: O(log n) | Explain rotations and recolouring. |
| B-tree | Supports ordered multiway searching | O(log n) search in tree height | Demonstrate node splitting and searching. |
| Hash table | Provides direct key-based lookup | Average O(1); worst case O(n) | Test collisions, updates, and missing keys. |
| Disjoint set | Tracks connected components in Kruskal's algorithm | Nearly constant amortised operations with path compression and rank | Show `find` and `union` operations. |
| Graph | Represents campus locations and road connections | Adjacency-list traversal: O(V + E) | Demonstrate connectivity, routing, and spanning networks. |

The custom hash table supports key-based lookup in the data-loading and indexing layers. The custom heap supports priority-driven operations. The graph representation allows the application to apply traversal, shortest-path, and minimum spanning tree algorithms to campus locations.

In the routing implementation, distance weights are represented as integer hundredths, using the relationship:

**Stored weight = distance × 100**

The conversion should be applied consistently when calculating and displaying route costs so that the reported values retain their intended meaning.

The existence of a custom structure does not automatically establish that every required operation is correct. Each structure should be supported by suitable tests and a trace demonstrating its behaviour.

# 9. Algorithms and Complexity Analysis

## 9.1 Searching and Sorting Algorithms

| Algorithm | Purpose | Time complexity | Important consideration |
|---|---|---|---|
| Linear search | Finds an item in an unsorted sequence | Best O(1), worst O(n) | Does not require sorted input. |
| Binary search | Searches an ordered sequence | Best O(1), worst O(log n) | Requires correctly sorted input. |
| Selection sort | Repeatedly selects the smallest remaining item | O(n²) | Simple but inefficient for large inputs. |
| Insertion sort | Inserts elements into their correct positions | Best O(n), average/worst O(n²) | Effective for small or nearly sorted inputs. |
| Merge sort | Divides and merges sorted sections | O(n log n) | Requires additional memory in the standard implementation. |
| Quicksort | Partitions elements around a pivot | Average O(n log n), worst O(n²) | Performance depends on pivot selection and input arrangement. |

These algorithms provide different approaches to searching and sorting. Linear search is suitable when data is unsorted or small, while binary search can reduce the number of comparisons when the input is ordered.

Selection sort and insertion sort are useful for understanding elementary sorting techniques. Merge sort and quicksort demonstrate divide-and-conquer approaches and provide opportunities to study performance on larger inputs.

## 9.2 Graph Algorithms

| Algorithm | Purpose | Typical time complexity | Important consideration |
|---|---|---|---|
| Breadth-first search (BFS) | Traverses reachable vertices level by level | O(V + E) with an adjacency list | Finds paths with the fewest edges in an unweighted graph, not necessarily the lowest weighted cost. |
| Depth-first search (DFS) | Explores a path before backtracking | O(V + E) with an adjacency list | Traversal order depends on neighbour ordering. |
| Dijkstra's algorithm | Finds shortest paths with non-negative weights | O((V + E) log V) with a suitable heap and adjacency list | Negative edge weights are not supported. |
| Prim's algorithm | Builds a minimum spanning tree | O(E log V) with a suitable heap and adjacency list | A disconnected graph requires component-aware handling. |
| Kruskal's algorithm | Builds a minimum spanning tree using sorted edges | O(E log E) | A disjoint set helps prevent cycles. |

The graph represents locations as vertices and roads as edges. Road weights represent the configured cost of travelling between locations.

BFS and DFS provide information about graph connectivity. Dijkstra's algorithm determines a minimum-weight route between locations. Prim's and Kruskal's algorithms construct a minimum spanning network by selecting edges while avoiding unnecessary cycles.

## 9.3 Scheduling and Optimisation Algorithms

The application uses priority-based scheduling and two approaches to selecting service requests under resource constraints.

The greedy approach selects eligible requests according to a priority rule and accepts requests that fit within the remaining capacity. Its decisions are made incrementally, so an early selection can affect the value of the final result.

The dynamic-programming approach uses the 0/1 knapsack problem to evaluate combinations of items under a capacity constraint.

| Algorithm | Purpose | Typical complexity | Limitation |
|---|---|---|---|
| Greedy scheduling | Selects compatible requests according to priority | Commonly O(n log n) for heap operations, excluding additional route-cost calculations | May not produce the globally optimal selection. |
| 0/1 knapsack dynamic programming | Finds a maximum-value item selection under a capacity limit | O(nC) time, where C is capacity | Pseudo-polynomial; large capacities may require substantial memory. |

## 9.4 Greedy and Dynamic-Programming Comparison

The greedy method prioritises eligible requests and accepts a request when its calculated cost fits within the remaining capacity. The knapsack method represents requests as items with costs and values, then uses dynamic programming to determine a high-value selection.

The value calculation used by the implementation is:

`value = max(1, 6 - urgency)`

This formula assigns a higher value to lower urgency numbers. The urgency convention must therefore remain consistent: if urgency 1 represents the highest priority, the formula gives urgency 1 the greatest value among the usual urgency levels.

A standard example illustrates why a greedy approach can be suboptimal. Suppose the capacity is 10 and three items have the following cost-value pairs:

| Item | Cost | Value |
|---|---:|---:|
| A | 6 | 12 |
| B | 5 | 10 |
| C | 5 | 10 |

A greedy method that selects item A first obtains a value of 12 and cannot add either of the remaining items. Selecting B and C produces a total value of 20 while using the full capacity.

This example demonstrates the limitation of a value-first greedy rule. However, if the implemented scheduler prioritises urgency rather than value, the example is illustrative rather than a direct test of the implementation. A separate counterexample should be constructed using the scheduler's actual priority and cost rules.

# 10. Operational Workflows and Pseudocode

## 10.1 Data Loading and Graph Construction

The loading process prepares the application data for use by the other services.

```text
LOAD-AND-BUILD

Reset in-memory collections.

If PostgreSQL is reachable:
    Initialise the required database tables.
    Attempt to load records from the database.
    If loading fails:
        Load records from CSV files.
Otherwise:
    Load records from CSV files.

Map each location ID to a graph index.

Create one graph vertex for each location.

For each road:
    Map its endpoint IDs to graph indexes.
    Add the weighted edge to the graph.

Return the loaded-record summary.
```

## 10.2 Priority-Based Resource Allocation

```text
GREEDY-ALLOCATE(requests, resource)

Create an empty selected-request list.

If the resource is absent or unavailable:
    Return the empty list.

Create an empty priority heap.

For each request:
    If the request is valid and pending
       and its type is compatible with the resource:
        Insert it into the heap according to urgency.

Set remaining capacity to resource capacity.

While the heap is not empty:
    Extract the highest-priority request.
    Calculate its operational cost.

    If the cost is valid and does not exceed
       the remaining capacity:
        Add the request to the selected list.
        Reduce the remaining capacity.

Return the selected requests.
```

The cost calculation must be consistent with the meaning of resource capacity. Requests that exceed the remaining capacity should not be accepted. The scheduler's actual behaviour should also be tested for tied priorities, unavailable resources, and cases where no requests are eligible.

## 10.3 Dijkstra's Shortest-Path Algorithm

```text
DIJKSTRA(G, source)

For each vertex v:
    distance[v] = infinity
    predecessor[v] = undefined

distance[source] = 0
Insert source into a minimum-priority heap.

While the heap is not empty:
    Extract vertex u with the smallest tentative distance.

    For each edge (u, v, weight):
        If distance[u] + weight < distance[v]:
            distance[v] = distance[u] + weight
            predecessor[v] = u
            Update or insert v in the heap.

Return distances and predecessors.
```

The algorithm calculates the shortest distances from a source vertex to other reachable vertices. The predecessor information can be used to reconstruct the route to a destination.

Correctness depends on non-negative edge weights and correct priority-queue handling. Unreachable destinations must be reported explicitly rather than treated as ordinary routes.

## 10.4 Binary Search

```text
BINARY-SEARCH(A, key)

Require A to be sorted according to the search ordering.

low = 0
high = length(A) - 1

While low <= high:
    mid = floor((low + high) / 2)

    If A[mid] equals key:
        Return mid.

    If A[mid] < key:
        low = mid + 1
    Otherwise:
        high = mid - 1.

Return NOT_FOUND.
```

Binary search reduces the search interval by approximately half during each iteration. Its sorted-input requirement is essential.

For example, the array `[1, 4, 2, 5]` is not sorted. Searching this array using binary search does not guarantee a correct result, even if the requested value exists. A trace using the actual implementation should be included to demonstrate this precondition.

## 10.5 0/1 Knapsack Dynamic Programming

```text
KNAPSACK(items, capacity C)

Initialise dp[0..n][0..C] to zero.

For i from 1 to n:
    For c from 0 to C:
        dp[i][c] = dp[i-1][c]

        If weight[i] <= c:
            dp[i][c] = max(
                dp[i-1][c],
                value[i] + dp[i-1][c-weight[i]]
            )

Trace backward through the table to reconstruct
the selected items.

Return the selected items and maximum value.
```

The table considers the best value obtainable from each prefix of the items for each capacity. At every step, the algorithm compares excluding the current item with including it once, provided that it fits.

# 11. Correctness and Testing

## 11.1 Correctness Arguments

Correctness arguments explain why an algorithm produces the intended result under its stated assumptions.

### Binary Search

The loop invariant states that if the search key exists in the sorted input, it remains within the current search interval. Each comparison removes a portion of the array that cannot contain the key. The algorithm terminates when the key is found or the interval becomes empty.

### Insertion Sort

Before each outer-loop iteration, the portion of the array preceding the current position is sorted and contains the same elements as the corresponding portion of the original input. The inner loop shifts larger elements to the right and inserts the current key into its correct position. When the algorithm terminates, the entire array is sorted.

### Dijkstra's Algorithm

Assuming all edge weights are non-negative, the vertex with the smallest tentative distance has reached its shortest distance when it is extracted from the priority queue. No path through an unprocessed vertex can subsequently improve that distance. This argument depends on the correct implementation of the priority queue and distance updates.

### Kruskal's Algorithm

Kruskal's algorithm considers edges in nondecreasing order of weight. It adds an edge only when its endpoints belong to different connected components, preventing the creation of a cycle. The cut property supports selecting a minimum-weight edge that joins separate components while maintaining the possibility of a minimum spanning forest.

### 0/1 Knapsack

For each number of considered items and each capacity, the recurrence compares the best solution that excludes the current item with the best solution that includes it, provided it fits. By induction over the number of items, the table contains the maximum attainable value for every considered capacity.


# 12. Setup and Execution Guide

## 12.1 Requirements

The application requires:

- A JDK compatible with the Gradle configuration; Java 21 is recommended by the project documentation.
- The included Gradle wrapper.
- PostgreSQL 16 or a compatible PostgreSQL installation, unless using the CSV fallback.
- Network access for initial Gradle distribution and dependency downloads, or an existing local cache.

## 12.2 Starting the Database

Run the following command from the project directory:

```bash
docker compose up -d
```

The default database is `ug_smart_service_db`, and the host port is `5432`.

The default local connection URL is:

```text
jdbc:postgresql://localhost:5432/ug_smart_service_db
```

For shared environments, configure the database credentials securely and supply the `DB_URL`, `DB_USER`, and `DB_PASSWORD` environment variables.

## 12.3 Building and Running the Application

On Linux or macOS, use:

```bash
./gradlew test
./gradlew run
```

On Windows, use:

```bat
gradlew.bat test
gradlew.bat run
```

Alternatively, open the project in IntelliJ IDEA, allow Gradle synchronisation to complete, and run the `com.ug.dsa.Main` class.

After starting the application, select the data-loading option before performing operations that depend on loaded records.


# 13. Conclusion 

The Ghana Smart Service Operations Optimizer demonstrates how data structures and algorithms can be applied to the organisation of campus service operations. The application combines a campus-oriented dataset, relational database access, custom data structures, searching and sorting algorithms, graph algorithms, request scheduling, and resource optimisation within a modular Java application.

The dataset contains 52 locations, 107 roads, 300 service requests, and 30 resources, meeting the required minimum record counts for these categories. The system's main components provide a foundation for demonstrating graph connectivity, shortest-path routing, minimum spanning networks, priority-based scheduling, and capacity-constrained selection.

The project also illustrates important differences between algorithms. Binary search requires ordered data, Dijkstra's algorithm depends on non-negative edge weights, and greedy allocation can produce a different result from dynamic programming. Understanding these differences is necessary when choosing an algorithm for a particular task.

The next stage should focus on completing the project's evaluation evidence. The team should run and document the test suite, verify database persistence, provide algorithm traces, record at least 30 algorithm runs, generate performance graphs, and document the source and construction of the dataset. The final report should include the actual outputs of these activities.

With these steps completed, the project will provide a stronger demonstration of the relationship between theoretical algorithm analysis and practical software implementation.

# References

Cormen, T. H., Leiserson, C. E., Rivest, R. L., & Stein, C. (2022). *Introduction to Algorithms* (4th ed.). MIT Press.

Goodrich, M. T., Tamassia, R., & Goldwasser, M. H. (2014). *Data Structures and Algorithms in Java* (6th ed.). Wiley.

Sedgewick, R., & Wayne, K. (2011). *Algorithms* (4th ed.). Addison-Wesley.

University of Ghana, Department of Computer Science. *Joint DSA Semester Project: Ghana Smart Service Operations Optimizer, DCIT 204/308*. Project brief.
