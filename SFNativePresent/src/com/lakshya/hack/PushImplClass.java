package com.lakshya.hack;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.util.Log;

import com.salesforce.androidsdk.push.PushNotificationInterface;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by govinda_jajoo on 23-Dec-16.
 */

public class PushImplClass implements PushNotificationInterface {
    private Context context;
    public PushImplClass(Context context){
        this.context = context;
    }
    @Override
    public void onPushMessageReceived(Bundle message) {
        String DoctorName = message.get("Doctor").toString();
        String Date = message.get("Date").toString();
        String Reason = message.get("Reason").toString();

        Date d=null;

        SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        SimpleDateFormat sdf1= new SimpleDateFormat("dd MMM hh:mm a");
        try {
            d= sdf.parse(Date);
            Date =sdf1.format(d);
        } catch (ParseException e) {

            e.printStackTrace();
        }
        String notificationText = "You appointment with \n"+DoctorName+" is scheduled at \n"+Date;

        Notification.Builder builder = new Notification.Builder(context)
                // Set Icon
                .setSmallIcon(R.drawable.sf__icon)
                // Set Ticker Message
                //  .setTicker(getString(R.string.notificationticker))
                // Set Title
                .setContentTitle("Appointment Booked")
                .setStyle(new Notification.BigTextStyle().bigText(notificationText))
                // Set Text
                .setContentText(notificationText)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))

                // Add an Action Button below Notification
                //.addAction(R.drawable.ic_launcher, "Action Button", pIntent)
                // Set PendingIntent into Notification
                // .setContentIntent(pIntent)
                // Dismiss Notification
                .setAutoCancel(true);

        // Create Notification Manager
        NotificationManager notificationmanager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        // Build Notification with Notification Manager
        notificationmanager.notify(0, builder.build());
        Log.d("Dataaaaa",message.toString());
    }
}
