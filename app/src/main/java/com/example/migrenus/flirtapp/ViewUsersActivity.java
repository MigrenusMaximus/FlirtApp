package com.example.migrenus.flirtapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ViewUsersActivity extends AppCompatActivity {
    private List<User> users;
    private Place place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_users);

        users = new ArrayList<>();
        for(int i = 0; i < 16; i++) {
            users.add(new User());
        }

        place = new Place("Some club", "Type", null, users);

        GridView usersGridView = (GridView) findViewById(R.id.usersGridView);

        usersGridView.setAdapter(new UsersGridViewAdapter(this, R.layout.users_grid_item_layout, place.getActiveUsers()));

        TextView text = (TextView) findViewById(R.id.locationNameText);
        text.setText(place.getName());
    }
}
