package com.smartcampus;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/rooms") // Maps to /api/v1/rooms
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {

    private RoomDAO roomDAO = new RoomDAO();

    // 1. READ ALL (GET)
    @GET
    public List<Room> getAllRooms() {
        return roomDAO.getAllRooms();
    }

    // 2. READ ONE (GET by ID)
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

    // 3. CREATE (POST)
    @POST
    public Response addRoom(Room room) {
        Room newRoom = roomDAO.addRoom(room);
        return Response.status(Response.Status.CREATED).entity(newRoom).build();
    }

    // 4. DELETE
    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = roomDAO.getRoom(roomId);
        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        
        // TODO for Part 3: We will add logic here to block deletion if sensors exist inside!
        roomDAO.deleteRoom(roomId);
        return Response.noContent().build(); // 204 No Content
    }
}