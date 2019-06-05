package it.uniroma2.dicii.sdcc.didatticamobile.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import it.uniroma2.dicii.sdcc.didatticamobile.R;
import it.uniroma2.dicii.sdcc.didatticamobile.model.Course;
import it.uniroma2.dicii.sdcc.didatticamobile.parcelable.CourseParcelable;
import it.uniroma2.dicii.sdcc.didatticamobile.task.ListCoursesTask;

public class CoursesActivity extends AppCompatActivity {

    private Context context;

    private class EventListener implements  View.OnClickListener {

        private Activity activity;

        private EventListener(Activity activity) {
            this.activity = activity;
        }

        // Handle the click on the Add Course button
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnAddCourse:
                    SharedPreferences sharedPreferences = getSharedPreferences("shared_preference", Context.MODE_PRIVATE);
                    String userType = sharedPreferences.getString("user_type","defaultType");
                    // If the logged user is a teacher, the app shows an activity that provides a
                    // form for entering information about the new course of the teacher
                    if (userType.equals("teacher")) {
                        Intent intent = new Intent(activity, AddCourseActivity.class);
                        activity.startActivity(intent);
                    // If the logged user is a student, the app shows an activity that allows the user
                    // to find the course he/she wants to attend
                    } else {
                        Intent intent = new Intent(activity, FindCoursesActivity.class);
                        activity.startActivity(intent);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);
        context = this;
        initializeWidget();
    }

    @Override
    protected void onStart() {
        super.onStart();
        populateCoursesListView();
    }

    private void populateCoursesListView() {
        ///A task is launched to show the courses of the logged user (attended in case of student, hold in case of teacher)
        ListCoursesTask listCoursesTask = new ListCoursesTask(this);
        listCoursesTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void initializeWidget() {
        Button btnAddCourse = findViewById(R.id.btnAddCourse);
        EventListener eventListener = new EventListener(this);
        btnAddCourse.setOnClickListener(eventListener);
        // The listener that handles the click on a list item is set
        ListView lvYourCourses = findViewById(R.id.lvYourCourses);
        lvYourCourses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Course courseClicked = (Course)parent.getItemAtPosition(position);

                //Saving in sharedPreference the name and the id of clicked course
                SharedPreferences sharedPreferences = context.getSharedPreferences("shared_preference", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("course_selected_name", courseClicked.getName());
                editor.putString("course_selected_id", courseClicked.getId());
                editor.apply();
                System.out.println(sharedPreferences.getString("course_selected_name", "default"));

                // The clicked  course is passed to the detail activity as a Parcelable as required by Android
                CourseParcelable courseParcelable = new CourseParcelable(courseClicked);
                Intent intent = new Intent(context, CourseDetailActivity.class);
                intent.putExtra("course_to_show", courseParcelable);
                // the current activity communicates to the following one the subscription status
                intent.putExtra("subscription", "active");
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}
