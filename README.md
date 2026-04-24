# Smart Campus API
**Module:** Client-Server Architectures
**Student:** Muditha Dodamwala - W2120414 / 20233064

## Build and Run Instructions
1. Clone the repository from GitHub.
2. Open the project in Apache NetBeans.
3. Right-click the project and select **Clean and Build**.
4. Deploy the generated `smart-campus-api-1.0-SNAPSHOT.war` file to an Apache Tomcat 9 server.
5. Base API URL: `http://localhost:8080/smart-campus-api-1.0-SNAPSHOT/api/v1/`

## Sample cURL Commands
```bash
# 1. Get API Discovery Info
curl -X GET http://localhost:8080/smart-campus-api-1.0-SNAPSHOT/api/v1/

# 2. Create a new Room
curl -X POST http://localhost:8080/smart-campus-api-1.0-SNAPSHOT/api/v1/rooms \
-H "Content-Type: application/json" \
-d '{"id": "R101", "name": "Lecture Theatre 1", "capacity": 150}'

# 3. Get all Rooms
curl -X GET http://localhost:8080/smart-campus-api-1.0-SNAPSHOT/api/v1/rooms

# 4. Create a new Sensor in Room R101
curl -X POST http://localhost:8080/smart-campus-api-1.0-SNAPSHOT/api/v1/sensors \
-H "Content-Type: application/json" \
-d '{"id": "S1", "roomId": "R101", "type": "CO2", "status": "ACTIVE", "currentValue": 400.5}'

# 5. Filter Sensors by Type
curl -X GET "http://localhost:8080/smart-campus-api-1.0-SNAPSHOT/api/v1/sensors?type=CO2"