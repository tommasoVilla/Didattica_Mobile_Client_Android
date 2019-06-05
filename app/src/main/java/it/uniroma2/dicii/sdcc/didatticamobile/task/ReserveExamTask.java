package it.uniroma2.dicii.sdcc.didatticamobile.task;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import it.uniroma2.dicii.sdcc.didatticamobile.R;
import it.uniroma2.dicii.sdcc.didatticamobile.dao.ExamDao;
import it.uniroma2.dicii.sdcc.didatticamobile.dao.ExamDaoFactory;
import it.uniroma2.dicii.sdcc.didatticamobile.error.ErrorHandler;

/*ReserveExamTask is a task responsible for making exam reservation request to the server*/
public class ReserveExamTask extends AsyncTask<String, Void, Void> {

    private Context context; // dialog context
    private Dialog dialog; // the caller dialog box
    private Exception thrownException; // contains the exception that the background thread may raise

    public ReserveExamTask(Context context, Dialog dialog) {
        this.context = context;
        this.dialog = dialog;
    }

    // During the execution the widget disappear and a progress bar is shown
    @Override
    protected void onPreExecute() {
        dialog.findViewById(R.id.pbLoadingDialogReserveExam).setVisibility(View.VISIBLE);
        dialog.findViewById(R.id.tvDialogReserveExamCall).setVisibility(View.GONE);
        dialog.findViewById(R.id.tvDialogReserveExamCallLabel).setVisibility(View.GONE);
        dialog.findViewById(R.id.tvDialogReserveExamDate).setVisibility(View.GONE);
        dialog.findViewById(R.id.tvDialogReserveExamDateLabel).setVisibility(View.GONE);
        dialog.findViewById(R.id.tvDialogReserveExamRoom).setVisibility(View.GONE);
        dialog.findViewById(R.id.tvDialogReserveExamRoomLabel).setVisibility(View.GONE);
        dialog.findViewById(R.id.tvDialogReserveExamExpirationDate).setVisibility(View.GONE);
        dialog.findViewById(R.id.tvDialogReserveExamExpirationDateLabel).setVisibility(View.GONE);
        dialog.findViewById(R.id.btnDialogReserveExamReserve).setVisibility(View.GONE);
    }

    @Override
    protected Void doInBackground(String... strings) {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences("shared_preference", Context.MODE_PRIVATE);
            // The username of the student is retrieved from the shared preferences
            String username = sharedPreferences.getString("user_username", "defaultUsername");
            // The token token is for authenticate the request server-side
            String token = sharedPreferences.getString("token", "defaultToken");
            ExamDaoFactory examDaoFactory = ExamDaoFactory.getInstance();
            ExamDao examDao = examDaoFactory.createExamDao();
            examDao.registerStudentToExam(username, strings[0], token);
            return null;
        } catch (Exception e){
            thrownException = e;
            return null;
        }
    }

    // After the background execution the visibility of the widgets is restored
    @Override
    protected void onPostExecute(Void aVoid) {
        dialog.findViewById(R.id.pbLoadingDialogReserveExam).setVisibility(View.GONE);
        dialog.findViewById(R.id.tvDialogReserveExamCall).setVisibility(View.VISIBLE);
        dialog.findViewById(R.id.tvDialogReserveExamCallLabel).setVisibility(View.VISIBLE);
        dialog.findViewById(R.id.tvDialogReserveExamDate).setVisibility(View.VISIBLE);
        dialog.findViewById(R.id.tvDialogReserveExamDateLabel).setVisibility(View.VISIBLE);
        dialog.findViewById(R.id.tvDialogReserveExamRoom).setVisibility(View.VISIBLE);
        dialog.findViewById(R.id.tvDialogReserveExamRoomLabel).setVisibility(View.VISIBLE);
        dialog.findViewById(R.id.tvDialogReserveExamExpirationDate).setVisibility(View.VISIBLE);
        dialog.findViewById(R.id.tvDialogReserveExamExpirationDateLabel).setVisibility(View.VISIBLE);
        dialog.findViewById(R.id.btnDialogReserveExamReserve).setVisibility(View.VISIBLE);
        if (thrownException != null) {
            ErrorHandler errorHandler = new ErrorHandler(context);
            errorHandler.show(thrownException);
        } else {
            // On success, an informative toast is shown
            Toast.makeText(context, R.string.examReservationSuccess_message, Toast.LENGTH_SHORT).show();
            // The button for requiring the reservation is then disabled
            dialog.findViewById(R.id.btnDialogReserveExamReserve).setClickable(false);
            // To stress the disabled state the button is made opaque
            dialog.findViewById(R.id.btnDialogReserveExamReserve).setAlpha(.5f);
        }
    }
}
