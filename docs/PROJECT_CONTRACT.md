# PROJECT CONTRACT

## Campus Service Hub — Data Structures and Algorithms Project

**Project:** Campus Service Hub  
**Document:** Project Contract  
**Purpose:** Define the shared data models, APIs, relationships, and integration rules for the project.

---

## 1. Purpose of This Contract

This document establishes the agreed contract for the core domain models used by the Campus Service Hub project.

The models defined in this document provide a common interface between the different teams working on:

- Graph representation
- Graph algorithms
- Searching and indexing
- Service request management
- Resource allocation
- Algorithm performance measurement
- Audit and event tracking

The purpose of this contract is to ensure that all teams use the same field names, data types, constructors, getters, setters, enums, and relationships.

---

# 2. General Contract Rules

1. The model field names and data types defined in this document are the agreed project contract.
2. Teams must use the defined getters and setters instead of directly accessing private fields.
3. Model IDs must be used consistently when referencing other model objects.
4. A `Location` represents a graph vertex.
5. A `Road` represents a graph edge.
6. `ServiceRequest` source and destination values refer to `Location.locationId`.
7. `Resource.homeLocation` refers to `Location.locationId`.
8. `Road.fromLocationId` and `Road.toLocationId` refer to `Location.locationId`.
9. Models must remain independent of specific service implementations.
10. Changes that break this contract must not be made independently.

---

# 3. Location Model

## 3.1 Responsibility

`Location` represents a physical campus location and corresponds conceptually to a graph vertex.

## 3.2 Fields

| Field | Type | Description |
|---|---|---|
| `locationId` | `int` | Unique identifier for the location |
| `name` | `String` | Name of the campus location |
| `area` | `String` | Campus area where the location is located |
| `type` | `String` | Type/category of the location |
| `latitude` | `double` | Geographic latitude |
| `longitude` | `double` | Geographic longitude |

## 3.3 Constructors

### Default constructor

```java
public Location()
```

### Full constructor

```java
public Location(
    int locationId,
    String name,
    String area,
    String type,
    double latitude,
    double longitude
)
```

## 3.4 Getters

```java
public int getLocationId()
public String getName()
public String getArea()
public String getType()
public double getLatitude()
public double getLongitude()
```

## 3.5 Setters

```java
public void setLocationId(int locationId)
public void setName(String name)
public void setArea(String area)
public void setType(String type)
public void setLatitude(double latitude)
public void setLongitude(double longitude)
```

---

# 4. Road Model

## 4.1 Responsibility

`Road` represents a connection between two campus locations and corresponds conceptually to a graph edge.

## 4.2 Fields

| Field | Type | Description |
|---|---|---|
| `roadId` | `int` | Unique identifier for the road |
| `fromLocationId` | `int` | ID of the starting location |
| `toLocationId` | `int` | ID of the destination location |
| `distance` | `double` | Distance associated with the road |
| `travelTime` | `double` | Expected travel time |
| `roadConditionWeight` | `double` | Weight representing road condition |

## 4.3 Constructors

### Default constructor

```java
public Road()
```

### Full constructor

```java
public Road(
    int roadId,
    int fromLocationId,
    int toLocationId,
    double distance,
    double travelTime,
    double roadConditionWeight
)
```

## 4.4 Getters

```java
public int getRoadId()
public int getFromLocationId()
public int getToLocationId()
public double getDistance()
public double getTravelTime()
public double getRoadConditionWeight()
```

## 4.5 Setters

```java
public void setRoadId(int roadId)
public void setFromLocationId(int fromLocationId)
public void setToLocationId(int toLocationId)
public void setDistance(double distance)
public void setTravelTime(double travelTime)
public void setRoadConditionWeight(double roadConditionWeight)
```

---

# 5. ServiceRequest Model

## 5.1 Responsibility

`ServiceRequest` represents a request for a campus service operation.

## 5.2 Fields

| Field | Type | Description |
|---|---|---|
| `requestId` | `int` | Unique identifier for the request |
| `source` | `int` | Source location ID |
| `destination` | `int` | Destination location ID |
| `category` | `String` | Category of service requested |
| `urgency` | `int` | Numerical urgency level |
| `timeSubmitted` | `String` | Time at which the request was submitted |
| `deadline` | `String` | Request completion deadline |
| `status` | `String` | Current status of the request |

## 5.3 Constructors

### Default constructor

```java
public ServiceRequest()
```

### Full constructor

```java
public ServiceRequest(
    int requestId,
    int source,
    int destination,
    String category,
    int urgency,
    String timeSubmitted,
    String deadline,
    String status
)
```

## 5.4 Getters

```java
public int getRequestId()
public int getSource()
public int getDestination()
public String getCategory()
public int getUrgency()
public String getTimeSubmitted()
public String getDeadline()
public String getStatus()
```

## 5.5 Setters

```java
public void setRequestId(int requestId)
public void setSource(int source)
public void setDestination(int destination)
public void setCategory(String category)
public void setUrgency(int urgency)
public void setTimeSubmitted(String timeSubmitted)
public void setDeadline(String deadline)
public void setStatus(String status)
```

---

# 6. Resource Model

## 6.1 Responsibility

`Resource` represents an operational resource available for fulfilling service requests.

## 6.2 Fields

| Field | Type | Description |
|---|---|---|
| `resourceId` | `int` | Unique identifier for the resource |
| `type` | `String` | Type of operational resource |
| `homeLocation` | `int` | ID of the resource's home location |
| `capacity` | `int` | Capacity supported by the resource |
| `availabilityStatus` | `String` | Current availability status |

## 6.3 Constructors

### Default constructor

```java
public Resource()
```

### Full constructor

```java
public Resource(
    int resourceId,
    String type,
    int homeLocation,
    int capacity,
    String availabilityStatus
)
```

## 6.4 Getters

```java
public int getResourceId()
public String getType()
public int getHomeLocation()
public int getCapacity()
public String getAvailabilityStatus()
```

## 6.5 Setters

```java
public void setResourceId(int resourceId)
public void setType(String type)
public void setHomeLocation(int homeLocation)
public void setCapacity(int capacity)
public void setAvailabilityStatus(String availabilityStatus)
```

---

# 7. AlgorithmRun Model

## 7.1 Responsibility

`AlgorithmRun` stores empirical performance information produced when running algorithms.

It can be used to compare the performance of different algorithms based on input size, execution time, and memory usage.

## 7.2 Fields

| Field | Type | Description |
|---|---|---|
| `runId` | `int` | Unique identifier for the performance run |
| `algorithmName` | `String` | Name of the algorithm executed |
| `inputSize` | `int` | Size of the input used |
| `timeNs` | `long` | Execution time in nanoseconds |
| `memoryKb` | `double` | Memory usage in kilobytes |
| `dateRun` | `String` | Date on which the algorithm was executed |

## 7.3 Constructors

### Default constructor

```java
public AlgorithmRun()
```

### Full constructor

```java
public AlgorithmRun(
    int runId,
    String algorithmName,
    int inputSize,
    long timeNs,
    double memoryKb,
    String dateRun
)
```

## 7.4 Getters

```java
public int getRunId()
public String getAlgorithmName()
public int getInputSize()
public long getTimeNs()
public double getMemoryKb()
public String getDateRun()
```

## 7.5 Setters

```java
public void setRunId(int runId)
public void setAlgorithmName(String algorithmName)
public void setInputSize(int inputSize)
public void setTimeNs(long timeNs)
public void setMemoryKb(double memoryKb)
public void setDateRun(String dateRun)
```

---

# 8. AuditEvent Model

## 8.1 Responsibility

`AuditEvent` records important events that occur during the operation of the Campus Service Hub.

## 8.2 Event Types

The following event types are supported:

```java
public enum EventType {
    REQUEST_CREATED,
    REQUEST_ASSIGNED,
    REQUEST_COMPLETED,
    RESOURCE_ALLOCATED,
    REQUEST_CANCELLED
}
```

## 8.3 Fields

| Field | Type | Description |
|---|---|---|
| `eventId` | `int` | Unique identifier for the audit event |
| `eventType` | `EventType` | Type of event that occurred |
| `requestId` | `int` | ID of the associated service request |
| `timestamp` | `String` | Time at which the event occurred |
| `description` | `String` | Human-readable description of the event |

## 8.4 Constructors

### Default constructor

```java
public AuditEvent()
```

### Full constructor

```java
public AuditEvent(
    int eventId,
    EventType eventType,
    int requestId,
    String timestamp,
    String description
)
```

## 8.5 Getters

```java
public int getEventId()
public EventType getEventType()
public int getRequestId()
public String getTimestamp()
public String getDescription()
```

## 8.6 Setters

```java
public void setEventId(int eventId)
public void setEventType(EventType eventType)
public void setRequestId(int requestId)
public void setTimestamp(String timestamp)
public void setDescription(String description)
```

---

# 9. Model Relationships

The models have the following logical relationships.

```text
Location
   │
   ├────────────── Road.fromLocationId
   │
   ├────────────── Road.toLocationId
   │
   ├────────────── ServiceRequest.source
   │
   ├────────────── ServiceRequest.destination
   │
   └────────────── Resource.homeLocation
```

Therefore:

```text
Road.fromLocationId
        ↓
Location.locationId

Road.toLocationId
        ↓
Location.locationId

ServiceRequest.source
        ↓
Location.locationId

ServiceRequest.destination
        ↓
Location.locationId

Resource.homeLocation
        ↓
Location.locationId
```

`AuditEvent.requestId` refers to `ServiceRequest.requestId`.

`AlgorithmRun` is independent of the operational models and records algorithm-performance measurements.

---

# 10. Graph Representation Relationship

A `Location` represents a domain-level graph vertex.

A `Road` represents a domain-level graph edge.

The graph implementation may internally represent vertices using integer indices. The domain model's `locationId` should therefore not automatically be assumed to be the same as the internal graph index unless the Graph team explicitly establishes that mapping.

For example:

```text
Domain model:

Location.locationId = 101

Graph:

vertex index = 0
```

The service/algorithm layer is responsible for maintaining any required mapping between domain location IDs and internal graph indices.

---

# 11. Expected Model Inputs and Outputs

## Location

**Input:** location ID, name, area, type, latitude and longitude.

**Output:** a `Location` object containing campus location information.

## Road

**Input:** road ID, source location ID, destination location ID, distance, travel time and road condition weight.

**Output:** a `Road` object representing a connection between two locations.

## ServiceRequest

**Input:** request ID, source, destination, category, urgency, submission time, deadline and status.

**Output:** a `ServiceRequest` object representing a campus service request.

## Resource

**Input:** resource ID, resource type, home location, capacity and availability status.

**Output:** a `Resource` object representing an available operational resource.

## AlgorithmRun

**Input:** run ID, algorithm name, input size, execution time, memory usage and execution date.

**Output:** an `AlgorithmRun` object containing performance measurements.

## AuditEvent

**Input:** event ID, event type, request ID, timestamp and description.

**Output:** an `AuditEvent` object representing an important system event.

---

# 12. Existing Data-Structure APIs

The service and algorithm teams must use the project's existing data-structure implementations rather than creating duplicate data structures for the same purpose.

The exact APIs below represent the interfaces expected to be consumed by service/algorithm code.

## 12.1 Graph

The Graph implementation is responsible for graph storage and basic graph operations.

Expected operations include:

```java
addVertex(...)
removeVertex(...)
addEdge(...)
removeEdge(...)
getNeighbors(...)
```

Services must use the existing Graph implementation rather than creating a separate graph representation.

---

## 12.2 Edge

Road/network connections are represented at the domain level by `Road`.

Where the existing graph implementation uses an `Edge` data structure, services should use the project's existing `Edge` implementation and map its endpoints/weight to the relevant `Road` information.

---

## 12.3 B-Tree

The existing B-Tree implementation should be used where ordered indexing/searching is required.

Expected operations include:

```java
insert(...)
search(...)
delete(...)
```

The exact generic types and method signatures must follow the current implementation provided by the Data Structures team.

---

## 12.4 Hash Table

The existing Hash Table should be used where constant-time key-based lookup is required.

Expected operations include:

```java
put(...)
get(...)
remove(...)
containsKey(...)
```

Teams must follow the actual implementation/API provided in the repository.

---

## 12.5 Queue

The existing Queue implementation should be used for FIFO processing.

Expected operations include:

```java
enqueue(...)
dequeue(...)
peek(...)
isEmpty(...)
```

---

## 12.6 Stack

The existing Stack implementation should be used for LIFO processing.

Expected operations include:

```java
push(...)
pop(...)
peek(...)
isEmpty(...)
```

---

# 13. Algorithm Interface Expectations

The algorithm implementations should operate on the project's agreed graph representation.

Algorithms may include:

- BFS
- DFS
- Dijkstra
- Prim
- Kruskal

Algorithm services should accept the required graph/input data and return the result expected by the service layer.

For routing algorithms, the result should provide sufficient information for the service layer to determine the path between the requested source and destination locations.

---

# 14. Performance Measurement Contract

Algorithm teams conducting empirical analysis should create an `AlgorithmRun` record after an algorithm execution.

The record should capture:

```text
algorithmName
inputSize
timeNs
memoryKb
dateRun
```

For example:

```text
algorithmName = Dijkstra
inputSize     = 100
timeNs        = 45200
memoryKb      = 128.5
dateRun       = 2026-08-17
```

This allows algorithm performance to be compared consistently.

---

# 15. ID and Reference Rules

The following IDs are unique within their respective model types:

```text
Location.locationId
Road.roadId
ServiceRequest.requestId
Resource.resourceId
AlgorithmRun.runId
AuditEvent.eventId
```

Cross-model references must use the corresponding integer ID.

Examples:

```text
Road.fromLocationId → Location.locationId
Road.toLocationId   → Location.locationId

ServiceRequest.source      → Location.locationId
ServiceRequest.destination → Location.locationId

Resource.homeLocation → Location.locationId

AuditEvent.requestId → ServiceRequest.requestId
```

---

# 16. Validation Expectations

Model classes primarily represent data and should not contain complex business logic.

Services are responsible for business-level validation.

Examples of service-level validation include:

- checking whether a referenced location exists
- checking whether a destination is reachable
- checking whether a resource is available
- checking whether a request deadline has passed
- checking whether a resource has sufficient capacity

The model classes should remain lightweight and reusable.

---

# 17. Breaking-Change Policy

Once this contract has been accepted by the project teams, the model structure must not be changed independently.

The following are considered breaking changes:

- changing a field name
- changing a field's data type
- removing a field
- changing constructor parameters
- removing a getter or setter
- changing a method signature
- renaming an enum value
- changing the meaning of an existing field

Before making a breaking change:

1. Identify all affected teams.
2. Discuss the proposed change with those teams.
3. Determine the impact on existing code.
4. Agree on the new contract.
5. Update `PROJECT_CONTRACT.md`.
6. Update affected implementations.
7. Test the affected components.
8. Communicate the change to the project team.

No team should silently modify a shared model contract.

---

# 18. Versioning

Changes to the project contract should be traceable through Git.

A contract change should include:

- a clear commit message
- an updated `PROJECT_CONTRACT.md`
- updates to affected model classes
- updates to affected tests
- communication with affected teams

Example:

```text
docs: update project contract for resource model
```

or:

```text
feat(models): update service request model
```

---

# 19. Ownership

The Models team is responsible for maintaining:

```text
Location.java
Road.java
ServiceRequest.java
Resource.java
AlgorithmRun.java
AuditEvent.java
PROJECT_CONTRACT.md
```

Other teams may depend on these models but should not independently modify them.

If another team requires a model change, the request should be discussed with the Models team and all affected teams before implementation.

---

# 20. Final Contract Summary

The project-wide model contract is:

```text
Location
    locationId : int
    name : String
    area : String
    type : String
    latitude : double
    longitude : double

Road
    roadId : int
    fromLocationId : int
    toLocationId : int
    distance : double
    travelTime : double
    roadConditionWeight : double

ServiceRequest
    requestId : int
    source : int
    destination : int
    category : String
    urgency : int
    timeSubmitted : String
    deadline : String
    status : String

Resource
    resourceId : int
    type : String
    homeLocation : int
    capacity : int
    availabilityStatus : String

AlgorithmRun
    runId : int
    algorithmName : String
    inputSize : int
    timeNs : long
    memoryKb : double
    dateRun : String

AuditEvent
    eventId : int
    eventType : EventType
    requestId : int
    timestamp : String
    description : String
```

This document serves as the shared contract for the project models and should be consulted before modifying any of the agreed model structures.