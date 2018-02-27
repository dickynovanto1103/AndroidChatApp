package com.example.android.shakeandchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
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
    private DatabaseReference mDBListToken;
    private Uri filePath;
    private final int IMAGE_REQUEST = 71;
    private FirebaseStorage storage;
    private StorageReference storageReference;

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
                if(addedChat.getType() == "image"){
                    Intent intent = new Intent(ChatActivity.this, DownloadIntentService.class);
                    intent.putExtra("url", addedChat.getMessage());
                    ChatActivity.this.startService(intent);
                }


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
        mDBListToken = FirebaseDatabase.getInstance().getReference("listToken");
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

        ImageButton takePictureBtn = (ImageButton) findViewById(R.id.chatSendImage);
        Log.d("test", "halo");
        final ImageButton sendBtn = (ImageButton) findViewById(R.id.chatSendButton);

        RelativeLayout container = (RelativeLayout) findViewById(R.id.chatcontainer);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String messageText = messageET.getText().toString();
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

                mDBListToken.child(decodeID(dest.getEmail())).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        UserWithFirebaseToken user = dataSnapshot.getValue(UserWithFirebaseToken.class);
                        if(user != null){
                            Log.d("Destination Token", user.getFirebaseToken());
                            NotificationPost notif = new NotificationPost(sender.getDisplayName(), messageText, user.getFirebaseToken());
                            notif.execute();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        takePictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("test", "testingchoose0");
               chooseImage();
               Log.d("test", "testingchoose1");
            }
        });
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("halo", "halo1");
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("halo", "haloHalo");
        if(requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            Log.d("status", "berhasil cuy");
            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                storage = FirebaseStorage.getInstance();
                storageReference = storage.getReference();
                uploadImage();

            }catch(IOException e) {
                e.printStackTrace();
            }
        }else{
            Log.d("status", "gagal cuy");
        }
    }

    private void uploadImage() {
        if(filePath!=null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading files");
            Log.d("status", "uploading files");
//            progressDialog.show();

            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Log.d("status", "uploaded");
                            Uri url = taskSnapshot.getDownloadUrl();
                            Log.d("url", "url: "+url);
                            ChatMessage chatMessage = new ChatMessage();
                            String timeNow = DateFormat.getDateTimeInstance().format(new Date());

                            String messageText = url.toString();
                            chatMessage.setSenderID(sender.getEmail());
                            chatMessage.setMessage(messageText);
                            chatMessage.setDateTime(timeNow);
                            chatMessage.setDestID(dest.getEmail());
                            chatMessage.setType("image");

                            messageET.setText("");
                            mDBChatChannel.push().setValue(chatMessage);

                            ChatFriend senderChatFriend = new ChatFriend(dest.getName(), dest.getPhotoURL(), timeNow, messageText, true, dest.getEmail());
                            mDBChatFriend.child(decodeID(sender.getEmail())).child(decodeID(dest.getEmail())).setValue(senderChatFriend);

                            ChatFriend destChatFriend = new ChatFriend(sender.getDisplayName(), sender.getPhotoUrl().toString(), timeNow, messageText, false, sender.getEmail());
                            mDBChatFriend.child(decodeID(dest.getEmail())).child(decodeID(sender.getEmail())).setValue(destChatFriend);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("status", "failed");
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            Log.d("status", "sedang upload");
                        }
                    });
        }
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
