package org.appspot.apprtc.collection;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.appspot.apprtc.R;

/**
 * Created by kippe_000 on 2017-10-27.
 */

public class EditActivity extends AppCompatActivity {
    String data;
    RelativeLayout main_layout, menu_layout;
    TextView textView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_activity);

        Intent intent = getIntent();
        data = intent.getStringExtra("data");

        main_layout = (RelativeLayout)findViewById(R.id.main_layout);
        textView = (TextView)findViewById(R.id.data);
        menu_layout = (RelativeLayout)findViewById(R.id.menu_layout);


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




        textView.setText(data);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
