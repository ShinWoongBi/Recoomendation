package org.appspot.apprtc.board;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.appspot.apprtc.Connect_server;
import org.appspot.apprtc.R;
import org.appspot.apprtc.user.UserProfile;
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

/**
 * Created by kippe_000 on 2017-10-07.
 */

public class Main extends Fragment {
    Context context;
    ListView listView;
    ArrayList<Data> arrayList_item;
    int NowPage = 0;
    ListViewAdapter listViewAdapter;
    CircleImageView circleImageView;
    LinearLayout linearLayout;
    SwipeRefreshLayout mSwipeRefreshLayout;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.board_main, container, false); 

        // 초기화
        context = view.getContext();
        listView = (ListView)view.findViewById(R.id.listView);
        arrayList_item = new ArrayList<>();
        listViewAdapter = new ListViewAdapter(getContext());
        linearLayout = (LinearLayout)view.findViewById(R.id.linear);

        // 리스트뷰 어뎁터 연결
        listView.setAdapter(listViewAdapter);

        // 자신 프로필 이미지 띄우기
        circleImageView = (CircleImageView)view.findViewById(R.id.profile_image);
        circleImageView.setImageBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath()+"/recommendation/.my_picture.jpg"));


        // 리스너 연결
        listView.setOnScrollListener(onScrollListener);
        linearLayout.setOnClickListener(linearOnClickListener);
        circleImageView.setOnClickListener(cirOnClickListener);


        // listview 빈 헤더
        listView.addHeaderView(getActivity().getLayoutInflater().inflate(R.layout.board_main_header,container, false));


        // 세로고침 View
        mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );
        mSwipeRefreshLayout.setOnRefreshListener(refreshListener);



        // 게시물 가져오기
        Get_post();

        return view;
    }
    SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            // list 데이터 비우
            arrayList_item.clear();


            // 게시물 새로고침
            Get_post();

            // 새로고침 완료
            mSwipeRefreshLayout.setRefreshing(false);

        }
    };

    ListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            int scrollY = getScrollY();
            linearLayout.setTranslationY(Math.max(-scrollY, -scrollY));

        }



    };

    public int getScrollY() {
        View c = listView.getChildAt(0);
        if (c == null) {
            return 0;
        }

        int firstVisiblePosition = listView.getFirstVisiblePosition();
        int top = c.getTop();

        int headerHeight = 0;
        if (firstVisiblePosition >= 1) {
            headerHeight = linearLayout.getHeight(); // 수정
        }

        int result = -top + firstVisiblePosition * c.getHeight() + headerHeight;


        return result;
    }


    @Override
    public void onResume() {
        super.onResume();


    }

    CircleImageView.OnClickListener cirOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("Profile", Context.MODE_PRIVATE);
            String mail = sharedPreferences.getString("mail","");
            String name = sharedPreferences.getString("name","");

            // 자신 프로필 들어가기
            Intent intent = new Intent(getContext(), UserProfile.class);
            intent.putExtra("mail", mail);
            intent.putExtra("name", name);
            startActivity(intent);
        }
    };

    LinearLayout.OnClickListener linearOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // 글쓰기 Activity
            startActivity(new Intent(getActivity(), WriteActivity.class));

        }
    };


    void Get_post(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("Profile", Context.MODE_PRIVATE);
        String mail = sharedPreferences.getString("mail","");

        Connect_server connect_server = new Connect_server();
        connect_server.SetUrl("http://tlsdndql27.vps.phps.kr/recommendation/community/GetPost.php");
        connect_server.AddParams("NowPage", String.valueOf(NowPage)).AddParams("mail", mail).AddParams("token",sharedPreferences.getString("token",""))
                        .AddParams("user_type",sharedPreferences.getString("user_type",""));
        BufferedReader bufferedReader = connect_server.Connect(true);

        try {
            connect_server.Buffer_read(bufferedReader);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String buffer = null;
        JSONObject jsonObject = null;
        Data data = null;
        try {
            while((buffer = bufferedReader.readLine()) != null){
                jsonObject = new JSONObject(buffer);
                data = new Data(jsonObject.getString("mail"), jsonObject.getString("name"), jsonObject.getInt("post_id"),
                        jsonObject.getString("time"), jsonObject.getString("content"),
                        jsonObject.getInt("answer_c"), jsonObject.getInt("liked"), jsonObject.getInt("my_like"));

                arrayList_item.add(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Get_ProfileImage();

    }


    // 글쓴이의 프로필 사진 가져오기
    void Get_ProfileImage(){
        final android.os.Handler handelr = new android.os.Handler();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                for(int i = 0; i < arrayList_item.size(); i++){
                    InputStream inputstream = null;
                    try {
                        inputstream = new Get_Profile_Image(arrayList_item.get(i).mail).execute().get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    arrayList_item.get(i).bitmap = BitmapFactory.decodeStream(inputstream);
                }

                handelr.post(new Runnable() {
                    @Override
                    public void run() {
                        listViewAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
        thread.start();
    }



    class ListViewAdapter extends BaseAdapter{
        Context context;
        Data data;

          ListViewAdapter(Context context){
              this.context = context;
          }

        @Override
        public int getCount() {
            return arrayList_item.size();
        }

        @Override
        public Object getItem(int position) {
            return arrayList_item.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.board_item,parent,false);
            }

            data = arrayList_item.get(position);

            // 프로필 사진 올리기
            de.hdodenhof.circleimageview.CircleImageView circleImageView = (de.hdodenhof.circleimageview.CircleImageView)convertView.findViewById(R.id.profile_image);
            circleImageView.setImageBitmap(data.bitmap);

            // 이름 올리기
            TextView name_T = (TextView)convertView.findViewById(R.id.name);
            name_T.setText(data.profile_name);

            // 제목 올리기
            final TextView content = (TextView)convertView.findViewById(R.id.content);
            content.setText(data.content);

            // 종아요수 올리기
            TextView like_c = (TextView) convertView.findViewById(R.id.like_c);
            if(data.like_c != 0) {
                like_c.setVisibility(View.VISIBLE);
                ((ImageView)convertView.findViewById(R.id.imageView)).setVisibility(View.VISIBLE);
                like_c.setText(data.like_c + "");
            }else{
                like_c.setVisibility(View.INVISIBLE);
                ((ImageView)convertView.findViewById(R.id.imageView)).setVisibility(View.INVISIBLE);
            }

            // 답변수 올리기
            TextView answer_c = (TextView) convertView.findViewById(R.id.answer_c);
            if(data.answer_c != 0) {
                answer_c.setVisibility(View.VISIBLE);
                answer_c.setText(data.answer_c + " Answer");
            }else{
                answer_c.setVisibility(View.GONE);
            }


            // 좋아요를 한지 안한지
            if(data.liked){
                ((ImageView)convertView.findViewById(R.id.like_image)).setImageResource(R.drawable.heart2);
            }else{
                ((ImageView)convertView.findViewById(R.id.like_image)).setImageResource(R.drawable.heart1);
            }

            // 답변 Activity 띄우기
            LinearLayout answer_btn = (LinearLayout)convertView.findViewById(R.id.answer_btn);
            answer_btn.setTag(position);
            answer_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), AnswerActivity.class);
                    intent.putExtra("post_id",arrayList_item.get((Integer) v.getTag()).post_id);
                    startActivity(intent);
                }
            });


            // 좋아요 누르기
            LinearLayout like_btn = (LinearLayout)convertView.findViewById(R.id.like_btn);
            like_btn.setTag(position);
            like_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Profile",Context.MODE_PRIVATE);

                    Connect_server connect_server = new Connect_server();
                    connect_server.SetUrl("http://tlsdndql27.vps.phps.kr/recommendation/community/Like.php");
                    connect_server.AddParams("mail",sharedPreferences.getString("mail","")).AddParams("token",sharedPreferences.getString("token",""))
                                    .AddParams("user_type",sharedPreferences.getString("user_type","")).AddParams("id", arrayList_item.get((Integer) v.getTag()).post_id+"");
                    BufferedReader bufferedReader;

                    if(!arrayList_item.get((Integer) v.getTag()).liked){ // 좋아요 올리기
                        connect_server.AddParams("flag", "1");
                        bufferedReader = connect_server.Connect(false);
                        try {
                            connect_server.Buffer_read(bufferedReader);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else{ // 좋아요 삭제
                        connect_server.AddParams("flag", "2");
                        bufferedReader = connect_server.Connect(true);
                        try {
                            connect_server.Buffer_read(bufferedReader);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    // list 데이터 비우
                    arrayList_item.clear();


                    // 게시물 새로고침
                    Get_post();
                }
            });

            circleImageView.setTag(position);
            circleImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 프로필 들어가기
                    Intent intent = new Intent(getContext(), UserProfile.class);
                    intent.putExtra("mail", arrayList_item.get((Integer) view.getTag()).mail);
                    intent.putExtra("name", arrayList_item.get((Integer) view.getTag()).profile_name);
                    startActivity(intent);
                }
            });


            return convertView;
        }
    }


    class Get_Profile_Image extends AsyncTask<Void, Void, InputStream>{
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
