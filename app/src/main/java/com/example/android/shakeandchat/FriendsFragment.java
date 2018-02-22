package com.example.android.shakeandchat;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by um on 02/20/18.
 */

public class FriendsFragment extends Fragment {
    private static final String TAG = "FriendsFragment";

    private ListView mListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.friends_fragment, container, false);
        Log.d(TAG, "onCreateView_Friends");

        mListView = view.findViewById(R.id.listFriends);
        Log.d(TAG, "Test0");
        ArrayList<User> friendList = new ArrayList<User>();
        friendList.add(new User("Achmad Fahrurrozi Maskur", "fahrurrozi31@gmail.com", 1, 1));
        friendList.add(new User("Dicky Novanto", "dickynovanto1103@gmail.com", 2, 2));
        friendList.add(new User("Pratamamia Agung P.", "apratamamia@gmail.com", 3, 3));
        friendList.add(new User("It's dummy 1", "dummy1@gmail.com", 99, 99));
        friendList.add(new User("It's dummy 2", "dummy2@gmail.com", 99, 99));
        friendList.add(new User("It's dummy 3", "dummy3@gmail.com", 99, 99));
        friendList.add(new User("It's dummy 4", "dummy4@gmail.com", 99, 99));
        friendList.add(new User("It's dummy 5", "dummy5@gmail.com", 99, 99));
        friendList.add(new User("It's dummy 6", "dummy6@gmail.com", 99, 99));
        friendList.add(new User("It's dummy 7", "dummy7@gmail.com", 99, 99));
        Log.d(TAG, "Test1");
        FriendsAdapter friendsAdapter = new FriendsAdapter(getActivity(), R.layout.friends_layout, friendList);
        Log.d(TAG, "Test2");
        mListView.setAdapter(friendsAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume_Friends");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause_Friends");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart_Friends");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop_Friends");
    }

    class FriendsAdapter extends ArrayAdapter<User> {

        ArrayList<User> userList;
        private Context context;
        private int resource;
        private View view;
        private User user;

        public FriendsAdapter(Context context, int resource, ArrayList<User> user) {
            super(context, resource, user);
            this.context = context;
            this.resource = resource;
            this.userList = user;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(resource, viewGroup, false);

            ImageView imageFriends = view.findViewById(R.id.imageFriends);
            TextView friendsName = view.findViewById(R.id.friendsName);
            TextView friendsEmail = view.findViewById(R.id.friendsEmail);

            user = userList.get(i);

            imageFriends.setImageResource(R.drawable.ic_launcher_background);
            friendsName.setText(user.username);
            friendsEmail.setText(user.key);

            return view;
        }
    }


}
