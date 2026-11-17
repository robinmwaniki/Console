# Console To-Do List Application

A lightweight Java command-line task manager. The application provides an interactive console workflow for organizing tasks and their categories, with H2 used as the embedded database.

## Features

- Interactive command-line interface.
- Create and manage to-do tasks.
- Organize tasks using categories.
- Persist application data using H2.
- Package the application as an executable JAR with Maven Shade Plugin.

## Technology stack

- Java 11
- Maven
- H2 Database
- Maven Shade Plugin

## Prerequisites

- JDK 11 or later
- Maven 3.8+

## Getting started

1. Clone the repository:
   ```bash
   git clone https://github.com/robinmwaniki/Console.git
   cd Console
   ```
2. Build the executable JAR:
   ```bash
   mvn clean package
   ```
3. Run the generated JAR from the `target` directory. The configured artifact name is `Simple to do list application.jar`:
   ```bash
   java -jar "target/Simple to do list application.jar"
   ```

## Project structure

- `main/java/ToDoListApp.java` — application entry point and console interaction.
- `main/java/ToDoList.java` — task-list operations.
- `main/java/Task.java` — task model.
- `main/java/Category.java` — category model.

## Notes

The project is a console application; it does not require a browser or a separate database server. H2 is included as a project dependency.

## License

No license file is currently documented in this repository.