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
curl -X GET http://localhost:8080/smart-campus-api-1.0-SNAPSHOT/api/v1/

**2. Create a new Room (POST)**
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

## Conceptual Report

### Part 1: Service Architecture & Setup
**Q: Explain the default lifecycle of a JAX-RS Resource class. How does this impact in-memory data structures?**
By default, JAX-RS resource classes are **Request-Scoped**. This means the JAX-RS runtime instantiates a brand new object of the resource class for every single incoming HTTP request, and the object is destroyed once the response is sent. Because of this lifecycle, any data stored in standard instance variables will be lost between requests. To maintain data persistence without a database, our in-memory data structures (like HashMaps in our DAO classes) must be declared as `static`. Furthermore, because Tomcat handles multiple requests simultaneously across different threads, these static data structures must be thread-safe (e.g., `ConcurrentHashMap`) to prevent race conditions and data corruption.

**Q: Why is "Hypermedia" (HATEOAS) considered a hallmark of advanced RESTful design?**
HATEOAS (Hypermedia As The Engine Of Application State) elevates an API from a simple data repository to a dynamic, self-documenting service. Instead of relying on static, external documentation where clients must hardcode endpoint URLs, HATEOAS embeds navigation links directly within the JSON response payload (`_links`). This dramatically benefits client developers by decoupling the client from the server's routing logic. If the backend team modifies a URL path, the client application will not break because it dynamically reads the updated URI directly from the API response.

### Part 2: Room Management
**Q: When returning a list of rooms, what are the implications of returning only IDs versus returning the full room objects?**
Returning only IDs minimizes the initial payload size, saving network bandwidth. However, it severely increases client-side processing and latency because it forces the client to make subsequent, individual `GET` requests for every single ID to fetch the actual data (the N+1 query problem). Returning the full room objects consumes more bandwidth initially, but drastically reduces the total number of HTTP requests, allowing the client application to render much faster and more efficiently.

**Q: Is the DELETE operation idempotent in your implementation? Provide a detailed justification.**
Yes, the `DELETE` operation is strictly idempotent. In REST, idempotency means that making multiple identical requests has the same effect on the server's state as making a single request. In my implementation, if a client sends `DELETE /rooms/R102`, the room is removed and the server returns a `204 No Content`. If the client mistakenly sends the exact same request again, `roomDAO.getRoom("R102")` will return `null`, and the server will respond with a `404 Not Found`. Although the HTTP status code changed, the actual state of the server's database remained exactly the same as it was after the very first request. 

### Part 3: Sensor Operations & Linking
**Q: Explain the technical consequences if a client attempts to send data in a different format (e.g., text/plain) when @Consumes(MediaType.APPLICATION_JSON) is used.**
If a client sends a request with a `Content-Type` header that does not match the `@Consumes` annotation, the JAX-RS runtime intercepts the request before it reaches the Java method. Because the server explicitly declared it only understands JSON for this endpoint, JAX-RS automatically rejects the request and returns an HTTP `415 Unsupported Media Type` error to the client. This protects the backend from parsing exceptions and enforces a strict API contract.

**Q: Contrast filtering via @QueryParam with an alternative design where the type is part of the URL path. Why is the query parameter approach superior?**
Path parameters (e.g., `/sensors/{id}`) are strictly used to uniquely identify a specific resource. If we used `/sensors/type/CO2`, it creates a rigid hierarchy that treats a filter as a structural resource. Query parameters (`?type=CO2`) are superior for searching and filtering because they are optional, stackable, and do not mutate the base URI of the collection. A client can easily request the entire collection (`/sensors`), or apply multiple filters dynamically (`/sensors?type=CO2&status=ACTIVE`) without the backend needing to define complex, hardcoded URL routes.

### Part 4: Deep Nesting with Sub-Resources
**Q: Discuss the architectural benefits of the Sub-Resource Locator pattern. How does delegating logic help manage complexity?**
The Sub-Resource Locator pattern prevents "Controller Bloat" by adhering to the Single Responsibility Principle. If a single `SensorResource` class attempted to define every nested path (`/sensors`, `/sensors/{id}`, `/sensors/{id}/readings`), the file would quickly become an unmanageable monolith. By using the Sub-Resource Locator pattern, JAX-RS allows the parent resource to dynamically delegate the HTTP request to smaller, dedicated child classes (like `SensorReadingResource`). This drastically improves code maintainability and readability because each class strictly handles the HTTP logic for its specific domain entity.

### Part 5: Advanced Error Handling, Exception Mapping & Logging
**Q: Why is HTTP 422 often considered more semantically accurate than a standard 404 when the issue is a missing reference inside a valid JSON payload?**
If a client attempts to assign a sensor to a fake room ID, the `/sensors` endpoint itself exists, so a `404 Not Found` response is highly misleading. `422 Unprocessable Entity` correctly indicates that the server understands the content type and syntax of the request entity, but cannot process the contained instructions due to semantic logic errors (the invalid foreign key).

**Q: Explain the risks associated with exposing internal Java stack traces to external API consumers.**
Uncaught exceptions that leak Java stack traces to the client expose internal directory structures, framework versions, and backend logic flaws. Attackers use this reconnaissance data to identify known Common Vulnerabilities and Exposures (CVEs) in specific library versions or to craft targeted injection attacks against exposed internal components. Utilizing a global `ExceptionMapper<Throwable>` ensures only sanitized `500 Internal Server Error` messages reach the consumer.

**Q: Why is it advantageous to use JAX-RS filters for cross-cutting concerns like logging?**
Using JAX-RS filters enforces the DRY (Don't Repeat Yourself) principle and cleanly separates business logic from infrastructure concerns. It centralizes the logging logic in one place, ensuring every single API endpoint is automatically monitored without requiring developers to manually insert boilerplate `Logger.info()` code every time they write a new method. This guarantees consistent observability across the entire application.