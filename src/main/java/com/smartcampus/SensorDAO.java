package com.smartcampus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SensorDAO {
    private static Map<String, Sensor> sensors = new HashMap<>();

    // We use a query parameter "type" to filter. If it's null, we return all of them.
    public List<Sensor> getAllSensors(String type) {
        if (type == null || type.trim().isEmpty()) {
            return new ArrayList<>(sensors.values());
        }
        // Filter the list based on the requested type (e.g., only "CO2" sensors)
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
    
    // Helper method to check if a room has sensors (We will use this later for the DELETE room rule!)
    public boolean hasSensorsForRoom(String roomId) {
        return sensors.values().stream().anyMatch(s -> s.getRoomId().equals(roomId));
    }
}