package org.appspot.apprtc.board;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.appspot.apprtc.Connect_server;
import org.appspot.apprtc.R;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by kippe_000 on 2017-10-28.
 */

public class AnswerActivity extends Activity {
    Adapter adapter;
    ListView listView;
    int post_id;
    ArrayList<AnswerData> arrayList;
    EditText editText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.answer_activity);

        adapter = new Adapter(getApplicationContext());
        ImageButton post_btn = (ImageButton)findViewById(R.id.post_btn);
        listView = (ListView)findViewById(R.id.listView);
        editText = (EditText)findViewById(R.id.editText);
        arrayList = new ArrayList<>();
        listView.setAdapter(adapter);


        post_id = getIntent().getIntExtra("post_id",0);
        Log.d("post_id", post_id+"");


        post_btn.setOnClickListener(postListener);

        // Answer 가져오기
        GetAnswer();


        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                listView.setSelection(arrayList.size()-1);
            }
        });
    }

    void GetAnswer(){
        Connect_server connect_server = new Connect_server();
        connect_server.SetUrl("http://tlsdndql27.vps.phps.kr/recommendation/community/GetAnswer.php");
        connect_server.AddParams("post_id",post_id+"");
        BufferedReader bufferedReader = connect_server.Connect(true);

        try {
            connect_server.Buffer_read(bufferedReader);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String buffer = "";
        try {

            JSONObject jsonObject;
            int id;
            String writer,name,time,content;
            AnswerData answerData;

            while((buffer=bufferedReader.readLine()) != null){
                jsonObject = new JSONObject(buffer);

                id = jsonObject.getInt("id");
                writer = jsonObject.getString("writer");
                name = jsonObject.getString("name");
                time = jsonObject.getString("time");
                content = jsonObject.getString("content");


                answerData = new AnswerData(id,writer,name,time,content);
                arrayList.add(answerData);
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter.notifyDataSetChanged();
        listView.setSelection(arrayList.size()-1);

    }


    ImageButton.OnClickListener postListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Profile",Context.MODE_PRIVATE);

            String message = editText.getText().toString();
            editText.setText("");

            Connect_server connect_server = new Connect_server();
            connect_server.SetUrl("http://tlsdndql27.vps.phps.kr/recommendation/community/WriteAnswer.php");
            connect_server.AddParams("mail",sharedPreferences.getString("mail","")).AddParams("token",sharedPreferences.getString("token",""))
                    .AddParams("user_type",sharedPreferences.getString("user_type",""))
                    .AddParams("message", message).AddParams("post_id", post_id+"");
            BufferedReader bufferedReader = connect_server.Connect(false);

            GetAnswer();

        }
    };

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

//        LinearLayout parent_layout = (LinearLayout)findViewById(R.id.parent);
//        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
//        int width = dm.widthPixels;
//        int height = dm.heightPixels;
//        ViewGroup.LayoutParams params = parent_layout.getLayoutParams();
//        params.height = height;
//        params.width = width;
//        parent_layout.setLayoutParams(params);
    }

    class Adapter extends BaseAdapter{
        Context context;

        Adapter(Context context){
            this.context = context;
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.answer_item, parent, false);
            }
            AnswerData answerData = arrayList.get(position);


            ImageButton imageButton = (ImageButton)convertView.findViewById(R.id.imageView);
            TextView name_T = (TextView)convertView.findViewById(R.id.name_T);
            TextView content_T = (TextView)convertView.findViewById(R.id.content_T);


            Picasso.with(getApplicationContext())
                    .load("http://tlsdndql27.vps.phps.kr/recommendation/upload/Profile_Image/"+answerData.mail+".jpg")
                    .into(imageButton);

            name_T.setText(answerData.name);
            content_T.setText(answerData.content);



            Log.d("id",answerData.id+"");

            return convertView;
        }
    }
}
