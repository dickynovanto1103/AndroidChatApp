package com.example.android.shakeandchat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private EditText messageET;
    private ListView messagesContainer;
    private ChatAdapter adapter;
    private ArrayList<ChatMessage> chatHistory;
    private GoogleSignInAccount sender;
    private FriendUser dest;
    private DatabaseReference mDBChatChannel;
    private DatabaseReference mDBChatFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        dest = (FriendUser) getIntent().getSerializableExtra("friendClicked");
        sender = (GoogleSignInAccount) getIntent().getParcelableExtra("Account");

        String decodedSenderID = decodeID(sender.getEmail());
        String decodedDestID = decodeID(dest.getEmail());

        String chat_id;
        if (decodedSenderID.compareTo(decodedDestID) < 0) {
            chat_id = decodedSenderID + "-" + decodedDestID;
        } else {
            chat_id = decodedDestID + "-" + decodedSenderID;
        }

        if (dest != null) {
            setTitle(dest.getName());
        } else {
            setTitle("NULL");
        }

        setupDB(chat_id);
        initControls();

        chatHistory = new ArrayList<ChatMessage>();
        adapter = new ChatAdapter(ChatActivity.this, new ArrayList<ChatMessage>(), sender.getEmail());
        messagesContainer.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void setupDB(String chat_id){

        mDBChatChannel = FirebaseDatabase.getInstance().getReference("chat_channel/" + chat_id);
        mDBChatChannel.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ChatMessage addedChat = dataSnapshot.getValue(ChatMessage.class);
                chatHistory.add(addedChat);
                displayMessage(addedChat);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Error", "DB chat_channel connection error" , databaseError.toException());
            }
        });

        mDBChatFriend = FirebaseDatabase.getInstance().getReference("chatList");
        markAsRead(2);
    }

    private void markAsRead(int pos){
        Log.d("MARK AS READ FROM:", String.valueOf(pos));
        Map<String, Object> hasOpened = new HashMap<String, Object>();
        hasOpened.put("isOpen", true);
        mDBChatFriend.child(decodeID(sender.getEmail())).child(decodeID(dest.getEmail())).updateChildren(hasOpened);
    }

    private String decodeID(String id){
        String result = "";
        for(int i = 0; i < id.length(); i++){
            if(id.charAt(i) != '@' && id.charAt(i) != '.' && id.charAt(i) != '-')
                result += id.charAt(i);
        }
        return result;
    }

    private void initControls() {
        messagesContainer = (ListView) findViewById(R.id.messagesContainer);
        messageET = (EditText) findViewById(R.id.messageEdit);
        final ImageButton sendBtn = (ImageButton) findViewById(R.id.chatSendButton);

        RelativeLayout container = (RelativeLayout) findViewById(R.id.chatcontainer);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageText = messageET.getText().toString();
                if (messageText.isEmpty()) {
                    return;
                }

                ChatMessage chatMessage = new ChatMessage();
                String timeNow = DateFormat.getDateTimeInstance().format(new Date());

                chatMessage.setSenderID(sender.getEmail());
                chatMessage.setMessage(messageText);
                chatMessage.setDateTime(timeNow);
                chatMessage.setDestID(dest.getEmail());
                chatMessage.setType("text");

                messageET.setText("");
                mDBChatChannel.push().setValue(chatMessage);

                ChatFriend senderChatFriend = new ChatFriend(dest.getName(), dest.getPhotoURL(), timeNow, messageText, true, dest.getEmail());
                mDBChatFriend.child(decodeID(sender.getEmail())).child(decodeID(dest.getEmail())).setValue(senderChatFriend);

                ChatFriend destChatFriend = new ChatFriend(sender.getDisplayName(), sender.getPhotoUrl().toString(), timeNow, messageText, false, sender.getEmail());
                mDBChatFriend.child(decodeID(dest.getEmail())).child(decodeID(sender.getEmail())).setValue(destChatFriend);
            }
        });
    }

    private void displayMessage(ChatMessage message) {
        adapter.add(message);
        adapter.notifyDataSetChanged();
        scroll();
    }

    private void scroll() {
        messagesContainer.setSelection(messagesContainer.getCount() - 1);
    }
}
