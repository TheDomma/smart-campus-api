package com.smartcampus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SensorDAO {
    private static Map<String, Sensor> sensors = new HashMap<>();

    public List<Sensor> getAllSensors(String type) {
        if (type == null || type.trim().isEmpty()) {
            return new ArrayList<>(sensors.values());
        }
        return sensors.values().stream()
                .filter(s -> s.getType().equalsIgnoreCase(type))
                .collect(Collectors.toList());
    }

    public Sensor getSensor(String id) {
        return sensors.get(id);
    }

    public Sensor addSensor(Sensor sensor) {
        sensors.put(sensor.getId(), sensor);
        return sensor;
    }
    
    public boolean hasSensorsForRoom(String roomId) {
        return sensors.values().stream().anyMatch(s -> s.getRoomId().equals(roomId));
    }

    // NEW FOR PART 4: Get all sensors that belong to a specific room
    public List<Sensor> getSensorsByRoom(String roomId) {
        return sensors.values().stream()
                .filter(s -> s.getRoomId().equals(roomId))
                .collect(Collectors.toList());
    }
}