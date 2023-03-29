package ru.netology;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;

import java.io.BufferedReader;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Request {
    private final String method;
    private final String path;
    private final BufferedReader body;
    private final List<NameValuePair> queryParams;

    public Request(String method, String path, BufferedReader body) throws URISyntaxException {
        this.method = method;
        this.body = body;
        URIBuilder builder = new URIBuilder(path, StandardCharsets.UTF_8);
        this.path = builder.getPath();
        queryParams = builder.getQueryParams();
    }

    public List<NameValuePair> getQueryParams() {
        return queryParams;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public BufferedReader getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "Request{" +
                "method='" + method + '\'' +
                ", path='" + path + '\'' +
                ", body=" + body +
                '}';
    }
}
