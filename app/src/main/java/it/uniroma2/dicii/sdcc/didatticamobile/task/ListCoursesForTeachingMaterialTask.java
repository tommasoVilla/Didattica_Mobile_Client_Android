package it.uniroma2.dicii.sdcc.didatticamobile.task;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;

import java.util.List;

import it.uniroma2.dicii.sdcc.didatticamobile.R;
import it.uniroma2.dicii.sdcc.didatticamobile.activity.utility.InternetConnectionStatus;
import it.uniroma2.dicii.sdcc.didatticamobile.adapter.CoursesListAdapter;
import it.uniroma2.dicii.sdcc.didatticamobile.dao.CourseDao;
import it.uniroma2.dicii.sdcc.didatticamobile.dao.CourseDaoFactory;
import it.uniroma2.dicii.sdcc.didatticamobile.error.ErrorHandler;
import it.uniroma2.dicii.sdcc.didatticamobile.error.NoInternetConnectionException;
import it.uniroma2.dicii.sdcc.didatticamobile.model.Course;

/* A ListCoursesForTeachingMaterialTask is responsible for showing on a ListView the courses
 * attended by a student or hold by a teacher (It depends on which type of user is currently logged)
 * in order to select the teaching material to visualize */
public class ListCoursesForTeachingMaterialTask extends AsyncTask<Void, Void, Void> {

    private Activity activity;
    private List<Course> courses;
    /*
     * This field is populated with the exception thrown inside doInBackground. If an exception occurs
     * this field is not null and inside onPostExecute is called the ErrorHandler object to show the
     * error to the user.
     * */
    private Exception thrownException;


    public ListCoursesForTeachingMaterialTask(Activity activity) {
        this.activity = activity;
    }

    /* During the execution a progress bar is shown and the other widgets are hidden.*/
    @Override
    protected void onPreExecute() {
        activity.findViewById(R.id.pbTeachingMaterial).setVisibility(View.VISIBLE);
        activity.findViewById(R.id.tvTeachingMaterialTitle).setVisibility(View.GONE);
        activity.findViewById(R.id.lvCoursesTeachingMaterial).setVisibility(View.GONE);
        activity.findViewById(R.id.tvNoMaterialToShow).setVisibility(View.GONE);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            /* If the device is not connected to Internet an error is raised*/
            InternetConnectionStatus internetConnectionStatus = new InternetConnectionStatus();
            if (!internetConnectionStatus.isDeviceConnectedToInternet(activity)) {
                throw new NoInternetConnectionException();
            }
            SharedPreferences sharedPreferences = activity.getSharedPreferences("shared_preference", Context.MODE_PRIVATE);
            String userType = sharedPreferences.getString("user_type","defaultType");
            String token = sharedPreferences.getString("token","defaultToken");
            CourseDaoFactory courseDaoFactory = CourseDaoFactory.getInstance();
            CourseDao courseDao = courseDaoFactory.createCourseDao();

            // If the logged user is a teacher, the app retrieves the courses hold by him/her and shows them
            if (userType.equals("teacher")){
                String teacherName = sharedPreferences.getString("user_name","defaultName") +
                        "-" +  sharedPreferences.getString("user_surname","defaultSurname");
                courses = courseDao.findBy("teacher", teacherName, token);

            // If the logged user is a student, the app retrieves the courses attended by him/her and shows them
            } else {
                String studentUsername = sharedPreferences.getString("user_username", "defaultUsername");
                courses = courseDao.findBySubscribedStudent(studentUsername, token);
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
        activity.findViewById(R.id.pbTeachingMaterial).setVisibility(View.GONE);
        activity.findViewById(R.id.tvTeachingMaterialTitle).setVisibility(View.VISIBLE);
        activity.findViewById(R.id.lvCoursesTeachingMaterial).setVisibility(View.VISIBLE);
        if (thrownException != null) {
            ErrorHandler errorHandler = new ErrorHandler(activity);
            errorHandler.show(thrownException);
        } else {
            // If no courses have been found, an informative text view is shown
            if (courses.isEmpty()) {
                activity.findViewById(R.id.tvNoMaterialToShow).setVisibility(View.VISIBLE);
                // Otherwise, the list of courses is shown
            } else {
                ListView lvCourses = activity.findViewById(R.id.lvCoursesTeachingMaterial);
                CoursesListAdapter coursesListAdapter = new CoursesListAdapter(activity, R.layout.course_list_item, courses);
                lvCourses.setAdapter(coursesListAdapter);
                activity.findViewById(R.id.lvCoursesTeachingMaterial).setVisibility(View.VISIBLE);
            }
        }
    }
}
