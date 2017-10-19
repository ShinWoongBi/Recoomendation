package org.appspot.apprtc;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class TcpService extends Service {
    Socket socket = null;
    String url = "115.71.232.134";
    int port = 9000;

    BufferedReader reader;
    PrintWriter writer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("test", "서비스의 onBind");

        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 서비스에서 가장 먼저 호출됨(최초에 한번만)
        Log.d("test", "서비스의 onCreate");


        Connect();
    }

    void Connect(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // 소켓 연결
                try {
                    socket = new Socket(url, port);
                    Log.d("소켓 연결", socket.isConnected() +"");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // reader writer 연결
                try {
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    writer = new PrintWriter(socket.getOutputStream(), true);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // 서버에서 소켓과 mail 연결
                SharedPreferences sharedPreferences = getSharedPreferences("Profile", MODE_PRIVATE);
                String mail = sharedPreferences.getString("mail","");
                writer.println(mail);
            }
        });
        thread.start();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 서비스가 호출될 때마다 실행
        Log.d("test", "서비스의 onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("test", "서비스의 onDestroy");
    }


}
