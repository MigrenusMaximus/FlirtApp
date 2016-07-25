package com.example.migrenus.flirtapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.List;

public class MatchedUsersActivity extends AppCompatActivity {
    private List<User> mMatchedUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matched_users);

        mMatchedUsers = ((User) getIntent().getSerializableExtra("user")).getMatches();

        GridView usersGridView = (GridView) findViewById(R.id.usersGridView);
        usersGridView.setAdapter(new UsersGridViewAdapter(this, R.layout.users_grid_item_layout, mMatchedUsers));

        usersGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent newIntent = new Intent(MatchedUsersActivity.this, ChatActivity.class);
                newIntent.putExtras(getIntent());
                newIntent.putExtra("matched_user", mMatchedUsers.get(position));
                startActivity(newIntent);
            }
        });
    }
}
