package org.appspot.apprtc.collection;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import org.appspot.apprtc.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by kippe_000 on 2017-10-27.
 */

public class EditActivity extends AppCompatActivity {
    String message;
    RelativeLayout main_layout, menu_layout;
    TextView font1,font2,font3,font4,font5,font6,font7,font8,font9;
    ImageButton color1, color2, color3, color4, color5, color6, color7, color8;
    TextView day_T, month_T, year_T, month_year_T;
    ImageButton color_btn, text_size_btn, location_btn,  font_btn;
    TextView text_size_monitor;
    Data data;
    String fonts[] = {"fonts/font1.ttf","fonts/font2.ttf","fonts/font3.ttf","fonts/font4.ttf","fonts/font5.ttf"
                        ,"fonts/font6.ttf","fonts/font7.ttf","fonts/font8.ttf","fonts/font9.ttf"};
    CollectionDataBase collectionDataBase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_activity);

        // 초기화
        data = new Data();
        collectionDataBase = new CollectionDataBase(getApplicationContext(), "recommendation.db", null, 1);
        SeekBar seekBar = (SeekBar)findViewById(R.id.seekBar);
        main_layout = (RelativeLayout)findViewById(R.id.main_layout);
        menu_layout = (RelativeLayout)findViewById(R.id.menu_layout);
        color_btn = (ImageButton)findViewById(R.id.color_btn);
        text_size_btn = (ImageButton)findViewById(R.id.text_size_btn);
        location_btn = (ImageButton)findViewById(R.id.location_btn);
        font_btn = (ImageButton)findViewById(R.id.font_btn);
        text_size_monitor = (TextView)findViewById(R.id.text_size_monitor);
        Button location_back = (Button)findViewById(R.id.location_back);
        Button location_next = (Button)findViewById(R.id.location_next);
        Button done_btn = (Button)findViewById(R.id.done_btn);
        day_T = (TextView)findViewById(R.id.day_T);
        month_year_T = (TextView)findViewById(R.id.month_year_T);
//        month_T = (TextView)findViewById(R.id.month_T);
//        year_T  = (TextView)findViewById(R.id.year_T);
        color1 = (ImageButton) findViewById(R.id.color_btn1);
        color2 = (ImageButton)findViewById(R.id.color_btn2);
        color3 = (ImageButton)findViewById(R.id.color_btn3);
        color4 = (ImageButton)findViewById(R.id.color_btn4);
        color5 = (ImageButton)findViewById(R.id.color_btn5);
        color6 = (ImageButton)findViewById(R.id.color_btn6);
        color7 = (ImageButton)findViewById(R.id.color_btn7);
        color8 = (ImageButton)findViewById(R.id.color_btn8);
        font1 = (TextView)findViewById(R.id.font1_T);
        font2 = (TextView)findViewById(R.id.font2_T);
        font3 = (TextView)findViewById(R.id.font3_T);
        font4 = (TextView)findViewById(R.id.font4_T);
        font5 = (TextView)findViewById(R.id.font5_T);
        font6 = (TextView)findViewById(R.id.font6_T);
        font7 = (TextView)findViewById(R.id.font7_T);
        font8 = (TextView)findViewById(R.id.font8_T);
        font9 = (TextView)findViewById(R.id.font9_T);



        // intent 메시지 입력
        Intent intent = getIntent();
        message = intent.getStringExtra("data");
        data.message = message;



        // 리스너 연결
        done_btn.setOnClickListener(done_btn_listener);
        color1.setOnClickListener(colorOnclickListener);
        color2.setOnClickListener(colorOnclickListener);
        color3.setOnClickListener(colorOnclickListener);
        color4.setOnClickListener(colorOnclickListener);
        color5.setOnClickListener(colorOnclickListener);
        color6.setOnClickListener(colorOnclickListener);
        color7.setOnClickListener(colorOnclickListener);
        color8.setOnClickListener(colorOnclickListener);
        color_btn.setOnClickListener(menuOnClickListener);
        text_size_btn.setOnClickListener(menuOnClickListener);
        location_btn.setOnClickListener(menuOnClickListener);
        font_btn.setOnClickListener(menuOnClickListener);
        seekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
        location_back.setOnClickListener(buttonOnClickListener);
        location_next.setOnClickListener(buttonOnClickListener);
        font1.setOnClickListener(textOnClickListener);
        font2.setOnClickListener(textOnClickListener);
        font3.setOnClickListener(textOnClickListener);
        font4.setOnClickListener(textOnClickListener);
        font5.setOnClickListener(textOnClickListener);
        font6.setOnClickListener(textOnClickListener);
        font7.setOnClickListener(textOnClickListener);
        font8.setOnClickListener(textOnClickListener);
        font9.setOnClickListener(textOnClickListener);



        // 툴버튼 생성
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");


        // 뷰 크기 변경 (정사각형)
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        ViewGroup.LayoutParams params = main_layout.getLayoutParams();
        params.width = width;
        params.height = width;
        main_layout.setLayoutParams(params);


        // 폰트설정
        FirstFontSet();

        // 메시지 초기 설정
        FirstMessageSet();

        // 날짜 초기 설정
        FirtstdataSet();

        // data 초기 설정
        data.background = 1;
        data.font = 0;
    }

    void FirstFontSet(){
        font1.setTypeface(Typeface.createFromAsset(getAssets(), fonts[0]));
        font2.setTypeface(Typeface.createFromAsset(getAssets(), fonts[1]));
        font3.setTypeface(Typeface.createFromAsset(getAssets(), fonts[2]));
        font4.setTypeface(Typeface.createFromAsset(getAssets(), fonts[3]));
        font5.setTypeface(Typeface.createFromAsset(getAssets(), fonts[4]));
        font6.setTypeface(Typeface.createFromAsset(getAssets(), fonts[5]));
        font7.setTypeface(Typeface.createFromAsset(getAssets(), fonts[6]));
        font8.setTypeface(Typeface.createFromAsset(getAssets(), fonts[7]));
        font9.setTypeface(Typeface.createFromAsset(getAssets(), fonts[8]));

        for(int i = 0; i < data.location.length; i++){
            for(int l = 0; l < data.location[i].length; l++){
                ((TextView)findViewById(data.location[i][l])).setTypeface(Typeface.createFromAsset(getAssets(),fonts[0]));
            }
        }
    }

    // 날짜 초기 설정
    void FirtstdataSet(){
        Calendar calendar = Calendar.getInstance();
        java.util.Date date = calendar.getTime();

        String day = (new SimpleDateFormat("dd").format(date));
        String month = (new SimpleDateFormat("MMM").format(date));
        String year = (new SimpleDateFormat("yyyy").format(date));
        String month_year = month+"\n"+year;

        day_T.setText(day);
        month_year_T.setText(month_year);

        data.day = day;
        data.month_year = month+"\\n"+year;
    }

    // 메시지 초기 설정
    void FirstMessageSet(){

        float size = ((TextView)findViewById(data.location[1][1])).getTextSize();
        text_size_monitor.setText(((int)size)+"px");
        data.size = size;

        for(int i = 0; i < data.location.length; i++){
            for(int l = 0; l < data.location[i].length; l++){
                ((TextView)findViewById(data.location[i][l])).setText(message);
                ((TextView)findViewById(data.location[i][l])).setTextSize(size);
            }
        }
        ((TextView)findViewById(data.location[1][1])).setVisibility(View.VISIBLE);

    }

    // 버튼 비율 코딩
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        ViewGroup.LayoutParams params;

    }

    // DONE버튼 리스너
    Button.OnClickListener done_btn_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // DB에 저장
            collectionDataBase.insert(data);
            finish();
        }
    };

    // 위치 변경 버튼 리스너
    Button.OnClickListener buttonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int x = data.x;
            int y = data.y;

            ((TextView)findViewById(data.location[x][y])).setVisibility(View.INVISIBLE);

            if(v.getId() == R.id.location_back){ // 이전 버튼
                if(x == 0) {
                    if(y == 0)
                        y = 2;
                    else
                        y--;
                    x = 2;
                }else
                    x--;
            }else{ // 다음 버튼
                if(x == 2) {
                    if(y == 2) {
                        y = 0;
                    }else
                        y++;
                    x = 0;
                }else
                    x++;
            }

            data.x = x;
            data.y = y;

            Log.d("x", data.x+"");
            Log.d("y", data.y+"");

            ((TextView)findViewById(data.location[data.x][data.y])).setVisibility(View.VISIBLE);
        }
    };

    // 폰트 변경 리스너
    TextView.OnClickListener textOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int flag = 0;
            switch (v.getId()){
                case R.id.font1_T:
                    flag = 0;
                    break;
                case R.id.font2_T:
                    flag = 1;
                    break;
                case R.id.font3_T:
                    flag = 2;
                    break;
                case R.id.font4_T:
                    flag = 3;
                    break;
                case R.id.font5_T:
                    flag = 4;
                    break;
                case R.id.font6_T:
                    flag = 5;
                    break;
                case R.id.font7_T:
                    flag = 6;
                    break;
                case R.id.font8_T:
                    flag = 7;
                    break;
                case R.id.font9_T:
                    flag = 8;
                    break;
            }
            data.font = flag;
            for(int i = 0; i < data.location.length; i++){
                for(int l = 0; l < data.location[i].length; l++){
                    ((TextView)findViewById(data.location[i][l])).setTypeface(Typeface.createFromAsset(getAssets(),fonts[flag]));
                }
            }
        }
    };

    // 텍스트 크기 조절 seekbar
    SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            float size = 30f;

            Log.d("i",i+"");

            i -= 5;
            Log.d("i",i+"");
            i = i * 5;

            Log.d("i", i+"");


            size += i;

            Log.d("size", size+"");
            text_size_monitor.setText(((int) size)+"px");
            for(int j = 0; j < data.location.length; j++){
                for(int l = 0; l < data.location[j].length; l++){
                    ((TextView)findViewById(data.location[j][l])).setTextSize(size);
                }
            }

            data.size = size;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            Log.d("i","sadf:");

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            Log.d("p","ppp");

        }
    };

    // 메뉴 레이아웃 화면 띄우기 버튼 리스너
    ImageButton.OnClickListener menuOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LinearLayout text_location_layout = (LinearLayout)findViewById(R.id.text_location_layout);
            LinearLayout color_layout = (LinearLayout)findViewById(R.id.color_layout);
            LinearLayout text_size_layout = (LinearLayout)findViewById(R.id.text_size_layout);
            LinearLayout font_layout = (LinearLayout)findViewById(R.id.font_layout);

            switch (v.getId()){
                case R.id.color_btn:

                    text_size_layout.setVisibility(View.INVISIBLE);
                    font_layout.setVisibility(View.INVISIBLE);
                    text_location_layout.setVisibility(View.INVISIBLE);


                    if(color_layout.getVisibility() == View.INVISIBLE)
                        color_layout.setVisibility(View.VISIBLE);
                    else
                        color_layout.setVisibility(View.INVISIBLE);


                    break;
                case R.id.text_size_btn:

                    color_layout.setVisibility(View.INVISIBLE);
                    font_layout.setVisibility(View.INVISIBLE);
                    text_location_layout.setVisibility(View.INVISIBLE);

                    if(text_size_layout.getVisibility() == View.INVISIBLE)
                        text_size_layout.setVisibility(View.VISIBLE);
                    else
                        text_size_layout.setVisibility(View.INVISIBLE);


                    break;
                case R.id.location_btn:
                    color_layout.setVisibility(View.INVISIBLE);
                    text_size_layout.setVisibility(View.INVISIBLE);
                    font_layout.setVisibility(View.INVISIBLE);

                    if(text_location_layout.getVisibility() == View.VISIBLE)
                        text_location_layout.setVisibility(View.INVISIBLE);
                    else
                        text_location_layout.setVisibility(View.VISIBLE);
                    break;
                case R.id.font_btn:
                    color_layout.setVisibility(View.INVISIBLE);
                    text_size_layout.setVisibility(View.INVISIBLE);
                    text_location_layout.setVisibility(View.INVISIBLE);

                    if(font_layout.getVisibility() == View.INVISIBLE)
                        font_layout.setVisibility(View.VISIBLE);
                    else
                        font_layout.setVisibility(View.INVISIBLE);

                    break;
            }
        }
    };

    // 배경 변경 리스너
    ImageButton.OnClickListener colorOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {


            int flag = 0;
            Log.d("god","gp");

            switch (view.getId()){
                case R.id.color_btn1:
                    flag = 1;
                    break;
                case R.id.color_btn2:
                    flag = 2;

                    break;
                case R.id.color_btn3:
                    flag = 3;
                    break;
                case R.id.color_btn4:
                    flag = 4;
                    break;
                case R.id.color_btn5:
                    flag = 5;
                    break;
                case R.id.color_btn6:
                    flag = 6;
                    break;
                case R.id.color_btn7:
                    flag = 7;
                    break;
                case R.id.color_btn8:
                    flag = 8;
                    break;

            }
            data.background = flag;
            background(flag);

        }
    };

    // 배경 변경 메소드
    void background(int flag){
        String text_color = "#";
        switch (flag){
            case 1:
                text_color = "#0F0F0F";

                day_T.setTextColor(Color.parseColor("#0F0F0F"));
                month_year_T.setTextColor(Color.parseColor("#0F0F0F"));
//                month_T.setTextColor(Color.parseColor("#0F0F0F"));
//                year_T.setTextColor(Color.parseColor("#0F0F0F"));
                break;
            case 2:
                main_layout.setBackgroundColor(Color.parseColor("#F5837B"));
                text_color = "#F0FDC3";

                day_T.setTextColor(Color.parseColor("#F0FDC3"));
                month_year_T.setTextColor(Color.parseColor("#F0FDC3"));
//                month_T.setTextColor(Color.parseColor("#F0FDC3"));
//                year_T.setTextColor(Color.parseColor("#F0FDC3"));

                break;
            case 3:
                main_layout.setBackgroundColor(Color.parseColor("#F7B959"));
                text_color = "#3B2E1A";

                day_T.setTextColor(Color.parseColor("#3B2E1A"));
                month_year_T.setTextColor(Color.parseColor("#3B2E1A"));
//                month_T.setTextColor(Color.parseColor("#3B2E1A"));
//                year_T.setTextColor(Color.parseColor("#3B2E1A"));
                break;
            case 4:
                main_layout.setBackgroundColor(Color.parseColor("#5D9D8D"));
                text_color = "#FAFFFF";

                day_T.setTextColor(Color.parseColor("#FAFFFF"));
                month_year_T.setTextColor(Color.parseColor("#FAFFFF"));
//                month_T.setTextColor(Color.parseColor("#FAFFFF"));
//                year_T.setTextColor(Color.parseColor("#FAFFFF"));
                break;
            case 5:
                main_layout.setBackgroundColor(Color.parseColor("#488B9F"));
                text_color = "#FAFFFF";

                day_T.setTextColor(Color.parseColor("#FAFFFF"));
                month_year_T.setTextColor(Color.parseColor("#FAFFFF"));
//                month_T.setTextColor(Color.parseColor("#FAFFFF"));
//                year_T.setTextColor(Color.parseColor("#FAFFFF"));
                break;
            case 6:
                main_layout.setBackgroundColor(Color.parseColor("#867896"));
                text_color = "#FAFFFF";

                day_T.setTextColor(Color.parseColor("#FAFFFF"));
                month_year_T.setTextColor(Color.parseColor("#FAFFFF"));
//                month_T.setTextColor(Color.parseColor("#FAFFFF"));
//                year_T.setTextColor(Color.parseColor("#FAFFFF"));
                break;
            case 7:
                main_layout.setBackgroundColor(Color.parseColor("#C9A3BA"));
                text_color = "#FAFFFF";

                day_T.setTextColor(Color.parseColor("#FAFFFF"));
                month_year_T.setTextColor(Color.parseColor("#FAFFFF"));
//                month_T.setTextColor(Color.parseColor("#FAFFFF"));
//                year_T.setTextColor(Color.parseColor("#FAFFFF"));
                break;
            case 8:
                main_layout.setBackgroundColor(Color.parseColor("#333333"));
                text_color = "#FAFFFF";

                day_T.setTextColor(Color.parseColor("#FAFFFF"));
                month_year_T.setTextColor(Color.parseColor("#FAFFFF"));
//                month_T.setTextColor(Color.parseColor("#FAFFFF"));
//                year_T.setTextColor(Color.parseColor("#FAFFFF"));
                break;
        }

        for(int i = 0; i < data.location.length; i++){
            for(int l = 0; l < data.location[i].length; l++){
                ((TextView)findViewById(data.location[i][l])).setTextColor(Color.parseColor(text_color));
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
