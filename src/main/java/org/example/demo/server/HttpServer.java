package org.example.demo.server;

import org.example.demo.api.RequestParam;
import org.example.demo.api.Request;
import java.net.*;
import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class HttpServer {

    private static final Map<String, Method> endPoints = new HashMap<>();
    private static final Map<String, Object> controllers = new HashMap<>();
    private static String staticFileLocation = "www";

    public static void staticfiles(String path) {
        staticFileLocation = path;
    }

    public static void addEndpoint(String path, Method method, Object instance) {
        endPoints.put(path, method);
        controllers.put(path, instance);
    }

    public static void start(int port) throws IOException, URISyntaxException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server ready to receive on port " + port + "...");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 OutputStream outputStream = clientSocket.getOutputStream()) {

                String inputLine = in.readLine();
                if (inputLine == null) continue;

                String[] firstLineTokens = inputLine.split(" ");
                String reqPath = "";
                String reqQuery = "";

                if (firstLineTokens.length >= 2) {
                    URI requestedURI = new URI(firstLineTokens[1]);
                    reqPath = requestedURI.getPath();
                    reqQuery = requestedURI.getQuery() != null ? requestedURI.getQuery() : "";
                }
                while (in.ready() && in.readLine() != null);
                if (endPoints.containsKey(reqPath)) {
                    Method m = endPoints.get(reqPath);
                    Object instance = controllers.get(reqPath);
                    Request req = new Request(reqPath, reqQuery);

                    Parameter[] parameters = m.getParameters();
                    Object[] argsToPass = new Object[parameters.length];

                    for (int i = 0; i < parameters.length; i++) {
                        Parameter p = parameters[i];
                        if (p.isAnnotationPresent(RequestParam.class)) {
                            RequestParam ann = p.getAnnotation(RequestParam.class);
                            String val = req.getQueryParam(ann.value());
                            argsToPass[i] = (val != null) ? val : ann.defaultValue();
                        }
                    }
                    String responseBody = (String) m.invoke(instance, argsToPass);
                    out.println("HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\n\r\n" + responseBody);
                } else {
                    serveStaticFile(reqPath, outputStream);
                }
            } catch (Exception e) {
                System.err.println("Request Error: " + e.getMessage());
            }
        }
    }
    private static void serveStaticFile(String reqPath, OutputStream outStream) {
        try {
            if (reqPath.equals("/")) reqPath = "/index.html";
            File file = new File("target/classes/" + staticFileLocation + reqPath);

            if (file.exists() && !file.isDirectory()) {
                byte[] fileBytes = Files.readAllBytes(file.toPath());
                String header = "HTTP/1.1 200 OK\r\n"
                        + "Content-Type: " + getContentType(reqPath) + "\r\n"
                        + "Content-Length: " + fileBytes.length + "\r\n\r\n";
                outStream.write(header.getBytes());
                outStream.write(fileBytes);
            } else {
                outStream.write("HTTP/1.1 404 Not Found\r\n\r\n404 Not Found".getBytes());
            }
            outStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getContentType(String path) {
        if (path.endsWith(".html")) return "text/html";
        if (path.endsWith(".png")) return "image/png";
        if (path.endsWith(".js")) return "application/javascript";
        if (path.endsWith(".css")) return "text/css";
        return "text/plain";
    }
}