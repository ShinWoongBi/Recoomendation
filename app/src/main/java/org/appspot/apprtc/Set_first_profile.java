package org.appspot.apprtc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by kippe on 2017-10-08.
 */

public class Set_first_profile extends AppCompatActivity {
    Button next_btn;
    EditText age_edit;
    CheckBox male_check, female_check;
    int age = 0;
    int gender = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_first_profile);

        setTitle("프로필 설정");

        next_btn = (Button)findViewById(R.id.next_btn);
        age_edit = (EditText) findViewById(R.id.age_edit);
        male_check = (CheckBox)findViewById(R.id.check1);
        female_check = (CheckBox)findViewById(R.id.check2);

        next_btn.setOnClickListener(btn_listener);
        male_check.setOnClickListener(check_listener);
        female_check.setOnClickListener(check_listener);
    }


    Button.OnClickListener btn_listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            age = Integer.parseInt(age_edit.getText().toString());


            if(age != 0 && gender != 0){
                SharedPreferences sharedPreferences = getSharedPreferences("Profile", MODE_PRIVATE);
                String mail = sharedPreferences.getString("mail","");
                String user_type = sharedPreferences.getString("user_type","");
                String token = sharedPreferences.getString("token", "");

                Connect_server connect_server = new Connect_server();
                connect_server.SetUrl("http://tlsdndql27.vps.phps.kr/recommendation/login/first_edit_profile.php");
                connect_server.AddParams("mail", mail);
                connect_server.AddParams("user_type", user_type);
                connect_server.AddParams("token", token);
                connect_server.AddParams("gender", gender+"");
                connect_server.AddParams("age", age+"");
                BufferedReader bufferedReader = connect_server.Connect(false);


                try {
                    if(bufferedReader.readLine().equals("1")){
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("age", age+"");
                        editor.putString("gender", gender+"");
                        editor.apply();

                        finish();
                        startActivity(new Intent(Set_first_profile.this, Main_menu.class));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }else{
                if(age == 0){
                    Toast.makeText(Set_first_profile.this, "나이를 입력해 주세요", Toast.LENGTH_SHORT).show();
                }else if(gender == 0){
                    Toast.makeText(Set_first_profile.this, "성별을 선택해 주세요", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(Set_first_profile.this, "모두 입력해 주세요", Toast.LENGTH_SHORT).show();
                }
            }


        }
    };

   CheckBox.OnClickListener check_listener = new View.OnClickListener() {
       @Override
       public void onClick(View view) {
           switch (view.getId()){
               case R.id.check1:
                   female_check.setChecked(false);
                   gender = 1;
                   break;
               case R.id.check2:
                   male_check.setChecked(false);
                   gender = 2;
                   break;
           }
       }
   };

}
