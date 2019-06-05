package it.uniroma2.dicii.sdcc.didatticamobile.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import it.uniroma2.dicii.sdcc.didatticamobile.R;
import it.uniroma2.dicii.sdcc.didatticamobile.dialog.ExamReservationDialog;
import it.uniroma2.dicii.sdcc.didatticamobile.model.Exam;
import it.uniroma2.dicii.sdcc.didatticamobile.task.ListExamsTask;

public class ExamsActivity extends AppCompatActivity {

    private Button btnAddExam;
    private Context context;

    private class EventListener implements  View.OnClickListener {

        private Activity activity;

        private EventListener(Activity context) {
            this.activity = context;
        }

        // Handle the click on the Add exam button
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnAddExam:
                    gotoCreateExam();
                    break;
                default:
                    break;
            }
        }

        private void gotoCreateExam() {
            Intent intent = new Intent(activity, AddExamActivity.class);
            String courseId = getIntent().getStringExtra("course_id");
            intent.putExtra("course_id", courseId);
            activity.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exams);
        this.context = this;
        initializeWidget();
    }


    @Override
    protected void onStart() {
        super.onStart();
        // A task is launched to show the existing exam of the selected course in the listview
        ListExamsTask listExamsTaks = new ListExamsTask(this);
        String courseId = getIntent().getStringExtra("course_id");
        listExamsTaks.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, courseId);
    }

    private void initializeWidget() {

        // Initialize the title of activity with the name of the course
        ((TextView)findViewById(R.id.tvExamsCourseName)).setText(getString(R.string.tvExamsTitle_text)
                + " " + getIntent().getStringExtra("course_name"));

        // The button to add an new exam for the course is shown only if the logged user is a teacher.
        btnAddExam = findViewById(R.id.btnAddExam);
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared_preference", Context.MODE_PRIVATE);
        String userType = sharedPreferences.getString("user_type","defaultType");
        if (userType.equals("student")){
            btnAddExam.setVisibility(View.GONE);
        }
        EventListener eventListener = new EventListener(this);
        btnAddExam.setOnClickListener(eventListener);

        // When the user clicks on an exam, a dialog to make a reservation is shown
        ListView lvExams = findViewById(R.id.lvExams);
        lvExams.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Exam examClicked = (Exam)parent.getItemAtPosition(position);
                ExamReservationDialog examReservationDialog = new ExamReservationDialog(ExamsActivity.this, examClicked);
                examReservationDialog.show();
                Window window = examReservationDialog.getWindow();
                if (window != null) {
                    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                }
            }
        });
    }

    // When the user clicks on the back button, the Courses Activity is shown
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, CoursesActivity.class);
        startActivity(intent);
    }
}
