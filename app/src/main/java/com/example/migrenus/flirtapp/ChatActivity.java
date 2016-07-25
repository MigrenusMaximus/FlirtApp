package com.example.migrenus.flirtapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;

public class ChatActivity extends AppCompatActivity {
    private static String classLogTag = "ChatActivity";

    private User mMatchedUser;
    private Bitmap mMatchedUserAvatar;
    private TextView mMessageText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mMatchedUser = (User) getIntent().getSerializableExtra("matched_user");
        mMessageText = (TextView) findViewById(R.id.messageText);

        ListView chatOptionsListView = (ListView) findViewById(R.id.chatOptionsListView);
        chatOptionsListView.setAdapter(new ChatOptionAdapter(this, R.layout.chat_option_item_layout));

        chatOptionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sendMessage(position);
                Toast.makeText(getApplicationContext(), "Message sent", Toast.LENGTH_SHORT);
            }
        });

        final ImageView userAvatar = (ImageView) findViewById(R.id.userAvatar);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    InputStream in = new URL(mMatchedUser.getPhotoUri()).openStream();
                    mMatchedUserAvatar = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    Log.d(classLogTag, "Image loading failed: " + e.getMessage());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                if (mMatchedUserAvatar != null)
                    userAvatar.setImageBitmap(mMatchedUserAvatar);
            }
        }.execute();
    }
    // TODO: Implement message sending
    private AsyncTask<Integer, Void, Void> sendMessage(int position) {
        return new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... params) {
                int i = params[0];
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {

            }
        };
    }
    // TODO: Implement message receiving
    private AsyncTask<Void, Void, String> messageListener() {
        return new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                return "message";
            }

            @Override
            protected void onPostExecute(String result) {
                mMessageText.setText(result);
            }
        };
    }
}
