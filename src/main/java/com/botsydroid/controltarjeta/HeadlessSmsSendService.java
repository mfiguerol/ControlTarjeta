package com.botsydroid.controltarjeta;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by jofl on 03/07/2016.
 */
public class HeadlessSmsSendService extends Service {
    public HeadlessSmsSendService() {
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}