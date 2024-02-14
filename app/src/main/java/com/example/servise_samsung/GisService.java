package com.example.servise_samsung;

import android.app.Service;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;


public class GisService extends Service {
    public static final String CHANNEL = "GIS_SERVICE";
    public static final String INFO = "INFO";
    private Handler h;
    @Override
    public void onCreate() {
        h = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                Intent i = new Intent(CHANNEL);
                i.putExtra(INFO, msg.obj.toString());
                sendBroadcast(i);
                Log.i(CHANNEL, "get info" + msg.obj.toString());

            }
        };

        Toast.makeText(this, "Служба создана",Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Служба запущена",Toast.LENGTH_SHORT).show();
        String city = intent.getStringExtra("city");
        Thread weather = new Thread(new HTTPRequest(h, city));
        weather.start();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Служба остановлена", Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}