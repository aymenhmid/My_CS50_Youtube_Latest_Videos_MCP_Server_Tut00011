## MCP Server Tutorial (Spring Boot)

A minimal Spring Boot MCP server showcasing a tool that fetches the latest CS50 YouTube videos via the YouTube Data API v3. Built with Spring Boot 3.5, Java 21, and Spring AI MCP Server (WebMVC) starter.

### Prerequisites
- Java 21 (JDK 21)
- Maven Wrapper (included: `mvnw`, `mvnw.cmd`)
- YouTube Data API key
- Windows PowerShell (commands below are for Windows; adapt for macOS/Linux as needed)

### Project Layout
```
src/
  main/
    java/com/example/mcp_server_tutorial/
      McpServerTutorialApplication.java
      McpTools.java
    resources/
      application.yaml
pom.xml
```

### Dependencies (pom.xml)
Key dependencies used:
- spring-boot-starter-web — Web + MVC runtime
- spring-ai-starter-mcp-server-webmvc — Spring AI MCP Server (WebMVC transport)
- org.json:json — Minimal JSON parsing
- spring-boot-starter-test — Test support

Java and BOM properties:
- java.version: 21
- spring-ai.version: 1.1.0-M3 (via `spring-ai-bom`)

### Configuration (application.yaml)
The server configuration is centralized in `src/main/resources/application.yaml`:

```12:15:src/main/resources/application.yaml
spring:
  ai:
    mcp:
      server:
        name: mcp-server-tutorial
        version: 0.0.1
        protocol: streamable
        annotation-scanner:
          enabled: true
```
```10:12:src/main/resources/application.yaml
server:
  port: 9999
```
```13:15:src/main/resources/application.yaml
youtube:
  api:
    key: ${YOUTUBE_API_KEY}
```

Notes:
- MCP server runs on port `9999`.
- Protocol set to `streamable`.
- Annotation scanner is enabled to auto-expose tools annotated with `@McpTool`.
- `YOUTUBE_API_KEY` is read from your environment.

### Exposed MCP Tool
`McpTools` defines a single tool:

```14:26:src/main/java/com/example/mcp_server_tutorial/McpTools.java
@Component
public class McpTools {

    private static final Logger log = LoggerFactory.getLogger(McpTools.class);

    @Value("${YOUTUBE_API_KEY}")
    private String youtubeApiKey;

    private static final String CHANNEL_ID = "UCcabW7890RKJzL968QWEykA"; // CS50 channel

    @McpTool(name = "CS50 latest videos", description = "Fetches the latest CS50 videos from YouTube")
    public String getLastVideos(@McpToolParam Integer limit) {
```

- **Tool name**: `CS50 latest videos`
- **Parameter**: `limit` (Integer) — number of videos to fetch
- **Output**: Formatted list of titles + YouTube URLs

### Environment Variables (Windows PowerShell)
Set your YouTube API key before running:

```powershell
$env:YOUTUBE_API_KEY = "YOUR_YOUTUBE_API_KEY"
```

To persist it for your current user (PowerShell profile):

```powershell
[Environment]::SetEnvironmentVariable("YOUTUBE_API_KEY", "YOUR_YOUTUBE_API_KEY", "User")
```

Restart the terminal after setting a User-level environment variable.

### Build, Run, and Test
- Build (without tests):

```powershell
./mvnw.cmd -q -DskipTests package
```

- Run (dev mode):

```powershell
./mvnw.cmd spring-boot:run
```

- Or run the built jar:

```powershell
java -jar target/mcp_server_tutorial-0.0.1-SNAPSHOT.jar
```

- Run tests:

```powershell
./mvnw.cmd test
```

Server will listen at `http://localhost:9999/`.

### Using MCP Inspector
You can interactively explore and invoke MCP tools using MCP Inspector.

#### Option A: Desktop app (recommended)
1. Install MCP Inspector (Desktop). If you don’t have it, get the latest release from the MCP community site.
2. Open MCP Inspector.
3. Click “Add Server”.
4. Configure:
   - Transport: HTTP
   - URL: `http://localhost:9999/`
   - Protocol: `streamable`
   - Headers: leave empty
5. Save, then connect. You should see the tool: `CS50 latest videos`.
6. Invoke the tool with a parameter, e.g., `limit = 5`.

#### Option B: Web/CLI variants
If you use a CLI or web-based Inspector variant that supports HTTP MCP:
- Start the server locally (see Run above).
- Add a server with:
  - Transport: `http`
  - URL: `http://localhost:9999/`
  - Protocol: `streamable`
- Discover and call the tool, passing `limit` as needed.

Troubleshooting tips:
- Ensure `YOUTUBE_API_KEY` is set in your environment before starting the server.
- Verify port `9999` is free or adjust `server.port` in `application.yaml`.
- If Inspector cannot discover tools, confirm `annotation-scanner.enabled: true`.

### Git Setup and Workflow
Initialize Git (if not already):

```powershell
git init
git branch -M main
git remote add origin <YOUR_REMOTE_URL>
```

Basic ignore rules are provided in `.gitignore`.

Set author info:

```powershell
git config user.name "Your Name"
git config user.email "you@example.com"
```

Commit workflow:

```powershell
git add .
git commit -m "feat: initial MCP server with CS50 videos tool"
git push -u origin main
```

Create feature branches:

```powershell
git checkout -b feat/more-tools
# ... make changes ...
git add .
git commit -m "feat: add new MCP tool"
git push -u origin feat/more-tools
```

### Inspector Tool Invocation Examples
- `CS50 latest videos` with `limit = 3` should return the three most recent videos from the CS50 channel.
- If the YouTube API quota is exceeded or the key is invalid, the tool returns an error message string.

### Common Issues
- Missing/invalid `YOUTUBE_API_KEY`: Set the environment variable and restart the app.
- Network restrictions: Ensure outbound HTTPS to `www.googleapis.com` is allowed.
- Port conflicts: Change `server.port` in `application.yaml`.

### License
This project is a tutorial example. Use at your own discretion.


