package com.example.laveen.exploreandrooid;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.widget.Toast;

/**
 * Created by laveen on 29/5/15.
 */
public class MyService extends Service {

    public static final String PLAY = "com.example.laven.PLAY";

    private MyBroadcast broadcast;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        broadcast = new MyBroadcast();
        IntentFilter filter = new IntentFilter();
        filter.addAction(PLAY);
        registerReceiver(broadcast, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcast);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    class MyBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "BROADCAST", Toast.LENGTH_SHORT).show();
        }
    }
}
