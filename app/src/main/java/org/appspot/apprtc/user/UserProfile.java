package org.appspot.apprtc.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import org.appspot.apprtc.Connect_server;
import org.appspot.apprtc.R;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by woongbishin on 2017. 10. 21..
 */

public class UserProfile extends AppCompatActivity {
    Data data;
    String mail;
    CircleImageView circleImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);


        TextView name_T = (TextView)findViewById(R.id.name);
        TextView age_T = (TextView)findViewById(R.id.age);
        TextView gender_T = (TextView)findViewById(R.id.gender);
        circleImageView = (CircleImageView)findViewById(R.id.circleImageView);
        ImageButton message_btn = (ImageButton)findViewById(R.id.message_btn);

        message_btn.setOnClickListener(onClickListener);

        // 인텐트 값 받아오기
        Intent intent = getIntent();
        mail = intent.getStringExtra("mail");


        // 프로필 가져오기
        Get_Profile();

        // 툴버튼 생성
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(data.name+"의 프로필");

        // 이름 올리기
        name_T.setText(data.name);

        // 나이 올리기
        age_T.setText(data.age+"세");

        // 성별 올리기
        if(data.gender == 1) {
            gender_T.setText("남자");
        }else{
            gender_T.setText("여자");
        }

        // 사진 올리기
        Get_Profile_Image();


    }

    CircleImageView.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), org.appspot.apprtc.chat.main.class);

            startActivity(intent);
        }
    };


    void Get_Profile(){
        Connect_server connect_server = new Connect_server();
        connect_server.SetUrl("http://tlsdndql27.vps.phps.kr/recommendation/user/GetUserProfile.php");
        connect_server.AddParams("mail",mail);
        BufferedReader bufferedReader = connect_server.Connect(false);
        String buffer = null;
        try {
            buffer  = bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            JSONObject jsonObject = new JSONObject(buffer);
            data = new Data(mail, jsonObject.getString("name"), jsonObject.getInt("age"), jsonObject.getInt("gender"));


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void Get_Profile_Image(){
        final Handler handler = new Handler();

        Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();

                try {
                    URL url = new URL("http://tlsdndql27.vps.phps.kr/recommendation/upload/Profile_Image/"+mail+".jpg");
                    InputStream inputStream = url.openStream();
                    data.bitmap = BitmapFactory.decodeStream(inputStream);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            circleImageView.setImageBitmap(data.bitmap);
                        }
                    });

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };
        thread.start();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
