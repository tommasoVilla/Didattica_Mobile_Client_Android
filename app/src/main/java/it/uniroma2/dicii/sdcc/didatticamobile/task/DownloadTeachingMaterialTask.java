package it.uniroma2.dicii.sdcc.didatticamobile.task;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import it.uniroma2.dicii.sdcc.didatticamobile.R;
import it.uniroma2.dicii.sdcc.didatticamobile.activity.InternetConnectionStatus;
import it.uniroma2.dicii.sdcc.didatticamobile.dao.TeachingMaterialDao;
import it.uniroma2.dicii.sdcc.didatticamobile.dao.TeachingMaterialDaoFactory;
import it.uniroma2.dicii.sdcc.didatticamobile.error.ErrorHandler;
import it.uniroma2.dicii.sdcc.didatticamobile.error.NoInternetConnectionException;

public class DownloadTeachingMaterialTask extends AsyncTask<String, Void, Void> {


    private Activity activity;
    /*
     * This field is populated with the exception thrown inside doInBackground. If an exception occurs
     * this field is not null and inside onPostExecute is called the ErrorHandler object to show the
     * error to the user.
     * */
    private Exception thrownException;

    private String downloadLink;

    public DownloadTeachingMaterialTask (Activity activity){ this.activity = activity; }

    /* During the execution a progress bar is shown and the other widgets are hidden.*/
    @Override
    protected void onPreExecute() {
        activity.findViewById(R.id.pbCourseTeachingMaterial).setVisibility(View.VISIBLE);
        activity.findViewById(R.id.tvTeachingMaterialForCourse).setVisibility(View.GONE);
        activity.findViewById(R.id.tvTeachingMaterialNameCourse).setVisibility(View.GONE);
        activity.findViewById(R.id.lvTeachingMaterial).setVisibility(View.GONE);
        activity.findViewById(R.id.tvNoMaterialToShowForCourse).setVisibility(View.GONE);
    }

    @Override
    protected Void doInBackground(String... params) {
        try {
            /* If the device is not connected to Internet an error is raised*/
            InternetConnectionStatus internetConnectionStatus = new InternetConnectionStatus();
            if (!internetConnectionStatus.isDeviceConnectedToInternet(activity)) {
                throw new NoInternetConnectionException();
            }
            SharedPreferences sharedPreferences = activity.getSharedPreferences("shared_preference", Context.MODE_PRIVATE);
            String username = sharedPreferences.getString("user_username","defaultUsername");
            String token = sharedPreferences.getString("token","defaultToken");

            // Asking to a repository class to obtain the link for temporally download
            TeachingMaterialDaoFactory teachingMaterialDaoFactory = TeachingMaterialDaoFactory.getInstance();
            TeachingMaterialDao teachingMaterialDao = teachingMaterialDaoFactory.createTeachingMaterialDao();
            downloadLink = teachingMaterialDao.getDownloadLink(username, params[0], params[1], token);
            return null;

        } catch (Exception e) {
            thrownException = e;
            return null;
        }
    }

    /* After the execution, the visibility of the widgets is restored.*/
    @Override
    protected void onPostExecute(Void aVoid) {

        activity.findViewById(R.id.pbCourseTeachingMaterial).setVisibility(View.GONE);
        activity.findViewById(R.id.tvTeachingMaterialForCourse).setVisibility(View.VISIBLE);
        activity.findViewById(R.id.tvTeachingMaterialNameCourse).setVisibility(View.VISIBLE);
        activity.findViewById(R.id.lvTeachingMaterial).setVisibility(View.VISIBLE);
        activity.findViewById(R.id.tvNoMaterialToShowForCourse).setVisibility(View.GONE);
        if (thrownException != null) {
            ErrorHandler errorHandler = new ErrorHandler(activity);
            errorHandler.show(thrownException);
        } else {
            // If no exception occurred the requested file is open through obtained link using default browser
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(downloadLink));
            PackageManager packageManager = activity.getPackageManager();
            if (browserIntent.resolveActivity(packageManager) != null) {
                activity.startActivity(browserIntent);
            } else {
                Toast.makeText(activity, R.string.notVisualizableElementByBrowser_message, Toast.LENGTH_SHORT).show();
            }
        }
    }
}



