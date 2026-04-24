package com.smartcampus;

public class Sensor {
    private String id;
    private String type;
    private String status; // Added to match spec (e.g., "ACTIVE")
    private double currentValue;
    private String roomId;

    public Sensor() {
    }

    public Sensor(String id, String roomId, String type, String status, double currentValue) {
        this.id = id;
        this.roomId = roomId;
        this.type = type;
        this.status = status;
        this.currentValue = currentValue;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getCurrentValue() { return currentValue; }
    public void setCurrentValue(double currentValue) { this.currentValue = currentValue; }

    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }
}