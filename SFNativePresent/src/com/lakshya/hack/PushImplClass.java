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
        Log.d("Dataaaaa",message.toString());
        //String DoctorName = message.get("Doctor").toString();
        //String Date = getFormattedDate(message.get("Date").toString());
        //String Reason = message.get("Reason").toString();

        String notificationText = message.get("Message").toString();

        Notification.Builder builder = new Notification.Builder(context)
                // Set Icon
                .setSmallIcon(R.drawable.ic_launcher)
                .setTicker("Appointment Has Been Booked")
                .setContentTitle("Appointment Booked")
                .setStyle(new Notification.BigTextStyle().bigText(notificationText))
                .setContentText(notificationText)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setPriority(Notification.PRIORITY_MAX)
                .setAutoCancel(true);

        NotificationManager notificationmanager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationmanager.notify(0, builder.build());





    }

    public String getFormattedDate(String date){
        Date d=null;
        String returnDate = null;
        SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        SimpleDateFormat sdf1= new SimpleDateFormat("dd MMM hh:mm a");
        try {
            d= sdf.parse(date);
            returnDate  =sdf1.format(d);
        } catch (ParseException e) {

            e.printStackTrace();
        }
        return returnDate;
    }



}
