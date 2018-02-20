package com.example.android.shakeandchat;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        Log.d(TAG, "onResume_Chats");
        ((HomeActivity) getActivity()).mToolbar.setTitle("Friends");
        ((HomeActivity) getActivity()).tabLayout.getTabAt(0).setIcon(R.drawable.icon_friends);
        ((HomeActivity) getActivity()).tabLayout.getTabAt(1).setIcon(R.drawable.icon_chats_w);
        ((HomeActivity) getActivity()).tabLayout.getTabAt(2).setIcon(R.drawable.icon_profile_w);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause_Chats");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart_Chats");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop_Chats");
    }
}
