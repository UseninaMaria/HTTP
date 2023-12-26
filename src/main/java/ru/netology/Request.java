package ru.netology;

import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.net.URLEncodedUtils;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Request {
    private String method;
    private String path;
    private Map<String, String> headers;
    private InputStream body;

    private String protocol;
    private Map<String, List<String>> queryParams;

    public Request(String method, String path, String protocol, Map<String, String> headers, InputStream body) {
        this.method = method;
        this.path = path;
        this.protocol = protocol;
        this.headers = headers;
        this.body = body;
    }

    public Request(String method, String path, String protocol, Map<String, String> headers) {
        this.method = method;
        this.path = path;
        this.protocol = protocol;
        this.headers = headers;
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

    public void setQueryParams(Map<String, List<String>> parsedParams) {
        this.queryParams = parsedParams;
    }

    public String getQueryParam(String name) {
        if (queryParams != null) {
            List<String> values = queryParams.get(name);
            if (values != null && !values.isEmpty()) {
                return values.get(0);
            }
        }
        return null;
    }
}

