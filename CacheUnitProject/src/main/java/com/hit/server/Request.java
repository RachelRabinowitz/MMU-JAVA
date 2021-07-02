package main.java.com.hit.server;

import java.util.Map;

/**
 * This class implements request structure.
 * @param <T> Generic body type
 */
public class Request<T> extends java.lang.Object implements java.io.Serializable {

    private Map<String, String> headers;
    private T body;

    public Request(Map<String, String> headers, T body) {
        this.headers = headers;
        this.body = body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return getHeaders().toString().split("=")[1].toUpperCase().replace("}", "");
    }
}
