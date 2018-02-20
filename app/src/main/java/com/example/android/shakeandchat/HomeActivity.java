package com.example.android.shakeandchat;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.support.v7.widget.Toolbar;


public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";

    private SectionsPageAdapter mSectionsPageAdapter;
    public Toolbar mToolbar;
    private ViewPager mViewPager;
    public TabLayout tabLayout;
    private int[] tabIcons_w = {
            R.drawable.icon_friends_w,
            R.drawable.icon_chats_w,
            R.drawable.icon_profile_w
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Log.d(TAG, "onCreate: Bismillah");

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        mToolbar = findViewById(R.id.toolbar);

        mViewPager = findViewById(R.id.container);
        setupViewPager(mViewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        setupTabIcons();

    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons_w[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons_w[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons_w[2]);
    }


    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new FriendsFragment(), "Friends");
        adapter.addFragment(new ChatsFragment(), "Chats");
        adapter.addFragment(new ProfileFragment(), "Profile");

        viewPager.setAdapter(adapter);
    }

}
