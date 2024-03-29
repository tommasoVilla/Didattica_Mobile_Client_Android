package it.uniroma2.dicii.sdcc.didatticamobile.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import it.uniroma2.dicii.sdcc.didatticamobile.R;
import it.uniroma2.dicii.sdcc.didatticamobile.activity.utility.KeyboardHider;
import it.uniroma2.dicii.sdcc.didatticamobile.task.RegistrationTask;

public class RegistrationActivity extends AppCompatActivity {
    
    private EditText etName;
    private EditText etSurname;
    private EditText etFiscalCode;
    private EditText etUsername;
    private EditText etPassword;
    private EditText etMail;
    private ConstraintLayout layout;

    private class EventListener implements View.OnClickListener{

        private Activity context;

        EventListener(Activity context) { this.context = context; }

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.btnRegistrationRegistration:
                    doRegistration();
                    break;
                case R.id.registrationLayout:
                    KeyboardHider.hideKeyboard(context, layout);
                    break;
                default:
                    break;
            }
            
        }

        private void doRegistration() {
            String name = etName.getText().toString();
            String surname = etSurname.getText().toString();
            String fiscalCode = etFiscalCode.getText().toString();
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();
            String mail = etMail.getText().toString();

            /* Check the correctness of the given parameters from a format point of view */
            Boolean isCorrectInput = checkParameter(name, surname, fiscalCode, username, password, mail);
            if (!isCorrectInput){
                return;
            }
            /* Launch the task that makes the user registration request to the server*/
            RegistrationTask registrationTask = new RegistrationTask(context);
            registrationTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, name, surname, username, password, fiscalCode, mail);


        }

        private Boolean checkParameter(String name, String surname, String fiscalCode,
                                       String username, String password, String mail) {

            if (surname.trim().equals("") || fiscalCode.trim().equals("") ||
                    username.trim().equals("") || password.trim().equals("")){
                Toast.makeText(this.context, R.string.toastNotInsertedField_text, Toast.LENGTH_SHORT).show();
                return false;
            }

            if (!name.equals(name.replace(" ","")) ||
                    !surname.equals(surname.replace(" ","")) ||
                    !fiscalCode.equals(fiscalCode.replace(" ","")) ||
                    !username.equals(username.replace(" ","")) ||
                    !password.equals(password.replace(" ",""))){
                Toast.makeText(this.context, R.string.toastNotSpacesAllowed_text, Toast.LENGTH_SHORT).show();
                return false;
            }

            if (!surname.matches("[a-zA-Z]+")|| !name.matches("[a-zA-Z]+")){
                Toast.makeText(this.context, R.string.toastNameNotValidRegistration_text, Toast.LENGTH_SHORT).show();
                return false;
            }

            if(!android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches())
            {
                Toast.makeText(this.context, R.string.InsertValidMail_message, Toast.LENGTH_SHORT).show();
                return false;
            }

            return true;
        }
    }
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initializeWidget();
    }

    private void initializeWidget() {
    
        etName = findViewById(R.id.etNameRegistration);
        etSurname = findViewById(R.id.etSurnameRegistration);
        etFiscalCode = findViewById(R.id.etFiscalCodeRegistration);
        etUsername = findViewById(R.id.etUsernameRegistration);
        etPassword = findViewById(R.id.etPasswordRegistration);
        etMail = findViewById(R.id.etMailRegistration);
        Button btnRegistration = findViewById(R.id.btnRegistrationRegistration);
        layout = findViewById(R.id.registrationLayout);
        
        EventListener eventListener = new EventListener(this);
        btnRegistration.setOnClickListener(eventListener);
        layout.setOnClickListener(eventListener);
    }


}
