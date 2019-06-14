package it.uniroma2.dicii.sdcc.didatticamobile.dao;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import it.uniroma2.dicii.sdcc.didatticamobile.config.AppConfiguration;
import it.uniroma2.dicii.sdcc.didatticamobile.error.LoginAuthorizationException;
import it.uniroma2.dicii.sdcc.didatticamobile.error.RegistrationAuthorizationException;
import it.uniroma2.dicii.sdcc.didatticamobile.error.BadRequestException;
import it.uniroma2.dicii.sdcc.didatticamobile.error.UsernameConflictException;
import it.uniroma2.dicii.sdcc.didatticamobile.error.ParserException;
import it.uniroma2.dicii.sdcc.didatticamobile.error.TemporaryUnavailableException;
import it.uniroma2.dicii.sdcc.didatticamobile.error.UnexpectedServerResponseException;
import it.uniroma2.dicii.sdcc.didatticamobile.error.UserDaoException;
import it.uniroma2.dicii.sdcc.didatticamobile.model.User;
import it.uniroma2.dicii.sdcc.didatticamobile.dto.UserDTO;
/*
 * An UserDaoREST object is a concrete implementation of UserDao interface. It provides method
 * to make requests to a persistence layer consisting in a remote REST service.
 * */
public class UserDaoREST implements UserDao {


    @Override
    public User register(User user, String fiscalCode) throws UsernameConflictException, RegistrationAuthorizationException, TemporaryUnavailableException, UserDaoException {

        try {
            AppConfiguration appConfiguration = AppConfiguration.getInstance();

            // Build the query used to register the user
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(appConfiguration.readProperty("apigateway_address"));
            stringBuilder.append(appConfiguration.readProperty("application_url"));
            stringBuilder.append("users");
            String query = stringBuilder.toString();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", user.getUsername());
            jsonObject.put("password", user.getPassword());
            jsonObject.put("name", user.getName());
            jsonObject.put("surname", user.getSurname());
            jsonObject.put("fiscalCode", fiscalCode);
            jsonObject.put("mail", user.getMail());
            String jsonString = jsonObject.toString();

            // Make the POST request of registration to the server
            HttpURLConnection connection = ConnectionHelper.sendPost(query, jsonString);

            // Checking the response code. Upon success the created user is returned. Otherwise,
            // an ad hoc exception is thrown
            int responseCode = connection.getResponseCode();
            switch (responseCode) {
                case HttpURLConnection.HTTP_CREATED:
                    try (InputStream inputStream = connection.getInputStream()) {
                        JSONParser jsonParser = new JSONParser();
                        UserDTO userDTO = (UserDTO) jsonParser.fromStreamToObject(inputStream, UserDTO.class);
                        return userDTO.getUser();
                    }
                case HttpURLConnection.HTTP_CONFLICT:
                    throw new UsernameConflictException();
                case HttpURLConnection.HTTP_FORBIDDEN:
                    throw new RegistrationAuthorizationException();
                case HttpURLConnection.HTTP_BAD_REQUEST:
                    throw new BadRequestException();
                case HttpURLConnection.HTTP_UNAVAILABLE:
                    throw new TemporaryUnavailableException();
                default:
                    throw new UnexpectedServerResponseException();
            }
        } catch (BadRequestException | ParserException | IOException | JSONException | UnexpectedServerResponseException e) {
            throw new UserDaoException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public String login(String username, String password) throws LoginAuthorizationException, TemporaryUnavailableException, UserDaoException {

        try {
            AppConfiguration appConfiguration = AppConfiguration.getInstance();

            // Build the query used to make the login request
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(appConfiguration.readProperty("apigateway_address"));
            stringBuilder.append(appConfiguration.readProperty("application_url"));
            stringBuilder.append("token");
            String query = stringBuilder.toString();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", username);
            jsonObject.put("password", password);
            String jsonString = jsonObject.toString();

            // Make the POST request of login to the server
            HttpURLConnection connection = ConnectionHelper.sendPost(query, jsonString);

            // Checking the response code. Upon success the created access token is returned. Otherwise,
            // an ad hoc exception is thrown
            int responseCode = connection.getResponseCode();
            switch (responseCode) {
                case HttpURLConnection.HTTP_CREATED:
                    String token = connection.getHeaderField("Set-Cookie").split("=")[1];
                    return token;
                case HttpURLConnection.HTTP_UNAUTHORIZED:
                    throw new LoginAuthorizationException();
                case HttpURLConnection.HTTP_BAD_REQUEST:
                    throw new BadRequestException();
                case HttpURLConnection.HTTP_UNAVAILABLE:
                    throw new TemporaryUnavailableException();
                default:
                    throw new UnexpectedServerResponseException();
            }
        } catch (IOException | JSONException | BadRequestException | UnexpectedServerResponseException e) {
            throw new UserDaoException(e.getMessage(), e.getCause());
        }
    }
}
