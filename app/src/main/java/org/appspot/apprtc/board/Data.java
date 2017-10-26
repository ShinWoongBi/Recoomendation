package org.appspot.apprtc.board;


import android.graphics.Bitmap;

public class Data {
    String mail;
    String profile_picture;
    String profile_name;

    int post_id;

    String time;
    String content;

    int answer_c;
    int like_c;

    boolean liked;

    Bitmap bitmap;

    Data(String mail, String profile_name, int post_id, String time, String content, int answer_c, int like_c, int my_like){
        this.mail = mail;
        this.profile_name = profile_name;
        this.post_id = post_id;
        this.time = time;
        this.content = content;
        this.answer_c = answer_c;
        this.like_c = like_c;
        if(my_like == 1)
            liked = true;
        else
            liked = false;
    }


}
