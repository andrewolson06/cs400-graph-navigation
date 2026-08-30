# CS400 Graph Navigation

A Java-based graph navigation application developed as part of a larger software engineering project. The application uses graph data structures and shortest-path algorithms to determine routes and travel times between locations in a European rail network.

Here is a video demonstration and walk-through of how the application works: https://mediaspace.wisc.edu/media/1_exteeenv

## Overview

This project integrates several software components into a complete graph-based navigation application. The backend processes graph data, performs shortest-path calculations, and provides route and travel-time information to the frontend.

The project demonstrates the implementation and integration of:

- Graph data structures
- Dijkstra's shortest-path algorithm
- Hash table data structures
- Backend and frontend interfaces
- File-based data processing
- Automated testing
- Java application integration

## Features

- Load and process European railway network data
- Find the shortest path between two locations
- Calculate travel times along routes
- Identify locations that are furthest from a given set of locations
- Store and manage data using a hash table implementation
- Separate frontend and backend functionality through Java interfaces
- Automated testing for backend and frontend components

## Technologies

- **Java**
- **Dijkstra's Algorithm**
- **Graph Data Structures**
- **Hash Tables**
- **Object-Oriented Programming**
- **JUnit Testing**
- **Make**
- **HTML**

## Project Structure

| File | Description |
|------|-------------|
| `Backend.java` | Implements the application's backend functionality |
| `Frontend.java` | Handles frontend interaction and user-facing functionality |
| `DijkstraGraph.java` | Implements graph traversal and shortest-path functionality |
| `BaseGraph.java` | Provides the underlying graph implementation |
| `GraphADT.java` | Graph interface defining core graph operations |
| `HashTableMap.java` | Hash table-based map implementation |
| `BackendTests.java` | Tests backend functionality |
| `FrontendTests.java` | Tests frontend functionality |
| `WebApp.java` | Application entry point for the web-based interface |
| `Makefile` | Provides commands for compiling and running the project |
| `europeanRail.dot` | Graph data representing the European rail network |

## How It Works

The application represents railway stations and connections as a weighted graph. Locations are represented as graph nodes, while railway connections are represented as weighted edges.

When a user requests a route between two locations, the backend uses a shortest-path algorithm to determine the optimal route through the graph. The application can then return the sequence of locations and the associated travel time.

The project separates application logic into backend and frontend components using Java interfaces, allowing individual components to be developed and tested independently.

## Testing

The project includes automated tests for major application components, including backend functionality, frontend functionality, graph operations, and data structures.

Testing was used throughout development to verify correctness and identify integration issues between individual components.

## Skills Demonstrated

This project demonstrates experience with:

- Designing and implementing data structures
- Algorithm implementation and analysis
- Object-oriented software design
- Java interfaces and abstraction
- Unit and integration testing
- Debugging and troubleshooting
- Working with an existing multi-component codebase
- Integrating independently developed software components
- Using Git for version control

## Academic Context

Developed as part of the University of Wisconsin–Madison CS400: Programming III course.
