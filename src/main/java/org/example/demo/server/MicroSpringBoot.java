package org.example.demo.server;

import org.example.demo.GetMapping;
import org.example.demo.RestController;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;

public class MicroSpringBoot {

    public static void main(String[] args) {
        System.out.println("Starting MicroSpringBoot...");

        scanAndRegister("org.example.demo");

        try {
            HttpServer.start(8080);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void scanAndRegister(String basePackage) {
        try {
            String path = basePackage.replace('.', '/');
            URL resource = ClassLoader.getSystemClassLoader().getResource(path);

            if (resource != null) {
                File directory = new File(resource.getFile());
                if (directory.exists()) {
                    for (File file : directory.listFiles()) {
                        if (file.getName().endsWith(".class")) {
                            String className = basePackage + '.' + file.getName().substring(0, file.getName().length() - 6);

                            Class<?> c = Class.forName(className);
                            if (c.isAnnotationPresent(RestController.class)) {
                                Object instance = c.getDeclaredConstructor().newInstance();

                                for (Method m : c.getDeclaredMethods()) {
                                    if (m.isAnnotationPresent(GetMapping.class)) {
                                        GetMapping mapping = m.getAnnotation(GetMapping.class);
                                        System.out.println("Auto-Mapped URI: " + mapping.value() + " -> " + m.getName());
                                        HttpServer.addEndpoint(mapping.value(), m, instance);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error during classpath scanning: " + e.getMessage());
        }
    }
}