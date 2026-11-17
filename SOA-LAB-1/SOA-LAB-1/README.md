# SOA Lab: Controller Unit Testing with MockMvc

This project demonstrates isolated Spring MVC controller tests for automation and user management endpoints. `@WebMvcTest` creates the MVC slice and `MockMvc` exercises routing, JSON binding, validation, status codes, and error handling without starting the full application server. Service interfaces are mocked in the controller tests.

## Requirements

- Java 17 or newer
- Maven 3.6.3 or newer

## Run the tests

```bash
mvn test
```

## Start the service

```bash
mvn spring-boot:run
```

The sample service stores data in memory; restarting it clears all records.

## Run in VS Code

1. Open this folder in VS Code.
2. Install the recommended **Extension Pack for Java** when prompted (or install it from Extensions).
3. Wait for the Java project import to finish.
4. Open `SoaLabApplication.java` and select **Run** above `main`, or press **F5** and choose the Spring Boot launch configuration.
5. To run tests, open the Command Palette (`Cmd+Shift+P` on macOS / `Ctrl+Shift+P` on Windows/Linux) and choose **Tasks: Run Task → Run Maven tests**.

The launch and test task are also available under **Run and Debug** and **Terminal → Run Task**. Java 17 or newer and Maven 3.6.3 or newer must be installed.

## Routes

| Method | Route | Purpose |
| --- | --- | --- |
| `GET` | `/api/automations` | List automations |
| `GET` | `/api/automations/{id}` | Get one automation |
| `POST` | `/api/automations` | Create automation (`name`, `action`, `enabled`) |
| `DELETE` | `/api/automations/{id}` | Delete automation |
| `GET` | `/api/users` | List users |
| `GET` | `/api/users/{id}` | Get one user |
| `POST` | `/api/users` | Create user (`name`, `email`) |
| `DELETE` | `/api/users/{id}` | Delete user |

Create requests return `201 Created`; deletes return `204 No Content`; unknown IDs return `404 Not Found`. Required fields and email format are validated and invalid requests return `400 Bad Request`.
