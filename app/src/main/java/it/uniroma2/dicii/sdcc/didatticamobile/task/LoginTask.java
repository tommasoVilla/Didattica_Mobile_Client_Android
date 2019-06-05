package it.uniroma2.dicii.sdcc.didatticamobile.task;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Base64;
import android.view.View;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import it.uniroma2.dicii.sdcc.didatticamobile.R;
import it.uniroma2.dicii.sdcc.didatticamobile.activity.HomeActivity;
import it.uniroma2.dicii.sdcc.didatticamobile.activity.InternetConnectionStatus;
import it.uniroma2.dicii.sdcc.didatticamobile.dao.UserDao;
import it.uniroma2.dicii.sdcc.didatticamobile.dao.UserDaoFactory;
import it.uniroma2.dicii.sdcc.didatticamobile.error.ErrorHandler;
import it.uniroma2.dicii.sdcc.didatticamobile.error.NoInternetConnectionException;

/* A LoginTask is a task responsible to make the login request to the server.*/
public class LoginTask extends AsyncTask<String, Void, Void>{

    private Activity loginActivity;

    /*
     * This field is populated with the exception thrown inside doInBackground. If an exception occurs
     * this field is not null and inside onPostExecute is called the ErrorHandler object to show the
     * error to the user.
     * */
    private Exception thrownException;

    public LoginTask(Activity activity){
        this.loginActivity = activity;
    }

    /* During the execution a progress bar is shown and the other widgets are hidden.*/
    @Override
    protected void onPreExecute() {
        loginActivity.findViewById(R.id.btnLoginMain).setVisibility(View.GONE);
        loginActivity.findViewById(R.id.etUsernameMain).setVisibility(View.GONE);
        loginActivity.findViewById(R.id.etPasswordMain).setVisibility(View.GONE);
        loginActivity.findViewById(R.id.btnRegistrationMain).setVisibility(View.GONE);
        loginActivity.findViewById(R.id.tvDoRegistrationMain).setVisibility(View.GONE);
        loginActivity.findViewById(R.id.pbLoadingMain).setVisibility(View.VISIBLE);
    }

    @Override
    protected Void doInBackground(String... strings) {
        try {
            /* If the device is not connected to Internet an error is raised*/
            InternetConnectionStatus internetConnectionStatus = new InternetConnectionStatus();
            if (!internetConnectionStatus.isDeviceConnectedToInternet(loginActivity)) {
                throw new NoInternetConnectionException();
            }
            /* The interaction with the persistence layer is delegated to an UserDao object*/
            UserDaoFactory userDaoFactory = UserDaoFactory.getInstance();
            UserDao userDao = userDaoFactory.createUserDao();
            String token = userDao.login(strings[0], strings[1]);
            parseAndStoreToken(token);
            // Store username and password in the shared preference to make them available for the
            // refresh token task
            SharedPreferences sharedPreferences = loginActivity.getSharedPreferences("shared_preference", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("user_username", strings[0]);
            editor.putString("user_password", strings[1]);
            editor.apply();

        } catch (Exception e) {
            thrownException = e;
        }

    return null;
    }

    /**
     * The function parses the access token returned from a successfully login request and stores it
     * into a SharedPreference file making it available for future authentications. Moreover, the token
     * is decoded and the user information read from it are stored in the SharedPreference file too.
     * @param token the string representing the access token
     * */
    private void parseAndStoreToken(String token) throws UnsupportedEncodingException, JSONException {

        String[] encodedClaims = token.split("\\.");
        String body = new String(Base64.decode(encodedClaims[1], Base64.URL_SAFE), "UTF-8");
        JSONObject claims = new JSONObject(body);


        SharedPreferences sharedPreference = loginActivity.getSharedPreferences("shared_preference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.putString("token", token);
        editor.putString("user_name", claims.get("Name").toString());
        editor.putString("user_surname", claims.get("Surname").toString());
        editor.putString("user_type", claims.get("Type").toString());
        editor.apply();

    }

    /* After the execution, the visibility of the widgets is restored.*/
    @Override
    protected void onPostExecute(Void v) {
        if (thrownException != null){
            ErrorHandler errorHandler = new ErrorHandler(loginActivity);
            errorHandler.show(thrownException);

            loginActivity.findViewById(R.id.btnLoginMain).setVisibility(View.VISIBLE);
            loginActivity.findViewById(R.id.etUsernameMain).setVisibility(View.VISIBLE);
            loginActivity.findViewById(R.id.etPasswordMain).setVisibility(View.VISIBLE);
            loginActivity.findViewById(R.id.btnRegistrationMain).setVisibility(View.VISIBLE);
            loginActivity.findViewById(R.id.tvDoRegistrationMain).setVisibility(View.VISIBLE);
            loginActivity.findViewById(R.id.pbLoadingMain).setVisibility(View.GONE);

        } else {
            // If the login succeeds the HomeActivity is shown and the task responsible for refreshing
            // the access token is launched.
            RefreshTokenTask refreshTokenTask = new RefreshTokenTask(loginActivity);
            refreshTokenTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            Intent intent = new Intent(loginActivity, HomeActivity.class);
            loginActivity.startActivity(intent);
        }
    }

}
