package com.example.migrenus.flirtapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by Migrenus on 7/19/2016.
 *
 * Here users select whether they want to view
 * men or women who've logged in at the same
 * establishment, or if they just want to see
 * a list of people who've logged in since their
 * last visit.
 */
public class ViewSelectionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_selection);

        Button viewMenButton      = (Button)   findViewById(R.id.viewMenButton);
        Button viewWomenButton    = (Button)   findViewById(R.id.viewWomenButton);
        Button viewNewUsersButton = (Button)   findViewById(R.id.viewNewUsersButton);
        TextView locationNameText = (TextView) findViewById(R.id.locationNameText);

        final Intent intent = new Intent(ViewSelectionActivity.this, ViewUsersActivity.class);
        intent.putExtras(getIntent());

        assert locationNameText != null;
        locationNameText.setText(((Place) intent.getSerializableExtra("place_info")).getName());

        assert viewMenButton != null;
        viewMenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("gender", Gender.Male);
                startActivity(intent);
            }
        });

        assert viewWomenButton != null;
        viewWomenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("gender", Gender.Female);
                startActivity(intent);
            }
        });

        assert viewNewUsersButton != null;
        viewNewUsersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("time", Calendar.getInstance().get(Calendar.SECOND));
                intent.putExtra("gender", Gender.None);
                startActivity(intent);
            }
        });
    }
}
