# Smart Campus API
**Module:** Client-Server Architectures (5COSC022W)
**Student:** Muditha Dodamwala - W2120414 / 20233064

## 1. Overview of API Design
The Smart Campus API is a robust, highly available RESTful web service built using Java and JAX-RS (Jakarta RESTful Web Services). It is designed to manage university campus infrastructure, specifically tracking physical `Rooms`, the IoT `Sensors` deployed within them, and the historical `SensorReadings` captured by that hardware. 

The API adheres to strict REST architectural principles:
* **Resource-Based Routing:** Endpoints are structured around logical domain entities (`/rooms`, `/sensors`).
* **HATEOAS Compliance:** The root discovery endpoint provides hypermedia links to guide client navigation.
* **Deep Nesting & Sub-Resources:** Relationships are mapped semantically (e.g., `/sensors/{id}/readings`) utilizing JAX-RS Sub-Resource Locators.
* **Standardized HTTP Methods & Status Codes:** Proper utilization of GET, POST, and DELETE alongside precise status codes (200, 201, 204, 403, 409, 422, 500) managed through custom Exception Mappers.
* **In-Memory Storage:** Data is persisted using thread-safe data structures (`ConcurrentHashMap`) as per the coursework constraints.

---

## 2. Build and Launch Instructions
Follow these steps to deploy the API to a local environment:

1. **Clone the Repository:** Clone this project to your local machine using Git.
2. **Open the IDE:** Open the project directory in Apache NetBeans (or a compatible Java IDE supporting Maven).
3. **Build the Application:** Right-click the `smart-campus-api` project folder in the Project Explorer and select **Clean and Build**. Ensure Maven successfully generates the `smart-campus-api-1.0-SNAPSHOT.war` file in the `/target` directory.
4. **Launch the Server:** Start your local Apache Tomcat 9 server.
5. **Deploy the WAR:** Access the Tomcat Manager web interface (`http://localhost:8080/manager/html`). Under the "Deploy" section, upload the generated `.war` file and click **Deploy**.
6. **Access the API:** The service will now be live at:
   `http://localhost:8080/smart-campus-api-1.0-SNAPSHOT/api/v1/`

---

## 3. Sample cURL Commands
Here are five commands to test the core functionality of the API.

**1. Get API Discovery Metadata (HATEOAS)**
```
curl -X GET http://localhost:8080/smart-campus-api-1.0-SNAPSHOT/api/v1/ ```

**2. Create a new Room (POST)**
```
curl -X POST http://localhost:8080/smart-campus-api-1.0-SNAPSHOT/api/v1/rooms \
-H "Content-Type: application/json" \
-d '{"id": "R101", "name": "Lecture Theatre 1", "capacity": 150}' 

**3. Create a new Sensor with Cross-Resource Validation (POST)**
curl -X POST http://localhost:8080/smart-campus-api-1.0-SNAPSHOT/api/v1/sensors \
-H "Content-Type: application/json" \
-d '{"id": "S1", "roomId": "R101", "type": "CO2", "status": "ACTIVE", "currentValue": 0.0}'

**4. Filter Sensors using Query Parameters (GET)**
curl -X GET "http://localhost:8080/smart-campus-api-1.0-SNAPSHOT/api/v1/sensors?type=CO2"

**5. Append a Historical Reading via Sub-Resource Locator (POST)**
curl -X POST http://localhost:8080/smart-campus-api-1.0-SNAPSHOT/api/v1/sensors/S1/readings \
-H "Content-Type: application/json" \
-d '{"id": "RD-001", "timestamp": 1670000000, "value": 415.5}'

