import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.salesforce.androidsdk.push.PushNotificationInterface;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by govinda_jajoo on 21-Dec-16.
 */

public class NotificationPushImpl implements PushNotificationInterface {
    private Context context;
    public NotificationPushImpl(Context context){
        this.context = context;
    }
    @Override
    public void onPushMessageReceived(Bundle message) {
        Log.d("Dataaaaa",message.toString());
        /*Notification.Builder builder = new Notification.Builder(context)
                // Set Icon
                .setSmallIcon(android.R.drawable.btn_plus)
                // Set Ticker Message
              //  .setTicker(getString(R.string.notificationticker))
                // Set Title
                .setContentTitle(message.get("Name").toString())
                // Set Text
                .setContentText(message.get("Name").toString())
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
        */

    }
}
