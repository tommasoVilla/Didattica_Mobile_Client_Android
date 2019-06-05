package it.uniroma2.dicii.sdcc.didatticamobile.activity;

import android.app.Activity;
import android.os.AsyncTask;
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
import java.util.Calendar;

import it.uniroma2.dicii.sdcc.didatticamobile.R;
import it.uniroma2.dicii.sdcc.didatticamobile.task.AddExamTask;

import static java.util.Calendar.YEAR;
import static java.util.Calendar.getInstance;

public class AddExamActivity extends AppCompatActivity {

    private Spinner cbExamDay;
    private Spinner cbExamMonth;
    private Spinner cbExamYear;
    private Spinner cbExpDay;
    private Spinner cbExpMonth;
    private Spinner cbExpYear;
    private Spinner cbStartTime;
    private Button btnAddExam;
    private EditText etRoom;
    private RadioGroup rgCall;

    private class EventListener implements  View.OnClickListener {

        private Activity context;

        private EventListener(Activity context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnConfirmAddExam:
                    if (isCorrectInput()){
                        createExam();
                    }
                    break;
                default:
                    break;
            }
        }

        private boolean isCorrectInput() {
            /*Checking selected room for the exam*/
            if (etRoom.getText().toString().replace(" ", "").equals("")) {
                Toast.makeText(context, R.string.toastNotInsertedField_text, Toast.LENGTH_LONG).show();
                return false;
            }

            /*Checking dates*/
            int currentYear = Calendar.getInstance().get(YEAR);
            int currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1; // Months of the year start from 0
            int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            int examYear = (int)cbExamYear.getSelectedItem();
            int examMonth = (int)cbExamMonth.getSelectedItem();
            int examDay = (int)cbExamDay.getSelectedItem();
            int expYear = (int)cbExpYear.getSelectedItem();
            int expMonth = (int)cbExpMonth.getSelectedItem();
            int expDay = (int)cbExpDay.getSelectedItem();


            /*Checking if current date precedes the date of expiring for the exam reservation*/
            if (!isLaterDate(currentDay, currentMonth, currentYear, expDay, expMonth, expYear)){
                Toast.makeText(context, R.string.notValidExamDate_message, Toast.LENGTH_LONG).show();
                return false;
            }
            /*Checking if expiring date precedes the date of the exam*/
            if (!isLaterDate(expDay, expMonth, expYear, examDay, examMonth, examYear)){
                Toast.makeText(context, R.string.notValidExamDate_message, Toast.LENGTH_LONG).show();
                return false;
            }
            return true;
        }

        /*This function return true if the previous date precedes the following one, false otherwise*/
        private boolean isLaterDate(int previousDay, int previousMonth, int previousYear, int followingDay,
                                 int followingMonth, int followingYear){
            if (followingYear > previousYear){
                return true;
            } else if (followingYear < previousYear){
                return false;
            } else {
                if (followingMonth > previousMonth){
                    return true;
                } else if (followingMonth < previousMonth){
                    return false;
                } else {
                    if (followingDay > previousDay){
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }

        private void createExam() {
            /*Collecting exam's information*/
            Bundle examInformation = new Bundle();

            int call = rgCall.getCheckedRadioButtonId();
            switch (call){
                case R.id.rbCall1:
                    call = 1;
                    break;
                case R.id.rbCall2:
                    call = 2;
                    break;
                case R.id.rbCall3:
                    call = 3;
                    break;
                case R.id.rbCall4:
                    call = 4;
                    break;
                case R.id.rbCall5:
                    call = 5;
                    break;
                case R.id.rbCall6:
                    call = 6;
                    break;
                default:
                    break;
            }

            examInformation.putInt("call", call);
            examInformation.putInt("examDay", (int)cbExamDay.getSelectedItem());
            examInformation.putInt("examMonth", (int)cbExamMonth.getSelectedItem());
            examInformation.putInt("examYear", (int)cbExamYear.getSelectedItem());
            examInformation.putString("startTime", cbStartTime.getSelectedItem().toString());
            examInformation.putInt("expDay", (int)cbExpDay.getSelectedItem());
            examInformation.putInt("expMonth", (int)cbExpMonth.getSelectedItem());
            examInformation.putInt("expYear", (int)cbExpYear.getSelectedItem());
            examInformation.putString("room", etRoom.getText().toString());
            examInformation.putString("course", getIntent().getStringExtra("course_id"));
            examInformation.putString("course_name", getIntent().getStringExtra("course_name"));

            /* Launch the task that makes the exam creation request to the server*/
            AddExamTask addExamTask = new AddExamTask(context);
            addExamTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, examInformation);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exam);
        initializeWidget();
    }

    private void initializeWidget() {

        //Initialize spinners
        cbExamDay = findViewById(R.id.cbDayExam);
        cbExamMonth = findViewById(R.id.cbMonthExam);
        cbExamYear = findViewById(R.id.cbYearExam);
        cbExpDay = findViewById(R.id.cbAddExamExpDay);
        cbExpMonth = findViewById(R.id.cbAddExamExpMonth);
        cbExpYear = findViewById(R.id.cbAddExamExpYear);
        cbStartTime = findViewById(R.id.cbAddExamStartTime);

        ArrayList<String> times = new ArrayList<>();
        for (int i = 7; i < 19; i++){
            times.add(i + ":" + "00");
            times.add(i + ":" + "30"); }
        ArrayAdapter<String> adapterTime = new ArrayAdapter<>(this,
                R.layout.spinner_item, times);
        adapterTime.setDropDownViewResource(R.layout.spinner_item_dropdown);
        cbStartTime.setAdapter(adapterTime);

        ArrayList<Integer> days = new ArrayList<>();
        for (Integer i = 1; i <= 31; i++ ){
            days.add(i); }
        ArrayAdapter<Integer> adapterDay = new ArrayAdapter<>(this,
                R.layout.spinner_item, days);
        adapterTime.setDropDownViewResource(R.layout.spinner_item_dropdown);
        cbExamDay.setAdapter(adapterDay);
        cbExpDay.setAdapter(adapterDay);

        ArrayList<Integer> months = new ArrayList<>();
        for (Integer i = 1; i <= 12; i++ ){
            months.add(i); }
        ArrayAdapter<Integer> adapterMonths = new ArrayAdapter<>(this,
                R.layout.spinner_item, months);
        adapterTime.setDropDownViewResource(R.layout.spinner_item_dropdown);
        cbExamMonth.setAdapter(adapterMonths);
        cbExpMonth.setAdapter(adapterMonths);

        ArrayList<Integer> years = new ArrayList<>();
        int currentYear = getInstance().get(YEAR);
        for (Integer i = currentYear; i < currentYear + 5; i++){
            years.add(i); }
        ArrayAdapter<Integer> adapterYears = new ArrayAdapter<>(this,
                R.layout.spinner_item, years);
        adapterTime.setDropDownViewResource(R.layout.spinner_item_dropdown);
        cbExpYear.setAdapter(adapterYears);
        cbExamYear.setAdapter(adapterYears);

        //Initialize listener for button of confirm
        btnAddExam = findViewById(R.id.btnConfirmAddExam);
        EventListener eventListener = new EventListener(this);
        btnAddExam.setOnClickListener(eventListener);

        //Initialize room's edit text and call's radiogroup
        etRoom = findViewById(R.id.etAddExamRoom);
        rgCall = findViewById(R.id.rgCall);
    }
}
