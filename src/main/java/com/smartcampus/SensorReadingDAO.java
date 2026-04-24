package com.smartcampus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SensorReadingDAO {
    // We use a Map where the Key is the SensorID, and the Value is a List of readings
    private static Map<String, List<SensorReading>> readingsMap = new ConcurrentHashMap<>();

    public List<SensorReading> getReadingsForSensor(String sensorId) {
        return readingsMap.getOrDefault(sensorId, new ArrayList<>());
    }

    public SensorReading addReading(String sensorId, SensorReading reading) {
        readingsMap.putIfAbsent(sensorId, new ArrayList<>());
        readingsMap.get(sensorId).add(reading);
        return reading;
    }
}