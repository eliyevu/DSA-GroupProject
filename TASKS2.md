\# Smart Service Operations Optimizer — Remaining Coding Tasks

## Project Goal

Complete the remaining coding work for the **Smart Service Operations Optimizer** after the custom data structures and algorithms have been completed.

The implementation must remain simple, integrated, and faithful to the project brief.

---

# 1. Final Project Structure

```text
SmartServiceOperationsOptimizer/
│
├── PROJECT_CONTRACT.md
├── README.md
├── Main.java
│
├── models/
│   ├── Location.java
│   ├── Road.java
│   ├── ServiceRequest.java
│   ├── Resource.java
│   ├── AlgorithmRun.java
│   └── AuditEvent.java
│
├── database/
│   ├── DatabaseConnection.java
│   ├── DatabaseInitializer.java
│   ├── LocationDAO.java
│   ├── RoadDAO.java
│   ├── ServiceRequestDAO.java
│   ├── ResourceDAO.java
│   ├── AlgorithmRunDAO.java
│   └── AuditEventDAO.java
│
├── services/
│   ├── DataLoaderService.java
│   ├── SchedulingService.java
│   ├── IndexingService.java
│   ├── RoutingService.java
│   ├── OptimizationService.java
│   └── SmartOperationsEngine.java
│
├── algorithms/          ← COMPLETED
└── datastructures/      ← COMPLETED
```

---

# 2. Team Allocation — 15 Members

| Team                           |                                                  Members | Main Responsibility |
|--------------------------------|---------------------------------------------------------:|---|
| Team 1 — Models & Contracts    |                       Nynel Mensah<br/>Joseph Osei Boadi | Models, interfaces, shared contracts |
| Team 2 — Database & Data       | Rafiu Abdul Razak<br/>Roselyn Francis<br/>Maxwell Otsiwa | PostgreSQL, DAOs, schema, seed data |
| Team 3 — Scheduling            |               Alvin Abia-Williams<br/>Irene Darah-Mensah | Scheduling engine |
| Team 4 — Indexing/Search       |            Edward Amoah Junior<br/>Abiwu Kelvin Nutifafa | Indexing engine |
| Team 5 — Routing               |                       Armah Franklyn<br/>Sukah Jerry Eli | Graph/routing engine |
| Team 6 — Optimization          |                             Enock Kyei<br/>Amartey Felix | Greedy + Dynamic Programming |
| Team 7 — Integration & Testing |                    Nana Kofi Agyin<br/>Elizabeth Johnson | Main, engine integration, testing |

**Total: 15 members**

---

# 3. TEAM 1 — Models & Contracts (Nynel Mensah, Joseph Osei Boadi)

## Files

```text
models/
├── Location.java
├── Road.java
├── ServiceRequest.java
├── Resource.java
├── AlgorithmRun.java
└── AuditEvent.java
```

## Responsibilities

### Location.java
Represents a campus location / graph vertex.

Required fields: 

- int locationId
- String name
- String area
- String type
- double latitude
- double longitude

### Road.java
Represents a road / graph edge.

Required fields should support:

- int roadId
- int fromLocationId
- int toLocationId
- double distance
- double travelTime
- doubble roadConditionWeight

### ServiceRequest.java
Represents a campus service operation/request.

Required fields:

- int requestId
- int source
- int destination 
- String category
- int urgency
- String timeSubmitted
- String deadline
- String status

### Resource.java
Represents an available operational resource.

Required fields:

- int resourceId
- String type
- int homeLocation
- int capacity
- String availabilityStatus

### AlgorithmRun.java
Stores empirical algorithm-performance results.

Required fields:

- int runId
- String algorithmName
- int inputSize
- long timeNs
- double memoryKb
- String dateRun

### AuditEvent.java
Stores important system events.

Possible event types:

- REQUEST_CREATED
- REQUEST_ASSIGNED
- REQUEST_COMPLETED
- RESOURCE_ALLOCATED
- REQUEST_CANCELLED

## Critical Deliverable: PROJECT_CONTRACT.md

Before other teams begin serious implementation, document:

- final model fields
- data types
- constructors
- getters/setters
- enums
- relationships
- method signatures
- expected inputs/outputs
- existing data-structure APIs that services must use

### RULE

Once the models and contracts are agreed upon, **do not change them independently**.

Any breaking change must be discussed with all affected teams first.

---

# 4. TEAM 2 — Database & Data (Rafiu Abdul Razak, Roselyn Francis, Maxwell Otsiwa)

## Files

```text
database/
├── DatabaseConnection.java
├── DatabaseInitializer.java
├── LocationDAO.java
├── RoadDAO.java
├── ServiceRequestDAO.java
├── ResourceDAO.java
├── AlgorithmRunDAO.java
└── AuditEventDAO.java
```

## Responsibilities

### DatabaseConnection.java

- Connect Java to PostgreSQL
- Provide/reuse database connections
- Handle connection errors
- Avoid hardcoding sensitive credentials where possible

### DatabaseInitializer.java

Create the required tables and constraints.

Core tables:

```text
locations
roads
service_requests
resources
algorithm_runs
audit_events
```

### DAO Classes

Implement database operations for each entity.

At minimum:

```text
create/save
findById
findAll
update
delete
```

Additional operations can be added where needed, e.g.:

```text
getPendingRequests()
getAvailableResources()
updateRequestStatus()
updateResourceAvailability()
```

## Dataset

Create:

```text
data/
├── locations.csv
├── roads.csv
├── service_requests.csv
└── resources.csv
```

Minimum dataset requirements:

- 50 locations
- 100 roads/edges
- 300 service requests
- 30 resources
- 30 algorithm-run records

All IDs must be consistent across the dataset.

Example:

```text
Location 1 = UG Main Gate
```

Every team must use the same meaning for Location 1.

---

# 5. TEAM 3 — Scheduling (Alvin Abia-Williams, Irene Darah-Mensah)

## File

```text
services/SchedulingService.java
```

## Responsibilities

Build the campus service-request scheduling engine using the completed custom data structures.

Must demonstrate:

- FIFO queue
- Circular queue
- Deque
- Priority queue / heap

Possible operations:

```text
scheduleFIFO()
scheduleByPriority()
scheduleUrgent()
getNextRequest()
```

## Main question answered

> Which service request should be handled next?

## Dependency

Requires:

- ServiceRequest model
- Resource model where needed
- Existing queue/deque/heap structures

Do NOT create another implementation of the data structures.

---

# 6. TEAM 4 — Indexing/Search (Edward Amoah Junior, Abiwu Kelvin Nutifafa)

## File

```text
services/IndexingService.java
```

## Responsibilities

Use the completed search/indexing structures:

- BST
- Red-Black Tree
- B-Tree
- Hash Table

Possible searchable data:

- Service requests
- Locations
- Resources

Possible operations:

```text
findRequestById()
findRequestsByCategory()
findLocation()
findResource()
```

## Dependency

Requires:

- agreed model definitions
- existing tree/hash-table implementations

Do NOT create duplicate data structures.

---

# 7. TEAM 5 — Routing (Armah Franklyn, Sukah Jerry Eli)

## File

```text
services/RoutingService.java
```

## Responsibilities

Integrate the existing Graph and graph algorithms with the campus location/road data.

Must support:

- BFS
- DFS
- Dijkstra
- Prim
- Kruskal

Possible operations:

```text
findReachableLocations()
findShortestRoute()
buildMinimumNetwork()
```

## IMPORTANT DEPENDENCY CHECK

Before implementation, confirm with the existing Graph/algorithm code:

- vertex representation
- edge representation
- vertex IDs
- edge weights
- adjacency-list API
- adjacency-matrix API
- expected algorithm inputs
- algorithm return types

The Routing team must use the existing Graph and algorithms rather than creating another Graph implementation.

---

# 8. TEAM 6 — Optimization (Enock Kyei, Amartey Felix)

## File

```text
services/OptimizationService.java
```

## Responsibilities

Use the completed optimization algorithms.

Must include:

- at least one Greedy application
- at least one Dynamic Programming application

Possible operations:

```text
allocateResources()
selectRequests()
optimizeUnderConstraint()
```

Possible optimization problem:

> Select/allocate the best combination of service requests and available resources under limited capacity/time/resource constraints.

The team must also be able to demonstrate a greedy failure/counterexample as required by the project brief.

---

# 9. TEAM 7 — Integration & Testing (Nana Kofi Agyin, Elizabeth Johnson)

## Files

```text
services/SmartOperationsEngine.java
Main.java
```

## SmartOperationsEngine.java

Coordinates all major services:

```text
DataLoaderService
SchedulingService
IndexingService
RoutingService
OptimizationService
```

The engine should provide the overall operational workflow.

Example:

```text
Service Request
      ↓
Search / validate
      ↓
Schedule
      ↓
Allocate resource
      ↓
Find route
      ↓
Produce optimized result
      ↓
Save relevant result/events
```

## Main.java

Create a simple console interface.

Suggested menu:

```text
==========================================
 SMART SERVICE OPERATIONS OPTIMIZER
==========================================

1. Load / Reload Database Data
2. View Service Requests
3. Search Service Requests
4. Schedule Requests
5. Find Shortest Route
6. Find Reachable Locations
7. Optimize Resource Allocation
8. Run Algorithm Performance Test
9. View Algorithm Runs
10. View Audit Events
0. Exit
```

The UI does not need to be sophisticated.

The goal is to make the system easy for an examiner to run and demonstrate.

---

# 10. DataLoaderService

## File

```text
services/DataLoaderService.java
```

## Responsibilities

Connect the database to the custom data structures.

Main flow:

```text
CSV / PostgreSQL
      ↓
Java Models
      ↓
Custom Data Structures
      ↓
Algorithms
```

Possible operations:

```text
loadLocations()
loadRoads()
loadRequests()
loadResources()
loadIntoGraph()
loadIntoIndexes()
```

This is the bridge between the database and the DSA layer.

---

# 11. Dependency Order

```text
                 MODELS
                   │
                   ▼
               DATABASE
                   │
        ┌──────────┼───────────┐
        ▼          ▼           ▼
   SCHEDULING   INDEXING    ROUTING
        │          │           │
        └──────────┼───────────┘
                   ▼
              OPTIMIZATION
                   │
                   ▼
           SMART OPERATIONS
                ENGINE
                   │
                   ▼
                 MAIN
                   │
                   ▼
               TESTING
```

Some teams can begin in parallel **after the contracts are frozen**.

---

# 12. Collaboration Rules

## Rule 1 — One Shared Contract

Everyone must work from:

```text
PROJECT_CONTRACT.md
```

No team should invent its own model or method signature.

---

## Rule 2 — No Duplicate DSA

The following are already completed:

```text
algorithms/
datastructures/
```

Do not recreate:

- Graph
- Queue
- Heap
- BST
- Hash Table
- Sorting algorithms
- Searching algorithms
- etc.

Services should call the existing implementations.

---

## Rule 3 — Git Branching

Use:

```text
main
  │
  └── dev
       │
       ├── feature/models
       ├── feature/database
       ├── feature/data-loader
       ├── feature/scheduling
       ├── feature/indexing
       ├── feature/routing
       ├── feature/optimization
       ├── feature/integration
       └── feature/testing
```

Nobody should push directly to `main`.

Workflow:

```text
feature branch
      ↓
Pull Request
      ↓
Code Review
      ↓
dev
      ↓
Integration testing
      ↓
main
```
---


# 13. Daily Integration

Do not wait until the end of the week.

Every day:

```text
Team work
   ↓
Push to feature branch
   ↓
Pull Request
   ↓
Review
   ↓
Merge into dev
   ↓
Compile entire project
   ↓
Run tests
   ↓
Fix integration problems
```

This should happen daily.

---

# 16. Definition of Coding Complete

The coding portion should be considered complete when the following end-to-end workflow works:

```text
PostgreSQL
    ↓
Load locations, roads, requests and resources
    ↓
Custom Data Structures
    ↓
Search / Index
    ↓
Schedule requests
    ↓
Optimize resources
    ↓
Find route
    ↓
Generate operational result
    ↓
Record relevant audit/performance data
    ↓
Display result in Main.java
```

The project brief requires persistent database integration, a console/simple GUI demonstration, and the use of the custom structures and algorithms to solve the campus operational problems.

---

# 17. Golden Rule for the Team

> **Nobody codes in isolation.**

Before implementing a service, the developer must know:

```text
What data do I receive?
What existing structure/algorithm do I use?
What does my method return?
Who consumes my result?
```

This prevents the dependency problem experienced during the Data Structures and Algorithms phase.

The goal is not simply to finish individual files.

The goal is to finish **one working system**.
