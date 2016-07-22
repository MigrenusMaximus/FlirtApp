package com.example.migrenus.flirtapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ViewUsersActivity extends AppCompatActivity {
    private User mUser;
    private List<User> mUsers;
    private List<User> mLikedUsers;
    private ColorFilter mSelectionHighlight = new PorterDuffColorFilter(Color.YELLOW, PorterDuff.Mode.OVERLAY);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_users);

        Intent intent = getIntent();
        Place place = (Place) intent.getSerializableExtra("place_info");
        place.retrieveUsersForLocation();

        mLikedUsers = new ArrayList<>();

        mUser = (User) intent.getSerializableExtra("user");
        if (mUser == null)
            throw new RuntimeException("User is null.");

        if (intent.getSerializableExtra("gender") == Gender.None) {
            mUsers = place.filterUsers(intent.getIntExtra("time", Calendar.getInstance().get(Calendar.SECOND)));
        } else {
            mUsers = place.filterUsers((Gender) intent.getSerializableExtra("gender"));
        }

        GridView usersGridView = (GridView) findViewById(R.id.usersGridView);
        usersGridView.setAdapter(new UsersGridViewAdapter(this, R.layout.users_grid_item_layout, mUsers));

        TextView text = (TextView) findViewById(R.id.locationNameText);
        text.setText(place.getName());

        final Button matchButton = (Button) findViewById(R.id.matchButton);

        usersGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageView imageView = (ImageView) view.findViewById(R.id.match_grid_item_image);
                if (imageView == null)
                    throw new RuntimeException("ImageView is null.");

                if (imageView.getColorFilter() == mSelectionHighlight) {
                    imageView.clearColorFilter();
                    if (!mLikedUsers.contains(mUsers.get(position)))
                        mLikedUsers.add(mUsers.get(position));
                } else {
                    imageView.setColorFilter(mSelectionHighlight);
                    if (mLikedUsers.contains(mUsers.get(position)))
                        mLikedUsers.remove(mUsers.get(position));
                }
            }
        });

        matchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUser.setLikedUsers(mLikedUsers);

                Intent newIntent = new Intent(ViewUsersActivity.this, ViewSelectionActivity.class);
                newIntent.putExtras(getIntent());
                newIntent.putExtra("user", mUser);
                startActivity(newIntent);
            }
        });
    }
}
