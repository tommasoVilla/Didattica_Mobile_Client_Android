package it.uniroma2.dicii.sdcc.didatticamobile.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import it.uniroma2.dicii.sdcc.didatticamobile.R;
import it.uniroma2.dicii.sdcc.didatticamobile.parcelable.CourseParcelable;
import it.uniroma2.dicii.sdcc.didatticamobile.task.CourseSubscriptionTask;

public class CourseDetailActivity extends AppCompatActivity {

    private Activity activity;
    private CourseParcelable courseParcelable; // showed course

    private class EventListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnCourseDetailActionOnCourse:
                    // Launch the task to subscribe or unsubscribe from the showed course
                    String btnFunctionality = ((Button)v).getText().toString();
                    CourseSubscriptionTask courseSubscriptionTask;
                    if (btnFunctionality.equals("Annulla Iscrizione")) {
                        courseSubscriptionTask = new CourseSubscriptionTask(false, activity);
                    } else {
                        courseSubscriptionTask = new CourseSubscriptionTask(true, activity);
                    }
                    courseSubscriptionTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, courseParcelable.getId());
                    break;
                case R.id.btnCourseDetailExams:
                    viewExams();
                    break;
                default:
                    break;
            }
        }

        private void viewExams() {
            Intent intent = new Intent(activity, ExamsActivity.class);
            CourseParcelable courseParcelable = getIntent().getParcelableExtra("course_to_show");
            intent.putExtra("course_id", courseParcelable.getId());
            intent.putExtra("course_name", courseParcelable.getName());
            activity.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        activity = this;
        initializeWidget();
    }

    private void initializeWidget() {

        TextView tvCourseDetailName = findViewById(R.id.tvCourseDetailName);
        TextView tvCourseDetailDepartment = findViewById(R.id.tvCourseDetailDepartment);
        TextView tvCourseDetailTeacher = findViewById(R.id.tvCourseDetailTeacher);
        TextView tvCourseDetailYear = findViewById(R.id.tvCourseDetailYear);
        TextView tvCourseDetailSemester = findViewById(R.id.tvCourseDetailSemester);
        TextView tvCourseDetailDescription = findViewById(R.id.tvCourseDetailDescription);
        TextView tvCourseDetailSchedule1 = findViewById(R.id.tvCourseDetailSchedule1);
        TextView tvCourseDetailSchedule2 = findViewById(R.id.tvCourseDetailSchedule2);
        TextView tvCourseDetailSchedule3 = findViewById(R.id.tvCourseDetailSchedule3);

        // The course clicked in the previous activity is used to populate the widgets
        courseParcelable = getIntent().getParcelableExtra("course_to_show");

        tvCourseDetailName.setText(courseParcelable.getName());
        tvCourseDetailDepartment.setText(courseParcelable.getDepartment());
        tvCourseDetailTeacher.setText(courseParcelable.getTeacher());
        tvCourseDetailYear.setText(courseParcelable.getYear());
        tvCourseDetailSemester.setText(String.valueOf(courseParcelable.getSemester()));
        tvCourseDetailDescription.setText(courseParcelable.getDescription());
        tvCourseDetailSchedule1.setText(courseParcelable.getSchedule1());
        tvCourseDetailSchedule2.setText(courseParcelable.getSchedule2());
        tvCourseDetailSchedule3.setText(courseParcelable.getSchedule3());

        SharedPreferences sharedPreferences = getSharedPreferences("shared_preference", Context.MODE_PRIVATE);
        String userType = sharedPreferences.getString("user_type", "defaultType");
        Button btnActionOnCourse = findViewById(R.id.btnCourseDetailActionOnCourse);
        EventListener eventListener = new EventListener();
        btnActionOnCourse.setOnClickListener(eventListener);
        Button btnExams = findViewById(R.id.btnCourseDetailExams);
        btnExams.setOnClickListener(eventListener);
        if (userType.equals("teacher")) {
            btnActionOnCourse.setVisibility(View.GONE);
        }
        // The button functionality depends on subscription status
        String subscriptionStatus = getIntent().getStringExtra("subscription");
        if (subscriptionStatus.equals("active")) {
            btnActionOnCourse.setText(R.string.btnCourseDetailUnsubscribe_text);
        } else {
            btnActionOnCourse.setText(R.string.btnCourseDetailSubscribe_text);
        }
    }
}
