package it.uniroma2.dicii.sdcc.didatticamobile.task;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import it.uniroma2.dicii.sdcc.didatticamobile.R;
import it.uniroma2.dicii.sdcc.didatticamobile.activity.InternetConnectionStatus;
import it.uniroma2.dicii.sdcc.didatticamobile.activity.RegistrationCompletedActivity;
import it.uniroma2.dicii.sdcc.didatticamobile.dao.UserDao;
import it.uniroma2.dicii.sdcc.didatticamobile.dao.UserDaoFactory;
import it.uniroma2.dicii.sdcc.didatticamobile.error.ErrorHandler;
import it.uniroma2.dicii.sdcc.didatticamobile.error.NoInternetConnectionException;
import it.uniroma2.dicii.sdcc.didatticamobile.model.User;

/* A RegistrationTask is a task responsible to make the registration request to the server.*/
public class RegistrationTask  extends AsyncTask<String, Void, Void> {

    private Activity registrationActivity;

    /*
     * This field is populated with the exception thrown inside doInBackground. If an exception occurs
     * this field is not null and inside onPostExecute is called the ErrorHandler object to show the
     * error to the user.
     * */
    private Exception thrownException;

    public RegistrationTask(Activity activity){
        this.registrationActivity = activity;
    }

    /* During the execution a progress bar is shown and the other widgets are hidden.*/
    @Override
    protected void onPreExecute() {
        registrationActivity.findViewById(R.id.tvInsertDataRegistration).setVisibility(View.GONE);
        registrationActivity.findViewById(R.id.etNameRegistration).setVisibility(View.GONE);
        registrationActivity.findViewById(R.id.etSurnameRegistration).setVisibility(View.GONE);
        registrationActivity.findViewById(R.id.etFiscalCodeRegistration).setVisibility(View.GONE);
        registrationActivity.findViewById(R.id.etUsernameRegistration).setVisibility(View.GONE);
        registrationActivity.findViewById(R.id.etPasswordRegistration).setVisibility(View.GONE);
        registrationActivity.findViewById(R.id.btnRegistrationRegistration).setVisibility(View.GONE);
        registrationActivity.findViewById(R.id.pbLoadingRegistration).setVisibility(View.VISIBLE);

    }

    @Override
    protected Void doInBackground(String... strings) {
        try {
            /* If the device is not connected to Internet an error is raised*/
            InternetConnectionStatus internetConnectionStatus = new InternetConnectionStatus();
            if (!internetConnectionStatus.isDeviceConnectedToInternet(registrationActivity)) {
                throw new NoInternetConnectionException();
            }
            /* The interaction with the persistence layer is delegated to an UserDao object*/
            UserDaoFactory userDaoFactory = UserDaoFactory.getInstance();
            UserDao userDao = userDaoFactory.createUserDao();
            User user = new User(strings[0], strings[1], strings[2], strings[3]);
            userDao.register(user, strings[4]);
            return null;
        } catch (Exception e) {
            thrownException = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(Void v) {
        if (thrownException != null) {
            ErrorHandler errorHandler = new ErrorHandler(registrationActivity);
            errorHandler.show(thrownException);

            /* After the execution, the visibility of the widgets is restored.*/
            registrationActivity.findViewById(R.id.tvInsertDataRegistration).setVisibility(View.VISIBLE);
            registrationActivity.findViewById(R.id.etNameRegistration).setVisibility(View.VISIBLE);
            registrationActivity.findViewById(R.id.etSurnameRegistration).setVisibility(View.VISIBLE);
            registrationActivity.findViewById(R.id.etFiscalCodeRegistration).setVisibility(View.VISIBLE);
            registrationActivity.findViewById(R.id.etUsernameRegistration).setVisibility(View.VISIBLE);
            registrationActivity.findViewById(R.id.etPasswordRegistration).setVisibility(View.VISIBLE);
            registrationActivity.findViewById(R.id.btnRegistrationRegistration).setVisibility(View.VISIBLE);
            registrationActivity.findViewById(R.id.pbLoadingRegistration).setVisibility(View.GONE);
        } else {
            // If the registration succeeds an activity for confirm is shown to the user.
            Intent intent = new Intent(registrationActivity, RegistrationCompletedActivity.class);
            registrationActivity.startActivity(intent);
        }

    }
}
