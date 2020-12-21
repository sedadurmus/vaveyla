package com.sedadurmus.yenivavi.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.sedadurmus.yenivavi.MainActivity;
import com.sedadurmus.yenivavi.R;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FirebaseMessageTAG";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){
        Log.d(TAG,"Message data = "+remoteMessage.getFrom());
        if (remoteMessage.getData().size()>0){
            Log.d(TAG,"Message data = "+ remoteMessage.getData());
            sendNotification(remoteMessage.getNotification().getBody());
        }
        if (remoteMessage.getNotification() !=null){
            Log.d(TAG,"Message Notification Body: "+ remoteMessage.getNotification().getBody());
        }
    }

    private void sendNotification(String messageBody){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("VAVEYLA")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,notificationBuilder.build());
    }
}
