# PulseDesk Comment-to-Ticket Triage

A Spring Boot backend for the IBM Internship technical exercise. The application accepts user comments, analyzes them with the Hugging Face Inference API, and creates support tickets for actionable issues such as bugs, billing problems, account issues, and feature requests.

## What it does

PulseDesk simulates a feedback triage workflow used in internal tools and product platforms. Each submitted comment is automatically analyzed by an AI model. If the comment represents a real issue or request, the system generates a structured ticket with:

- title
- category
- priority
- summary

Comments and tickets are stored in an embedded H2 database and exposed through REST endpoints.

## Tech stack

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- H2 Database
- Hugging Face Inference API
- Maven
- Docker (multi-stage image)

## Features

- Submit comments through `POST /comments`
- View all comments through `GET /comments`
- View all tickets through `GET /tickets`
- View a ticket by ID through `GET /tickets/{ticketId}`
- Automatic AI-based ticket triage
- Structured ticket generation
- Embedded database for simple local setup

## API endpoints

### `POST /comments`

Creates a comment, sends it to Hugging Face for analysis, and creates a ticket if needed.

Example request:

```json
{
  "text": "I was charged twice for my subscription and cannot download my invoice.",
  "source": "web-form"
}
```

Example response:

```json
{
  "id": 1,
  "text": "I was charged twice for my subscription and cannot download my invoice.",
  "source": "web-form",
  "createdAt": "2026-04-19T16:50:51.6266683",
  "analyzed": true,
  "shouldCreateTicket": true,
  "ticketId": 1
}
```

### `GET /comments`

Returns all submitted comments.

### `GET /tickets`

Returns all created tickets.

### `GET /tickets/{ticketId}`

Returns a single ticket by ID.

## AI integration

The project uses Hugging Face Inference Providers through the OpenAI-compatible chat completions endpoint:

`https://router.huggingface.co/v1/chat/completions`

The model is configured in `application.properties`, and the API is authenticated using a Hugging Face access token provided through the `HF_API_TOKEN` environment variable.

The model is prompted to return strict JSON with:

- `shouldCreateTicket`
- `title`
- `category`
- `priority`
- `summary`

## Categories and priorities

Allowed categories:

- `bug`
- `feature`
- `billing`
- `account`
- `other`

Allowed priorities:

- `low`
- `medium`
- `high`

## Local setup

### Prerequisites

- Java 17+
- Maven
- Docker (optional)
- Hugging Face account
- Hugging Face fine-grained access token with **Inference → Make calls to Inference Providers**

### 1. Clone the repository

```bash
git clone <your-repo-url>
cd pulsedesk-triage
```

### 2. Configure Hugging Face token

#### PowerShell

```powershell
$env:HF_API_TOKEN="your_huggingface_token_here"
```

(Use the same environment variable name in any other shell, e.g. `export HF_API_TOKEN=...` on Linux/macOS.)

### 3. Run the app

```bash
mvn clean spring-boot:run
```

The application will start at:

```text
http://localhost:8080
```

## Configuration

Example `src/main/resources/application.properties`:

```properties
server.port=8080

spring.datasource.url=jdbc:h2:mem:pulsedeskdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

huggingface.api.url=https://router.huggingface.co/v1/chat/completions
huggingface.api.model=meta-llama/Llama-3.1-8B-Instruct
huggingface.api.token=${HF_API_TOKEN:}
```

## H2 console

The H2 console is available at:

```text
http://localhost:8080/h2-console
```

Use these values:

- Driver Class: `org.h2.Driver`
- JDBC URL: `jdbc:h2:mem:pulsedeskdb`
- User Name: `sa`
- Password: `password`

Note: because the database is in-memory, data resets whenever the application restarts.

## Example test requests

### Create a billing issue

```powershell
$body = @{
    text = "I was charged twice for my subscription and cannot download my invoice."
    source = "web-form"
} | ConvertTo-Json

Invoke-RestMethod `
  -Method Post `
  -Uri "http://localhost:8080/comments" `
  -ContentType "application/json" `
  -Body $body
```

### Get all tickets

```powershell
Invoke-RestMethod -Method Get -Uri "http://localhost:8080/tickets"
```

## Example comments

### Billing

`I was charged twice for my subscription and cannot download my invoice.`

### Account

`I reset my password but I still cannot log in to my account.`

### Bug

`The app crashes every time I try to upload a profile picture.`

### Feature

`Please add dark mode and CSV export for reports.`

### Compliment

`Love the app, great job team.`

## Docker

This project can also be run with Docker. The Dockerfile uses a multi-stage build, so Maven packaging happens inside the image build.

### Build the Docker image

```bash
docker build -t pulsedesk-triage .
```

### Run the container

```bash
docker run -p 8080:8080 -e HF_API_TOKEN=your_huggingface_token_here pulsedesk-triage
```

The application will then be available at:

```text
http://localhost:8080
```