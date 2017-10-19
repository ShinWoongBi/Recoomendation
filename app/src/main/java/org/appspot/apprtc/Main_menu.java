package org.appspot.apprtc;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;

import org.appspot.apprtc.board.Main;

import java.util.ArrayList;

/**
 * Created by kippe_000 on 2017-10-05.
 */

public class Main_menu extends AppCompatActivity {
    ViewPager viewPager;
    Button left_btn, center_btn, right_btn;
    DrawerLayout drawerLayout;
    LinearLayout linearLayout;
    LoginButton loginButton;
    CallbackManager callbackManager;
    ActionBar actionBar;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        left_btn = (Button)findViewById(R.id.left_btn);
        center_btn = (Button)findViewById(R.id.center_btn);
        right_btn = (Button)findViewById(R.id.right_btn);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        viewPager = (ViewPager)findViewById(R.id.viewpager);
        loginButton = (LoginButton)findViewById(R.id.login_button);

        // 탭 버튼 리스너 적용
        left_btn.setOnClickListener(btn_listener);
        center_btn.setOnClickListener(btn_listener);
        right_btn.setOnClickListener(btn_listener);

        // 페이스북 로그아웃 CallBack
        callbackManager = CallbackManager.Factory.create();
        loginButton.unregisterCallback(callbackManager);

        // ViewPager 리스터 적용
        viewPager.addOnPageChangeListener(onPageChangeListener);


        // ViewPager Adapter 적용
        final ArrayList<Fragment> fragment_list = new ArrayList<>();
        fragment_list.add(new org.appspot.apprtc.collection.Main());
        fragment_list.add(new org.appspot.apprtc.chatbot.Main());
        fragment_list.add(new Main());
        viewPager.setAdapter(new FragmentPagerAdapter(this.getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragment_list.get(position);
            }

            @Override
            public int getCount() {
                return fragment_list.size();
            }
        });

        // DrawerLayout 그림자 적용
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        // 툴버튼 생성
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);


        // 1번 탭 이동
        viewPager.setCurrentItem(1);

        // 프로필 가져오기
        Get_Profile();


        // TcpService 시작
        if(!isServiceRunning("org.appspot.apprtc.TcpService")){
            Intent TcpService = new Intent(Main_menu.this, org.appspot.apprtc.TcpService.class);

            startService(TcpService);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        callbackManager.onActivityResult(requestCode, resultCode, data);
        Log.d("requestCode",requestCode+"");
        Log.d("resultCode", resultCode+"");
        super.onActivityResult(requestCode, resultCode, data);
    }

    public Boolean isServiceRunning(String serviceName) {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo runningServiceInfo : activityManager.getRunningServices(Integer.MAX_VALUE)) {

            if (serviceName.equals(runningServiceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }




    void Get_Profile(){
        Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath()+"/recommendation/.my_picture.jpg");

        de.hdodenhof.circleimageview.CircleImageView circleImageView = (de.hdodenhof.circleimageview.CircleImageView)findViewById(R.id.profile_image);
        circleImageView.setImageBitmap(bitmap);

        SharedPreferences sharedPreferences = getSharedPreferences("Profile", MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "");
        String age = sharedPreferences.getString("age","");
        String gender = sharedPreferences.getString("gender","");
        if(gender.equals("1")){
            gender = "남자";
        }else if(gender.equals("2")){
            gender = "여자";
        }else{
            gender = "??";
        }



        TextView name_T, age_T, gender_T;
        name_T = (TextView)findViewById(R.id.name_T);
        age_T = (TextView)findViewById(R.id.age_T);
        gender_T = (TextView)findViewById(R.id.gender_T);

        name_T.setText(name+"");
        age_T.setText(age+"세");
        gender_T.setText(gender+"");;
    }


    // 탭 버튼 리스너
    Button.OnClickListener btn_listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.left_btn:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.center_btn:
                    viewPager.setCurrentItem(1);

                    break;
                case R.id.right_btn:
                    viewPager.setCurrentItem(2);

                    break;
            }
        }
    };

    // ViewPager 리스너
    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position){
                case 0:
                    left_btn.setBackgroundColor(Color.parseColor("#CCCCCC"));
                    center_btn.setBackgroundColor(Color.parseColor("#ffffff"));
                    right_btn.setBackgroundColor(Color.parseColor("#ffffff"));
                    setTitle("Collection");

                    break;
                case 1:
                    left_btn.setBackgroundColor(Color.parseColor("#ffffff"));
                    center_btn.setBackgroundColor(Color.parseColor("#CCCCCC"));
                    right_btn.setBackgroundColor(Color.parseColor("#ffffff"));
                    setTitle("Chatbot");
                    break;
                case 2:
                    left_btn.setBackgroundColor(Color.parseColor("#ffffff"));
                    center_btn.setBackgroundColor(Color.parseColor("#ffffff"));
                    right_btn.setBackgroundColor(Color.parseColor("#CCCCCC"));
                    setTitle("Community");

                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
