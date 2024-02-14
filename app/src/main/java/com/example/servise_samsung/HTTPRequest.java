package com.example.servise_samsung;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import javax.net.ssl.HttpsURLConnection;

public class HTTPRequest implements Runnable {

    private URL url;
    private Handler handler;
    public static final String KEY = "b1c1f23c9d3ec0f78feba83819c23fd9";

    HTTPRequest(Handler h, String city) {
        try {
            this.url = new URL("https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid="+KEY+"&units=metric");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        this.handler = h;
    }

    @Override
    public void run() {
        try {
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            Scanner scanner = new Scanner(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            while(scanner.hasNext()) {
                response.append(scanner.nextLine());
            }
            scanner.close();
            connection.disconnect();
            Message m = Message.obtain();
            m.obj = response.toString();
            handler.sendMessage(m);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}