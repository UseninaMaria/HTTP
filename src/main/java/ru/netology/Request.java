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

    public String getQueryParam(String name) {
        String query = getPath();
        List<NameValuePair> params = null;
        try {
            params = URLEncodedUtils.parse(new URI("?" + query), StandardCharsets.UTF_8);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        if (params != null) {
            for (NameValuePair param : params) {
                if (param.getName().equals(name)) {
                    return param.getValue();
                }
            }
        }
        return null;
    }

    public Map<String, List<String>> getQueryParams() {
        String query = getPath();
        List<NameValuePair> params = null;
        try {
            params = URLEncodedUtils.parse(new URI(query), StandardCharsets.UTF_8);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        Map<String, List<String>> queryParams = new HashMap<>();
        if (params != null) {
            for (NameValuePair param : params) {
                if (!queryParams.containsKey(param.getName())) {
                    queryParams.put(param.getName(), new ArrayList<>());
                }
                queryParams.get(param.getName()).add(param.getValue());
            }
        }
        return queryParams;
    }
}

