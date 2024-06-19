--liquibase formatted sql

--changeset raivo:1

CREATE TABLE Airport (
    airport VARCHAR(10) PRIMARY KEY,
    country VARCHAR(100) NOT NULL,
    city VARCHAR(100) NOT NULL
);

CREATE TABLE Flight (
    flight_id INT PRIMARY KEY AUTO_INCREMENT,
    from_airport VARCHAR(10),
    to_airport VARCHAR(10),
    carrier VARCHAR(100) NOT NULL,
    departure_time TIMESTAMP NOT NULL,
    arrival_time TIMESTAMP NOT NULL,
    FOREIGN KEY (from_airport) REFERENCES Airport(airport),
    FOREIGN KEY (to_airport) REFERENCES Airport(airport)
);