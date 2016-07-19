package com.example.migrenus.flirtapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;

public class ViewUsersActivity extends AppCompatActivity {
    private List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_users);
    }
}
