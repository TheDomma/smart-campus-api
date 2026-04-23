package com.smartcampus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomDAO {
    // A static map to hold data in-memory across all requests
    private static Map<String, Room> rooms = new HashMap<>();

    // We initialize it with two rooms so you have data to test immediately!
    static {
        rooms.put("R101", new Room("R101", "Lecture Theatre 1", 150));
        rooms.put("R102", new Room("R102", "Computer Lab A", 30));
    }

    public List<Room> getAllRooms() {
        return new ArrayList<>(rooms.values());
    }

    public Room getRoom(String id) {
        return rooms.get(id);
    }

    public Room addRoom(Room room) {
        rooms.put(room.getId(), room);
        return room;
    }

    public void deleteRoom(String id) {
        rooms.remove(id);
    }
}