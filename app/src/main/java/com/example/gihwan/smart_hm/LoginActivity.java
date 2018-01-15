package com.example.gihwan.smart_hm;

import android.app.ActivityGroup;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.util.KakaoParameterException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by GiHwan on 2017. 12. 27..
 */

public class LoginActivity extends ActivityGroup {

    TabHost tabhost1;
    private KakaoLink kakaoLink; // 카카오톡 메신저를 사용하기 위해 선언해놓은 변수
    EditText Mypage_id;


    String usr_id_recv = "";
    String usr_code_recv = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent intent = getIntent();
        usr_id_recv = intent.getExtras().getString("usr_id");
        usr_code_recv = intent.getExtras().getString("usr_code");
        Log.e("usr_id_recv_Login", usr_id_recv);
        Log.e("usr_code_recv_Login", usr_code_recv);

        tabhost1 = (TabHost) findViewById(R.id.tabhost1);
        tabhost1.setup(getLocalActivityManager());

        tabhost1.addTab(tabhost1.newTabSpec("tab1tag")                      // newTabSpec - TabSpec 객체는 탭버튼의 형태와 탭의 내용을 관리하는 객체입니다.
                .setIndicator(getString(R.string.tab1))                     // TabSpec 객체를 생성할때 사용할 tag 이름을 지정합니다.
                .setContent(new Intent(this, StateActivity.class)));
        tabhost1.addTab(tabhost1.newTabSpec("tab2tag")
                .setIndicator(getString(R.string.tab2))
                .setContent(new Intent(this, ChartActivity.class)));
        tabhost1.addTab(tabhost1.newTabSpec("tab3tag")
                .setIndicator(getString(R.string.tab3))
                .setContent(new Intent(this,MypageActivity.class).putExtra("usr_id_recv",usr_id_recv).putExtra("usr_code_recv",usr_code_recv)));

        createTab();



        try {
            kakaoLink = KakaoLink.getKakaoLink(LoginActivity.this);
        } catch (KakaoParameterException e) {
            e.printStackTrace();
        }

    }

    public void createTab() {

        tabhost1.setOnTabChangedListener(new TabHost.OnTabChangeListener() {


            @Override
            public void onTabChanged(String tabId) {
                switch (tabId) {
                    case "tab1tag":
                        com.nispok.snackbar.Snackbar.with(getApplicationContext())
                                .text("이 곳에서는 센서를 제어할 수 있습니다")
                                .show(LoginActivity.this);
                        break;
                    case "tab2tag":
                        com.nispok.snackbar.Snackbar.with(getApplicationContext())
                                .text("이 곳에서는 전기 사용량을 확인 할 수 있습니다")
                                .show(LoginActivity.this);
                        break;
                    case "tab3tag":
                        /*Send_Data send_data = new Send_Data();
                        send_data.execute();*/
                        com.nispok.snackbar.Snackbar.with(getApplicationContext())
                                .text("회원의 정보를 수정 및 발급받은 코드를 전송할 수 있습니다.")
                                .show(LoginActivity.this);
                        break;

                }
            }
        });
    }
/*
    // 보이지 않게 뒤에서 intent를 통해 값을 전달.
    public class Send_Data extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("잠시만 기다려 주세요.");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);
            progressDialog.dismiss();

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                for (int i = 0; i < 3; i++) {
                    progressDialog.setProgress(i * 30);
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

    }*/
}




    /*@Override                     // 툴바 사용시 사용할 것
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_kakao:
                final KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder
                        = kakaoLink.createKakaoTalkLinkMessageBuilder();
                try {
                    kakaoTalkLinkMessageBuilder.addText("전송된 코드는 \n => ");
                    kakaoLink.sendMessage(kakaoTalkLinkMessageBuilder, this);   // 메시지 전송
                } catch (KakaoParameterException e) {
                    e.printStackTrace();
                }
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }*/
