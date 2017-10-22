package org.appspot.apprtc.chat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.appspot.apprtc.Connect_server;
import org.appspot.apprtc.R;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;


public class main extends AppCompatActivity {
    ChatDataBase chatDataBase = null;
    ArrayList<Data> arrayList;
    ListView listView;
    ListviewAdapter listviewAdapter;
    String name;
    Bitmap bitmap;
    static public org.appspot.apprtc.chat.Handler handler = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_main);

        int flag = 0;
        chatDataBase = new ChatDataBase(getApplicationContext(), "recommendation.db", null, 1);
        listView = (ListView)findViewById(R.id.listView);
        listviewAdapter = new ListviewAdapter(getApplicationContext());
        handler = new org.appspot.apprtc.chat.Handler(main.this);
        arrayList = new ArrayList<>();

        listView.setAdapter(listviewAdapter);

        Intent intent = getIntent();
        flag = intent.getIntExtra("flag",0);

        // actionbar 생성
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Q&A Talk");
        actionBar.setDisplayHomeAsUpEnabled(true);


        listView.setOnItemClickListener(onItemClickListener);


        switch(flag){
            case 1: // 홍에서  이동한 경우



                break;

            case 2: // 상대방 프로필에서 이동한 경우
                Intent new_intent = new Intent(getApplicationContext(), ChatRoom.class);
                new_intent.putExtra("mail", intent.getStringExtra("mail"));
                startActivity(new_intent);
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        handler = null;
    }

    public void handleMessage(Message msg) {
        Get_All();
    }
    

    @Override
    protected void onResume() {
        super.onResume();

        // 메시지 목록 가져오기
        Get_All();
        handler = new org.appspot.apprtc.chat.Handler(main.this);



    }

    public void Get_All(){
        arrayList = chatDataBase.select_all();
        Get_ProfileImage();
    }

    ListView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(main.this, ChatRoom.class);
            intent.putExtra("mail", arrayList.get(position).friend_mail);
            startActivity(intent);
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void Get_ProfileImage(){
        final android.os.Handler handeler = new Handler(Looper.getMainLooper());


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                for(int i = 0; i < arrayList.size(); i++){
                    InputStream inputstream = null;
                    try {
                        inputstream = new Get_Profile_Image(arrayList.get(i).friend_mail).execute().get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    arrayList.get(i).bitmap = BitmapFactory.decodeStream(inputstream);
                }

                handeler.post(new Runnable() {
                    @Override
                    public void run() {
                        listviewAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
        thread.start();
    }

    class ListviewAdapter extends BaseAdapter{
        Context context;

        ListviewAdapter(Context context){
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
                convertView = layoutInflater.inflate(R.layout.chat_main_item, parent, false);
            }
            Data data = arrayList.get(position);
            String name;


            // 메시지 올리기
            TextView message_T = (TextView)convertView.findViewById(R.id.message);
            message_T.setText(data.message+"");


            //프로필 가져오기
            Connect_server connect_server = new Connect_server();
            connect_server.SetUrl("http://tlsdndql27.vps.phps.kr/recommendation/user/GetUserProfile.php");
            connect_server.AddParams("mail", data.friend_mail);
            BufferedReader bufferedReader = connect_server.Connect(false);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(bufferedReader.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                name = jsonObject.getString("name");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            //사진 올리기
            if(data.bitmap != null) {
                CircleImageView circleImageView = (CircleImageView) convertView.findViewById(R.id.profile_image);
                circleImageView.setImageBitmap(data.bitmap);
            }


            // 이름 올리기
            TextView name_T = (TextView)convertView.findViewById(R.id.name);
            try {
                name_T.setText(jsonObject.getString("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // 시간 올리기
            TextView time_T = (TextView)convertView.findViewById(R.id.time);
            time_T.setText(data.time+"");


            // 안 읽은 메시지 수 올리기
            int count = 0;
            SharedPreferences sharedPreferences = getSharedPreferences("count", Context.MODE_PRIVATE);
            count = sharedPreferences.getInt(data.friend_mail + "~count", 0);
            Log.d("count", count+"");
            FrameLayout frameLayout = (FrameLayout) convertView.findViewById(R.id.frame);
            if(count != 0) {
                frameLayout.setVisibility(View.VISIBLE);
                ((TextView) convertView.findViewById(R.id.chat_main_item_numtext)).setText(count+"");
            }else{
                frameLayout.setVisibility(View.GONE);
            }


            return convertView;
        }
    }



    class Get_Profile_Image extends AsyncTask<Void, Void, InputStream> {
        String mail;
        InputStream inputStream;

        Get_Profile_Image(String mail){
            this.mail = mail;
        }

        @Override
        protected InputStream doInBackground(Void... voids) {
            try {
                URL url = new URL("http://tlsdndql27.vps.phps.kr/recommendation/upload/Profile_Image/"+mail+".jpg");
                inputStream = url.openStream();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            super.onPostExecute(inputStream);
        }
    }
}
