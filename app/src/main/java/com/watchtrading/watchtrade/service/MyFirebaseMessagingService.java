package com.watchtrading.watchtrade.service;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.watchtrading.watchtrade.Activities.ChattingActivity;
import com.watchtrading.watchtrade.Activities.MainActivity;
import com.watchtrading.watchtrade.Activities.SplashActivity;
import com.watchtrading.watchtrade.R;

import org.json.JSONObject;

import java.util.Iterator;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "DosAttack";
    //this method will receive message from the firebase which we will have to customize
    String reference_id = "";
    String key = "";

    @SuppressLint("LongLogTag")
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        //the message we receive from server we will show in the console with log
        Log.e(TAG, "from:" + remoteMessage);
        Log.e(TAG, "frombody:" + remoteMessage.getNotification());
        Log.e(TAG, "frombody:" + remoteMessage.getData());
        //Log.e(TAG,"Notification Message Body:"+remoteMessage.getNotification().getBody());

        try {
            JSONObject json = new JSONObject(remoteMessage.getData());
            Iterator itr = json.keys();
            while (itr.hasNext()) {
                String key = (String) itr.next();
                if (key.equals("key")) {
                    this.key = json.getString(key);
                }

                if (key.equals("reference_id")) {
                    reference_id = json.getString(key);
                }
                Log.d(TAG, "..." + key + " => " + json.getString(key));
            }
            Log.d(TAG, "..." + " => " + json.toString());

        } catch (Exception e) {
            Log.d("Exception", e + "");
        }

        if (!reference_id.equals("") || !key.equals("")) {
            broadcastFireBaseMessaging(key);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            sendNotificationForOreoAndAbove(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        } else {
            sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }


    }

    private void broadcastFireBaseMessaging(String string) {
        Intent intent = new Intent("data_action_firebase");
        intent.putExtra("firebase", "" + string);
        sendBroadcast(intent);
    }


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("TOKEN", s);
        // sendRegistrationToServer(token);
    }

    private void sendNotification(String title, String body) {
        // is service sa message hum orderdetail activity ma la ja rae han
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultsoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.app_icon)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultsoundUri)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, noBuilder.build());
    }

    private void sendNotificationForOreoAndAbove(String title, String body) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(0 + "", body, NotificationManager.IMPORTANCE_DEFAULT);
            // Configure the notification channel.
            notificationManager.createNotificationChannel(notificationChannel);
        }
        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.app_icon);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, 0 + "")
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.drawable.app_icon)
                .setLargeIcon(bitmap)
                .setContentTitle(title)
                .setContentText(body)
                .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        notificationManager.notify(0, builder.build());


    }


}
