package com.example.migrenus.flirtapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
    }
}
