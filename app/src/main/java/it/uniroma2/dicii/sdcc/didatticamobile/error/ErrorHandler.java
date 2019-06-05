package it.uniroma2.dicii.sdcc.didatticamobile.error;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;
import it.uniroma2.dicii.sdcc.didatticamobile.R;
import it.uniroma2.dicii.sdcc.didatticamobile.activity.MainActivity;
/*
 * An ErrorHandler object is an utility object that provides method to gracefully communicates error
 * to the user through the GUI.
 * */
public class ErrorHandler {

    private Context context;

    public ErrorHandler(Context context) {
        this.context = context;
    }

    public void show(Exception thrownException) {

        int messageCode;

        /*For debug purpose the thrownException is printed on Logcat*/
        thrownException.printStackTrace();

        boolean critical = false;
        if (thrownException instanceof NoInternetConnectionException) {
            messageCode = R.string.noInternetConnectionException_message;
            critical = true;
        } else if (thrownException instanceof RegistrationAuthorizationException) {
            messageCode = R.string.registrationAuthorizationException_message;
        } else if (thrownException instanceof UsernameConflictException) {
            messageCode = R.string.usernameConflictException_message;
        } else if (thrownException instanceof TemporaryUnavailableException) {
            messageCode = R.string.temporaryUnavailableException_message;
        } else if (thrownException instanceof LoginAuthorizationException) {
            messageCode = R.string.loginAuthorizationException_message;
        } else  if (thrownException instanceof ConflictException ) {
            messageCode = R.string.conflictException_message;
        } else if (thrownException instanceof AuthorizationException) {
            messageCode = R.string.authorizationException_message;
        } else if (thrownException instanceof ExpiredTokenException) {
            messageCode = R.string.expiredTokenException_message;
            AlertDialog.Builder dialog = new AlertDialog.Builder(context, R.style.ErrorDialogTheme);
            dialog.setTitle(R.string.error_dialog_title);
            dialog.setMessage(messageCode);
            dialog.setCancelable(false);
            dialog.setNeutralButton(R.string.ok_dialog_button, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(context, MainActivity.class);
                    context.startActivity(intent);
                }
            });
            dialog.show();
            return;
        } else if (thrownException instanceof ExistentSubscriptionException) {
            messageCode = R.string.existentSusbcriptionException_message;
        } else if (thrownException instanceof NotAllowedExamReservationException) {
            messageCode = R.string.notAllowedExamReservationException_message;
        } else {
            messageCode = R.string.internalException_message;
            critical = true;
        }
        if (critical) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(context, R.style.ErrorDialogTheme);
            dialog.setTitle(R.string.error_dialog_title);
            dialog.setMessage(messageCode);
            dialog.setCancelable(false);
            dialog.setNeutralButton(R.string.ok_dialog_button, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ((Activity) context).finishAffinity();
                    System.exit(1);
                }
            });
            dialog.show();
        } else {
            Toast.makeText(context, messageCode, Toast.LENGTH_SHORT).show();
        }
    }

}
