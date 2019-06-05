package it.uniroma2.dicii.sdcc.didatticamobile.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import it.uniroma2.dicii.sdcc.didatticamobile.R;
import it.uniroma2.dicii.sdcc.didatticamobile.task.ConfigurationTask;
import it.uniroma2.dicii.sdcc.didatticamobile.task.LoginTask;

public class MainActivity extends AppCompatActivity {

    private Button btnRegistration;
    private Button btnLogin;
    private EditText etUsername;
    private EditText etPassword;


    private class EventListener implements  View.OnClickListener {

        private Activity context;

        private EventListener(Activity context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnRegistrationMain:
                    goToRegistration();
                    break;
                case R.id.btnLoginMain:
                    doLogin();
                    break;
                default:
                    break;
            }
        }

        private void goToRegistration() {
            /* Open Registration activity*/
            Intent intent = new Intent(context, RegistrationActivity.class);
            context.startActivity(intent);
        }

        private void doLogin(){
            /* Check the correctness of the given credential from a format point of view*/
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();
            if (!checkParameter(username, password)){
                return;
            }
            /* Launch the task that makes the login request to the server*/
            LoginTask loginTask = new LoginTask(context);
            loginTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, username, password);


        }

        private Boolean checkParameter(String username, String password){
            if (username.trim().equals("") || password.trim().equals("")){
                Toast.makeText(this.context, R.string.toastNotInsertedField_text, Toast.LENGTH_SHORT).show();
                return false;
            }

            if (!username.equals(username.replace(" ","")) ||
                    !password.equals(password.replace(" ",""))){
                Toast.makeText(this.context, R.string.toastNotSpacesAllowed_text, Toast.LENGTH_SHORT).show();
                return false;
            }

            return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeWidgets();
        loadConfiguration();
    }

    private void loadConfiguration() {
        /* Launch the task that reads the configuration parameters from a property file and stores
         * them into an AppConfiguration object (singleton)*/
        ConfigurationTask configurationTask = new ConfigurationTask(this);
        configurationTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    private void initializeWidgets() {
        etUsername = findViewById(R.id.etUsernameMain);
        etPassword = findViewById(R.id.etPasswordMain);
        btnRegistration = findViewById(R.id.btnRegistrationMain);
        btnLogin = findViewById(R.id.btnLoginMain);
        EventListener eventListener = new EventListener(this);
        btnRegistration.setOnClickListener(eventListener);
        btnLogin.setOnClickListener(eventListener);

    }
}
