package it.uniroma2.dicii.sdcc.didatticamobile.task;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import it.uniroma2.dicii.sdcc.didatticamobile.activity.InternetConnectionStatus;
import it.uniroma2.dicii.sdcc.didatticamobile.dao.UserDao;
import it.uniroma2.dicii.sdcc.didatticamobile.dao.UserDaoFactory;
import it.uniroma2.dicii.sdcc.didatticamobile.error.ErrorHandler;
import it.uniroma2.dicii.sdcc.didatticamobile.error.UnexpectedServerResponseException;

/* A RefreshTokenTask is a task responsible for refreshing the JWT token used in the user
 * authentication. The task periodically make a token request to the server providing the user
 * credentials to prevent the token expiration upon user requests and reduce the overhead concerning
 * its renewal.
 * The returned token is stored in a SharedPreferences file and made available for the
 * future authentications by the client.*/
public class RefreshTokenTask extends AsyncTask<Void, Void, Void> {

    private Context context;
    private Exception thrownException;

    public RefreshTokenTask(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        while (true) {
            try {
                Thread.sleep(540000);
                SharedPreferences sharedPreferences =
                        context.getSharedPreferences("shared_preference",Context.MODE_PRIVATE);
                String username = sharedPreferences.getString("user_username", "defaultUsername");
                String password = sharedPreferences.getString("user_password", "defaultPassword");
                InternetConnectionStatus internetConnectionStatus = new InternetConnectionStatus();
                if (internetConnectionStatus.isDeviceConnectedToInternet(context)) {

                    UserDaoFactory userDaoFactory = UserDaoFactory.getInstance();
                    UserDao userDao = userDaoFactory.createUserDao();
                    String token = userDao.login(username, password);
                    // store the new token in the shared preferences file
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("token", token);
                    editor.apply();
                }
            } catch (Exception e) {
                thrownException = new UnexpectedServerResponseException(e.getMessage(), e.getCause());
            }
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (thrownException != null) {
            ErrorHandler errorHandler = new ErrorHandler(context);
            errorHandler.show(thrownException);
        }
        // Unreachable
    }
}

