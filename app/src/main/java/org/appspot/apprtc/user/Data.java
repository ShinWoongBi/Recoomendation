package org.appspot.apprtc.user;

import android.graphics.Bitmap;

/**
 * Created by woongbishin on 2017. 10. 21..
 */

public class Data {
    String mail;
    String name;
    Bitmap bitmap;

    int age;
    int gender;

    Data(String mail, String name, int age, int gender){
        this.mail = mail;
        this.name = name;
        this.age = age;
        this.gender = gender;

    }


}
