package org.appspot.apprtc.collection;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by kippe_000 on 2017-10-28.
 */

public class CollectionDataBase extends SQLiteOpenHelper {

    public CollectionDataBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    void insert(Data data){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "INSERT INTO collection(x,y,size,background,day,month_year,message,font) " +
                "VALUES("+data.x+","+data.y+","+data.size+","+data.background+",'"+data.day+"','"+data.month_year+"','"+data.message+"',"+data.font+")";
        Log.d("sql", sql);
        db.execSQL(sql);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE collection(" +
                "id integer primary key not null," +
                "x integer not null," +
                "y integer not null," +
                "size float not null," +
                "background integer not null," +
                "day varchar(5) not null," +
                "month_year varchar(10) not null," +
                "message varchar(100) not null," +
                "font integer not null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
