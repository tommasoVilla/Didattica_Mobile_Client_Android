package it.uniroma2.dicii.sdcc.didatticamobile.task;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import it.uniroma2.dicii.sdcc.didatticamobile.R;
import it.uniroma2.dicii.sdcc.didatticamobile.activity.CoursesActivity;
import it.uniroma2.dicii.sdcc.didatticamobile.dao.CourseDao;
import it.uniroma2.dicii.sdcc.didatticamobile.dao.CourseDaoFactory;
import it.uniroma2.dicii.sdcc.didatticamobile.error.ErrorHandler;

/* This task is responsible for requiring to the backend the publishing of a notification concerning
* a course held by a teacher*/
public class SendCourseNotificationTask extends AsyncTask<String, Void, Void> {

    private Activity activity;
    private Exception thrownException; // this is populated if the execution of a task raise an exception

    public SendCourseNotificationTask(Activity activity) {
        this.activity = activity;
    }

    // During the interaction with the backend a progress bar is shown
    @Override
    protected void onPreExecute() {
        activity.findViewById(R.id.pbCourseNotification).setVisibility(View.VISIBLE);
        activity.findViewById(R.id.tvCourseNotificationHeader).setVisibility(View.GONE);
        activity.findViewById(R.id.tvCourseNotificationHeader).setVisibility(View.GONE);
        activity.findViewById(R.id.tvCourseNotificationCourseName).setVisibility(View.GONE);
        activity.findViewById(R.id.svCourseNotificationMessage).setVisibility(View.GONE);
        activity.findViewById(R.id.btnCourseNotificationSend).setVisibility(View.GONE);
    }

    @Override
    protected Void doInBackground(String... strings) {
        try {
            String courseId = strings[0];
            String notificationMessage = strings[1];
            // The token, needed for authenticating the request, is read from the shared preferences of the application
            SharedPreferences sharedPreferences = activity.getSharedPreferences("shared_preference", Context.MODE_PRIVATE);
            String token = sharedPreferences.getString("token", "defaultToken");
            CourseDaoFactory courseDaoFactory = CourseDaoFactory.getInstance();
            CourseDao courseDao = courseDaoFactory.createCourseDao();
            courseDao.sendCourseNotification(courseId, notificationMessage, token);
            return null;
        } catch (Exception e) {
            thrownException = e;
            return null;
        }
    }

    // On success, the control returns to the Courses Activity
    @Override
    protected void onPostExecute(Void aVoid) {
        if (thrownException != null) {
            ErrorHandler errorHandler = new ErrorHandler(activity);
            errorHandler.show(thrownException);

            activity.findViewById(R.id.pbCourseNotification).setVisibility(View.GONE);
            activity.findViewById(R.id.tvCourseNotificationHeader).setVisibility(View.VISIBLE);
            activity.findViewById(R.id.tvCourseNotificationHeader).setVisibility(View.VISIBLE);
            activity.findViewById(R.id.tvCourseNotificationCourseName).setVisibility(View.VISIBLE);
            activity.findViewById(R.id.svCourseNotificationMessage).setVisibility(View.VISIBLE);
            activity.findViewById(R.id.btnCourseNotificationSend).setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(activity, R.string.courseNotificationConfirm_message, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(activity, CoursesActivity.class);
            activity.startActivity(intent);
        }
    }
}
