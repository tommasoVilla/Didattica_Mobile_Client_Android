package it.uniroma2.dicii.sdcc.didatticamobile.task;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;

import java.util.List;

import it.uniroma2.dicii.sdcc.didatticamobile.R;
import it.uniroma2.dicii.sdcc.didatticamobile.activity.InternetConnectionStatus;
import it.uniroma2.dicii.sdcc.didatticamobile.adapter.CoursesListAdapter;
import it.uniroma2.dicii.sdcc.didatticamobile.adapter.ExamsListAdapter;
import it.uniroma2.dicii.sdcc.didatticamobile.dao.ExamDao;
import it.uniroma2.dicii.sdcc.didatticamobile.dao.ExamDaoFactory;
import it.uniroma2.dicii.sdcc.didatticamobile.error.ErrorHandler;
import it.uniroma2.dicii.sdcc.didatticamobile.error.NoInternetConnectionException;
import it.uniroma2.dicii.sdcc.didatticamobile.model.Exam;

/* A ListExamsTask is responsible for showing on a ListView the exam belong to selected course.*/
public class ListExamsTask extends AsyncTask <String, Void, Void> {

    private Activity examsActivity;
    private List<Exam> exams;

    /*
     * This field is populated with the exception thrown inside doInBackground. If an exception occurs
     * this field is not null and inside onPostExecute is called the ErrorHandler object to show the
     * error to the user.
     * */
    private Exception thrownException;

    public ListExamsTask (Activity examsActivity){this.examsActivity = examsActivity; }

    /* During the execution a progress bar is shown and the other widgets are hidden.*/
    @Override
    protected void onPreExecute() {
        examsActivity.findViewById(R.id.pbLoadingExamsList).setVisibility(View.VISIBLE);
        examsActivity.findViewById(R.id.btnAddExam).setVisibility(View.GONE);
        examsActivity.findViewById(R.id.lvExams).setVisibility(View.GONE);
    }

    @Override
    protected Void doInBackground(String... params) {
        try {
            /* If the device is not connected to Internet an error is raised*/
            InternetConnectionStatus internetConnectionStatus = new InternetConnectionStatus();
            if (!internetConnectionStatus.isDeviceConnectedToInternet(examsActivity)) {
                throw new NoInternetConnectionException();
            }

            ExamDaoFactory examDaoFactory = ExamDaoFactory.getInstance();
            ExamDao examDao = examDaoFactory.createExamDao();
            SharedPreferences sharedPreferences = examsActivity.getSharedPreferences("shared_preference", Context.MODE_PRIVATE);
            String token = sharedPreferences.getString("token","defaultToken");
            exams = examDao.findByCourse(params[0], token);
            return null;

        } catch (Exception e) {
            thrownException = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        /* After the execution, the visibility of the widgets is restored.*/
        examsActivity.findViewById(R.id.pbLoadingExamsList).setVisibility(View.GONE);
        SharedPreferences sharedPreferences = examsActivity.getSharedPreferences("shared_preference", Context.MODE_PRIVATE);
        String userType = sharedPreferences.getString("user_type", "defaultType");
        if (userType.equals("teacher")) {
            examsActivity.findViewById(R.id.btnAddExam).setVisibility(View.VISIBLE);
        }

        if (thrownException != null) {
            ErrorHandler errorHandler = new ErrorHandler(examsActivity);
            errorHandler.show(thrownException);
        } else {
            // If no courses have been found, an informative text view is shown
            if (exams.isEmpty()) {
                examsActivity.findViewById(R.id.tvActivityExamsNoFoundExams).setVisibility(View.VISIBLE);
            } else {
                ListView lvExams = examsActivity.findViewById(R.id.lvExams);
                ExamsListAdapter examsListAdapter = new ExamsListAdapter(examsActivity, R.layout.exam_list_item, exams);
                lvExams.setAdapter(examsListAdapter);
                examsActivity.findViewById(R.id.lvExams).setVisibility(View.VISIBLE);
            }
        }
    }
}
