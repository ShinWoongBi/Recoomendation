package org.appspot.apprtc.chat;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by kippe_000 on 2017-10-22.
 */

public class ChatDataBase extends SQLiteOpenHelper {
    public ChatDataBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // Chat_main에 모든 채팅 내용 가져오기
    public ArrayList select_all(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor1 = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name ='chat'" , null);
        cursor1.moveToFirst();

        if(cursor1.getCount()==0){
            return null;
        }

        String sql = "SELECT * FROM chat ORDER BY id DESC";
        ArrayList<Data> arrayList = new ArrayList<>();
        Data data;
        int count = 0;

        Cursor cursor = db.rawQuery(sql, null);
        while(cursor.moveToNext()){
            count = 0;

            for(int i = 0; i < arrayList.size(); i++){
                if(arrayList.get(i).friend_mail.equals(cursor.getString(1))){
                    count++;
                    break;
                }

            }

            if(count == 0){
                data = new Data(cursor.getString(1), cursor.getString(2));
                data.time = cursor.getString(4);

                arrayList.add(data);
            }

        }

        return arrayList;
    }

    public Objects delete(){
        SQLiteDatabase db = getWritableDatabase();

        SQLiteDatabase db_r = getReadableDatabase();
        Cursor cursor1 = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name ='chat'" , null);
        cursor1.moveToFirst();

        if(cursor1.getCount()==0){
            return null;
        }

        String sql = "DELETE FROM chat";
        db.execSQL(sql);

        return null;
    }



    // 방에 표시될 특정 사람의 채팅 내용 가져오기
    public JSONArray select_room(String friend_mail){
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor1 = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name ='chat'" , null);
        cursor1.moveToFirst();

        if(cursor1.getCount()==0){
            return null;
        }

        String sql = null;

        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();


        sql = "SELECT * FROM chat WHERE friend_mail='"+friend_mail+"'";
        Log.d("sql", sql);
        Cursor cursor = db.rawQuery(sql, null);
        while(cursor.moveToNext()){
            try {
                jsonObject.put("id", cursor.getInt(0));
                jsonObject.put("friend_mail", cursor.getString(1));
                jsonObject.put("message", cursor.getString(2));
                jsonObject.put("my_message", cursor.getString(3));
                jsonObject.put("time", cursor.getString(4));

                Log.d("select json", jsonObject.toString());


                jsonArray.put(jsonObject);

                jsonObject = new JSONObject();
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        return jsonArray;
    }


    // 실행될 insert문
    public void insert(int flag, String json_data){
        int my_message = 0;
        int read = 0;
        String friend_mail = null;
        String message = null;
        String time = null;
        String sql = null;

        Log.d("json_data", json_data+"");


        if(flag == 1) // 자신이 보낸 메시지
            my_message = 1;
        else if(flag == 2) // 안 읽은 메시지 표시
            read = 1;

        try {
            JSONObject jsonObject = new JSONObject(json_data);

            friend_mail = "'"+jsonObject.getString("friend_mail")+"'";
            message = "'"+jsonObject.getString("message")+"'";
            time = "'"+jsonObject.getString("time")+"'";
        } catch (JSONException e) {
            e.printStackTrace();
        }


        sql = "INSERT INTO chat(friend_mail,message,my_message,time,read) VALUES ("+friend_mail+","+message+","+my_message+","+time+","+read+")";
        Log.d("sql",sql);

        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(sql);
        db.close();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE chat(" +
                "id integer primary key not null," +
                "friend_mail varchar(80) not null," +
                "message mediumtext not null," +
                "my_message integer not null," +
                "time varchar(15) not null," +
                "read integer not null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
