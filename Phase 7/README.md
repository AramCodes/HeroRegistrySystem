# Hero Registry System

The **Hero Registry System** is a desktop application for managing a database of heroes used by Hero Centers around the world.  
It provides a JavaFX graphical interface on top of a Java/Spring Boot & database-backed backend so staff can search, add, edit, and maintain a structured registry of heroes.



---

## Table of Contents

-   [Features](#features)
-   [Tech Stack](#tech-stack)
-   [Prerequisites](#prerequisites)
-   [Getting Started](#getting-started)
    -   [1. Clone the repository](#1-clone-the-repository)
    -   [2. Import into IntelliJ IDEA](#2-import-into-intellij-idea)
    -   [3. Run from IntelliJ (Play button)](#3-run-from-intellij-play-button)
-   [Database Configuration](#database-configuration)
-   [Project Structure](#project-structure)
-   [Common Issues & Troubleshooting](#common-issues--troubleshooting)
-   [Roadmap](#roadmap)
-   [Contact](#contact)

---

## Features

-   JavaFX desktop UI for viewing and managing heroes
-   Persistent storage using a relational database (e.g., MySQL)
-   Create, read, update, and deactivate heroes
-   Basic validation and error handling
-   Support for loading/importing heroes from external sources (e.g., files) in newer builds
-   Separation of concerns between UI, service, and persistence layers

---

## Tech Stack

-   **Language:** Java (LTS version recommended, e.g., Java 17+)
-   **UI:** JavaFX
-   **Build Tool:** Maven
-   **Database:** MySQL
-   **IDE (recommended):** IntelliJ IDEA

> JavaFX and other dependencies are managed through Maven, but your runtime still needs to be correctly configured (see below).

---

## Prerequisites

Before you run a local copy, make sure you have:

1. **Java JDK**

    - Java 17+ (LTS recommended).
    - `java -version` should show a JDK, not just a JRE.

2. **Maven**

    - Version 3.6+ recommended.
    - Check with `mvn -v`.
    - IntelliJ can also use its bundled Maven.

3. **JavaFX support**

    - If using Maven with JavaFX dependencies defined in `pom.xml`, the libraries will be downloaded automatically.
    - For manual JAR execution, you may need a JavaFX SDK and to set a `--module-path`.

4. **Database server (optional but recommended)**

    - A local MySQL (or MySQL-compatible) server if you want persistence.
    - Create a database and user for the Hero Registry System and update configuration values.

5. **IntelliJ IDEA** (recommended)
    - Community or Ultimate Edition.
    - Makes it easy to click **Run ▶** to start the app.

---

## Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/<your-username>/hero-registry-system.git
cd hero-registry-system
```

