package it.uniroma2.dicii.sdcc.didatticamobile.activity;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import it.uniroma2.dicii.sdcc.didatticamobile.R;
import it.uniroma2.dicii.sdcc.didatticamobile.task.SendCourseNotificationTask;

public class CourseNotificationActivity extends AppCompatActivity {

    private EditText etMessage;

    private class EventListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnCourseNotificationSend:
                    // The task responsible for publishing the course notification is launched
                    String message = etMessage.getText().toString();
                    if (message.equals("")) {
                        Toast.makeText(CourseNotificationActivity.this, R.string.courseNotificationEmptyMessage_message, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String courseId = getIntent().getStringExtra("course_id");
                    SendCourseNotificationTask sendCourseNotificationTask = new SendCourseNotificationTask(CourseNotificationActivity.this);
                    sendCourseNotificationTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, courseId, message);
                    break;
                default:
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_notification);
        initializeWidget();
    }

    private void initializeWidget() {
        TextView tvCourse = findViewById(R.id.tvCourseNotificationCourseName);
        tvCourse.setText(getIntent().getStringExtra("course_name"));
        etMessage = findViewById(R.id.etCourseNotificationMessage);
        Button btnSendNotification = findViewById(R.id.btnCourseNotificationSend);
        btnSendNotification.setOnClickListener(new EventListener());
    }
}
