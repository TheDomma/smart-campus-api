package com.smartcampus;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {

    private String sensorId;
    private SensorReadingDAO readingDAO = new SensorReadingDAO();
    private SensorDAO sensorDAO = new SensorDAO();

    // The Parent Resource passes the sensorId into this constructor!
    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    @GET
    public List<SensorReading> getReadings() {
        return readingDAO.getReadingsForSensor(sensorId);
    }

    @POST
    public Response addReading(SensorReading reading) {
        // 1. Save the historical reading
        SensorReading newReading = readingDAO.addReading(sensorId, reading);

        // 2. THE COURSEWORK SIDE EFFECT: Update the parent sensor's currentValue!
        Sensor parentSensor = sensorDAO.getSensor(sensorId);
        if (parentSensor != null && "MAINTENANCE".equalsIgnoreCase(parentSensor.getStatus())) {
            throw new SensorUnavailableException("Sensor is disconnected for MAINTENANCE and cannot accept readings.");
        }

        return Response.status(Response.Status.CREATED).entity(newReading).build();
    }
}