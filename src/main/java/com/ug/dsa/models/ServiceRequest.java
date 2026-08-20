package com.ug.dsa.models;

public class ServiceRequest implements Comparable<ServiceRequest> {

    private int requestId;
    private int source;
    private int destination;
    private String category;
    private int urgency;
    private String timeSubmitted;
    private String deadline;
    private String status;

    public ServiceRequest() { }

    public ServiceRequest(int requestId,
                          int source,
                          int destination,
                          String category,
                          int urgency,
                          String timeSubmitted,
                          String deadline,
                          String status) {
        this.requestId = requestId;
        this.source = source;
        this.destination = destination;
        this.category = category;
        this.urgency = urgency;
        this.timeSubmitted = timeSubmitted;
        this.deadline = deadline;
        this.status = status;
    }

    // Getters and setters (unchanged)
    public int getRequestId() { return requestId; }
    public void setRequestId(int requestId) { this.requestId = requestId; }

    public int getSource() { return source; }
    public void setSource(int source) { this.source = source; }

    public int getDestination() { return destination; }
    public void setDestination(int destination) { this.destination = destination; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getUrgency() { return urgency; }
    public void setUrgency(int urgency) { this.urgency = urgency; }

    public String getTimeSubmitted() { return timeSubmitted; }
    public void setTimeSubmitted(String timeSubmitted) { this.timeSubmitted = timeSubmitted; }

    public String getDeadline() { return deadline; }
    public void setDeadline(String deadline) { this.deadline = deadline; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "ServiceRequest{" +
                "requestId=" + requestId +
                ", source=" + source +
                ", destination=" + destination +
                ", category='" + category + '\'' +
                ", urgency=" + urgency +
                ", timeSubmitted='" + timeSubmitted + '\'' +
                ", deadline='" + deadline + '\'' +
                ", status='" + status + '\'' +
                '}';
    }


     // Compare ServiceRequests by urgency (descending).


    @Override
    public int compareTo(ServiceRequest other) {
        return Integer.compare(other.urgency, this.urgency);
    }
}
