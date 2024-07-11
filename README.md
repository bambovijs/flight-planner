# Flight Planner Application

## Overview
The Flight Planner Application is designed to facilitate the planning and management of flights. It allows users to add, search, and delete flights, as well as search for airports. The application is built using a microservices architecture, focusing on reliability, scalability, and ease of use.

## Technologies Used

### Backend
- **Java**: The core programming language used for developing the application's backend logic.
- **Spring Boot**: A framework that simplifies the bootstrapping and development of new Spring applications. It's used for creating the RESTful API, handling dependency injection, and configuring the application.
- **Spring Data JPA**: This module of Spring is used for data access layer. It simplifies the implementation of data access layers by reducing the amount of boilerplate code required.
- **Hibernate**: An object-relational mapping tool for Java, used in conjunction with Spring Data JPA to map Java classes to database tables.
- **Gradle**: A build automation tool used to manage dependencies, compile the project, and package the application.

### Database
- **SQL**: The language used for managing and querying the database.
- **H2 Database**: An in-memory database used for development and testing purposes. It simplifies the setup process and is easily configurable with Spring Boot.

### Testing
- **JUnit**: A unit testing framework for Java, used to write and run repeatable tests.
- **Mockito**: A mocking framework used in conjunction with JUnit to mock objects in tests, allowing for isolated testing of service layers.

### Development Tools
- **IntelliJ IDEA**: The integrated development environment (IDE) used for developing the application. It provides tools for code editing, running, and debugging applications, as well as executing tests.

## Features
- **Flight Management**: Users can add, search, and delete flights using the RESTful API.
- **Airport Search**: The application provides functionality to search for airports based on partial or complete search terms.
- **Duplicate Flight Check**: Before adding a new flight, the application checks for duplicate entries based on specific criteria to prevent redundancy.

## Configuring the Repository

The Flight Planner Application can be configured to use either an in-memory repository for development and testing or a persistent database repository for production. Follow the steps below to switch between the two.

### Switching to In-Memory Repository

1. Open the `application.properties` file located in the `src/main/resources` directory.
2. Comment out or remove the database configuration properties (usually prefixed with `spring.datasource`).
3. Or change flight.storage.mode=database to fligt.storage.mode=in-memory
