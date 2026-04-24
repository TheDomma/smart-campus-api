package com.smartcampus;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import javax.ws.rs.core.Context;

@Path("/rooms") 
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {

    private RoomDAO roomDAO = new RoomDAO();
    private SensorDAO sensorDAO = new SensorDAO(); // We need this to check sensor data!

    @GET
    public List<Room> getAllRooms() {
        return roomDAO.getAllRooms();
    }

    @GET
    @Path("/{roomId}")
    public Response getRoom(@PathParam("roomId") String roomId) {
        Room room = roomDAO.getRoom(roomId);
        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Room not found\"}").build();
        }
        return Response.ok(room).build();
    }

    @POST
    public Response addRoom(Room room, @Context javax.ws.rs.core.UriInfo uriInfo) {
        Room newRoom = roomDAO.addRoom(room);
        
        // This generates the exact URL for the newly created room (e.g., /api/v1/rooms/R101)
        java.net.URI location = uriInfo.getAbsolutePathBuilder().path(newRoom.getId()).build();
        
        // Return 201 Created, the Location header, AND the JSON body
        return Response.created(location).entity(newRoom).build();
    }

    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = roomDAO.getRoom(roomId);
        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        
        // COURSEWORK RULE: Block deletion if sensors exist inside!
        if (sensorDAO.hasSensorsForRoom(roomId)) {
            return Response.status(409) // 409 Conflict
                    .entity("{\"error\":\"Cannot delete room. It still has sensors inside!\"}")
                    .build();
        }
        
        roomDAO.deleteRoom(roomId);
        return Response.noContent().build();
    }

    // PART 4: DEEP NESTING ENDPOINT
    // Maps exactly to /api/v1/rooms/{roomId}/sensors
    @GET
    @Path("/{roomId}/sensors")
    public Response getSensorsForRoom(@PathParam("roomId") String roomId) {
        // First, check if the room even exists
        if (roomDAO.getRoom(roomId) == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Room not found\"}").build();
        }
        
        // Fetch and return only the sensors for this specific room
        List<Sensor> roomSensors = sensorDAO.getSensorsByRoom(roomId);
        return Response.ok(roomSensors).build();
    }
}