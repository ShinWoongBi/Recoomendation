package org.appspot.apprtc.chat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import org.appspot.apprtc.Connect_server;
import org.appspot.apprtc.R;
import org.appspot.apprtc.webRTC.ConnectActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class ChatRoom extends AppCompatActivity {
    String friend_mail = null;
    String friend_name = null;
    Socket socket = null;
    int port = 9000;
    String url = "115.71.232.134";
    BufferedReader reader = null;
    PrintWriter writer = null;
    ListView listView = null;
    ImageButton message_btn = null;
    EditText message_E = null;
    ArrayList<Data> listView_list = null;
    ListView_Adapter listView_adapter = null;
    ChatDataBase chatDataBase = null;

    String my_mail = null;
    String print_message = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_room);

        ImageButton call_btn = (ImageButton)findViewById(R.id.call_btn);
        SharedPreferences sharedPreferences = getSharedPreferences("Profile", MODE_PRIVATE);
        listView = (ListView)findViewById(R.id.listView);
        message_btn = (ImageButton)findViewById(R.id.message_btn);
        message_E = (EditText)findViewById(R.id.message_E);
        listView_list = new ArrayList<>();
        listView_adapter = new ListView_Adapter(getApplicationContext());
        chatDataBase = new ChatDataBase(getApplicationContext(), "recommendation.db", null, 1);
        my_mail = sharedPreferences.getString("mail","");


        // 채팅할 상대방 메일
        Intent intent = getIntent();
        friend_mail = intent.getStringExtra("mail");

        SharedPreferences count_shPreferences = getSharedPreferences("count", MODE_PRIVATE);
        SharedPreferences.Editor editor = count_shPreferences.edit();
        String mail_c =friend_mail+"~count";
        editor.putInt(mail_c, 0);
        editor.apply();
        editor.commit();


        // 상대방 이름 가져오기
        Connect_server connect_server = new Connect_server();
        connect_server.SetUrl("http://tlsdndql27.vps.phps.kr/recommendation/user/GetUserProfile.php");
        connect_server.AddParams("mail", friend_mail);
        BufferedReader bufferedReader = connect_server.Connect(false);
        try {
            friend_name = (new JSONObject(bufferedReader.readLine())).getString("name");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        // 리스트뷰 어뎁터 연결
        listView.setAdapter(listView_adapter);


        // 리스너 연결
        message_btn.setOnClickListener(message_btn_listener);
        call_btn.setOnClickListener(onClickListener);


        // actionbar 생성
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(friend_name+"님과의 대화");
        actionBar.setDisplayHomeAsUpEnabled(true);

        // 채팅 내용 가져오기
        ChangeNewListView();

    }

    @Override
    protected void onResume() {
        super.onResume();

        // 소켓 통신 시작
        Connect();
    }

    ImageButton.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getApplicationContext(), ConnectActivity.class));
        }
    };


    final Handler handler = new Handler();
    // 소켓 통신 Reciver
    Thread Receiver_Thread = new Thread(new Runnable() {
        @Override
        public void run() {
            String read = null;

            try {
                while(true){
                    if(socket.isConnected()) // 서버와 연결이 되어 있다면
                        read = reader.readLine();
                    Log.d("read", read+"");


                    // 시간 구하기
                    Calendar calendar = Calendar.getInstance();
                    java.util.Date date = calendar.getTime();
                    String today = (new SimpleDateFormat("yyyy-MM-dd/HH:mm:ss").format(date));


                    JSONObject jsonObject = new JSONObject(read);
                    jsonObject.put("time", today);

                    // ChatDataBase 저장
                    chatDataBase.insert(3, jsonObject.toString());


                    // 채팅방 갱신
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            ChangeNewListView();
                        }
                    });



                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    });


    // 메시지 보내기
    ImageButton.OnClickListener message_btn_listener = new View.OnClickListener() {


        @Override
        public void onClick(View v) {
            Log.d("click","click");

            print_message = message_E.getText().toString();
            if(!print_message.equals("")) {


                // 소켓 통신 writer
                Thread Writer_Thread = new Thread(new Runnable() {

                    @Override
                    public void run() {

                        // 시간 구하기
                        Calendar calendar = Calendar.getInstance();
                        java.util.Date date = calendar.getTime();
                        String today = (new SimpleDateFormat("yyyy-MM-dd/HH:mm:ss").format(date));


                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("my_mail", my_mail);
                            jsonObject.put("friend_mail", friend_mail);
                            jsonObject.put("message", print_message);
                            Log.d("jsondata", jsonObject+"");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        writer.println(jsonObject.toString());


                        // ChatDataBase 저장
                        try {
                            jsonObject.put("time", today);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        chatDataBase.insert(1, jsonObject.toString());


                        // 채팅방 갱신
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                message_E.setText("");
                                ChangeNewListView();
                            }
                        });
                    }
                });
                Writer_Thread.start();
            }
        }
    };


    void ChangeNewListView(){
        listView_list.clear();
        Data data;

        int id = 0;
        String friend_mail;
        String message;
        int my_message;
        String time;

        JSONArray jsonArray = chatDataBase.select_room(this.friend_mail);
        JSONObject jsonObject;
        for(int i = 0; i < jsonArray.length(); i++){
            try {
                jsonObject = jsonArray.getJSONObject(i);

                friend_mail = jsonObject.getString("friend_mail");
                message = jsonObject.getString("message");
                my_message = jsonObject.getInt("my_message");
                time = jsonObject.getString("time");


                data = new Data(friend_mail, message);
                data.my_message = my_message;
                data.time = time;
                data.id = id;

                listView_list.add(data);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        listView_adapter.notifyDataSetChanged();
    }




    // 소켓 통신(TCP/ID) 시작
    void Connect(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // 소켓 연결
                try {
                    socket = new Socket(url, port);
                    Log.d("소켓 연결", socket.isConnected() +"");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // reader writer 연결
                try {
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    writer = new PrintWriter(socket.getOutputStream(), true);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // 서버에서 소켓과 mail 연결
                SharedPreferences sharedPreferences = getSharedPreferences("Profile", MODE_PRIVATE);
                String mail = sharedPreferences.getString("mail","");
                writer.println(mail);

                // Receiver_Thread 실행
                Receiver_Thread.start();
            }
        });
        thread.start();
    }


    class ListView_Adapter extends BaseAdapter{
        Context context;

        ListView_Adapter(Context context){
            this.context = context;
        }

        @Override
        public int getCount() {
            return listView_list.size();
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
                convertView = layoutInflater.inflate(R.layout.chat_item, parent, false);
            }

            Data data = listView_list.get(position);

            TextView textView = (TextView)convertView.findViewById(R.id.data);
            textView.setText(data.message);


            return convertView;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
