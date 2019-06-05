package it.uniroma2.dicii.sdcc.didatticamobile.activity;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import it.uniroma2.dicii.sdcc.didatticamobile.R;
import it.uniroma2.dicii.sdcc.didatticamobile.task.DownloadTeachingMaterialTask;
import it.uniroma2.dicii.sdcc.didatticamobile.task.ListTeachingMaterialForCourseTask;

public class CourseTeachingMaterialsActivity extends AppCompatActivity {

    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_teaching_materials);
        activity = this;
        initializeWidget();
    }

    @Override
    protected void onStart() {
        super.onStart();
        populateCoursesListView();
    }

    private void populateCoursesListView() {
        ///A task is launched to show the teaching material for the selected course
        ListTeachingMaterialForCourseTask listCoursesForTeachingMaterialTask =
                new ListTeachingMaterialForCourseTask(this);
        listCoursesForTeachingMaterialTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                getIntent().getStringExtra("courseId"));
    }

    private void initializeWidget() {
        ((TextView)findViewById(R.id.tvTeachingMaterialNameCourse)).setText(getIntent().getStringExtra("courseName"));

        // Setting the listener for click over an list item: when an item is clicked the selected
        // file is visualized by browser
        ListView lvTeachingMaterial = findViewById(R.id.lvTeachingMaterial);
        lvTeachingMaterial.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedFile = (String)parent.getItemAtPosition(position);
                // Launching a task responsible to obtain the download link and display the file through browser
                DownloadTeachingMaterialTask downloadTeachingMateriaTask = new DownloadTeachingMaterialTask(activity);
                downloadTeachingMateriaTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, getIntent().getStringExtra("courseId"), clickedFile );
            }
        });
    }
}
