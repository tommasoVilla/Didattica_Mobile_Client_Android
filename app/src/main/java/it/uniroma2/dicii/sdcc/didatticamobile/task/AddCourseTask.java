package it.uniroma2.dicii.sdcc.didatticamobile.task;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import it.uniroma2.dicii.sdcc.didatticamobile.R;
import it.uniroma2.dicii.sdcc.didatticamobile.activity.CoursesActivity;
import it.uniroma2.dicii.sdcc.didatticamobile.activity.InternetConnectionStatus;
import it.uniroma2.dicii.sdcc.didatticamobile.dao.CourseDao;
import it.uniroma2.dicii.sdcc.didatticamobile.dao.CourseDaoFactory;
import it.uniroma2.dicii.sdcc.didatticamobile.error.ErrorHandler;
import it.uniroma2.dicii.sdcc.didatticamobile.error.NoInternetConnectionException;
import it.uniroma2.dicii.sdcc.didatticamobile.model.Course;

/* An AddCourseTask is a task responsible to make the insert request to the server for a course.*/
public class AddCourseTask extends AsyncTask<Bundle, Void, Void> {

    private Activity context;

    /*
     * This field is populated with the exception thrown inside doInBackground. If an exception occurs
     * this field is not null and inside onPostExecute is called the ErrorHandler object to show the
     * error to the user.
     * */
    private Exception thrownException;

    public AddCourseTask(Activity activity){
        this.context = activity;
    }

    /* During the execution a progress bar is shown and the other widgets are hidden.*/
    @Override
    protected void onPreExecute() {
        context.findViewById(R.id.tvInsertCourseDescription).setVisibility(View.GONE);
        context.findViewById(R.id.svDescription).setVisibility(View.GONE);
        context.findViewById(R.id.btnConfirmAddCourse).setVisibility(View.GONE);
        context.findViewById(R.id.pbAddCourse).setVisibility(View.VISIBLE);

    }

    @Override
    protected Void doInBackground(Bundle ... bundle) {
        try {
            /* If the device is not connected to Internet an error is raised*/
            InternetConnectionStatus internetConnectionStatus = new InternetConnectionStatus();
            if (!internetConnectionStatus.isDeviceConnectedToInternet(context)) {
                throw new NoInternetConnectionException();
            }

            /* The interaction with the persistence layer is delegated to an CourseDao object*/
            CourseDaoFactory courseDaoFactory = CourseDaoFactory.getInstance();
            CourseDao courseDao = courseDaoFactory.createCourseDao();
            Course course = new Course(bundle[0]);

            /* The request is embedded with an access token read from shared preferences*/
            SharedPreferences sharedPreferences = context.getSharedPreferences("shared_preference", Context.MODE_PRIVATE);
            String token = sharedPreferences.getString("token","defaultToken");
            courseDao.add(course, token);
            return null;

        } catch (Exception e) {
            thrownException = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(Void v) {
        if (thrownException != null) {
            ErrorHandler errorHandler = new ErrorHandler(context);
            errorHandler.show(thrownException);

            /* After the execution, the visibility of the widgets is restored.*/
            context.findViewById(R.id.tvInsertCourseDescription).setVisibility(View.VISIBLE);
            context.findViewById(R.id.svDescription).setVisibility(View.VISIBLE);
            context.findViewById(R.id.btnConfirmAddCourse).setVisibility(View.VISIBLE);
            context.findViewById(R.id.pbAddCourse).setVisibility(View.GONE);
        } else {
            Toast.makeText(context, R.string.courseCreationConfirm_message, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(context, CoursesActivity.class);
            context.startActivity(intent);
        }

    }
}
