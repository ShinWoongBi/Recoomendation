package org.appspot.apprtc.board;

import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.appspot.apprtc.Connect_server;
import org.appspot.apprtc.R;

import java.io.BufferedReader;

/**
 * Created by kippe_000 on 2017-10-25.
 */

public class WriteActivity extends AppCompatActivity {
    Button post_btn;
    EditText editText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_activity);


        post_btn = (Button)findViewById(R.id.post);
        editText = (EditText)findViewById(R.id.edit_text);


        // 툴버튼 생성
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Post to App");


        // 프로필 설정
        ((ImageView)findViewById(R.id.profile_image)).setImageBitmap(BitmapFactory.decodeFile(
                Environment.getExternalStorageDirectory().getAbsolutePath()+"/recommendation/.my_picture.jpg"
        ));
        SharedPreferences sharedPreferences = getSharedPreferences("Profile",MODE_PRIVATE);
        ((TextView)findViewById(R.id.name_T)).setText(sharedPreferences.getString("name",""));


        post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 사진 올리기 버튼
                Toast.makeText(WriteActivity.this, "asdf", Toast.LENGTH_SHORT).show();
                Post();
            }
        });

        // edittext에 글을 쓸 경우 POST버튼 활성화
        editText.addTextChangedListener(textWatcher);


    }


    void Post(){
        String content = editText.getText().toString();

        SharedPreferences sharedPreferences = getSharedPreferences("Profile", MODE_PRIVATE);
        String mail = sharedPreferences.getString("mail","");
        String user_type = sharedPreferences.getString("user_type","");
        String token = sharedPreferences.getString("token","");

        Connect_server connect_server = new Connect_server();
        connect_server.SetUrl("http://tlsdndql27.vps.phps.kr/recommendation/community/WritePost.php");
        connect_server.AddParams("mail",mail).AddParams("token",token).AddParams("user_type", user_type).AddParams("content", content);
        BufferedReader bufferedReader = connect_server.Connect(false);


        finish();

    }


    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if(!editText.getText().toString().equals("") || editText.getText().toString() == null){
                post_btn.setClickable(true);
            }else{
                post_btn.setClickable(false);
            }
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if(!editText.getText().toString().equals("") || editText.getText().toString() == null){
                post_btn.setEnabled(true);
                post_btn.setTextColor(Color.parseColor("#ffffff"));
            }else{
                post_btn.setEnabled(false);
                post_btn.setTextColor(Color.parseColor("#7792CB"));
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
