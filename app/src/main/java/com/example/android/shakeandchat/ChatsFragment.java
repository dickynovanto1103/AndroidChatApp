package com.example.android.shakeandchat;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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
 * Created by um on 02/20/18.
 */

public class ChatsFragment extends Fragment {
    private static final String TAG = "ChatsFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chats_fragment, container, false);
        Log.d(TAG, "onCreateView_Chats");
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "CF_onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "CF_onPause");

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "CF_onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "CF_onStop");
    }

    class ChatsAdapter extends ArrayAdapter<ChatFriend> {

//        ArrayList<FriendUser> userList;
//        private Context context;
//        private int resource;
//        private View view;
//        private FriendUser user;
//
//        public FriendsAdapter(Context context, int resource, ArrayList<FriendUser> user) {
//            super(context, resource, user);
//            this.context = context;
//            this.resource = resource;
//            this.userList = user;
//        }
//
//        @Nullable
//        @Override
//        public FriendUser getItem(int position) {
//            return super.getItem(position);
//        }
//
//        @Override
//        public View getView(int i, View view, ViewGroup viewGroup) {
//
//            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            view = inflater.inflate(resource, viewGroup, false);
//
//            ImageView imageFriends = view.findViewById(R.id.imageFriends);
//            TextView friendsName = view.findViewById(R.id.friendsName);
//            TextView friendsEmail = view.findViewById(R.id.friendsEmail);
//
//            user = userList.get(i);
//
//            friendsName.setText(user.name);
//            friendsEmail.setText(user.email);
//            if ((user.photoURL).equals("default")) {
//                imageFriends.setImageResource(R.drawable.default_profile);
//            } else {
//                int radius = 30;
//                int margin = 0;
//                Glide.with(context).load(user.photoURL)
//                        .thumbnail(0.5f)
//                        .crossFade()
//                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                        .bitmapTransform(new RoundedCornersTransformation(context, radius, margin))
//                        .into(imageFriends);
//            }
//
//            return view;
//        }
    }
}
