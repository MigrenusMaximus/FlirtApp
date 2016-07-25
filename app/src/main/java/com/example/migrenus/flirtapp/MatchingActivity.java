package com.example.migrenus.flirtapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MatchingActivity extends AppCompatActivity {
    private static String classLogTag = "MatchingActivity";

    private User mUser;
    private List<User> mMatchedUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching);

        mUser = (User) getIntent().getSerializableExtra("user");
        mMatchedUsers = new ArrayList<>();

        final TextView matchingText = (TextView) findViewById(R.id.currentlyMatchingText);
        TextView locationNameText = (TextView) findViewById(R.id.locationNameText);
        locationNameText.setText(((Place) getIntent().getSerializableExtra("place_info")).getName());

        final Button viewMatchesButton = (Button) findViewById(R.id.viewMatchesButton);

        viewMatchesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(MatchingActivity.this, MatchedUsersActivity.class);
                newIntent.putExtra("place_info", getIntent().getSerializableExtra("place_info"));
                newIntent.putExtra("user", mUser);
                startActivity(newIntent);
            }
        });

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(5 * 1000); // wait 5 seconds
                } catch (InterruptedException e) {
                    Log.e(classLogTag, "Interrupted during matching: " + e.getMessage());
                    return null;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                mMatchedUsers = mUser.getMatches();
                if (mMatchedUsers.isEmpty()) {
                    matchingText.setText(getString(R.string.no_matches_text));
                } else {
                    matchingText.setVisibility(View.INVISIBLE);
                    viewMatchesButton.setEnabled(true);
                    viewMatchesButton.setVisibility(View.VISIBLE);
                }
            }
        }.execute();
    }
}
