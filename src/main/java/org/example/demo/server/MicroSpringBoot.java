package org.example.demo.server;

import org.example.demo.GetMapping;
import org.example.demo.RestController;

import java.lang.reflect.Method;

public class MicroSpringBoot {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Please provide a controller class name. Example: org.example.demo.HelloController");
            return;
        }

        try {
            Class<?> c = Class.forName(args[0]);
            Object controllerInstance = c.getDeclaredConstructor().newInstance();

            if (c.isAnnotationPresent(RestController.class)) {
                for (Method m : c.getDeclaredMethods()) {
                    if (m.isAnnotationPresent(GetMapping.class)) {
                        GetMapping mapping = m.getAnnotation(GetMapping.class);
                        System.out.println("Mapped URI: " + mapping.value() + " -> " + m.getName());

                        HttpServer.addEndpoint(mapping.value(), m, controllerInstance);
                    }
                }
            }
            HttpServer.start(8080);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}