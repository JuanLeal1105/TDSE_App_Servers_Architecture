package org.example.demo.api;

public class Response {
    private String body = "";
    private String contentType = "text/plain";

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }
}
