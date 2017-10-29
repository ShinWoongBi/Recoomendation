package org.appspot.apprtc.collection;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by kippe_000 on 2017-10-28.
 */

public class CollectionDataBase extends SQLiteOpenHelper {

    public CollectionDataBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    ArrayList<Data> select(int id){
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM collection WHERE id="+id;
        Data data = new Data();
        ArrayList<Data> arrayList = new ArrayList<>();


        Cursor cursor = db.rawQuery(sql, null);
        while(cursor.moveToNext()){
            data.id = cursor.getInt(0);
            data.x = cursor.getInt(1);
            data.y = cursor.getInt(2);
            data.size = cursor.getFloat(3);
            data.background = cursor.getInt(4);
            data.day = cursor.getString(5);
            data.month_year = cursor.getString(6);
            data.message = cursor.getString(7);
            data.font = cursor.getInt(8);

            arrayList.add(data);
        }

        return arrayList;
    }

    ArrayList<Data> select(){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Data> arrayList = new ArrayList<>();
        Data data = new Data();
        String sql = "SELECT id,picture,day,month_year FROM collection ORDER BY id DESC";
        ArrayList<String> datelist = new ArrayList<>();

        Cursor cursor = db.rawQuery(sql, null);

        while(cursor.moveToNext()){
            data.id = cursor.getInt(0);
            data.path = cursor.getString(1);
            data.day = cursor.getString(2);
            data.month_year = cursor.getString(3);

            int is_date = 0;
            for(int i = 0; i < datelist.size(); i++){
                if(datelist.get(i).equals(data.month_year)){
                    is_date++;
                    break;
                }
            }
            if(is_date==0) {
                Data data1 = new Data();
                data1.flag = 1;
                data1.month_year = data.month_year;
                datelist.add(data.month_year);
                arrayList.add(data1);
            }


            arrayList.add(data);
            data = new Data();
        }

        return arrayList;
    }

    void insert(Data data){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "INSERT INTO collection(x,y,size,background,day,month_year,message,font,picture) " +
                "VALUES("+data.x+","+data.y+","+data.size+","+data.background+",'"+data.day+"','"+data.month_year+"','"+data.message+"',"+data.font+",'"+data.path+"')";
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
                "font integer not null," +
                "picture varchar(100) not null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
