package org.example.demo;

import org.example.demo.api.RequestParam;

@RestController
public class HelloController {

    @GetMapping("/")
    public static String index() {
        return "Greetings from Spring Boot!";
    }
    @GetMapping("/pi")
    public static String getPI() {
        return "PI: "+ Math.PI;
    }
    @GetMapping("/hello")
    public static String helloWorld() {
        return "Hello World";
    }

    @GetMapping("/greeting")
    public String greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return "Hello " + name;
    }



}
