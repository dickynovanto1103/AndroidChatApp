package com.example.android.shakeandchat;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Pratama Agung on 2/22/2018.
 */

public class FriendAdapter extends ArrayAdapter {
    private final Activity context;
    private ArrayList<ActiveUser> user;

    public FriendAdapter(Activity context, ArrayList<ActiveUser> user) {
        super(context, R.layout.friends_layout, user);
        this.context = context;
        this.user = user;
    }

    @Override
    public View getView(int position, View view, ViewGroup paremt){
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.friends_layout, null, true);

        TextView mName = (TextView) rowView.findViewById(R.id.friendsName);
        TextView mEmail = (TextView) rowView.findViewById(R.id.friendsEmail);

        mName.setText(user.get(position).username);

        return rowView;
    }
}
