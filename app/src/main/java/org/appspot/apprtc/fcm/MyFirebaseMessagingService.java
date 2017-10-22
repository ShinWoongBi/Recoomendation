package org.appspot.apprtc.fcm;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.appspot.apprtc.chat.ChatDataBase;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // 시간 구하기
        Calendar calendar = Calendar.getInstance();
        java.util.Date date = calendar.getTime();
        String today = (new SimpleDateFormat("yyyy-MM-dd/HH:mm:ss").format(date));


        String message = remoteMessage.getData().get("message");
        JSONObject jsonObject = null;
        try {
            JSONArray jsonArray = new JSONArray(message);
            jsonObject = jsonArray.getJSONObject(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            jsonObject.put("time", today);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ChatDataBase chatDataBase = new ChatDataBase(MyFirebaseMessagingService.this, "recommendation.db", null, 1);
        chatDataBase.insert(2, jsonObject.toString());

        try {
            SharedPreferences sharedPreferences = getSharedPreferences("count", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            String mail_c = jsonObject.getString("friend_mail")+"~count";

            int count = sharedPreferences.getInt(mail_c, 0);
            Log.d("a",mail_c+"~count");
            count++;
            Log.d("count", count+":");
            editor.putInt(mail_c, count);
            editor.apply();
            editor.commit();

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


}