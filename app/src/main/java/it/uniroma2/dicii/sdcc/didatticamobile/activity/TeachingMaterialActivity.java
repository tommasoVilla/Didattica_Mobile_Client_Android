package it.uniroma2.dicii.sdcc.didatticamobile.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import it.uniroma2.dicii.sdcc.didatticamobile.R;
import it.uniroma2.dicii.sdcc.didatticamobile.model.Course;
import it.uniroma2.dicii.sdcc.didatticamobile.task.ListCoursesForTeachingMaterialTask;

public class TeachingMaterialActivity extends AppCompatActivity {

    private Context context;

    public TeachingMaterialActivity(){}

    public TeachingMaterialActivity(Context context){
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teaching_material);
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
        ListCoursesForTeachingMaterialTask listCoursesForTeachingMaterialTask = new ListCoursesForTeachingMaterialTask(this);
        listCoursesForTeachingMaterialTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void initializeWidget(){
        // Setting the listener for click over an list item
        ListView lvCourses = findViewById(R.id.lvCoursesTeachingMaterial);
        lvCourses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Course courseClicked = (Course)parent.getItemAtPosition(position);
                Intent intent = new Intent(context, CourseTeachingMaterialsActivity.class);
                intent.putExtra("courseId", courseClicked.getId());
                intent.putExtra("courseName", courseClicked.getName());
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
