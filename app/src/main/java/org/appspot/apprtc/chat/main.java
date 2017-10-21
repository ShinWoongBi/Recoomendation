package org.appspot.apprtc.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import org.appspot.apprtc.R;


public class main extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_main);

        int flag = 0;

        Intent intent = getIntent();
        flag = intent.getIntExtra("flag",0);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Q&A Talk");
        actionBar.setDisplayHomeAsUpEnabled(true);

        switch(flag){
            case 1: // 홍에서  이동한 경우



                break;

            case 2: // 상대방 프로필에서 이동한 경우
                Intent new_intent = new Intent(getApplicationContext(), ChatRoom.class);
                new_intent.putExtra("maili", intent.getStringExtra("mail"));
                startActivity(new_intent);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
