package it.uniroma2.dicii.sdcc.didatticamobile.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import it.uniroma2.dicii.sdcc.didatticamobile.R;

public class RegistrationCompletedActivity extends AppCompatActivity {

    private class EventListener implements View.OnClickListener {

        private Context context;

        EventListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_completed);
        Button btnGoToLogin = findViewById(R.id.btnGoToLogin);
        EventListener eventListener = new EventListener(this);
        btnGoToLogin.setOnClickListener(eventListener);
    }
}
