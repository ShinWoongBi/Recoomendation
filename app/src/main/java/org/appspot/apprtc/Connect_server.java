package org.appspot.apprtc;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * Created by kippe on 2017-10-08.
 */

public class Connect_server {
    String url = "";
    String param = "";
    int param_c = 0;

    public void SetUrl(String Url){
        this.url = Url;
    } // URL 설정

    public void AddParams(String key, String value) { // Parameter 설정
        String result = key+"="+value;
        if(param_c == 0)
            param += result;
        else
            param += "&"+result;

        param_c++;
    }

    static class Async extends AsyncTask<String, BufferedReader, BufferedReader>{
        String url = "";
        String param = "";
        Async(String url, String param){
            this.url = url;
            this.param = param;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected BufferedReader doInBackground(String... strings) {
            URL URL = null;
            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;

            try{
                URL = new URL(url);
                httpURLConnection = (HttpURLConnection)URL.openConnection();
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("POST");

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(param.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
                bufferedReader = new BufferedReader(inputStreamReader);

            }catch (Exception e){
                e.printStackTrace();
            }
            return bufferedReader;
        }

        @Override
        protected void onPostExecute(BufferedReader s) {

            super.onPostExecute(s);
        }
    }

    public void Buffer_read(BufferedReader bufferedReader) throws Exception { // bufferedReader 로깅

        String buffer = "";
        int count = 1;

        Log.d("Connect_server.class", "***************HTTP 통신 결과***************");
        Log.d("URL",url);
        Log.d("param", param);
        bufferedReader.mark(1000);
        while((buffer = bufferedReader.readLine()) != null){
            Log.d(count+"번 줄", buffer);
            count++;
        }
        bufferedReader.reset();
        Log.d("Connect_server.class","********************************************");

    }


    public BufferedReader Connect(Boolean log){ // 통신 시작
        if(log) {
            Log.d("Connect_server.class", "***************HTTP 통신 시작***************");
            Log.d("URL", url);
            Log.d("param", param);
            Log.d("Connect_server.class", "********************************************");
        }

        Async async = new Async(url,param);
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = async.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return bufferedReader; // bufferedReader리턴
    }


}
