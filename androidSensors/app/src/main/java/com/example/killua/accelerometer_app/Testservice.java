package com.example.killua.accelerometer_app;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by killua on 4/29/18.
 */

public class Testservice extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "IAM IN TEST SERVICE",Toast.LENGTH_LONG).show();
        return START_STICKY;
    }
}
