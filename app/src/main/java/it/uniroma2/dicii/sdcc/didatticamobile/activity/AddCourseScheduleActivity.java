package it.uniroma2.dicii.sdcc.didatticamobile.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import it.uniroma2.dicii.sdcc.didatticamobile.R;
import it.uniroma2.dicii.sdcc.didatticamobile.activity.utility.KeyboardHider;

public class AddCourseScheduleActivity extends AppCompatActivity {

    private Spinner cbDay1;
    private Spinner cbDay2;
    private Spinner cbDay3;
    private Spinner cbStartTime1;
    private Spinner cbStartTime2;
    private Spinner cbStartTime3;
    private Spinner cbEndTime1;
    private Spinner cbEndTime2;
    private Spinner cbEndTime3;
    private Button btnAddCourseDescription;
    private EditText etRoom1;
    private EditText etRoom2;
    private EditText etRoom3;
    private ConstraintLayout layout;

    private class EventListener implements  View.OnClickListener {

        private Activity context;

        private EventListener(Activity context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnAddCourseDescription:
                    // Go on inserting other information for course creation
                    continueCourseCreation();
                    break;
                case R.id.addCourseScheduleLayout:
                    KeyboardHider.hideKeyboard(context, layout);
                    break;
                default:
                    break;
            }
        }

        private void continueCourseCreation() {

            String room1 = etRoom1.getText().toString();
            String room2 = etRoom2.getText().toString();
            String room3 = etRoom3.getText().toString();

            // Checking validity of rooms
            if (room1.replace(" ", "").equals("") ||
                    room2.replace(" ", "").equals("") ||
                    room3.replace(" ", "").equals("")){
                Toast toast = Toast.makeText(context,R.string.toastNotInsertedField_text, Toast.LENGTH_LONG);
                toast.show();
                return;
            }

            // The information inserted by the user are stored in a bundle passed to follow activity
            Bundle courseInformation = getIntent().getBundleExtra("courseInformation");
            courseInformation.putString("day1", (String)cbDay1.getSelectedItem());
            courseInformation.putString("day2", (String)cbDay2.getSelectedItem());
            courseInformation.putString("day3", (String)cbDay3.getSelectedItem());
            courseInformation.putString("startTime1", (String)cbStartTime1.getSelectedItem());
            courseInformation.putString("startTime2", (String)cbStartTime2.getSelectedItem());
            courseInformation.putString("startTime3", (String)cbStartTime3.getSelectedItem());
            courseInformation.putString("endTime1", (String)cbEndTime1.getSelectedItem());
            courseInformation.putString("endTime2", (String)cbEndTime2.getSelectedItem());
            courseInformation.putString("endTime3", (String)cbEndTime3.getSelectedItem());
            courseInformation.putString("room1", room1);
            courseInformation.putString("room2", room2);
            courseInformation.putString("room3", room3);
            Intent intent = new Intent(context, AddCourseDescriptionActivity.class);
            intent.putExtra("courseInformation", courseInformation);
            context.startActivity(intent);

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course_schedule);
        initializeWidget();
    }

    private void initializeWidget() {

        cbDay1 = findViewById(R.id.cbDay1);
        cbDay2 = findViewById(R.id.cbDay2);
        cbDay3 = findViewById(R.id.cbDay3);
        cbStartTime1 = findViewById(R.id.cbStartTime1);
        cbStartTime2 = findViewById(R.id.cbStartTime2);
        cbStartTime3 = findViewById(R.id.cbStartTime3);
        cbEndTime1 = findViewById(R.id.cbEndTime1);
        cbEndTime2 = findViewById(R.id.cbEndTime2);
        cbEndTime3 = findViewById(R.id.cbEndTime3);

        // Initialize data spinners
        ArrayList<String> days = new ArrayList<>();
        days.add(getString(R.string.monday));
        days.add(getString(R.string.tuesday));
        days.add(getString(R.string.wednesday));
        days.add(getString(R.string.thursday));
        days.add(getString(R.string.friday));
        days.add(getString(R.string.saturday));

        ArrayAdapter<String> adapterDay = new ArrayAdapter<>(this,
                R.layout.spinner_item, days);
        adapterDay.setDropDownViewResource(R.layout.spinner_item_dropdown);
        cbDay1.setAdapter(adapterDay);
        cbDay2.setAdapter(adapterDay);
        cbDay3.setAdapter(adapterDay);

        // Initialize time spinners
        ArrayList<String> times = new ArrayList<>();

        for (int i = 7; i < 19; i++){
            times.add(i + ":" + "00");
            times.add(i + ":" + "30");
        }
        ArrayAdapter<String> adapterTime = new ArrayAdapter<>(this,
                R.layout.spinner_item, times);
        adapterTime.setDropDownViewResource(R.layout.spinner_item_dropdown);
        cbStartTime1.setAdapter(adapterTime);
        cbStartTime2.setAdapter(adapterTime);
        cbStartTime3.setAdapter(adapterTime);
        cbEndTime1.setAdapter(adapterTime);
        cbEndTime2.setAdapter(adapterTime);
        cbEndTime3.setAdapter(adapterTime);

        btnAddCourseDescription = findViewById(R.id.btnAddCourseDescription);
        EventListener eventListener = new EventListener(this);
        btnAddCourseDescription.setOnClickListener(eventListener);

        etRoom1 = findViewById(R.id.etRoom1);
        etRoom2 = findViewById(R.id.etRoom2);
        etRoom3 = findViewById(R.id.etRoom3);

        layout = findViewById(R.id.addCourseScheduleLayout);
        layout.setOnClickListener(eventListener);

    }
}
