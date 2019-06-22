package it.uniroma2.dicii.sdcc.didatticamobile.task;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import it.uniroma2.dicii.sdcc.didatticamobile.R;
import it.uniroma2.dicii.sdcc.didatticamobile.activity.CoursesActivity;
import it.uniroma2.dicii.sdcc.didatticamobile.activity.utility.InternetConnectionStatus;
import it.uniroma2.dicii.sdcc.didatticamobile.dao.ExamDao;
import it.uniroma2.dicii.sdcc.didatticamobile.dao.ExamDaoFactory;
import it.uniroma2.dicii.sdcc.didatticamobile.error.ErrorHandler;
import it.uniroma2.dicii.sdcc.didatticamobile.error.NoInternetConnectionException;
import it.uniroma2.dicii.sdcc.didatticamobile.model.Exam;

/* A AddExamTask is a task responsible to make the insert request to the server for an exam.*/
public class AddExamTask extends AsyncTask<Bundle, Void, Void> {

    private Activity context;

    /*
     * This field is populated with the exception thrown inside doInBackground. If an exception occurs
     * this field is not null and inside onPostExecute is called the ErrorHandler object to show the
     * error to the user.
     * */
    private Exception thrownException;

    public AddExamTask(Activity activity){
        this.context = activity;
    }

    /* During the execution a progress bar is shown and the other widgets are hidden.*/
    @Override
    protected void onPreExecute() {
        context.findViewById(R.id.pbAddExam).setVisibility(View.VISIBLE);
        context.findViewById(R.id.tvCall).setVisibility(View.GONE);
        context.findViewById(R.id.rgCall).setVisibility(View.GONE);
        context.findViewById(R.id.tvAddExamDate).setVisibility(View.GONE);
        context.findViewById(R.id.cbDayExam).setVisibility(View.GONE);
        context.findViewById(R.id.cbMonthExam).setVisibility(View.GONE);
        context.findViewById(R.id.cbYearExam).setVisibility(View.GONE);
        context.findViewById(R.id.tvAddExamRoom).setVisibility(View.GONE);
        context.findViewById(R.id.etAddExamRoom).setVisibility(View.GONE);
        context.findViewById(R.id.tvAddExamStartTime).setVisibility(View.GONE);
        context.findViewById(R.id.cbAddExamStartTime).setVisibility(View.GONE);
        context.findViewById(R.id.tvAddExamExpDate).setVisibility(View.GONE);
        context.findViewById(R.id.cbAddExamExpDay).setVisibility(View.GONE);
        context.findViewById(R.id.cbAddExamExpMonth).setVisibility(View.GONE);
        context.findViewById(R.id.cbAddExamExpYear).setVisibility(View.GONE);
        context.findViewById(R.id.btnConfirmAddExam).setVisibility(View.GONE);
        context.findViewById(R.id.tvInsertExamInfos).setVisibility(View.GONE);
    }

    @Override
    protected Void doInBackground(Bundle ... bundle) {
        try {
            /* If the device is not connected to Internet an error is raised*/
            InternetConnectionStatus internetConnectionStatus = new InternetConnectionStatus();
            if (!internetConnectionStatus.isDeviceConnectedToInternet(context)) {
                throw new NoInternetConnectionException();
            }

            /* The interaction with the persistence layer is delegated to an ExamDao object*/
            ExamDaoFactory examDaoFactory = ExamDaoFactory.getInstance();
            ExamDao examDao = examDaoFactory.createExamDao();
            Exam exam = new Exam(bundle[0]);

            /* The request is embedded with an access token read from shared preferences*/
            SharedPreferences sharedPreferences = context.getSharedPreferences("shared_preference", Context.MODE_PRIVATE);
            String token = sharedPreferences.getString("token","defaultToken");
            examDao.add(exam, token);
            return null;

        } catch (Exception e) {
            thrownException = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(Void v) {
        if (thrownException != null) {
            ErrorHandler errorHandler = new ErrorHandler(context);
            errorHandler.show(thrownException);
            /* After the execution, the visibility of the widgets is restored.*/
            context.findViewById(R.id.pbAddExam).setVisibility(View.GONE);
            context.findViewById(R.id.tvCall).setVisibility(View.VISIBLE);
            context.findViewById(R.id.rgCall).setVisibility(View.VISIBLE);
            context.findViewById(R.id.tvAddExamDate).setVisibility(View.VISIBLE);
            context.findViewById(R.id.cbDayExam).setVisibility(View.VISIBLE);
            context.findViewById(R.id.cbMonthExam).setVisibility(View.VISIBLE);
            context.findViewById(R.id.cbYearExam).setVisibility(View.VISIBLE);
            context.findViewById(R.id.tvAddExamRoom).setVisibility(View.VISIBLE);
            context.findViewById(R.id.etAddExamRoom).setVisibility(View.VISIBLE);
            context.findViewById(R.id.tvAddExamStartTime).setVisibility(View.VISIBLE);
            context.findViewById(R.id.cbAddExamStartTime).setVisibility(View.VISIBLE);
            context.findViewById(R.id.tvAddExamExpDate).setVisibility(View.VISIBLE);
            context.findViewById(R.id.cbAddExamExpDay).setVisibility(View.VISIBLE);
            context.findViewById(R.id.cbAddExamExpMonth).setVisibility(View.VISIBLE);
            context.findViewById(R.id.cbAddExamExpYear).setVisibility(View.VISIBLE);
            context.findViewById(R.id.btnConfirmAddExam).setVisibility(View.VISIBLE);
            context.findViewById(R.id.tvInsertExamInfos).setVisibility(View.VISIBLE);

        } else {
            Toast.makeText(context, R.string.createdExam_message, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(context, CoursesActivity.class);
            context.startActivity(intent);
        }

    }
}
