[![CI Workflow](https://github.com/moravianblackcat/fetcher/actions/workflows/ci.yml/badge.svg)](https://github.com/moravianblackcat/fetcher/actions/workflows/ci.yml)
[![Dependabot](https://github.com/moravianblackcat/fetcher/actions/workflows/dependabot.yml/badge.svg)](https://github.com/moravianblackcat/fetcher/actions/workflows/dependabot.yml)

# Fetcher Project

## Overview
Fetcher is a Java-based project developed using Spring Boot and Gradle. It provides functionality for fetching and processing data efficiently, including retrieving data from external APIs, databases, or other sources. The project is designed to fetch data from various sources (e.g., RESTful services) and supports extensible configurations to accommodate new data sources or processing requirements. This ensures scalability and flexibility in managing diverse data-fetching scenarios.

### Data Fetching Workflow

The project follows a three-step workflow for fetching and processing data:

1. **Request Handling**:  
   A request for fetching data is received and stored. For example, `cz.dan.avro.fetcher.request.FootballPlayerRequest` represents a request for fetching football player data.

2. **Data Processing**:  
   The stored request is processed by retrieving the required data and saving it in the system. For instance, `cz.dan.fetcher.domain.football.request.player.outbox.entity.FootballPlayerRequestOutbox` is used to store the processed data.

3. **Data Dispatch**:  
   The processed data are sent to the designated destination. For example, `cz.dan.avro.fetcher.outbox.FootballPlayerOutboxPayload` represents the payload containing the fetched football player data ready for dispatch. The project is designed to support not only Kafka payloads but also other brokers. Actually, any broker might work.

### Subclassing `cz.dan.fetcher.domain.outbox.job.request.RequestJob`

#### When to Subclass
Subclass `RequestJob` to implement a new job for processing specific types of requests and outbox entities. This is necessary when the application needs to handle new data-fetching workflows or integrate with new data sources.

#### Why to Subclass
Subclassing `RequestJob` allows you to:
1. Define custom behavior for processing requests and saving outbox entities.
2. Implement specific logic for handling errors, retries, and resource management.
3. Extend the functionality to support new types of requests and outbox entities while maintaining consistency in the job execution workflow.

By subclassing, you ensure that the new job adheres to the established structure and processing logic of the application.

### Subclassing `cz.dan.fetcher.domain.outbox.job.request.RequestOutboxJob`

#### When to Subclass
Subclass `RequestOutboxJob` to implement a new job for processing specific types of outbox entities and dispatching them to their designated destinations. This is necessary when the application needs to handle new types of outbox workflows or integrate with new sending mechanisms.

#### Why to Subclass
Subclassing `RequestOutboxJob` allows you to:
1. Define custom behavior for fetching, sending, and deleting outbox entities.
2. Implement specific logic for handling new types of outbox entities and their associated senders.
3. Extend the functionality to support new workflows while maintaining consistency in the job execution process.

By subclassing, you ensure that the new job adheres to the established structure and processing logic of the application.

### Subclassing `cz.dan.fetcher.domain.outbox.sender.Sender`

#### When to Subclass
Subclass `Sender` to implement a new mechanism for dispatching outbox payloads to their designated destinations. This is necessary when the application needs to support new brokers, protocols, or delivery methods.

#### Why to Subclass
Subclassing `Sender` allows you to:
1. Define custom behavior for sending payloads to specific destinations.
2. Implement specific logic for handling delivery guarantees, retries, and error handling.
3. Extend the functionality to support new dispatch mechanisms while maintaining consistency in the sending process.

By subclassing, you ensure that the new sender adheres to the established structure and logic of the application.

## Prerequisites
- **Java 21**: Ensure Java 21 is installed and configured.
- **Gradle**: Install Gradle for building and running the project.
- **Docker**: Required for running integration tests.

## Build and Run
### Configure
Create `src/main/resources/token.properties` file with `sportmonks.api.token` equal to Sportmonks API token.
### Build the Project
Use the following command from the project root to clean and build the project:
```shell
  .\gradlew clean build
```
### Start the application
```shell
  .\gradlew bootRun
```
### Run integration tests
```shell
  docker compose up -d
```
```shell
  .\gradlew clean integrationTests
```

## Technical Debt

### Batch Inserts
At present, the application performs individual inserts to store requests. While this approach works well for the current scale, it may become inefficient as the scale increases. Using batch inserts for request storage could enhance performance and scalability. However, given the unknown scale of the application, addressing this technical debt is not prioritized at this time. The storage of fetched data in standalone transactions and inserts is intentional and by design.

### Internal and external IDs
To ensure unique internal identifiers across all services, the Fetcher assigns an internal ID to every fetched entity, such as entries in the `Person` table. Currently, this table accommodates Sportmonks as the only source, but in the future, it will also support custom-created persons (those not present in the Sportmonks database) and other external sources. A potential issue arises when external sources use String identifiers, as these cannot be directly persisted in the `Person` table due to the `source_id` column being of type `BIGINT`. The proposed solution is to add a nullable `mapped_source_id` column as a foreign key, referencing a mapping table where the original source ID is stored as a `VARCHAR`. While this approach may introduce some overhead, it ensures the service remains robust and scalable.