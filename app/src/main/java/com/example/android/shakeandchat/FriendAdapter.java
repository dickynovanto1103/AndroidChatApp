package com.example.android.shakeandchat;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

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
        ImageView profilePict = (ImageView) rowView.findViewById(R.id.imageFriends);

        mName.setText(user.get(position).username);
        mEmail.setText(user.get(position).email);

        if (user.get(position).displayImage.equals("default")) {
            profilePict.setImageResource(R.drawable.default_profile);
        } else {
            int radius = 30;
            int margin = 0;
            Glide.with(context).load(user.get(position).displayImage)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .bitmapTransform(new RoundedCornersTransformation(getContext(), radius, margin))
                    .into(profilePict);
        }
        return rowView;
    }
}
