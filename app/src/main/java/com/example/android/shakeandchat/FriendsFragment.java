package com.example.android.shakeandchat;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

/**
 * Created by um on 02/20/18.
 */

public class FriendsFragment extends Fragment {
    private static final String TAG = "FriendsFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.friends_fragment, container, false);
        Log.d(TAG, "onCreateView_Friends");

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume_Friends");
        ((HomeActivity) getActivity()).mToolbar.setTitle("Chats");
        ((HomeActivity) getActivity()).tabLayout.getTabAt(0).setIcon(R.drawable.icon_friends_w);
        ((HomeActivity) getActivity()).tabLayout.getTabAt(1).setIcon(R.drawable.icon_chats);
        ((HomeActivity) getActivity()).tabLayout.getTabAt(2).setIcon(R.drawable.icon_profile_w);
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
        ((HomeActivity) getActivity()).mToolbar.setTitle("Profile");
        ((HomeActivity) getActivity()).tabLayout.getTabAt(0).setIcon(R.drawable.icon_friends_w);
        ((HomeActivity) getActivity()).tabLayout.getTabAt(1).setIcon(R.drawable.icon_chats_w);
        ((HomeActivity) getActivity()).tabLayout.getTabAt(2).setIcon(R.drawable.icon_profile);
    }
}
