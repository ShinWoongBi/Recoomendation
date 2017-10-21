package org.appspot.apprtc.chat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import org.appspot.apprtc.R;

/**
 * Created by woongbishin on 2017. 10. 21..
 */

public class ChatRoom extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_room);
    }
}
