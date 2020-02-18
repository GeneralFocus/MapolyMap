package com.gaspercloud.gotozeal;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.gaspercloud.gotozeal.ui.actionProductActivity.SingleProductActivity;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;

import static com.gaspercloud.gotozeal.ui.actionProductActivity.SingleProductActivity.showNotifications;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = GeofenceBroadcastReceiver.class.getSimpleName();
    private NotificationManager systemService;
    Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            String errorMessage = getErrorString(geofencingEvent.getErrorCode());
            Log.e(TAG, errorMessage);
            return;
        }

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

            // Get the geofences that were triggered. A single event can trigger
            // multiple geofences.
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            // Get the transition details as a String.
            String geofenceTransitionDetails = getGeofenceTrasitionDetails(
                    geofenceTransition,
                    triggeringGeofences
            );

            // Send notification and log the transition details.
            sendNotification(geofenceTransitionDetails, "Destination Reached", "");
            Log.i(TAG, geofenceTransitionDetails);
        } else {
            // Log the error.
            Log.e(TAG, "ERROR GEOFENCE HERE <=> " + geofenceTransition);
        }
    }


    public static String status;

    private String getGeofenceTrasitionDetails(int geoFenceTransition, List<Geofence> triggeringGeofences) {
        // get the ID of each geofence triggered
        ArrayList<String> triggeringGeofencesList = new ArrayList<>();
        for (Geofence geofence : triggeringGeofences) {
            triggeringGeofencesList.add(geofence.getRequestId());
        }

        status = null;
        if (geoFenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER)
            status = "Entering ";
        else if (geoFenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT)
            status = "Exiting ";

        return TextUtils.join(", ", triggeringGeofencesList);
    }

    private void sendNotification(String msg, String smsMessage, String smsRecipients) {
        //Log.i(TAG, "sendNotification: " + msg);
        if (status.contains("Exiting")) {
            //send SMS Alert
            Log.i(TAG, "send SMS Alert Exist: ");

            //MainActivity.mainSMS(smsMessage, smsRecipients);
            //new MainActivity.AddUser(smsMessage, smsRecipients).execute();

        }
        if (status.contains("Entering") && showNotifications) {
            // Intent to start the main Activity
            Intent notificationIntent = makeNotificationIntent(
                    mContext, msg
            );

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
            stackBuilder.addParentStack(SingleProductActivity.class);
            stackBuilder.addNextIntent(notificationIntent);
            PendingIntent notificationPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


            Log.i(TAG, "showNotifications: " + showNotifications);
            Log.i(TAG, "send SMS Alert Enter: ");
            // Creating and sending Notification
            // NotificationManager notificatioMng =
            //       systemService;
            NotificationManager notificatioMng =
                    (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
                assert notificatioMng != null;
                notificatioMng.createNotificationChannel(notificationChannel);
            }
            notificatioMng.notify(
                    GEOFENCE_NOTIFICATION_ID,
                    createNotification(msg, notificationPendingIntent));
            //new SingleProductActivity().AddReview(mContext);
            //mContext.sendBroadcast(new Intent("NOTIFICATION_SENT"));//send broadCast so as to remove GeoFence
        }
    }

    public static final int GEOFENCE_NOTIFICATION_ID = 4;

    public static final String NOTIFICATION_CHANNEL_ID = "10001";

    // Create notification
    private Notification createNotification(String msg, PendingIntent notificationPendingIntent) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext, "testgdj");
        notificationBuilder
                .setSmallIcon(R.drawable.gotozeal_logo)
                .setColor(Color.RED)
                .setContentTitle(msg)
                .setContentText("You have reached your destination.\nThanks for using MAPOLY Tour Guide.")
                .setContentIntent(notificationPendingIntent)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                .setAutoCancel(true)
                .setChannelId(NOTIFICATION_CHANNEL_ID);
        return notificationBuilder.build();
    }


    private static String getErrorString(int errorCode) {
        switch (errorCode) {
            case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                return "GeoFence not available";
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                return "Too many GeoFences";
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                return "Too many pending intents";
            default:
                return "Unknown error.";
        }
    }

    private static final String NOTIFICATION_MSG = "NOTIFICATION MSG";

    // Create a Intent send by the notification
    public static Intent makeNotificationIntent(Context context, String msg) {
        Intent intent = new Intent(context, SingleProductActivity.class);
        intent.putExtra(NOTIFICATION_MSG, msg);
        return intent;
    }
}
