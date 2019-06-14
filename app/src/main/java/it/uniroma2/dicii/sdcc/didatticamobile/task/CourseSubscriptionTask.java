package it.uniroma2.dicii.sdcc.didatticamobile.task;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import it.uniroma2.dicii.sdcc.didatticamobile.R;
import it.uniroma2.dicii.sdcc.didatticamobile.dao.CourseDao;
import it.uniroma2.dicii.sdcc.didatticamobile.dao.CourseDaoFactory;
import it.uniroma2.dicii.sdcc.didatticamobile.error.ErrorHandler;
import it.uniroma2.dicii.sdcc.didatticamobile.error.InvalidConfigurationException;

/* A CourseSubscriptionTask object is responsible for subscribe a student to a course or unsubscribe
* a student from a course*/
public class CourseSubscriptionTask extends AsyncTask<String, Void, Void> {

    private boolean subscribe; // true if the task has to subscribe the student, false otherwise
    private Activity courseDetailActivity;
    private Exception thrownException;

    public CourseSubscriptionTask(boolean subscribe, Activity courseDetailActivity) {
        this.subscribe = subscribe;
        this.courseDetailActivity = courseDetailActivity;
    }

    /* During the execution a progress bar is shown and the other widgets are hidden.*/
    @Override
    protected void onPreExecute() {
        courseDetailActivity.findViewById(R.id.pbLoadingCourseDetail).setVisibility(View.VISIBLE);
        courseDetailActivity.findViewById(R.id.tvCourseDetailName).setVisibility(View.GONE);
        courseDetailActivity.findViewById(R.id.tvCourseDetailDepartmentLabel).setVisibility(View.GONE);
        courseDetailActivity.findViewById(R.id.tvCourseDetailDepartment).setVisibility(View.GONE);
        courseDetailActivity.findViewById(R.id.tvCourseDetailYearLabel).setVisibility(View.GONE);
        courseDetailActivity.findViewById(R.id.tvCourseDetailYear).setVisibility(View.GONE);
        courseDetailActivity.findViewById(R.id.tvCourseDetailTeacherLabel).setVisibility(View.GONE);
        courseDetailActivity.findViewById(R.id.tvCourseDetailTeacher).setVisibility(View.GONE);
        courseDetailActivity.findViewById(R.id.tvCourseDetailSemesterLabel).setVisibility(View.GONE);
        courseDetailActivity.findViewById(R.id.tvCourseDetailSemester).setVisibility(View.GONE);
        courseDetailActivity.findViewById(R.id.tvCourseDetailDescriptionLabel).setVisibility(View.GONE);
        courseDetailActivity.findViewById(R.id.tvCourseDetailDescription).setVisibility(View.GONE);
        courseDetailActivity.findViewById(R.id.tvCourseDetailScheduleLabel).setVisibility(View.GONE);
        courseDetailActivity.findViewById(R.id.tvCourseDetailSchedule1).setVisibility(View.GONE);
        courseDetailActivity.findViewById(R.id.tvCourseDetailSchedule2).setVisibility(View.GONE);
        courseDetailActivity.findViewById(R.id.tvCourseDetailSchedule3).setVisibility(View.GONE);
        courseDetailActivity.findViewById(R.id.btnCourseDetailActionOnCourse).setVisibility(View.GONE);
        courseDetailActivity.findViewById(R.id.btnCourseDetailExams).setVisibility(View.GONE);
    }

    @Override
    protected Void doInBackground(String... strings) {
        try {
            String courseId = strings[0];
            String courseName = strings[1];
            String courseDepartment = strings[2];
            String courseYear = strings[3];
            SharedPreferences sharedPreferences = courseDetailActivity.getSharedPreferences("shared_preference", Context.MODE_PRIVATE);
            String studentUsername = sharedPreferences.getString("user_username", "defaultUsername");
            String token = sharedPreferences.getString("token", "defaultToken"); // for authenticating the request
            // The course with courseId identifier is added to the logged student, with username studentUsername
            CourseDaoFactory courseDaoFactory = CourseDaoFactory.getInstance();
            CourseDao courseDao = courseDaoFactory.createCourseDao();
            if (subscribe) {
                courseDao.addCourseToStudent(courseId, courseName, courseDepartment,
                        courseYear, studentUsername, token);
            } else {
                courseDao.removeStudentFromCourse(courseId, courseName, courseDepartment,
                        courseYear, studentUsername, token);
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
        courseDetailActivity.findViewById(R.id.pbLoadingCourseDetail).setVisibility(View.GONE);
        courseDetailActivity.findViewById(R.id.tvCourseDetailName).setVisibility(View.VISIBLE);
        courseDetailActivity.findViewById(R.id.tvCourseDetailDepartmentLabel).setVisibility(View.VISIBLE);
        courseDetailActivity.findViewById(R.id.tvCourseDetailDepartment).setVisibility(View.VISIBLE);
        courseDetailActivity.findViewById(R.id.tvCourseDetailYearLabel).setVisibility(View.VISIBLE);
        courseDetailActivity.findViewById(R.id.tvCourseDetailYear).setVisibility(View.VISIBLE);
        courseDetailActivity.findViewById(R.id.tvCourseDetailTeacherLabel).setVisibility(View.VISIBLE);
        courseDetailActivity.findViewById(R.id.tvCourseDetailTeacher).setVisibility(View.VISIBLE);
        courseDetailActivity.findViewById(R.id.tvCourseDetailSemesterLabel).setVisibility(View.VISIBLE);
        courseDetailActivity.findViewById(R.id.tvCourseDetailSemester).setVisibility(View.VISIBLE);
        courseDetailActivity.findViewById(R.id.tvCourseDetailDescriptionLabel).setVisibility(View.VISIBLE);
        courseDetailActivity.findViewById(R.id.tvCourseDetailDescription).setVisibility(View.VISIBLE);
        courseDetailActivity.findViewById(R.id.tvCourseDetailScheduleLabel).setVisibility(View.VISIBLE);
        courseDetailActivity.findViewById(R.id.tvCourseDetailSchedule1).setVisibility(View.VISIBLE);
        courseDetailActivity.findViewById(R.id.tvCourseDetailSchedule2).setVisibility(View.VISIBLE);
        courseDetailActivity.findViewById(R.id.tvCourseDetailSchedule3).setVisibility(View.VISIBLE);
        courseDetailActivity.findViewById(R.id.btnCourseDetailActionOnCourse).setVisibility(View.VISIBLE);
        courseDetailActivity.findViewById(R.id.btnCourseDetailExams).setVisibility(View.VISIBLE);

        if (thrownException != null) {
            ErrorHandler errorHandler = new ErrorHandler(courseDetailActivity);
            errorHandler.show(thrownException);
        } else {
            if (subscribe) {
                Toast.makeText(courseDetailActivity, R.string.courseDetailSubscribtionCompleted_text, Toast.LENGTH_SHORT).show();
                ((Button)courseDetailActivity.findViewById(R.id.btnCourseDetailActionOnCourse)).setText(R.string.btnCourseDetailUnsubscribe_text);
            } else {
                Toast.makeText(courseDetailActivity, R.string.courseDetailUnsubscribtionCompleted_text, Toast.LENGTH_SHORT).show();
                ((Button)courseDetailActivity.findViewById(R.id.btnCourseDetailActionOnCourse)).setText(R.string.btnCourseDetailSubscribe_text);
            }
        }
    }
}
