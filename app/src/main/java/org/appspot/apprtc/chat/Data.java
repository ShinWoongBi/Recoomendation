package org.appspot.apprtc.chat;

import android.graphics.Bitmap;

/**
 * Created by kippe_000 on 2017-10-22.
 */

public class Data {
    int id;
    String friend_mail;
    String message;
    String time;
    int my_message;
    Bitmap bitmap = null;

    Data(String friend_mail, String message){
        this.friend_mail = friend_mail;
        this.message = message;
    }

}
