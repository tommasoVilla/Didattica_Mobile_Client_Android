package it.uniroma2.dicii.sdcc.didatticamobile.task;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import it.uniroma2.dicii.sdcc.didatticamobile.R;
import it.uniroma2.dicii.sdcc.didatticamobile.activity.InternetConnectionStatus;
import it.uniroma2.dicii.sdcc.didatticamobile.dao.TeachingMaterialDao;
import it.uniroma2.dicii.sdcc.didatticamobile.dao.TeachingMaterialDaoFactory;
import it.uniroma2.dicii.sdcc.didatticamobile.error.ErrorHandler;
import it.uniroma2.dicii.sdcc.didatticamobile.error.NoInternetConnectionException;
import it.uniroma2.dicii.sdcc.didatticamobile.error.TeachingMaterialDaoException;


public class ListTeachingMaterialForCourseTask extends AsyncTask<String, Void, Void> {

    private Activity activity;
    private List<String> fileNames = new ArrayList<>();
    /*
     * This field is populated with the exception thrown inside doInBackground. If an exception occurs
     * this field is not null and inside onPostExecute is called the ErrorHandler object to show the
     * error to the user.
     * */
    private Exception thrownException;


    public ListTeachingMaterialForCourseTask(Activity activity) {
        this.activity = activity;
    }

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
            String userType = sharedPreferences.getString("user_type","defaultType");
            String token = sharedPreferences.getString("token","defaultToken");

            TeachingMaterialDaoFactory teachingMaterialDaoFactory = TeachingMaterialDaoFactory.getInstance();
            TeachingMaterialDao teachingMaterialDao = teachingMaterialDaoFactory.createTeachingMaterialDao();
            ArrayList<String> fullFileNames = teachingMaterialDao.findTeachingMaterialByCourse(params[0], token);
            for (String fileName : fullFileNames){
                fileNames.add(fileName.split("_")[1]);
            }
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
        if (thrownException != null) {
            ErrorHandler errorHandler = new ErrorHandler(activity);
            errorHandler.show(thrownException);
        } else {
            // If are present no file for selected course, an informative text view is shown
            if (fileNames.isEmpty()) {
                activity.findViewById(R.id.tvNoMaterialToShowForCourse).setVisibility(View.VISIBLE);
                // Otherwise, the list of files is shown
            } else {
                ListView lvFiles = activity.findViewById(R.id.lvTeachingMaterial);
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(activity,
                       R.layout.spinner_item, fileNames);
                lvFiles.setAdapter(arrayAdapter);
                activity.findViewById(R.id.lvTeachingMaterial).setVisibility(View.VISIBLE);
            }
        }
    }
}
