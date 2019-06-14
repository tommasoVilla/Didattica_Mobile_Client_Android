package it.uniroma2.dicii.sdcc.didatticamobile.dao;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import it.uniroma2.dicii.sdcc.didatticamobile.config.AppConfiguration;
/*
 * ConnectionHelper is an utility class that provides methods used to interact with a remote server
 * through Http protocol.
 * */
public class ConnectionHelper {

    public static HttpURLConnection sendPost(String query, String jsonString) throws IOException {
        return sendPost(query, jsonString, null);
    }

    public static HttpURLConnection sendPostWithToken(String query, String jsonString, String token) throws IOException {
        return sendPost(query, jsonString, token);
    }

    /**
     * @param query the url to connect
     * @return {@code HttpURLConnection} the object to use to send requests and receive responses from the server
     * */
    private static HttpURLConnection createConnection(String query) throws IOException {
        URL url = new URL(query);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        int timeout = Integer.parseInt(AppConfiguration.getInstance().readProperty("connection_timeout"));
        connection.setConnectTimeout(timeout);
        connection.setReadTimeout(timeout);
        return connection;
    }

    /**
     * sendPost is the method used to make an HTTP POST request to the server
     * @param query the url to connect
     * @param jsonString the body of the Post
     * @param token the token to include in the header. Can be null.
     * @return {@code HttpURLConnection} the object used to check response code from the server
     * */
    private static HttpURLConnection sendPost(String query, String jsonString, String token) throws IOException {
        HttpURLConnection connection = createConnection(query);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        if (token != null){
            connection.setRequestProperty("Cookie", "token=" + token);
        }
        connection.setDoOutput(true);
        try(OutputStream outputStream = connection.getOutputStream()) {
            byte[] input = jsonString.getBytes(StandardCharsets.UTF_8);
            outputStream.write(input, 0, input.length);
        }
        return connection;
    }

    public static HttpURLConnection sendGetWithToken(String query, String token) throws IOException {
        HttpURLConnection connection = createConnection(query);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");
        if (token != null) {
            connection.setRequestProperty("Cookie", "token=" + token);
        }
        return connection;
    }

    public static HttpURLConnection sendPutWithToken(String query, String token, String body) throws IOException {
        HttpURLConnection connection = createConnection(query);
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        if (token != null){
            connection.setRequestProperty("Cookie", "token=" + token);
        }
        connection.setDoOutput(true);
        try(OutputStream outputStream = connection.getOutputStream()) {
            byte[] input = body.getBytes(StandardCharsets.UTF_8);
            outputStream.write(input, 0, input.length);
        }
        return connection;
    }

    public static HttpURLConnection sendDeleteWithToken(String query, String token, String body) throws IOException {
        HttpURLConnection connection = createConnection(query);
        connection.setRequestMethod("DELETE");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        if (token != null){
            connection.setRequestProperty("Cookie", "token=" + token);
        }
        connection.setDoOutput(true);
        try(OutputStream outputStream = connection.getOutputStream()) {
            byte[] input = body.getBytes(StandardCharsets.UTF_8);
            outputStream.write(input, 0, input.length);
        }
        return connection;
    }

    public static HttpURLConnection sendPutWithToken(String query, String token) throws IOException {
        HttpURLConnection connection = createConnection(query);
        connection.setRequestMethod("PUT");
        if (token != null) {
            connection.setRequestProperty("Cookie", "token=" + token);
        }
        return connection;
    }

    public static HttpURLConnection sendDeleteWithToken(String query, String token) throws IOException {
        HttpURLConnection connection = createConnection(query);
        connection.setRequestMethod("DELETE");
        if (token != null) {
            connection.setRequestProperty("Cookie", "token=" + token);
        }
        return connection;
    }
}
