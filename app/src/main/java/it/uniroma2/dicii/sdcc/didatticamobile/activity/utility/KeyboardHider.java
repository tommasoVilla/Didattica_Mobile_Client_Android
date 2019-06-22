package it.uniroma2.dicii.sdcc.didatticamobile.activity.utility;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.inputmethod.InputMethodManager;

/*Allows the user to close the keyboard just touching out of it on the layout*/
public class KeyboardHider {

    public static void hideKeyboard(Context context, ConstraintLayout layout) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(layout.getWindowToken(), 0);
        }
    }

}
