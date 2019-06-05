package it.uniroma2.dicii.sdcc.didatticamobile.task;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import it.uniroma2.dicii.sdcc.didatticamobile.R;
import it.uniroma2.dicii.sdcc.didatticamobile.config.AppConfiguration;
import it.uniroma2.dicii.sdcc.didatticamobile.error.ErrorHandler;
import it.uniroma2.dicii.sdcc.didatticamobile.error.InitializationException;
import it.uniroma2.dicii.sdcc.didatticamobile.error.InvalidConfigurationException;

/*
 * ConfigurationTask is a task responsible to read the configuration parameters and store them into
 * the AppConfiguration singleton object.
 * */
public class ConfigurationTask extends AsyncTask<Void, Void, Void> {

    private Activity mainActivity;

    /*
     * This field is populated with the exception thrown inside doInBackground. If an exception occurs
     * this field is not null and inside onPostExecute is called the ErrorHandler object to show the
     * error to the user.
     * */
    private Exception thrownException;

    public ConfigurationTask(Activity mainActivity) {
        this.mainActivity = mainActivity;
    }

    /* During the execution a progress bar is shown and the other widgets are hidden.*/
    @Override
    protected void onPreExecute() {
        mainActivity.findViewById(R.id.tvWelcomeMain).setVisibility(View.GONE);
        mainActivity.findViewById(R.id.tvDoLoginMain).setVisibility(View.GONE);
        mainActivity.findViewById(R.id.etUsernameMain).setVisibility(View.GONE);
        mainActivity.findViewById(R.id.etPasswordMain).setVisibility(View.GONE);
        mainActivity.findViewById(R.id.btnLoginMain).setVisibility(View.GONE);
        mainActivity.findViewById(R.id.tvDoRegistrationMain).setVisibility(View.GONE);
        mainActivity.findViewById(R.id.btnRegistrationMain).setVisibility(View.GONE);
        mainActivity.findViewById(R.id.pbLoadingMain).setVisibility(View.VISIBLE);
    }

    /* After the execution, the visibility of the widgets is restored.*/
    @Override
    protected void onPostExecute(Void aVoid) {

        if (thrownException != null ) {
            ErrorHandler errorHandler = new ErrorHandler(mainActivity);
            errorHandler.show(thrownException);
        }
        mainActivity.findViewById(R.id.tvWelcomeMain).setVisibility(View.VISIBLE);
        mainActivity.findViewById(R.id.tvDoLoginMain).setVisibility(View.VISIBLE);
        mainActivity.findViewById(R.id.etUsernameMain).setVisibility(View.VISIBLE);
        mainActivity.findViewById(R.id.etPasswordMain).setVisibility(View.VISIBLE);
        mainActivity.findViewById(R.id.btnLoginMain).setVisibility(View.VISIBLE);
        mainActivity.findViewById(R.id.tvDoRegistrationMain).setVisibility(View.VISIBLE);
        mainActivity.findViewById(R.id.btnRegistrationMain).setVisibility(View.VISIBLE);
        mainActivity.findViewById(R.id.pbLoadingMain).setVisibility(View.GONE);

    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            AppConfiguration.getInstance();
        } catch (Exception e) {
            try {
                throw new InitializationException(e.getMessage(), e.getCause());
            } catch (InitializationException e1) {
                thrownException = e1;
            }
        }
        return null;
    }
}
