package com.example.migrenus.flirtapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ViewUsersActivity extends AppCompatActivity {
    private List<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_users);

        users = new ArrayList<>();
        for(int i = 0; i < 16; i++) {
            users.add(new User());
        }

        GridView usersGridView = (GridView) findViewById(R.id.usersGridView);

        usersGridView.setAdapter(new UsersGridViewAdapter(this, R.layout.users_grid_item_layout, users));

//        View inflatedView = getLayoutInflater().inflate(R.layout.users_grid_item_layout, null);
//        ImageView imageView = (ImageView)  inflatedView.findViewById(R.id.match_grid_item_image);
//        usersGridView.setColumnWidth(imageView.getWidth() + imageView.getPaddingRight() + imageView.getPaddingLeft());
//        TextView text = (TextView) findViewById(R.id.locationNameText);
//        text.setText(Integer.toString(usersGridView.getColumnWidth()))
    }
}
