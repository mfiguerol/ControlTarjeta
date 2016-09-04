package com.botsydroid.controltarjeta;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by jofl on 05/07/2016.
 */
public class MessagingService extends IntentService {
    private static final String TAG = "MessagingService";
    // These actions are for this app only and are used by MessagingReceiver to start this service
    public static final String ACTION_MY_RECEIVE_SMS = "com.example.android.smssample.RECEIVE_SMS";
    public static final String ACTION_MY_RECEIVE_MMS = "com.example.android.smssample.RECEIVE_MMS";
    public MessagingService() {
        super(TAG);
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String intentAction = intent.getAction();
            if (ACTION_MY_RECEIVE_SMS.equals(intentAction)) {
                // TODO: Handle incoming SMS
                // Ensure wakelock is released that was created by the WakefulBroadcastReceiver
               // MessagingReceiver.completeWakefulIntent(intent);
            } else if (ACTION_MY_RECEIVE_MMS.equals(intentAction)) {
                // TODO: Handle incoming MMS
                // Ensure wakelock is released that was created by the WakefulBroadcastReceiver
                //MessagingReceiver.completeWakefulIntent(intent);
            }
        }
    }
}