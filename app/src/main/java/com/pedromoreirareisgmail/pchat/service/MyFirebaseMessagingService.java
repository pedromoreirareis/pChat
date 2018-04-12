package com.pedromoreirareisgmail.pchat.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;
import com.pedromoreirareisgmail.pchat.R;
import com.pedromoreirareisgmail.pchat.utils.Const;

import java.util.Objects;

public class MyFirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

     /*  Se ativar aqui deve ativar o notification no index

        RemoteMessage.Notification notification = Objects.requireNonNull(remoteMessage.getNotification());

        String titulo = notification.getTitle();
        String mensagem = notification.getBody();
        String click_action = notification.getClickAction();  */

        String titulo = remoteMessage.getData().get("title");
        String mensagem = remoteMessage.getData().get("body");
        String click_action = remoteMessage.getData().get("click_action");
        String from_userName = remoteMessage.getData().get("from_userName");
        String from_user_id = remoteMessage.getData().get("from_user_id");

        Uri notifSom = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(titulo)
                        .setContentText(mensagem)
                        .setSound(notifSom)
                        .setAutoCancel(true);

        Intent resultIntent = new Intent(click_action);
        resultIntent.putExtra(Const.INTENT_ID_OUTRO_USUARIO, from_user_id);
        resultIntent.putExtra(Const.INTENT_NOME_OUTRO_USUARIO, from_userName);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);

        int mNotificationId = (int) System.currentTimeMillis();

        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


        Objects.requireNonNull(mNotifyMgr).notify(mNotificationId, mBuilder.build());
    }
}