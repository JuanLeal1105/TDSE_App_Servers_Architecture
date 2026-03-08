# App Server Architecture

**Created by**

Juan Carlos Leal Cruz

## Laboratory Overview
This repository contains a Java web server and a lightweight IoC framework. It demonstrates serving HTML pages and PNG images, dynamically loading POJOs, and handling routes with annotations like `@RestController`, `@GetMapping`, and `@RequestParam. Included are example web applications that showcase the server’s capabilities and how to build modular Java web apps using reflection and IoC principles.

### **Prerequisites**
- `JDK`: 17 or higher
- `Apache Maven`

### **Project Structure**
The project is structured as follows:
```text
demo/
├── pom.xml
└── src/
    └── main/
        ├── java/
        │   └── org/
        │       └── example/
        │           └── demo/
        │               ├── api/
        │               │  ├── Request.java
        │               │   └── Response.java
        │               ├── server/
        │               │   ├── HttpServer.java
        │               │   └── MicroSpringBoot.java
        │               ├── DemoApplication.java
        │               ├── GetMapping.java
        │               ├── HelloController.java
        │               ├── InvokeMain.java
        │               ├── ReflexionNavigator.java
        │               └── RestController.java
        └── resources/
```
