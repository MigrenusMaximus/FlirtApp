package com.example.migrenus.flirtapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Migrenus on 7/25/2016.
 */
public class ChatOptionAdapter extends BaseAdapter {
    private static String classLogTag = "ChatOptionAdapter";

    private Context mContext;
    private int mLayoutResourceId;
    private List<String> mChatOptions;

    public ChatOptionAdapter(Context context, int layoutResourceId) {
        mContext = context;
        mLayoutResourceId = layoutResourceId;
        mChatOptions = new ArrayList<>(Arrays.asList(mContext.getResources().getStringArray(R.array.chat_options)));
    }

    @Override
    public int getCount() {
        return mChatOptions.size();
    }

    @Override
    public Object getItem(int position) {
        return mChatOptions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mChatOptions.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View chatOptionsView;

        if (convertView == null) {
            chatOptionsView = layoutInflater.inflate(mLayoutResourceId, null);

            TextView messageText = (TextView) chatOptionsView.findViewById(R.id.chat_option_text);
            messageText.setText(mChatOptions.get(position));
        } else {
            chatOptionsView = convertView;
        }

        return chatOptionsView;
    }
}
