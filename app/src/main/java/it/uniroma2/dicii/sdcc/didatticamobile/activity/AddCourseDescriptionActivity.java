package it.uniroma2.dicii.sdcc.didatticamobile.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import it.uniroma2.dicii.sdcc.didatticamobile.R;
import it.uniroma2.dicii.sdcc.didatticamobile.activity.utility.KeyboardHider;
import it.uniroma2.dicii.sdcc.didatticamobile.task.AddCourseTask;

public class AddCourseDescriptionActivity extends AppCompatActivity {

    private EditText etDescriptionCourse;
    private Button btnConfirm;
    private ConstraintLayout layout;

    private class EventListener implements  View.OnClickListener {

        private Activity context;

        private EventListener(Activity context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnConfirmAddCourse:
                    createCourse();
                    break;
                case R.id.addCourseDescriptionLayout:
                    KeyboardHider.hideKeyboard(context, layout);
                    break;
                default:
                    break;
            }
        }

        private void createCourse() {

            // Collect information from bundle prepared by previous activity
            Bundle courseInformation = getIntent().getBundleExtra("courseInformation");
            String courseDescription = etDescriptionCourse.getText().toString();
            courseInformation.putString("description", courseDescription);

            // Add teacher name to course information taking this from shared preferences of logged user
            SharedPreferences sharedPreference = context.getSharedPreferences("shared_preference", Context.MODE_PRIVATE);
            String teacher = sharedPreference.getString("user_name", "DefaultName") +
                    " " + sharedPreference.getString("user_surname", "DefaultSurname");
            courseInformation.putString("teacher", teacher);

            /* Launch the task that makes the course creation request to the server*/
            AddCourseTask addCourseTask = new AddCourseTask(context);
            addCourseTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, courseInformation);
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course_description);
        initializeWidget();
    }

    private void initializeWidget() {
        etDescriptionCourse = findViewById(R.id.etAddDescriptionCourse);
        btnConfirm = findViewById(R.id.btnConfirmAddCourse);
        layout = findViewById(R.id.addCourseDescriptionLayout);
        EventListener eventListener = new EventListener(this);
        btnConfirm.setOnClickListener(eventListener);
        layout.setOnClickListener(eventListener);
    }


}
