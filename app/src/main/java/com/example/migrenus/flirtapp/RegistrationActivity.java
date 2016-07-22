package com.example.migrenus.flirtapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

/**
 * Created by Migrenus on 7/18/2016.
 */
public class RegistrationActivity extends AppCompatActivity {
    private String classLogTag = "RegistrationActivity";
    // TODO: Add a way to identify users - IMEI has been approved

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        final RadioButton maleRadioButton   = (RadioButton) findViewById(R.id.maleRadioButton);
        final RadioButton femaleRadioButton = (RadioButton) findViewById(R.id.femaleRadioButton);
        assert maleRadioButton != null;
        assert femaleRadioButton != null;
        Button registerButton = (Button) findViewById(R.id.registerButton);

        final Intent intent = new Intent(RegistrationActivity.this, MapActivity.class);

        assert registerButton != null;
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (maleRadioButton.isChecked()) {
                    intent.putExtra("user", new User("male", Gender.Male));
                } else if (femaleRadioButton.isChecked()) {
                    intent.putExtra("user", new User("female", Gender.Female));
                } else {
                    Log.e(classLogTag, "Neither radio button checked.");
                    return;
                }

                startActivity(intent);
            }
        });
    }
}
