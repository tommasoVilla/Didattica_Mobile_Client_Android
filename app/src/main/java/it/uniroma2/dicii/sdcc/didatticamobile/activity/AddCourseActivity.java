package it.uniroma2.dicii.sdcc.didatticamobile.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;


import java.util.ArrayList;

import it.uniroma2.dicii.sdcc.didatticamobile.R;

import static java.util.Calendar.*;

public class AddCourseActivity extends AppCompatActivity {

    private Button btnAddCourseSchedule;
    private EditText etCourseName;
    private EditText etCourseDepartment;
    private Spinner cbYear;
    private RadioGroup rgSemester;

    private class EventListener implements  View.OnClickListener {

        private Activity context;

        private EventListener(Activity context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnAddCourseSchedule:
                    // Go on inserting other information for course creation
                    continueCourseCreation();
                    break;
                default:
                    break;
            }
        }

        private void continueCourseCreation() {

            // Collecting base information
            String courseName = etCourseName.getText().toString();
            String courseDepartment = etCourseDepartment.getText().toString();

            Boolean isCorrectInput = checkParameter(courseName, courseDepartment);
            if (!isCorrectInput){
                return;
            }

            String year = cbYear.getSelectedItem().toString();
            int semester = rgSemester.getCheckedRadioButtonId();
            if (semester == R.id.rbSemester1){
                semester = 1;
            } else if (semester == R.id.rbSemester2){
                semester = 2;
            }

            // The information inserted by the user are stored in a bundle passed to follow activity
            Intent intent = new Intent(context, AddCourseScheduleActivity.class);
            Bundle courseInformations = new Bundle();
            courseInformations.putString("name", courseName);
            courseInformations.putString("department", courseDepartment);
            courseInformations.putString("year", year);
            courseInformations.putInt("semester", semester);
            intent.putExtra("courseInformation", courseInformations);
            context.startActivity(intent);
        }

        private Boolean checkParameter(String courseName, String courseDepartment) {
            if (courseDepartment.trim().equals("") || courseName.trim().equals("")){
                Toast.makeText(this.context, R.string.toastNotInsertedField_text, Toast.LENGTH_SHORT).show();
                return false;
            }
            if (!courseName.replace(" ","").matches("[a-zA-Z0-9]+")||
                    !courseDepartment.replace(" ","").matches("[a-zA-Z]+")){
                Toast.makeText(this.context, R.string.toastCourseInfoNotValid_text, Toast.LENGTH_SHORT).show();
                return false;
            }
            return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        initializeWidget();
    }

    private void initializeWidget() {

        //Initialize years spinner
        cbYear = findViewById(R.id.cbYear);
        ArrayList<String> years = new ArrayList<>();
        int currentYear = getInstance().get(YEAR);
        for (int i = currentYear - 1; i < currentYear + 5; i++){
            years.add("" + i + "-" + (i+1));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.spinner_item, years);
        adapter.setDropDownViewResource(R.layout.spinner_item_dropdown);
        cbYear.setAdapter(adapter);

        //Initialize other widget
        rgSemester = findViewById(R.id.rgSemester);
        etCourseName = findViewById(R.id.etCourseName);
        etCourseDepartment = findViewById(R.id.erCourseDepartment);
        btnAddCourseSchedule = findViewById(R.id.btnAddCourseSchedule);
        EventListener eventListener = new EventListener(this);
        btnAddCourseSchedule.setOnClickListener(eventListener);
    }
}
