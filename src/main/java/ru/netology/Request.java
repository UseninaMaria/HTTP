package ru.netology;

import java.io.InputStream;
import java.util.Map;

public class Request {
    private String method;
    private String path;
    private Map<String, String> headers;
    private InputStream body;

    private String protocol;

    public Request(String method, String path, String protocol, Map<String, String> headers, InputStream body) {
        this.method = method;
        this.path = path;
        this.protocol = protocol;
        this.headers = headers;
        this.body = body;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public InputStream getBody() {
        return body;
    }

    public String getProtocol() {
        return protocol;
    }
}
