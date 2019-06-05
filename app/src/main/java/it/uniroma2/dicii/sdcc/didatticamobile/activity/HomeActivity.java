package it.uniroma2.dicii.sdcc.didatticamobile.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import it.uniroma2.dicii.sdcc.didatticamobile.R;
import it.uniroma2.dicii.sdcc.didatticamobile.task.RefreshTokenTask;

public class HomeActivity extends AppCompatActivity {

    private Button btnCourses;
    private Button btnTeachingMaterial;

    private class EventListener implements  View.OnClickListener {

        private Activity context;

        private EventListener(Activity context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()) {
                case R.id.btnCoursesHome:
                    intent = new Intent(context, CoursesActivity.class);
                    context.startActivity(intent);
                    break;
                case R.id.btnTeachingMaterialHome:
                    intent = new Intent(context, TeachingMaterialActivity.class);
                    context.startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initializeWidget();
        //launchRefreshTokenTask();
    }

    //TODO eventualmente rimuovere
    private void launchRefreshTokenTask() {
        /* Launch the task responsible to refresh automatically the access token before it expired*/
        RefreshTokenTask refreshTokenTask = new RefreshTokenTask(this);
        refreshTokenTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void initializeWidget() {
        btnCourses = findViewById(R.id.btnCoursesHome);
        btnTeachingMaterial = findViewById(R.id.btnTeachingMaterialHome);
        EventListener eventListener = new EventListener(this);
        btnCourses.setOnClickListener(eventListener);
        btnTeachingMaterial.setOnClickListener(eventListener);
    }

    // When the user clicks on the back button, the user is asked if he would close the app
    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        dialog.setTitle(R.string.exit_dialog_title);
        dialog.setMessage(R.string.exit_confirmation_message);
        dialog.setCancelable(false);
        dialog.setPositiveButton(R.string.ok_dialog_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
                System.exit(1);
            }
        });
        dialog.setNegativeButton(R.string.operation_not_confirmed_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.show();
    }
}
