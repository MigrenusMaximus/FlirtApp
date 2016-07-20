package com.example.migrenus.flirtapp;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Migrenus on 7/19/2016.
 *
 * ...
 */
public class UsersGridViewAdapter extends BaseAdapter {
    private Context context;
    private int layoutResourceId;
    private List<User> users = new ArrayList<>();

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
            //matchGridView = new View(context);
            matchGridView = layoutInflater.inflate(layoutResourceId, null);

            ImageView imageView = (ImageView) matchGridView.findViewById(R.id.match_grid_item_image);
            Uri.Builder imageUriBuilder = new Uri.Builder();
            imageUriBuilder.path(users.get(position).getPhotoUri());
            imageView.setImageURI(imageUriBuilder.build());
            imageView.setImageResource(R.mipmap.ic_launcher);
        } else {
            matchGridView = convertView;
        }

        return matchGridView;
    }
}
