package org.appspot.apprtc;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;

/**
 * Created by kippe_000 on 2017-10-04.
 */

public class Login extends AppCompatActivity{
    CallbackManager callbackManager = null;
    String fb_name = "";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        setTitle("로그인");



        SharedPreferences sharedPreferences = getSharedPreferences("Profile", MODE_PRIVATE);
        if(!sharedPreferences.getString("login","").equals("")){
            finish();
            startActivity(new Intent(Login.this, org.appspot.apprtc.Main_menu.class));
        }

        if (ContextCompat.checkSelfPermission(Login.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(Login.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }

        progressDialog = new ProgressDialog(Login.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("초기 설정");
        progressDialog.setMessage("프로필 설정중...");
        final Handler handler = new Handler();

        LoginButton loginButton = null;

        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile","email"));


        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        progressDialog.show();




                        Connect_server connect_server = new Connect_server(); // http통신 클래스

                        String fb_id = loginResult.getAccessToken().getUserId();
                        String fb_token =loginResult.getAccessToken().getToken();

                        GraphRequest request =GraphRequest.newMeRequest(loginResult.getAccessToken() , // 페이스북 이름 가져오기
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                    }
                                });
                        try {
                            GraphResponse graphResponse = request.executeAsync().get().get(0);
                            fb_name = graphResponse.getJSONObject().getString("name");
                        } catch (Exception e){
                            e.printStackTrace();
                        }



                        connect_server.SetUrl("http://tlsdndql27.vps.phps.kr/recommendation/login/login.php"); // http통신할 서버 주소
                        connect_server.AddParams("fb_id", fb_id); // 파라미터 추가
                        connect_server.AddParams("fb_token", fb_token);
                        connect_server.AddParams("fb_name", fb_name);
                        connect_server.AddParams("flag", "1");
                        BufferedReader bufferedReader = connect_server.Connect(false);
                        try {
                            connect_server.Buffer_read(bufferedReader);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            SetProfile(fb_id, fb_name, fb_token, 1); // 유저 프로필 로컬 설정
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Log.d("param", connect_server.param);
                        Log.d("url", connect_server.url);





                    }

                    @Override
                    public void onCancel() {
                        Log.e("취소","cansel");
                    }

                    @Override
                    public void onError(FacebookException error) {

                    }
                });

    }

    public void SetProfile(final String id, String name, String token, int user_type) throws IOException { // 유저 프로필 로컬 설정

        if(user_type == 1) { // 페이스북 유저 프로필 사진 가져오기

            Thread thread = new Thread(new Runnable() { // 사용자 프로필 사진 가져오기
                @Override
                public void run() {
                    URL url = null;
                    Bitmap bitmap = null;

                    try {
                        url = new URL("https://graph.facebook.com/" + id + "/picture");

                        URLConnection conn = url.openConnection();
                        conn.connect();
                        final BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
                        bitmap = BitmapFactory.decodeStream(bis);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //비트맵 로컬 사진 저장
                    File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/recommendation");
                    if (!dir.exists())
                    if (!dir.exists())
                        dir.mkdir();

                    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/recommendation/.my_picture.jpg");

                    try {
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    UpLoadImage upLoadImage = new UpLoadImage(Environment.getExternalStorageDirectory().getAbsolutePath()+"/recommendation/.my_picture.jpg");
                    upLoadImage.execute();

                }
            });
            thread.start();

        }

        SharedPreferences sharedPreferences = getSharedPreferences("Profile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // 이름 저장
        editor.putString("name", name);

        // 아이디 저장
        editor.putString("mail", id);

        // 토큰 저장
        editor.putString("token", token);

        // 유저 타입 저장
        editor.putString("user_type", String.valueOf(user_type));

        // 로그인 유지
        editor.putString("login", "1");

        // 비동기 저장 방식
        editor.apply();


        // 나이 성별을 입력하지 않은 사람 찾기
        Connect_server connect_server = new Connect_server();
        connect_server.SetUrl("http://tlsdndql27.vps.phps.kr/recommendation/login/check.php");
        connect_server.AddParams("mail", id);
        BufferedReader bufferedReader = connect_server.Connect(true);
        int result = 0;
        String age = null,gender = null;
        String buffer = bufferedReader.readLine();

        if(!buffer.equals("0")){
            result = 1;
            try {
                JSONObject jsonObject = new JSONObject(buffer);
                age = jsonObject.getString("age");
                gender = jsonObject.getString("gender");
                Log.e("age", jsonObject.getString("age"));
                Log.e("gender", jsonObject.getString("gender"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else{
            result = 0;
        }

        editor.putString("age", age+"");
        editor.putString("gender", gender+"");
        editor.apply();

        progressDialog.dismiss();
        finish();

        if(result == 1){
            // 나이와 성별을 입력 했을 때
            startActivity(new Intent(Login.this, org.appspot.apprtc.Main_menu.class));
        }else{
            // 나이와 성별을 입력하지 않았을 때
            startActivity(new Intent(Login.this, org.appspot.apprtc.Set_first_profile.class));
        }



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }



    class UpLoadImage extends AsyncTask<String, String, String> {
        String file_path = null;
        String Server = "http://tlsdndql27.vps.phps.kr/recommendation/upload/my_image.php";

        UpLoadImage(String path){
            this.file_path = path;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection connection = null;
            URL url = null;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            File file = new File(file_path);
            if (!file.isFile()) {
                Log.e("uploadFile", "Source File not exist :"+ file_path);
            }else{
                try {
                    FileInputStream fileInputStream = new FileInputStream(file);
                    url = new URL(Server);

                    connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setUseCaches(false);
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Connection", "Keep-Alive");
                    connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                    connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                    connection.setRequestProperty("uploaded_file", file_path);

                    SharedPreferences sharedPreferences = getSharedPreferences("Profile", MODE_PRIVATE);
                    String id = sharedPreferences.getString("mail","");

                    DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
//                    outputStream.write(("mail="+sharedPreferences.getString("mail","")).getBytes("UTF-8"));
                    outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                    outputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\"; filename=\""+id+"\""+lineEnd);
                    outputStream.writeBytes(lineEnd);

                    int available = fileInputStream.available(); // 파일 크기
                    int bufferSize = Math.min(available, 1 * 1024 * 1024); // 1M와 선택한 파일 크기 비교해서 작은쪽 반환
                    byte[] buffer = new byte[bufferSize];

                    while((fileInputStream.read(buffer, 0 ,bufferSize)) > 0){
                        outputStream.write(buffer, 0 ,bufferSize);
                        available = fileInputStream.available();
                        bufferSize = Math.min(available, 1*1024*1024);
                        buffer = new byte[bufferSize];
                        Log.e("asfd","asdf");
                    }
                    outputStream.writeBytes(lineEnd);
                    outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                    outputStream.flush();

                    outputStream.close();


                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String a = reader.readLine();
                    Log.d("a", ":::" + a);
                    while((a=reader.readLine()) != null){
                        Log.d("a", ":::" + a);
                    }

                } catch (Exception e){
                    e.printStackTrace();
                }
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


        }
    }


}