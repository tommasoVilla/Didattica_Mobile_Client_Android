package it.uniroma2.dicii.sdcc.didatticamobile.dao;

import com.google.gson.reflect.TypeToken;
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
import it.uniroma2.dicii.sdcc.didatticamobile.error.ErrorResponse;
import it.uniroma2.dicii.sdcc.didatticamobile.error.ExamDaoException;
import it.uniroma2.dicii.sdcc.didatticamobile.error.ExistentSubscriptionException;
import it.uniroma2.dicii.sdcc.didatticamobile.error.ExpiredTokenException;
import it.uniroma2.dicii.sdcc.didatticamobile.error.NotAllowedExamReservationException;
import it.uniroma2.dicii.sdcc.didatticamobile.error.ParserException;
import it.uniroma2.dicii.sdcc.didatticamobile.error.TemporaryUnavailableException;
import it.uniroma2.dicii.sdcc.didatticamobile.error.UnexpectedServerResponseException;
import it.uniroma2.dicii.sdcc.didatticamobile.model.Exam;

/*
* An ExamDaoREST object is a concrete implementation of ExamDao interface. It provides method
* to make requests to a persistence layer consisting in a remote REST service.
* */
public class ExamDaoREST implements ExamDao{

    @Override
    public Exam add(Exam exam, String token) throws ConflictException, ExpiredTokenException, TemporaryUnavailableException, ExamDaoException {
        try {
            AppConfiguration appConfiguration = AppConfiguration.getInstance();

            // Build the query used to add the course
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(appConfiguration.readProperty("apigateway_address"));
            stringBuilder.append(appConfiguration.readProperty("application_url"));
            stringBuilder.append("exams");
            String query = stringBuilder.toString();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("course", exam.getCourse());
            jsonObject.put("call", exam.getCall());
            jsonObject.put("date", exam.getDate());
            jsonObject.put("expirationDate", exam.getExpirationDate());
            jsonObject.put("room", exam.getRoom());
            jsonObject.put("startTime", exam.getStartTime());
            String jsonString = jsonObject.toString();

            // Make the POST request of inserting to the server
            HttpURLConnection connection = ConnectionHelper.sendPostWithToken(query, jsonString, token);

            // Checking the response code. Upon success the created exam is returned. Otherwise,
            // an ad hoc exception is thrown
            int responseCode = connection.getResponseCode();
            switch (responseCode) {
                case HttpURLConnection.HTTP_CREATED:
                    try (InputStream inputStream = connection.getInputStream()) {
                        JSONParser jsonParser = new JSONParser();
                        Exam addedExam = (Exam) jsonParser.fromStreamToObject(inputStream, Exam.class);
                        return addedExam;
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
            throw new ExamDaoException(e.getMessage(), e.getCause());
        }
    }


    @Override
    public List<Exam> findByCourse(String courseId, String token) throws ExpiredTokenException, BadRequestException, TemporaryUnavailableException, ExamDaoException {
        try {

            AppConfiguration appConfiguration = AppConfiguration.getInstance();

            // Build the query used to add the course
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(appConfiguration.readProperty("apigateway_address"));
            stringBuilder.append(appConfiguration.readProperty("application_url"));
            stringBuilder.append("exams/");
            stringBuilder.append(courseId);
            String query = stringBuilder.toString();

            HttpURLConnection connection = ConnectionHelper.sendGetWithToken(query, token);
            int responseCode = connection.getResponseCode();
            switch (responseCode) {
                case HttpURLConnection.HTTP_OK:
                    // The request body containing the matching courses is parsed using a JSONParser object
                    try (InputStream inputStream = connection.getInputStream()) {
                        JSONParser jsonParser = new JSONParser();
                        return jsonParser.fromStreamToList(inputStream, new TypeToken<Collection<Exam>>(){}.getType());
                    }
                case HttpURLConnection.HTTP_NOT_FOUND:
                    return new ArrayList<Exam>();
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
            throw new ExamDaoException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public void registerStudentToExam(String studentUsername, String examId, String token) throws ExpiredTokenException, BadRequestException, TemporaryUnavailableException, ExamDaoException, ExistentSubscriptionException, NotAllowedExamReservationException {
        try {

            AppConfiguration appConfiguration = AppConfiguration.getInstance();

            // Build the query used to add the course
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(appConfiguration.readProperty("apigateway_address"));
            stringBuilder.append(appConfiguration.readProperty("application_url"));
            stringBuilder.append("exams/");
            stringBuilder.append(examId);
            stringBuilder.append("/students/");
            stringBuilder.append(studentUsername);
            String query = stringBuilder.toString();

            HttpURLConnection connection = ConnectionHelper.sendPutWithToken(query, token);
            int responseCode = connection.getResponseCode();
            switch (responseCode) {
                case HttpURLConnection.HTTP_OK: // The reservation succeeds
                    return;
                case HttpURLConnection.HTTP_CONFLICT: // The reservation already exists
                    throw new ExistentSubscriptionException();
                case HttpURLConnection.HTTP_FORBIDDEN: // The student is not registered to the course
                    throw new NotAllowedExamReservationException();
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
            throw new ExamDaoException(e.getMessage(), e.getCause());
        }
    }

}
