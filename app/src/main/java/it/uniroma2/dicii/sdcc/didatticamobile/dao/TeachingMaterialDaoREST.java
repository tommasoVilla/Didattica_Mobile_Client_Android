package it.uniroma2.dicii.sdcc.didatticamobile.dao;

import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collection;

import it.uniroma2.dicii.sdcc.didatticamobile.config.AppConfiguration;
import it.uniroma2.dicii.sdcc.didatticamobile.error.ErrorResponse;
import it.uniroma2.dicii.sdcc.didatticamobile.error.ExpiredTokenException;
import it.uniroma2.dicii.sdcc.didatticamobile.error.ParserException;
import it.uniroma2.dicii.sdcc.didatticamobile.error.TeachingMaterialDaoException;
import it.uniroma2.dicii.sdcc.didatticamobile.error.TemporaryUnavailableException;
import it.uniroma2.dicii.sdcc.didatticamobile.error.UnexpectedServerResponseException;

public class TeachingMaterialDaoREST implements TeachingMaterialDao {

    @Override
    public ArrayList<String> findTeachingMaterialByCourse(String courseId, String token) throws ExpiredTokenException, TemporaryUnavailableException, TeachingMaterialDaoException {
        try {

            AppConfiguration appConfiguration = AppConfiguration.getInstance();

            // Build the query used to search all teaching material marked with given courseName
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(appConfiguration.readProperty("apigateway_address"));
            stringBuilder.append(appConfiguration.readProperty("application_url"));
            stringBuilder.append("teachingMaterials/");
            stringBuilder.append(courseId);
            String query = stringBuilder.toString();

            HttpURLConnection connection = ConnectionHelper.sendGetWithToken(query, token);
            int responseCode = connection.getResponseCode();
            switch (responseCode) {
                case HttpURLConnection.HTTP_OK:
                    // The request body containing the files matching with given course
                    try (InputStream inputStream = connection.getInputStream()) {
                        JSONParser jsonParser = new JSONParser();
                        return jsonParser.fromStreamToList(inputStream, new TypeToken<Collection<String>>(){}.getType());
                    }
                case HttpURLConnection.HTTP_UNAUTHORIZED:
                    JSONParser jsonParser = new JSONParser();
                    // If the access token is expired the user will be asked to redo the login
                    try (InputStream inputStream = connection.getErrorStream()) {
                        ErrorResponse errorResponse = (ErrorResponse) jsonParser.fromStreamToObject(inputStream, ErrorResponse.class);
                        if (errorResponse.getError().equals("Expired token")) {
                            throw new ExpiredTokenException();
                        }
                        throw new UnexpectedServerResponseException();
                    }
                case HttpURLConnection.HTTP_UNAVAILABLE:
                    throw new TemporaryUnavailableException();
                default:
                    throw new UnexpectedServerResponseException();
            }

        } catch (IOException | UnexpectedServerResponseException | ParserException e) {
            throw new TeachingMaterialDaoException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public String getDownloadLink(String username, String courseId, String filename, String token)
            throws ExpiredTokenException, TeachingMaterialDaoException, TemporaryUnavailableException {

        try {

            AppConfiguration appConfiguration = AppConfiguration.getInstance();

            // Build the query used to search all teaching material marked with given courseName
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(appConfiguration.readProperty("apigateway_address"));
            stringBuilder.append(appConfiguration.readProperty("application_url"));
            stringBuilder.append("teachingMaterials/download/");
            stringBuilder.append(username);
            stringBuilder.append("/");
            stringBuilder.append(courseId);
            stringBuilder.append("/");
            stringBuilder.append(filename);
            String query = stringBuilder.toString();

            HttpURLConnection connection = ConnectionHelper.sendGetWithToken(query, token);
            int responseCode = connection.getResponseCode();
            switch (responseCode) {
                case HttpURLConnection.HTTP_OK:
                    // The request body containing the link to download file
                    try (InputStream inputStream = connection.getInputStream()) {
                        return isToString(inputStream);
                    }
                case HttpURLConnection.HTTP_UNAUTHORIZED:
                    JSONParser jsonParser = new JSONParser();
                    // If the access token is expired the user will be asked to redo the login
                    try (InputStream inputStream = connection.getErrorStream()) {
                        ErrorResponse errorResponse = (ErrorResponse) jsonParser.fromStreamToObject(inputStream, ErrorResponse.class);
                        if (errorResponse.getError().equals("Expired token")) {
                            throw new ExpiredTokenException();
                        }
                        throw new UnexpectedServerResponseException();
                    }
                case HttpURLConnection.HTTP_UNAVAILABLE:
                    throw new TemporaryUnavailableException();
                default:
                    throw new UnexpectedServerResponseException();
            }

        } catch (IOException | UnexpectedServerResponseException | ParserException e) {
            throw new TeachingMaterialDaoException(e.getMessage(), e.getCause());
        }
    }

    // Utility method to obtain a string from an inputstream
    private String isToString(InputStream is) throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(is, "UTF-8");
        for (; ; ) {
            int rsz = in.read(buffer, 0, buffer.length);
            if (rsz < 0)
                break;
            out.append(buffer, 0, rsz);
        }
        return out.toString();
    }

}
