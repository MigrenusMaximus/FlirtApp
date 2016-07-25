package com.example.migrenus.flirtapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Migrenus on 7/19/2016.
 *
 * Used for loading, filling and inflating usersGridView
 */
public class UsersGridViewAdapter extends BaseAdapter {
    private String classLogTag = "UsersGridViewAdapter";

    private Context context;
    private int layoutResourceId;
    private List<User> users = new ArrayList<>();

    // AsyncTask requires a final variable if declared within
    // a method, or a class member
    private Bitmap currImage;

    public UsersGridViewAdapter(Context context, int layoutResourceId, List<User> users) {
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.users = users;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return users.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View matchGridView;

        if (convertView == null) {
            matchGridView = layoutInflater.inflate(layoutResourceId, null);

            // both are declared as final because
            // they are used in the AsyncTask
            final ImageView imageView = (ImageView) matchGridView.findViewById(R.id.match_grid_item_image);
            final User currUser = users.get(position);

            // load the image from the uri in the background
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        InputStream in = new URL(currUser.getPhotoUri()).openStream();
                        currImage = BitmapFactory.decodeStream(in);
                    } catch (Exception e) {
                        Log.d(classLogTag, "Image loading failed: " + e.getMessage());
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void result) {
                    if (currImage != null)
                        imageView.setImageBitmap(currImage);
                }
            }.execute();
        } else {
            matchGridView = convertView;
        }

        return matchGridView;
    }
}
