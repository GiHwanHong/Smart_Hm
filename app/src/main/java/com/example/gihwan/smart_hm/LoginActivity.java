package com.example.gihwan.smart_hm;

import android.app.ActivityGroup;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.kakao.kakaolink.KakaoLink;
import com.kakao.util.KakaoParameterException;

/**
 * Created by GiHwan on 2017. 12. 27..
 */

public class LoginActivity extends ActivityGroup {

    TabHost tabhost1;
    String usr_id_recv = "";
    String usr_code_recv = "";

    // 뒤로 가기 버튼 제어사용 변수
    long first_time;
    long second_time;

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

        tabhost1.addTab(tabhost1.newTabSpec("SensorControl")                      // newTabSpec - TabSpec 객체는 탭버튼의 형태와 탭의 내용을 관리하는 객체입니다.
                .setIndicator(getString(R.string.tab1))                     // TabSpec 객체를 생성할때 사용할 tag 이름을 지정합니다.
                .setContent(new Intent(this, ControlActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)));
        tabhost1.addTab(tabhost1.newTabSpec("Chart")
                .setIndicator(getString(R.string.tab2))
                .setContent(new Intent(this, ChartActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)));
        tabhost1.addTab(tabhost1.newTabSpec("Mypage")
                .setIndicator(getString(R.string.tab3))
                .setContent(new Intent(this,MypageActivity.class).putExtra("usr_id_recv",usr_id_recv).putExtra("usr_code_recv",usr_code_recv).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)));
        createTab();
        //추가한 라인
        FirebaseMessaging.getInstance().subscribeToTopic("news");
        FirebaseInstanceId.getInstance().getToken();
    }

    public void createTab() {
        // 선택되지 않은 것을 처리한다
        tabhost1.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {

                switch (tabId) {
                    case "SensorControl":

                        com.nispok.snackbar.Snackbar.with(getApplicationContext())
                                .text("이 곳에서는 센서를 제어할 수 있습니다")
                                .show(LoginActivity.this);
                        break;
                    case "Chart":
                        com.nispok.snackbar.Snackbar.with(getApplicationContext())
                                .text("이 곳에서는 전기 사용량을 확인 할 수 있습니다")
                                .show(LoginActivity.this);
                        break;
                    case "Mypage":
                        com.nispok.snackbar.Snackbar.with(getApplicationContext())
                                .text("회원의 정보를 수정 및 발급받은 코드를 전송할 수 있습니다.")
                                .show(LoginActivity.this);
                        break;

                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        second_time = System.currentTimeMillis();
        Toast.makeText(LoginActivity.this, "앱을 종료하시려면 한번 더 누르세요 ", Toast.LENGTH_SHORT).show();
        if(second_time - first_time < 2000){
            super.onBackPressed();
            finishAffinity();
        }

    }
}