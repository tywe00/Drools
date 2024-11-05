
package org.example.rulesengine;

public class Event {
    
    private String eventId;
    private String eventType; // Type of event (e.g., Fault, Warning, Info)
    private long timestamp;   // Time the event was generated
    private String source;    // Source of the event (e.g., component or subsystem name)
    private int severity;  // Severity level (e.g., Critical, High, Medium, Low)
    private String message;   // Descriptive message for the event

    // Constructors
    public Event() {}

    public Event(String eventId, String eventType, long timestamp, String source, 
                 int severity, String message) {
        this.eventId = eventId;
        this.eventType = eventType;
        this.timestamp = timestamp;
        this.source = source;
        this.severity = severity;
        this.message = message;
    }

    // Getters and Setters
    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getSeverity() {
        return severity;
    }

    public void setSeverity(int severity) {
        this.severity = severity;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // Utility method to print Event details
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Event{")
        .append("eventId='").append(eventId).append('\'')
        .append(", eventType='").append(eventType).append('\'')
        .append(", timestamp=").append(timestamp)
        .append(", source='").append(source).append('\'')
        .append(", severity='").append(severity).append('\'')
        .append(", message='").append(message).append('\'')
        .append('}');
        return sb.toString();
    }   

}
