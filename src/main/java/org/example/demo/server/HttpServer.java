package org.example.demo.server;

import org.example.demo.api.Request;

import java.net.*;
import java.io.*;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class HttpServer {

    static Map<String, Method> endPoints = new HashMap<>();
    static Map<String, Object> controllers = new HashMap<>();
    static String staticFileLocation;

    public static void staticfiles(String path){
        staticFileLocation = path;
    }

    public static void addEndpoint(String path, Method method, Object instance) {
        endPoints.put(path, method);
        controllers.put(path, instance);
    }

    public static void start(int port) throws IOException, URISyntaxException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server ready to receive on port " + port + "...");

        boolean running = true;
        while (running) {
            Socket clientSocket = serverSocket.accept();

            try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 OutputStream outputStream = clientSocket.getOutputStream()) {

                String inputLine;
                boolean isFirstLine = true;
                String reqPath = "";
                String reqQuery = "";

                while ((inputLine = in.readLine()) != null) {
                    if (isFirstLine) {
                        String[] firstLineTokens = inputLine.split(" ");
                        if (firstLineTokens.length >= 3) {
                            String uristr = firstLineTokens[1];
                            URI requestedURI = new URI(uristr);
                            reqPath = requestedURI.getPath();
                            reqQuery = requestedURI.getQuery() != null ? requestedURI.getQuery() : "";
                        }
                        isFirstLine = false;
                    }
                    if (!in.ready()) break;
                }

                Method m = endPoints.get(reqPath);
                if (m != null) {
                    Object instance = controllers.get(reqPath);
                    Request req = new Request(reqPath, reqQuery);

                    String responseBody = (String) m.invoke(instance);

                    String outputLine = "HTTP/1.1 200 OK\r\n"
                            + "Content-Type: text/plain\r\n" // Assuming REST returns plain text/JSON, not full HTML wrapped
                            + "\r\n"
                            + responseBody;
                    out.println(outputLine);
                } else {
                    serveStaticFile(reqPath, outputStream);
                }
            } catch (Exception e) {
                System.err.println("Error processing request: " + e.getMessage());
            }
        }
        serverSocket.close();
    }

    private static void serveStaticFile(String reqPath, OutputStream outStream) {
        try {
            if (reqPath.equals("/")) {
                reqPath = "/index.html";
            }

            File file = new File("target/classes/" + staticFileLocation + reqPath);
            if (file.exists() && !file.isDirectory()) {
                String contentType = getContentType(reqPath);
                byte[] fileBytes = Files.readAllBytes(file.toPath());

                String header = "HTTP/1.1 200 OK\r\n"
                        + "Content-Type: " + contentType + "\r\n"
                        + "Content-Length: " + fileBytes.length + "\r\n"
                        + "\r\n";

                outStream.write(header.getBytes());
                outStream.write(fileBytes);
                outStream.flush();
            } else {
                String response = "HTTP/1.1 404 Not Found\r\n\r\n404 File Not Found";
                outStream.write(response.getBytes());
                outStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getContentType(String path){
        if (path.endsWith(".html")) return "text/html";
        if (path.endsWith(".css")) return "text/css";
        if (path.endsWith(".js")) return "application/javascript";
        if (path.endsWith(".png")) return "image/png";
        if (path.endsWith(".jpg") || path.endsWith(".jpeg")) return "image/jpeg";
        return "text/plain";
    }
}