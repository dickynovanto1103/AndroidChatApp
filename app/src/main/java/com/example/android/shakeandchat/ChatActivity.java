package com.example.android.shakeandchat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    private EditText messageET;
    private ListView messagesContainer;
    private ImageButton sendBtn;
    private ChatAdapter adapter;
    private ArrayList<ChatMessage> chatHistory;
    private GoogleSignInAccount account;
    private String senderID;
    private String destID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        FriendUser friendUser = (FriendUser) getIntent().getSerializableExtra("friendClicked");
        account = (GoogleSignInAccount) getIntent().getParcelableExtra("Account");

        senderID = account.getEmail();
        destID = friendUser.getEmail();

        String chat_id;
        if (account.getEmail().compareTo(friendUser.getEmail()) < 0) {
            chat_id = account.getEmail() + "-" + friendUser.getEmail();
        } else {
            chat_id = friendUser.getEmail() + "-" + account.getEmail() ;
        }

        if (friendUser != null) {
            setTitle(friendUser.getName());
        } else {
            setTitle("NULL");
        }
        initControls();

        chatHistory = new ArrayList<ChatMessage>();
        adapter = new ChatAdapter(ChatActivity.this, new ArrayList<ChatMessage>(), senderID);
        messagesContainer.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initControls() {
        messagesContainer = (ListView) findViewById(R.id.messagesContainer);
        messageET = (EditText) findViewById(R.id.messageEdit);
        sendBtn = (ImageButton) findViewById(R.id.chatSendButton);

        RelativeLayout container = (RelativeLayout) findViewById(R.id.chatcontainer);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageText = messageET.getText().toString();
                if (messageText.isEmpty()) {
                    return;
                }

                ChatMessage chatMessage = new ChatMessage();

                chatMessage.setSenderID(senderID);
                chatMessage.setMessage(messageText);
                chatMessage.setDateTime(DateFormat.getDateTimeInstance().format(new Date()));
                chatMessage.setDestID(destID);
                chatMessage.setType("text");

                messageET.setText("");
                displayMessage(chatMessage);
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
