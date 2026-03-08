package org.example.demo.api;

import java.util.HashMap;
import java.util.Map;

public class Request {
    private String path;
    private String query;
    private Map<String, String> queryParams = new HashMap<>();

    public Request(String path, String query) {
        this.path = path;
        this.query = query;
        parseQuery(query);
    }

    private void parseQuery(String query) {
        if (query != null && !query.isEmpty()) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    queryParams.put(keyValue[0], keyValue[1]);
                }
            }
        }
    }

    public String getPath() { return path; }
    public String getQuery() { return query; }
    public String getQueryParam(String key) { return queryParams.get(key); }
}
