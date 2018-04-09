package com.example.user.mana_livechatv2.gcm;

/**
 * Created by user on 15/07/2016.
 */
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.user.mana_livechatv2.helper.Db_ChatRoom;
import com.example.user.mana_livechatv2.model.ChatRoom;
import com.google.android.gms.gcm.GcmListenerService;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.user.mana_livechatv2.ChatRoomActivity;
import com.example.user.mana_livechatv2.MainActivity;
import com.example.user.mana_livechatv2.app.Config;
import com.example.user.mana_livechatv2.app.MyApplication;
import com.example.user.mana_livechatv2.model.Message;
import com.example.user.mana_livechatv2.model.User;

public class MyGcmPushReceiver extends GcmListenerService {

    private static final String TAG = MyGcmPushReceiver.class.getSimpleName();

    private NotificationUtils notificationUtils;

    private Db_ChatRoom db_chatRoom = new Db_ChatRoom(this);

    /**
     * Called when message is received.
     *
     * @param from   SenderID of the sender.
     * @param bundle Data bundle containing message data as key/value pairs.
     *               For Set of keys use data.keySet().
     */

    @Override
    public void onMessageReceived(String from, Bundle bundle) {
        String title = bundle.getString("title");

//        Log.d("debug_gcmpushreceiver", from);
        Boolean isBackground = Boolean.valueOf(bundle.getString("is_background"));
        String flag = bundle.getString("flag");
        String data = bundle.getString("data");
//        Log.d(TAG, "From: " + from);
//        Log.d(TAG, "title: " + title);
//        Log.d(TAG, "isBackground: " + isBackground);
//        Log.d(TAG, "flag: " + flag);
//        Log.d(TAG, "data: " + data);

        if (flag == null)
            return;

        if(MyApplication.getInstance().getPrefManager().getUser() == null){
            // user is not logged in, skipping push notification
            Log.e(TAG, "user is not logged in, skipping push notification");
            return;
        }

        switch (Integer.parseInt(flag)) {
            case Config.PUSH_TYPE_CHATROOM:
                // push notification belongs to a chat room
                processChatRoomPush(title, isBackground, data);
                break;
            case Config.PUSH_TYPE_USER:
                // push notification is specific to user
                processUserMessage(title, isBackground, data);
                break;
        }
    }

    private void processChatRoomPush(String title, boolean isBackground, String data) {
        Log.d("debug", "masuk processchatroompush");
        if (!isBackground) {
            Log.d("debug_crpush", "isbackground false");
            try {
                JSONObject datObj = new JSONObject(data);

                String chatRoomId = datObj.getString("chat_room_id");

                JSONObject mObj = datObj.getJSONObject("message");
                Message message = new Message();
                message.setMessage(mObj.getString("message"));
                message.setId(mObj.getString("message_id"));
                message.setCreatedAt(mObj.getString("created_at"));
                String visibility = mObj.getString("visibility");

                JSONObject uObj = datObj.getJSONObject("user");
                User usr = new User();
                usr.setId(uObj.getString("user_id"));
                usr.setEmail(uObj.getString("email"));
                usr.setName(uObj.getString("name"));
                usr.setFullname(uObj.getString("fullname"));
                message.setUser(usr);

                if (visibility.equals("t")) {
                    // skip the message if the message belongs to same user as
                    // the user would be having the same message when he was sending
                    // but it might differs in your scenario
                    if (uObj.getString("user_id").equals(MyApplication.getInstance().getPrefManager().getUser().getId())) {
                        Log.e(TAG, "Skipping the push message as it belongs to same user");
                        return;
                    }

                    // verifying whether the app is in background or foreground
                    if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                        Log.d("debug_crpush", "app is in foreground");

                        String currentChatRoom = MyApplication.getInstance().getPrefManager().getCurrentChatRoom();
                        ChatRoom updatedRoom = db_chatRoom.getChatRoom(Integer.parseInt(chatRoomId));
                        //Ini jika aplikasi lagi jalan, tapi sedang di chat room tertentu, dan ingin update chat room lain
                        Log.d("debug_crpush", "Current chat room : " + currentChatRoom);
//                        Log.d("debug_crpush", "Currently want to updated : " + updatedRoom.getName() + "ID : " + updatedRoom.getId());

                        if (updatedRoom != null && !updatedRoom.getId().equals(currentChatRoom)){
                            Log.d("debug_crpush", "Other chatroom updated : " + updatedRoom.getName());
                            updatedRoom.setLastMessage(message.getMessage());
                            updatedRoom.setUnreadCount(updatedRoom.getUnreadCount()+1);
                            db_chatRoom.updateChatRoom(Integer.toString(updatedRoom.getUnreadCount()), updatedRoom.getLastMessage(), chatRoomId);
                            Log.d("debug_crpush", "chatRoom updated, ID : " + updatedRoom.getId());
                        }
                        // app is in foreground, broadcast the push message
                        Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                        pushNotification.putExtra("type", Config.PUSH_TYPE_CHATROOM);
                        pushNotification.putExtra("message", message);
                        pushNotification.putExtra("chat_room_id", chatRoomId);
                        LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);


                    } else {
                        Log.d("debug_crpush", "app is in background");
                        String currentChatRoom = MyApplication.getInstance().getPrefManager().getCurrentChatRoom();
                        ChatRoom updatedRoom = db_chatRoom.getChatRoom(Integer.parseInt(chatRoomId));
                        Log.d("debug_crpush", "Current chat room : " + currentChatRoom);
                        Log.d("debug_crpush", "Currently want to updated : " + updatedRoom.getName() + "ID : " + updatedRoom.getId());

                        //Ini jika aplikasi lagi jalan, tapi sedang di chat room tertentu, dan ingin update chat room lain
                        if (updatedRoom != null && !updatedRoom.getId().equals(currentChatRoom)){
//                            Log.d("debug_cr", updatedRoom.getName());
                            updatedRoom.setLastMessage(message.getMessage());
                            updatedRoom.setUnreadCount(updatedRoom.getUnreadCount()+1);
                            db_chatRoom.updateChatRoom(Integer.toString(updatedRoom.getUnreadCount()), updatedRoom.getLastMessage(), chatRoomId);
                            Log.d("debug_crpush", "chatRoom updated, ID : " + updatedRoom.getId());
                        }
//                        Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
//                        pushNotification.putExtra("type", Config.PUSH_TYPE_CHATROOM);
//                        pushNotification.putExtra("message", message);
//                        pushNotification.putExtra("chat_room_id", chatRoomId);
//                        LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);


                        // play notification sound
                        NotificationUtils notificationUtils = new NotificationUtils();
                        notificationUtils.playNotificationSound();
                        // app is in background. show the message in notification try
                        Intent resultIntent = new Intent(getApplicationContext(), ChatRoomActivity.class);
                        resultIntent.putExtra("chat_room_id", chatRoomId);
                        showNotificationMessage(getApplicationContext(), title, usr.getName() + " : " + message.getMessage(), message.getCreatedAt(), resultIntent);
                    }
                }
                else {
                    Log.d("debug_gcmpush", "trigger accepted :" + message.getMessage());
                    String[] themessage = message.getMessage().split("/");
                    String user_from = message.getUser().getFullname();
                    User user = MyApplication.getInstance().getPrefManager().getUser();
                    if (themessage[1].equals(user.getName())) {
                        //Insert chat room yang baru
                        ChatRoom cr = new ChatRoom();
//                        cr.setName(message.getUser().getName());
                        cr.setName(user_from);
                        cr.setId(chatRoomId);
                        cr.setLastMessage("");
                        cr.setUnreadCount(0);
                        cr.setTimestamp("");
                        if (db_chatRoom.getChatRoom(Integer.parseInt(cr.getId())) == null) {
                            Log.d("DEBUG", "sudah ada di database");
                            db_chatRoom.insertChatRoom(cr);
                        }
                        else {
                            Log.d("DEBUG", "sudah ada di database");
                        }
                        Intent intent = new Intent(this, GcmIntentService.class);
                        intent.putExtra(GcmIntentService.KEY, GcmIntentService.SUBSCRIBE);
                        intent.putExtra(GcmIntentService.TOPIC, "topic_" + chatRoomId);
                        startService(intent);
                        Log.d("debug_gcmpush", "new room, ID : " + cr.getId());
                    }
                }
            } catch (JSONException e) {
                Log.e(TAG, "json parsing error: " + e.getMessage());
                Toast.makeText(getApplicationContext(), "Terjadi error, silahkan coba lagi", Toast.LENGTH_LONG).show();
            }

        } else {
            Log.d("debug_crpush", "isbackground true");
            // the push notification is silent, may be other operations needed
            // like inserting it in to SQLite
        }
    }

    private void processUserMessage(String title, boolean isBackground, String data) {
        if (!isBackground) {

            try {
                JSONObject datObj = new JSONObject(data);

                String imageUrl = datObj.getString("image");

                JSONObject mObj = datObj.getJSONObject("message");
                Message message = new Message();
                message.setMessage(mObj.getString("message"));
                message.setId(mObj.getString("message_id"));
                message.setCreatedAt(mObj.getString("created_at"));

                JSONObject uObj = datObj.getJSONObject("user");
                User user = new User();
                user.setId(uObj.getString("user_id"));
                user.setEmail(uObj.getString("email"));
                user.setName(uObj.getString("name"));
                message.setUser(user);

                // verifying whether the app is in background or foreground
                if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {

                    // app is in foreground, broadcast the push message
                    Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                    pushNotification.putExtra("type", Config.PUSH_TYPE_USER);
                    pushNotification.putExtra("message", message);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                    // play notification sound
                    NotificationUtils notificationUtils = new NotificationUtils();
                    notificationUtils.playNotificationSound();
                } else {

                    // app is in background. show the message in notification try
                    Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);

                    // check for push notification image attachment
                    if (TextUtils.isEmpty(imageUrl)) {
                        showNotificationMessage(getApplicationContext(), title, user.getName() + " : " + message.getMessage(), message.getCreatedAt(), resultIntent);
                    } else {
                        // push notification contains image
                        // show it with the image
                        showNotificationMessageWithBigImage(getApplicationContext(), title, message.getMessage(), message.getCreatedAt(), resultIntent, imageUrl);
                    }
                }
            } catch (JSONException e) {
                Log.e(TAG, "json parsing error: " + e.getMessage());
                Toast.makeText(getApplicationContext(), "Terjadi error, silahkan coba lagi", Toast.LENGTH_LONG).show();
            }

        } else {
            // the push notification is silent, may be other operations needed
            // like inserting it in to SQLite
        }
    }
    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }
}