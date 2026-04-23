package com.smartcampus;

public class Sensor {
    private String id;
    private String roomId;
    private String type; // e.g., "CO2", "TEMPERATURE"
    private double currentValue;

    public Sensor() {
    }

    public Sensor(String id, String roomId, String type, double currentValue) {
        this.id = id;
        this.roomId = roomId;
        this.type = type;
        this.currentValue = currentValue;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public double getCurrentValue() { return currentValue; }
    public void setCurrentValue(double currentValue) { this.currentValue = currentValue; }
}