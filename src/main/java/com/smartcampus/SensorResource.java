package com.smartcampus;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    private SensorDAO sensorDAO = new SensorDAO();
    private RoomDAO roomDAO = new RoomDAO(); // We need this to check if the room exists!

    // 1. READ ALL (with optional ?type= filter)
    @GET
    public List<Sensor> getSensors(@QueryParam("type") String type) {
        return sensorDAO.getAllSensors(type);
    }

    // 2. CREATE (POST)
    @POST
    public Response addSensor(Sensor sensor) {
        // COURSEWORK CONSTRAINT: Verify the Room ID actually exists in our RoomDAO
        if (roomDAO.getRoom(sensor.getRoomId()) == null) {
            // Return 422 Unprocessable Entity if the room is fake
            return Response.status(422)
                    .entity("{\"error\":\"Cannot create sensor. Room ID does not exist.\"}")
                    .build();
        }
        
        Sensor newSensor = sensorDAO.addSensor(sensor);
        return Response.status(Response.Status.CREATED).entity(newSensor).build();
    }
}