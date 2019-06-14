package it.uniroma2.dicii.sdcc.didatticamobile.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import it.uniroma2.dicii.sdcc.didatticamobile.R;
import it.uniroma2.dicii.sdcc.didatticamobile.model.Exam;
import it.uniroma2.dicii.sdcc.didatticamobile.task.ReserveExamTask;

/*This class defines the custom dialog box that is shown when a student try to reserve an exam call*/
public class ExamReservationDialog extends Dialog {

    private Context context;
    private Exam exam;

    private class EventListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.btnDialogReserveExamReserve:
                    // Launch the task responsible for make the reservation request
                    ReserveExamTask reserveExamTask = new ReserveExamTask(context, ExamReservationDialog.this);
                    reserveExamTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, exam.getId());
                    break;
                default:
                    break;
            }
        }
    }

    public ExamReservationDialog(@NonNull Context context, Exam exam) {
        super(context);
        this.context = context;
        this.exam = exam;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_reserve_exam);
        initializeWidget();
    }

    private void initializeWidget() {
        TextView tvExamCall = findViewById(R.id.tvDialogReserveExamCall);
        tvExamCall.setText(String.valueOf(exam.getCall()));
        TextView tvExamDate = findViewById(R.id.tvDialogReserveExamDate);
        tvExamDate.setText(exam.getDate() + " " + exam.getStartTime());
        TextView tvExamRoom = findViewById(R.id.tvDialogReserveExamRoom);
        tvExamRoom.setText(exam.getRoom());
        TextView tvExpirationDate = findViewById(R.id.tvDialogReserveExamExpirationDate);
        tvExpirationDate.setText(exam.getExpirationDate());
        Button btnReserveExam = findViewById(R.id.btnDialogReserveExamReserve);
        btnReserveExam.setOnClickListener(new EventListener());
        // If the user is a teacher or the student is already registered the reservation button
        // is disabled when the dialog is opened or re-opened
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared_preference", Context.MODE_PRIVATE);
        String userType = sharedPreferences.getString("user_type", "defaultType");
        if (userType.equals("teacher")) {
            findViewById(R.id.btnDialogReserveExamReserve).setClickable(false);
            findViewById(R.id.btnDialogReserveExamReserve).setAlpha(.5f);
        }
        String studentUsername = sharedPreferences.getString("user_username", "defaultUsername");
        List<String> examStudents = exam.getStudents();
        if (examStudents.contains(studentUsername)) {
            findViewById(R.id.btnDialogReserveExamReserve).setClickable(false);
            findViewById(R.id.btnDialogReserveExamReserve).setAlpha(.5f);
        }
    }
}
