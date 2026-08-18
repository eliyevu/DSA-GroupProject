package com.ug.dsa.models;

public class AuditEvent {

    public enum EventType {
        REQUEST_CREATED,
        REQUEST_ASSIGNED,
        REQUEST_COMPLETED,
        RESOURCE_ALLOCATED,
        REQUEST_CANCELLED
    }

    private int eventId;
    private EventType eventType;
    private int requestId;
    private String timestamp;
    private String description;

    public AuditEvent() {
    }

    public AuditEvent(int eventId,
                      EventType eventType,
                      int requestId,
                      String timestamp,
                      String description) {
        this.eventId = eventId;
        this.eventType = eventType;
        this.requestId = requestId;
        this.timestamp = timestamp;
        this.description = description;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "AuditEvent{" +
                "eventId=" + eventId +
                ", eventType=" + eventType +
                ", requestId=" + requestId +
                ", timestamp='" + timestamp + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}