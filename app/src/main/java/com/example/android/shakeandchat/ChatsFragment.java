package com.example.android.shakeandchat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by um on 02/20/18.
 */

public class ChatsFragment extends Fragment {
    private static final String TAG = "ChatsFragment";

    private ListView mListView;
    private GoogleSignInAccount account;

    ArrayList<ChatFriend> chatFriendList;
    ChatsAdapter chatsAdapter;
    public int chatCount;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chats_fragment, container, false);
        Log.d(TAG, "onCreateView_Chats");

        mListView = view.findViewById(R.id.chatsList);
        chatFriendList = new ArrayList<ChatFriend>();
        chatsAdapter = new ChatsAdapter(getActivity(), R.layout.chats_layout, chatFriendList);
        mListView.setAdapter(chatsAdapter);
        return view;
    }

    public void setChatList(final GoogleSignInAccount account) {
        this.account = account;
        Log.d(TAG, "In: setChatList");
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference("chatList");
        chatFriendList = new ArrayList<ChatFriend>();
        Log.d(TAG, "getReference");
        String edited_email = String.valueOf(account.getEmail());
        Log.d("UMUMUMUM", edited_email);
        edited_email = edited_email.replaceAll("\\.","");
        edited_email = edited_email.replaceAll("@","");
        Log.d("UMUMUMUM", edited_email);
        reference.child(edited_email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chatCount = (int) dataSnapshot.getChildrenCount();
                Log.d(TAG, String.valueOf(chatCount));

                for (DataSnapshot chatSnapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, String.valueOf(chatSnapshot));
                    String name = String.valueOf((chatSnapshot.child("name")).getValue());
                    boolean isOpen = Boolean.parseBoolean(String.valueOf((chatSnapshot.child("isOpen")).getValue()));
                    String photoURL = String.valueOf((chatSnapshot.child("photoURL")).getValue());
                    String lastMessage = String.valueOf((chatSnapshot.child("lastMessage")).getValue());
                    String timeStamp = String.valueOf((chatSnapshot.child("timeStamp")).getValue());
                    String email = String.valueOf(chatSnapshot.child("email").getValue());
                    chatFriendList.add(new ChatFriend(name, photoURL, timeStamp, lastMessage, isOpen, email));
                }

                Log.d(TAG + "__a", String.valueOf(chatFriendList.size()));
                Log.d(TAG + "__b", String.valueOf(chatCount));

                final FragmentActivity activity = getActivity();
                if (activity != null) {
                    chatsAdapter = new ChatsAdapter(activity, R.layout.chats_layout, chatFriendList);
                    mListView.setAdapter(chatsAdapter);
                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            ChatFriend item = chatsAdapter.getItem(i);
                            FriendUser friend = new FriendUser(item.getName(), item.getEmail(), item.getPhotoURL());
                            if (item != null) {
                                Log.d("TeSTing", String.valueOf(item));
                                Log.d("TeSTing", String.valueOf(item.getName()));
                                Intent intent = new Intent(activity, ChatActivity.class);
                                intent.putExtra("friendClicked", friend);
                                intent.putExtra("Account", account);
                                startActivity(intent);
                            } else {
                                Log.d("Testing", "null bro");
                            }
                        }
                    });
                    chatsAdapter.notifyDataSetChanged();
                }

                Log.d(TAG, "done");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
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

        ArrayList<ChatFriend> userList;
        private Context context;
        private int resource;
        private View view;
        private ChatFriend chatFriend;

        public ChatsAdapter(Context context, int resource, ArrayList<ChatFriend> chatFriends) {
            super(context, resource, chatFriends);
            this.context = context;
            this.resource = resource;
            this.userList = chatFriends;
        }

        @Nullable
        @Override
        public ChatFriend getItem(int position) {
            return super.getItem(position);
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(resource, viewGroup, false);

            ImageView chatsImage = view.findViewById(R.id.chatsImage);
            TextView chatsName = view.findViewById(R.id.chatsName);
            TextView chatsLastMessage = view.findViewById(R.id.chatsLastMessage);
            TextView chatsTime = view.findViewById(R.id.chatsTime);
            TextView newBadge = view.findViewById(R.id.new_badge);

            chatFriend = userList.get(i);

            chatsName.setText(chatFriend.name);
            chatsLastMessage.setText(chatFriend.lastMessage);

            String newDateString = null;

            String oldDateString = chatFriend.getTimeStamp();

            if (oldDateString.substring(oldDateString.length() - 1).equals("M")) {
                final String OLD_FORMAT = "MMM dd, yyyy hh:mm:ss aaa";
                final String NEW_FORMAT = "HH:mm";


                SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
                Date d = null;
                try {
                    d = sdf.parse(oldDateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                sdf.applyPattern(NEW_FORMAT);
                newDateString = sdf.format(d);
            } else {
                final String OLD_FORMAT = "MMM dd, yyyy HH:mm:ss";
                final String NEW_FORMAT = "HH:mm";


                SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
                Date d = null;
                try {
                    d = sdf.parse(oldDateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                sdf.applyPattern(NEW_FORMAT);
                newDateString = sdf.format(d);

            }

            chatsTime.setText(newDateString);


            if (chatFriend.isOpen()) {
                newBadge.setVisibility(View.GONE);
            } else {
                newBadge.setVisibility(View.VISIBLE);
            }
            if ((chatFriend.photoURL).equals("default")) {
                chatsImage.setImageResource(R.drawable.default_profile);
            } else {
                int radius = 30;
                int margin = 0;
                Glide.with(context).load(chatFriend.photoURL)
                        .thumbnail(0.5f)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .bitmapTransform(new RoundedCornersTransformation(context, radius, margin))
                        .into(chatsImage);
            }

            return view;
        }
    }
}