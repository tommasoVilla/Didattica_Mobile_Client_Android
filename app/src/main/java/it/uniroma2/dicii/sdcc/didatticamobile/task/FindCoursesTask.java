package it.uniroma2.dicii.sdcc.didatticamobile.task;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import it.uniroma2.dicii.sdcc.didatticamobile.R;
import it.uniroma2.dicii.sdcc.didatticamobile.activity.InternetConnectionStatus;
import it.uniroma2.dicii.sdcc.didatticamobile.adapter.CoursesListAdapter;
import it.uniroma2.dicii.sdcc.didatticamobile.dao.CourseDao;
import it.uniroma2.dicii.sdcc.didatticamobile.dao.CourseDaoFactory;
import it.uniroma2.dicii.sdcc.didatticamobile.error.ErrorHandler;
import it.uniroma2.dicii.sdcc.didatticamobile.error.NoInternetConnectionException;
import it.uniroma2.dicii.sdcc.didatticamobile.model.Course;

// FindCoursesTask is responsible for finding courses by name or teacher
public class FindCoursesTask extends AsyncTask<String, Void, Void> {

    private Activity findCoursesActivity;
    private Exception thrownException;
    private List<Course> matchingCourses;

    public FindCoursesTask(Activity activity) {
        this.findCoursesActivity = activity;
    }

    // During courses retrieval a progress bar is shown
    @Override
    protected void onPreExecute() {
        ListView lv = findCoursesActivity.findViewById(R.id.lvMatchingCourses);
        findCoursesActivity.findViewById(R.id.lvMatchingCourses).setVisibility(View.GONE);
        findCoursesActivity.findViewById(R.id.pbLoadingFindCourses).setVisibility(View.VISIBLE);
    }

    @Override
    protected Void doInBackground(String... params) {
        try {
            /* If the device is not connected to Internet an error is raised*/
            InternetConnectionStatus internetConnectionStatus = new InternetConnectionStatus();
            if (!internetConnectionStatus.isDeviceConnectedToInternet(findCoursesActivity)) {
                throw new NoInternetConnectionException();
            }

            CourseDaoFactory courseDaoFactory = CourseDaoFactory.getInstance();
            CourseDao courseDao = courseDaoFactory.createCourseDao();
            /* The request is embedded with an access token read from shared preferences*/
            SharedPreferences sharedPreferences = findCoursesActivity.getSharedPreferences("shared_preference", Context.MODE_PRIVATE);
            String token = sharedPreferences.getString("token","defaultToken");
            matchingCourses = courseDao.findBy(params[0], params[1], token);
            return null;

        } catch (Exception e) {
            thrownException = e;
            return null;
        }
    }

    // After the execution the progress bar disappears and the list of courses is shown
    @Override
    protected void onPostExecute(Void aVoid) {
        findCoursesActivity.findViewById(R.id.pbLoadingFindCourses).setVisibility(View.GONE);
        if (thrownException != null) {
            ErrorHandler errorHandler = new ErrorHandler(findCoursesActivity);
            errorHandler.show(thrownException);
        } else {
            if (matchingCourses.isEmpty()) {
                Toast.makeText(findCoursesActivity, R.string.noMatchingCoursesEception_message, Toast.LENGTH_SHORT).show();
            } else {
                findCoursesActivity.findViewById(R.id.lvMatchingCourses).setVisibility(View.VISIBLE);
                ListView lvMatchingCourses = findCoursesActivity.findViewById(R.id.lvMatchingCourses);
                CoursesListAdapter coursesListAdapter = new CoursesListAdapter(findCoursesActivity, R.layout.course_list_item, matchingCourses);
                lvMatchingCourses.setAdapter(coursesListAdapter);
            }
        }
    }
}
