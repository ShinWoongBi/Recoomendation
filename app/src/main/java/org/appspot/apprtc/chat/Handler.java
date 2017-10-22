package org.appspot.apprtc.chat;

import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by kippe_000 on 2017-10-22.
 */

public class Handler extends android.os.Handler {
    WeakReference<main> activity;

    Handler(main main){
        activity = new WeakReference<main>(main);
    }



    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);

        main activity = this.activity.get();
        if(activity != null){
            activity.handleMessage(msg);
        }

    }
}
