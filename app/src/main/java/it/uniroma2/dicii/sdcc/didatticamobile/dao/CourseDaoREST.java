package it.uniroma2.dicii.sdcc.didatticamobile.dao;

import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import it.uniroma2.dicii.sdcc.didatticamobile.config.AppConfiguration;
import it.uniroma2.dicii.sdcc.didatticamobile.error.BadRequestException;
import it.uniroma2.dicii.sdcc.didatticamobile.error.ConflictException;
import it.uniroma2.dicii.sdcc.didatticamobile.error.CourseDaoException;
import it.uniroma2.dicii.sdcc.didatticamobile.error.ErrorResponse;
import it.uniroma2.dicii.sdcc.didatticamobile.error.ExistentSubscriptionException;
import it.uniroma2.dicii.sdcc.didatticamobile.error.ExpiredTokenException;
import it.uniroma2.dicii.sdcc.didatticamobile.error.ParserException;
import it.uniroma2.dicii.sdcc.didatticamobile.error.TemporaryUnavailableException;
import it.uniroma2.dicii.sdcc.didatticamobile.error.UnexpectedServerResponseException;
import it.uniroma2.dicii.sdcc.didatticamobile.model.Course;

/*
 * An CourseDaoREST object is a concrete implementation of CourseDao interface. It provides method
 * to make requests to a persistence layer consisting in a remote REST service.
 * */
public class CourseDaoREST implements CourseDao {

    @Override
    public Course add(Course course, String token) throws CourseDaoException, ConflictException, TemporaryUnavailableException, ExpiredTokenException {
        try {
            AppConfiguration appConfiguration = AppConfiguration.getInstance();

            // Build the query used to add the course
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(appConfiguration.readProperty("apigateway_address"));
            stringBuilder.append(appConfiguration.readProperty("application_url"));
            stringBuilder.append("courses");
            String query = stringBuilder.toString();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", course.getName());
            jsonObject.put("department", course.getDepartment());
            jsonObject.put("year", course.getYear());
            jsonObject.put("semester", course.getSemester());
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(course.getSchedule().get(0).toJsonObject());
            jsonArray.put(course.getSchedule().get(1).toJsonObject());
            jsonArray.put(course.getSchedule().get(2).toJsonObject());
            jsonObject.put("schedule", jsonArray);
            jsonObject.put("description", course.getDescription());
            jsonObject.put("teacher", course.getTeacher());
            String jsonString = jsonObject.toString();

            // Make the POST request of inserting to the server
            HttpURLConnection connection = ConnectionHelper.sendPostWithToken(query, jsonString, token);

            // Checking the response code. Upon success the created course is returned. Otherwise,
            // an ad hoc exception is thrown
            int responseCode = connection.getResponseCode();
            switch (responseCode) {
                case HttpURLConnection.HTTP_CREATED:
                    try (InputStream inputStream = connection.getInputStream()) {
                        JSONParser jsonParser = new JSONParser();
                        Course addedCourse = (Course) jsonParser.fromStreamToObject(inputStream, Course.class);
                        return addedCourse;
                    }
                case HttpURLConnection.HTTP_CONFLICT:
                    throw new ConflictException();
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
                case HttpURLConnection.HTTP_BAD_REQUEST:
                    throw new BadRequestException();
                case HttpURLConnection.HTTP_UNAVAILABLE:
                    throw new TemporaryUnavailableException();
                default:
                    throw new UnexpectedServerResponseException();
            }
        } catch (BadRequestException | ParserException | IOException | JSONException | UnexpectedServerResponseException e) {
            throw new CourseDaoException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public List<Course> findBy(String criterion, String pattern, String token) throws CourseDaoException, ExpiredTokenException, BadRequestException, TemporaryUnavailableException {
        try {

            AppConfiguration appConfiguration = AppConfiguration.getInstance();

            // Build the query used to search courses
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(appConfiguration.readProperty("apigateway_address"));
            stringBuilder.append(appConfiguration.readProperty("application_url"));
            stringBuilder.append("courses/");
            stringBuilder.append(criterion);
            stringBuilder.append("/");
            stringBuilder.append(pattern);
            String query = stringBuilder.toString();

            HttpURLConnection connection = ConnectionHelper.sendGetWithToken(query, token);
            int responseCode = connection.getResponseCode();
            switch (responseCode) {
                case HttpURLConnection.HTTP_OK:
                    // The request body containing the matching courses is parsed using a JSONParser object
                    try (InputStream inputStream = connection.getInputStream()) {
                        JSONParser jsonParser = new JSONParser();
                        return jsonParser.fromStreamToList(inputStream, new TypeToken<Collection<Course>>(){}.getType());
                    }
                case HttpURLConnection.HTTP_NOT_FOUND:
                    return new ArrayList<Course>();
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
                case HttpURLConnection.HTTP_BAD_REQUEST:
                    throw new BadRequestException();
                case HttpURLConnection.HTTP_UNAVAILABLE:
                    throw new TemporaryUnavailableException();
                default:
                    throw new UnexpectedServerResponseException();
            }

        } catch (IOException | UnexpectedServerResponseException | ParserException e) {
            throw new CourseDaoException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public void addCourseToStudent(String courseId, String studentUsername, String token) throws ExpiredTokenException, TemporaryUnavailableException, CourseDaoException, ExistentSubscriptionException {
        try {
            AppConfiguration appConfiguration = AppConfiguration.getInstance();

            // Build the query used to add the course to the student
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(appConfiguration.readProperty("apigateway_address"));
            stringBuilder.append(appConfiguration.readProperty("application_url"));
            stringBuilder.append("students/");
            stringBuilder.append(studentUsername);
            stringBuilder.append("/courses/");
            stringBuilder.append(courseId);
            String query = stringBuilder.toString();

            // Make the PUT request to the server
            HttpURLConnection connection = ConnectionHelper.sendPutWithToken(query, token);

            // Checking the response code. Upon failure, an ad hoc exception is thrown
            int responseCode = connection.getResponseCode();
            switch (responseCode) {
                case HttpURLConnection.HTTP_OK:
                    return;
                case HttpURLConnection.HTTP_CONFLICT:
                    throw new ExistentSubscriptionException();
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
        } catch (ParserException | IOException | UnexpectedServerResponseException e) {
            throw new CourseDaoException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public List<Course> findBySubscribedStudent(String studentUsername, String token) throws ExpiredTokenException, TemporaryUnavailableException, CourseDaoException {
        try {

            AppConfiguration appConfiguration = AppConfiguration.getInstance();

            // Build the query used to add the course
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(appConfiguration.readProperty("apigateway_address"));
            stringBuilder.append(appConfiguration.readProperty("application_url"));
            stringBuilder.append("courses/students/");
            stringBuilder.append(studentUsername);
            String query = stringBuilder.toString();

            HttpURLConnection connection = ConnectionHelper.sendGetWithToken(query, token);
            int responseCode = connection.getResponseCode();
            switch (responseCode) {
                case HttpURLConnection.HTTP_OK:
                    // The request body containing the matching courses is parsed using a JSONParser object
                    try (InputStream inputStream = connection.getInputStream()) {
                        JSONParser jsonParser = new JSONParser();
                        return jsonParser.fromStreamToList(inputStream, new TypeToken<Collection<Course>>(){}.getType());
                    }
                case HttpURLConnection.HTTP_NOT_FOUND:
                    return new ArrayList<Course>();
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
            throw new CourseDaoException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public void removeStudentFromCourse(String courseId, String studentUsername, String token)
            throws ExpiredTokenException, TemporaryUnavailableException, CourseDaoException {
        try {
            AppConfiguration appConfiguration = AppConfiguration.getInstance();

            // Build the query used to add the course to the student
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(appConfiguration.readProperty("apigateway_address"));
            stringBuilder.append(appConfiguration.readProperty("application_url"));
            stringBuilder.append("students/");
            stringBuilder.append(studentUsername);
            stringBuilder.append("/courses/");
            stringBuilder.append(courseId);
            String query = stringBuilder.toString();

            // Make the DELETE request to the server
            HttpURLConnection connection = ConnectionHelper.sendDeleteWithToken(query, token);

            // Checking the response code. Upon failure, an ad hoc exception is thrown
            int responseCode = connection.getResponseCode();
            switch (responseCode) {
                case HttpURLConnection.HTTP_OK:
                    return;
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
        } catch (ParserException | IOException | UnexpectedServerResponseException e) {
            throw new CourseDaoException(e.getMessage(), e.getCause());
        }
    }
}
