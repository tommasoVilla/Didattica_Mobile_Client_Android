package it.uniroma2.dicii.sdcc.didatticamobile.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import java.util.ArrayList;
import it.uniroma2.dicii.sdcc.didatticamobile.R;
import it.uniroma2.dicii.sdcc.didatticamobile.model.Course;
import it.uniroma2.dicii.sdcc.didatticamobile.parcelable.CourseParcelable;
import it.uniroma2.dicii.sdcc.didatticamobile.task.FindCoursesTask;

public class FindCoursesActivity extends AppCompatActivity {

    private Spinner cbCourseSearchType;
    private EditText etFindCourse;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_courses);
        context = this;
        initializeWidget();
    }

    private class EventListener implements View.OnClickListener {

        private Activity activity;

        public EventListener(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnFindCourse:
                    // Launch the task that finds the courses matching with the provided criteria
                    FindCoursesTask findCoursesTask = new FindCoursesTask(activity);
                    String selectedItem = cbCourseSearchType.getSelectedItem().toString();
                    String searchCriterion;
                    if (selectedItem.equals("Nome")) {
                        searchCriterion = "name";
                    } else {
                        searchCriterion = "teacher";
                    }
                    String searchPattern = etFindCourse.getText().toString();
                    findCoursesTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, searchCriterion, searchPattern);

                    break;
                default:
                    break;
            }
        }
    }

    private void initializeWidget() {

        // Initialize the combo box used to select search criteria (by name or by teacher)
        cbCourseSearchType = findViewById(R.id.cbCourseSearchType);
        ArrayList<String> searchTypes = new ArrayList<>();
        searchTypes.add(getString(R.string.searchTypeName));
        searchTypes.add(getString(R.string.searchTypeTeacher));
        ArrayAdapter<String> searchTypesAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, searchTypes);
        searchTypesAdapter.setDropDownViewResource(R.layout.spinner_item_dropdown);
        cbCourseSearchType.setAdapter(searchTypesAdapter);

        // Initialize the button that launch the research of the courses matching the provided criteria
        Button btnFindCourse = findViewById(R.id.btnFindCourse);
        EventListener eventListener = new EventListener(this);
        btnFindCourse.setOnClickListener(eventListener);

        etFindCourse = findViewById(R.id.etFindCourse);

        // The listener that handles the click on a list item is set
        ListView lvMatchingCourses = findViewById(R.id.lvMatchingCourses);
        lvMatchingCourses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Course courseClicked = (Course)parent.getItemAtPosition(position);
                // The clicked  course is passed to the detail activity as a Parcelable as required by Android
                CourseParcelable courseParcelable = new CourseParcelable(courseClicked);
                Intent intent = new Intent(context, CourseDetailActivity.class);
                intent.putExtra("course_to_show", courseParcelable);
                // The current activity communicates to the following one the subscription status
                intent.putExtra("subscription", "inactive");
                startActivity(intent);
            }
        });

    }
}
