package com.example.servise_samsung;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private TextView tempText;
    private ImageView imageView;
    private final String TAG = "Main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tempText = findViewById(R.id.temp_txt);
        registerReceiver(receiver, new IntentFilter(GisService.CHANNEL), Context.RECEIVER_NOT_EXPORTED);
        imageView = findViewById(R.id.imageView);
        Log.i(TAG, "create");
        Button button = findViewById(R.id.button);
        EditText textView = findViewById(R.id.editText);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GisService.class);
            intent.putExtra("city", textView.getText().toString());
            Log.i(TAG, textView.getText().toString());
            startService(intent);
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(this, GisService.class);
        stopService(intent);
    }


    protected BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                Log.i(TAG, "get info");

                JSONObject json = new JSONObject(intent.getStringExtra(GisService.INFO));
                Double result_temp = Double.parseDouble(json.getJSONObject("main").getString("temp"));

                tempText.setText("Температура" + result_temp.toString());
                if (result_temp < 0)
                    imageView.setImageResource(R.drawable.img);
                else if (result_temp < 15)
                    imageView.setImageResource(R.drawable.img_2);
                else
                    imageView.setImageResource(R.drawable.img_1);
                
            } catch (JSONException e) {
                Toast.makeText(MainActivity.this, "Неверный формат JSON", Toast.LENGTH_LONG).show();
            }
        }
    };
}