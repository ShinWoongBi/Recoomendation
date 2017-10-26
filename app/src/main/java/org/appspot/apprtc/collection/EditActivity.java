package org.appspot.apprtc.collection;

import android.content.Intent;
import android.graphics.Color;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import org.appspot.apprtc.R;

/**
 * Created by kippe_000 on 2017-10-27.
 */

public class EditActivity extends AppCompatActivity {
    String message;
    RelativeLayout main_layout, menu_layout;
    TextView top1,top2,top3,center1,center2,center3,bottom1,bottom2,bottom3;
    ImageButton color1, color2, color3, color4, color5, color6, color7, color8;
    TextView day_T, month_T, year_T;
    ImageButton color_btn, text_size_btn, gravity_btn;
    TextView text_size_monitor;
    Data data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_activity);

        // 메시지 입력
        Intent intent = getIntent();
        message = intent.getStringExtra("data");

        // 초기화
        data = new Data();
        SeekBar seekBar = (SeekBar)findViewById(R.id.seekBar);
        main_layout = (RelativeLayout)findViewById(R.id.main_layout);
        menu_layout = (RelativeLayout)findViewById(R.id.menu_layout);
        color_btn = (ImageButton)findViewById(R.id.color_btn);
        text_size_btn = (ImageButton)findViewById(R.id.text_size_btn);
        gravity_btn = (ImageButton)findViewById(R.id.gravity_btn);
        text_size_monitor = (TextView)findViewById(R.id.text_size_monitor);
        day_T = (TextView)findViewById(R.id.day_T);
        month_T = (TextView)findViewById(R.id.month_T);
        year_T  = (TextView)findViewById(R.id.year_T);
        color1 = (ImageButton) findViewById(R.id.color_btn1);
        color2 = (ImageButton)findViewById(R.id.color_btn2);
        color3 = (ImageButton)findViewById(R.id.color_btn3);
        color4 = (ImageButton)findViewById(R.id.color_btn4);
        color5 = (ImageButton)findViewById(R.id.color_btn5);
        color6 = (ImageButton)findViewById(R.id.color_btn6);
        color7 = (ImageButton)findViewById(R.id.color_btn7);
        color8 = (ImageButton)findViewById(R.id.color_btn8);
        top1 = (TextView)findViewById(R.id.data_top_left);
        top2 = (TextView)findViewById(R.id.data_top_center);
        top3 = (TextView)findViewById(R.id.data_top_right);
        center1 = (TextView)findViewById(R.id.data_center_left);
        center2 = (TextView)findViewById(R.id.data_center);
        center3 = (TextView)findViewById(R.id.data_center_right);
        bottom1 = (TextView)findViewById(R.id.data_bottom_left);
        bottom2 = (TextView)findViewById(R.id.data_bottom_center);
        bottom3 = (TextView)findViewById(R.id.data_bottom_right);




        // 리스너 연결
        color1.setOnClickListener(imageOnclickListener);
        color2.setOnClickListener(imageOnclickListener);
        color3.setOnClickListener(imageOnclickListener);
        color4.setOnClickListener(imageOnclickListener);
        color5.setOnClickListener(imageOnclickListener);
        color6.setOnClickListener(imageOnclickListener);
        color7.setOnClickListener(imageOnclickListener);
        color8.setOnClickListener(imageOnclickListener);
        color_btn.setOnClickListener(imageOnclickListener);
        text_size_btn.setOnClickListener(imageOnclickListener);
        gravity_btn.setOnClickListener(imageOnclickListener);
        seekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);


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




        // 메시지 초기 설정
        data.garvity = center2.getId();
        center2.setVisibility(View.VISIBLE);
        center2.setText(message);
        float size = center2.getTextSize();
        text_size_monitor.setText(((int)size)+"px");
        center2.setTextSize(size);


    }

    // 버튼 비율 코딩
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        ViewGroup.LayoutParams params;

        params = color_btn.getLayoutParams();
        params.height = color_btn.getHeight();
        params.width = color_btn.getHeight();
        color_btn.setLayoutParams(params);

//        LinearLayout text_size_layout = (LinearLayout)findViewById(R.id.text_size_layout);
//        params = text_size_layout.getLayoutParams();
//        params.height = text_size_layout.getHeight();
//        params.width = text_size_layout.getHeight();
//        text_size_layout.setLayoutParams(params);
    }

    // 텍스트 크기 조절 seekbar
    SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            float size = 40f;

            Log.d("i",i+"");

            i -= 5;
            Log.d("i",i+"");
            i = i * 8;

            Log.d("i", i+"");


            size += i;

            Log.d("size", size+"");
            text_size_monitor.setText(((int) size)+"px");
            ((TextView)findViewById(data.garvity)).setTextSize(size);
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

    // 이미지뷰 리스터
    ImageButton.OnClickListener imageOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            LinearLayout color_layout = (LinearLayout)findViewById(R.id.color_layout);
            LinearLayout text_size_layout = (LinearLayout)findViewById(R.id.text_size_layout);
            switch (view.getId()){
                case R.id.color_btn1:

                    main_layout.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    ((TextView)findViewById(data.garvity)).setTextColor(Color.parseColor("#0F0F0F"));

                    day_T.setTextColor(Color.parseColor("#0F0F0F"));
                    month_T.setTextColor(Color.parseColor("#0F0F0F"));
                    year_T.setTextColor(Color.parseColor("#0F0F0F"));
                    break;
                case R.id.color_btn2:
                    main_layout.setBackgroundColor(Color.parseColor("#F5837B"));
                    ((TextView)findViewById(data.garvity)).setTextColor(Color.parseColor("#F0FDC3"));

                    day_T.setTextColor(Color.parseColor("#F0FDC3"));
                    month_T.setTextColor(Color.parseColor("#F0FDC3"));
                    year_T.setTextColor(Color.parseColor("#F0FDC3"));

                    break;
                case R.id.color_btn3:
                    main_layout.setBackgroundColor(Color.parseColor("#F7B959"));
                    ((TextView)findViewById(data.garvity)).setTextColor(Color.parseColor("#3B2E1A"));

                    day_T.setTextColor(Color.parseColor("#3B2E1A"));
                    month_T.setTextColor(Color.parseColor("#3B2E1A"));
                    year_T.setTextColor(Color.parseColor("#3B2E1A"));
                    break;
                case R.id.color_btn4:
                    main_layout.setBackgroundColor(Color.parseColor("#5D9D8D"));
                    ((TextView)findViewById(data.garvity)).setTextColor(Color.parseColor("#FAFFFF"));

                    day_T.setTextColor(Color.parseColor("#FAFFFF"));
                    month_T.setTextColor(Color.parseColor("#FAFFFF"));
                    year_T.setTextColor(Color.parseColor("#FAFFFF"));
                    break;
                case R.id.color_btn5:
                    main_layout.setBackgroundColor(Color.parseColor("#488B9F"));
                    ((TextView)findViewById(data.garvity)).setTextColor(Color.parseColor("#FAFFFF"));

                    day_T.setTextColor(Color.parseColor("#FAFFFF"));
                    month_T.setTextColor(Color.parseColor("#FAFFFF"));
                    year_T.setTextColor(Color.parseColor("#FAFFFF"));
                    break;
                case R.id.color_btn6:
                    main_layout.setBackgroundColor(Color.parseColor("#867896"));
                    ((TextView)findViewById(data.garvity)).setTextColor(Color.parseColor("#FAFFFF"));

                    day_T.setTextColor(Color.parseColor("#FAFFFF"));
                    month_T.setTextColor(Color.parseColor("#FAFFFF"));
                    year_T.setTextColor(Color.parseColor("#FAFFFF"));
                    break;
                case R.id.color_btn7:
                    main_layout.setBackgroundColor(Color.parseColor("#C9A3BA"));
                    ((TextView)findViewById(data.garvity)).setTextColor(Color.parseColor("#FAFFFF"));

                    day_T.setTextColor(Color.parseColor("#FAFFFF"));
                    month_T.setTextColor(Color.parseColor("#FAFFFF"));
                    year_T.setTextColor(Color.parseColor("#FAFFFF"));
                    break;
                case R.id.color_btn8:
                    main_layout.setBackgroundColor(Color.parseColor("#333333"));
                    ((TextView)findViewById(data.garvity)).setTextColor(Color.parseColor("#FAFFFF"));

                    day_T.setTextColor(Color.parseColor("#FAFFFF"));
                    month_T.setTextColor(Color.parseColor("#FAFFFF"));
                    year_T.setTextColor(Color.parseColor("#FAFFFF"));
                    break;
                case R.id.color_btn:



                    if(color_layout.getVisibility() == View.GONE)
                        color_layout.setVisibility(View.VISIBLE);
                    else
                        color_layout.setVisibility(View.GONE);

                    if(text_size_layout.getVisibility() == View.VISIBLE)
                        text_size_layout.setVisibility(View.GONE);
                    break;
                case R.id.text_size_btn:

                    if(text_size_layout.getVisibility() == View.GONE)
                        text_size_layout.setVisibility(View.VISIBLE);
                    else
                        text_size_layout.setVisibility(View.GONE);


                    if(color_layout.getVisibility() == View.VISIBLE)
                        color_layout.setVisibility(View.GONE);
                    break;
                case R.id.gravity_btn:

                    break;
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
