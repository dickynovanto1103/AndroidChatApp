package com.example.android.shakeandchat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.koushikdutta.ion.Ion;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by um on 02/23/18.
 */

public class ChatAdapter extends BaseAdapter {

    private final List<ChatMessage> chatMessages;
    private Activity context;
    private String myID;

    public ChatAdapter(Activity context, List<ChatMessage> chatMessages, String myID) {
        this.context = context;
        this.chatMessages = chatMessages;
        this.myID = myID;
    }

    @Override
    public int getCount() {
        if (chatMessages != null) {
            return chatMessages.size();
        } else {
            return 0;
        }
    }

    @Override
    public ChatMessage getItem(int i) {
        if (chatMessages != null) {
            return chatMessages.get(i);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderImage holderImage = null;
        ViewHolderMessage holderMessage = null;
        ChatMessage chatMessage = getItem(position);

        LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (chatMessage.getType().equals("text")) {
            if (convertView == null) {
                convertView = vi.inflate(R.layout.list_item_chat, null);
                holderMessage = createViewHolderMessage(convertView);
                convertView.setTag(holderMessage);
            } else {
                if (chatMessage.getType().equals("text")) {
                    convertView = vi.inflate(R.layout.list_item_chat, null);
                    holderMessage = createViewHolderMessage(convertView);
                    convertView.setTag(holderMessage);
                }
            }

            boolean myMsg = chatMessage.getSenderID().equals(myID);//Just a dummy check
            //to simulate whether it me or other sender
            setAlignmentMessage(holderMessage, myMsg, false);
            holderMessage.txtMessage.setText(chatMessage.getMessage());
            holderMessage.txtInfo.setText(chatMessage.getDateTime());

            return convertView;
        } else {
            if (convertView == null) {
                convertView = vi.inflate(R.layout.list_item_image, null);
                holderImage = createViewHolderImage(convertView);
                convertView.setTag(holderImage);
            } else {
                if (chatMessage.getType().equals("image")) {
                    convertView = vi.inflate(R.layout.list_item_image, null);
                    holderImage = createViewHolderImage(convertView);
                    convertView.setTag(holderImage);
                }
            }

            boolean myMsg = chatMessage.getSenderID().equals(myID);//Just a dummy check
            //to simulate whether it me or other sender
            setAlignmentImage(holderImage, myMsg, true);
//            URL url = null;
//            try {
//                url = new URL(chatMessage.getMessage());
//                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//                holderImage.txtImage.setImageBitmap(bmp);
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            Ion.with(holderImage.txtImage)
                    .load(chatMessage.getMessage());
            holderImage.txtInfoImage.setText(chatMessage.getDateTime());

            return convertView;
        }
    }

    public void add(ChatMessage message) {
        chatMessages.add(message);
    }

    public void add(List<ChatMessage> messages) {
        chatMessages.addAll(messages);
    }

    private void setAlignmentMessage(ViewHolderMessage holder, boolean isMe, boolean isImage) {
        if (!isMe) {
            holder.contentWithBG.setBackgroundResource(R.drawable.out_message_bg);

            LinearLayout.LayoutParams layoutParams =
                    (LinearLayout.LayoutParams) holder.contentWithBG.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.contentWithBG.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams lp =
                    (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            holder.content.setLayoutParams(lp);

            layoutParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.txtMessage.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.txtInfo.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.txtInfo.setLayoutParams(layoutParams);
        } else {
            holder.contentWithBG.setBackgroundResource(R.drawable.in_message_bg);

            LinearLayout.LayoutParams layoutParams =
                    (LinearLayout.LayoutParams) holder.contentWithBG.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.contentWithBG.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams lp =
                    (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.content.setLayoutParams(lp);

            layoutParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.txtMessage.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.txtInfo.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.txtInfo.setLayoutParams(layoutParams);
        }
    }

    private void setAlignmentImage(ViewHolderImage holder, boolean isMe, boolean isImage) {
        if (!isMe) {
            holder.contentWithBGImage.setBackgroundResource(R.drawable.out_message_bg);

            LinearLayout.LayoutParams layoutParams =
                    (LinearLayout.LayoutParams) holder.contentWithBGImage.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.contentWithBGImage.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams lp =
                    (RelativeLayout.LayoutParams) holder.contentImage.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            holder.contentImage.setLayoutParams(lp);

            layoutParams = (LinearLayout.LayoutParams) holder.txtImage.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.txtImage.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.txtInfoImage.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.txtInfoImage.setLayoutParams(layoutParams);
        } else {
            holder.contentWithBGImage.setBackgroundResource(R.drawable.in_message_bg);

            LinearLayout.LayoutParams layoutParams =
                    (LinearLayout.LayoutParams) holder.contentWithBGImage.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.contentWithBGImage.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams lp =
                    (RelativeLayout.LayoutParams) holder.contentImage.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.contentImage.setLayoutParams(lp);

            layoutParams = (LinearLayout.LayoutParams) holder.txtImage.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.txtImage.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.txtInfoImage.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.txtInfoImage.setLayoutParams(layoutParams);
        }
    }


    private ViewHolderMessage createViewHolderMessage(View v) {
            ViewHolderMessage holder = new ViewHolderMessage();
            holder.txtMessage = (TextView) v.findViewById(R.id.txtMessage);
            holder.content = (LinearLayout) v.findViewById(R.id.content);
            holder.contentWithBG = (LinearLayout) v.findViewById(R.id.contentWithBackground);
            holder.txtInfo = (TextView) v.findViewById(R.id.txtInfo);
            return holder;
    }

    private ViewHolderImage createViewHolderImage(View v) {
        ViewHolderImage holder = new ViewHolderImage();
        holder.txtImage = (ImageView) v.findViewById(R.id.txtImage);
        holder.contentImage = (LinearLayout) v.findViewById(R.id.contentImage);
        holder.contentWithBGImage = (LinearLayout) v.findViewById(R.id.contentWithBackgroundImage);
        holder.txtInfoImage = (TextView) v.findViewById(R.id.txtInfoImage);
        return holder;
    }

    private static class ViewHolderMessage {
        public TextView txtMessage;
        public TextView txtInfo;
        public LinearLayout content;
        public LinearLayout contentWithBG;
    }

    private static class ViewHolderImage {
        public ImageView txtImage;
        public TextView txtInfoImage;
        public LinearLayout contentImage;
        public LinearLayout contentWithBGImage;
    }
}
