package com.example.gihwan.smart_hm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends Activity {
    LinearLayout show1 , show2 , show3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //startActivity(new Intent(this,SplashActivity.class));
        setContentView(R.layout.activity_main);

        show1 = (LinearLayout)findViewById(R.id.show1);
        show2 = (LinearLayout)findViewById(R.id.show2);
        show3 = (LinearLayout)findViewById(R.id.show3);

        show1.setVisibility(View.VISIBLE);
        show2.setVisibility(View.GONE);
        show3.setVisibility(View.GONE);

    }

    public void Main_Btn_click(View v){
        switch (v.getId()){
            case R.id.house_holder:             // 초기화면시 세대주 버튼 클릭시 처리
                show1.setVisibility(View.GONE);
                show2.setVisibility(View.VISIBLE);
                show3.setVisibility(View.GONE);
                break;
            case R.id.house_member:             // 초기화면시 구성원 버튼 클릭시 처리
                show1.setVisibility(View.GONE);
                show2.setVisibility(View.GONE);
                show3.setVisibility(View.VISIBLE);
                break;
            case R.id.join_btn:                 // 세대주 화면의 회원가입 버튼 클릭시 처리
                startActivity(new Intent(this,JoinActivity.class));
                break;
            case R.id.login_btn:                // 세대주 화면의 로그인 버튼 클릭시 처리
                startActivity(new Intent(this,LoginActivity.class));
                finish();
                break;
            case R.id.login_member:             // 구성원 화면의 로그인 버튼 클릭시 처리
                startActivity(new Intent(this,LoginActivity.class));
                finish();
                break;
        }
    }
}
