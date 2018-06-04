package com.hoonyeee.android.server_test;

import android.content.ContentValues;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    TextView tv;
    String surl;
    String receiveMsg, parameter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.tv);

        surl = "http://192.168.219.110:8080/start/main";

        new AsyncTask<String, Void, Void>(){
            @Override
            public Void doInBackground(String... strings) {
                try {
                    String str;
                    URL url = new URL(strings[0]);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    conn.setRequestMethod("POST");//GET, POST 설정

                    //parameter 전송
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(conn.getOutputStream());
                    parameter = "id="+strings[0]+"&pwd="+strings[1];
                    outputStreamWriter.write(parameter);
                    outputStreamWriter.flush();

                    //통신성공하면 결과출력
                    if(conn.getResponseCode() == conn.HTTP_OK) {
                        InputStreamReader inputStreamReader = new InputStreamReader(conn.getInputStream(), "UTF-8");
                        BufferedReader br = new BufferedReader(inputStreamReader);
                        StringBuffer sb = new StringBuffer();

                        while ((str = br.readLine()) != null) {
                            sb.append(str);
                        }
                        receiveMsg = sb.toString();
                    } else {
                        Log.i("error: ", conn.getResponseCode()+"에러");
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(receiveMsg);
                return null;
            }
        }.execute(surl,"urls");
    }
}
