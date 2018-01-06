package com.example.gihwan.smart_hm;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import static android.Manifest.permission.READ_PHONE_STATE;
import static android.support.v7.widget.StaggeredGridLayoutManager.TAG;

public class MainActivity extends Activity {

    RelativeLayout main_show , show1 , show2 , show3;           // main 레이아웃 제어하기 위함
    EditText usr_id, usr_pw , usr_code;                         // 로그인 버튼 클릭시 main edittext의 값을 초기화 해주기 위함
    // 뒤로 가기 버튼 제어사용 변수
    long first_time;
    long second_time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boolean isGrantStorage = grantExternalStoragePermission();
        main_show = (RelativeLayout) findViewById(R.id.main_show);
        show1 = (RelativeLayout)findViewById(R.id.show1);
        show2 = (RelativeLayout)findViewById(R.id.show2);
        show3 = (RelativeLayout)findViewById(R.id.show3);
        usr_id = (EditText)findViewById(R.id.house_holderid);       // 세대주의 아이디를 사용하기 위함.
        usr_pw = (EditText)findViewById(R.id.house_holderpw);       // 세대주의 비밀번호를 사용하기 위함.
        usr_code = (EditText)findViewById(R.id.member_code);        // 구성원의 입력 코드를 사용하기 위함.

    }

    @Override
    public void onBackPressed() { // 뒤로 가기 버튼 제어

        second_time = System.currentTimeMillis();
        Toast.makeText(MainActivity.this, "앱을 종료하시려면 한번 더 누르세요 ", Toast.LENGTH_SHORT).show();
        if(second_time - first_time < 2000){
            super.onBackPressed();
            finishAffinity();
        }
        else{       // 뒤로가기 버튼 한번 클릭 시 초기 화면으로 돌리기 위함
            first_time = System.currentTimeMillis();

            main_show.setVisibility(View.VISIBLE);
            show1.setVisibility(View.GONE);
            show2.setVisibility(View.GONE);
            show3.setVisibility(View.GONE);
        }
    }

    public void Main_Btn_click(View v){

        switch (v.getId()){
            case R.id.main_login:                   // 메인 화면의 로그인 버튼 클릭 시 처리
                show1.setVisibility(View.VISIBLE);

                main_show.setVisibility(View.GONE);
                show2.setVisibility(View.GONE);
                show3.setVisibility(View.GONE);
                break;
            case R.id.main_signup:                 // 메인 화면의 회원가입 버튼 클릭 시 처리
                startActivity(new Intent(this,JoinActivity.class));
                main_show.setVisibility(View.VISIBLE);
                show1.setVisibility(View.GONE);
                show2.setVisibility(View.GONE);
                show3.setVisibility(View.GONE);
                break;

            case R.id.house_holder:             // 초기화면시 세대주 버튼 클릭시 처리
                show2.setVisibility(View.VISIBLE);

                main_show.setVisibility(View.GONE);
                show1.setVisibility(View.GONE);
                show3.setVisibility(View.GONE);
                break;

            case R.id.house_member:             // 초기화면시 구성원 버튼 클릭시 처리
                show3.setVisibility(View.VISIBLE);

                main_show.setVisibility(View.GONE);
                show1.setVisibility(View.GONE);
                show2.setVisibility(View.GONE);
                break;

            case R.id.login_holder:                // 세대주 화면의 로그인 버튼 클릭시 처리
                startActivity(new Intent(this,LoginActivity.class));
                main_show.setVisibility(View.VISIBLE);
                usr_id.setText("");
                usr_pw.setText("");
                show1.setVisibility(View.GONE);
                show2.setVisibility(View.GONE);
                show3.setVisibility(View.GONE);

                break;
            case R.id.login_member:             // 구성원 화면의 로그인 버튼 클릭시 처리
                startActivity(new Intent(this,LoginActivity.class));
                main_show.setVisibility(View.VISIBLE);
                usr_code.setText("");
                show1.setVisibility(View.GONE);
                show2.setVisibility(View.GONE);
                show3.setVisibility(View.GONE);
                break;
        }
    }
    private boolean grantExternalStoragePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if ( checkSelfPermission(READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG, "Permission is granted");
                return true;
            } else {
                Log.e(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{ READ_PHONE_STATE}, 1);

                return false;
            }
        } else {
            com.nispok.snackbar.Snackbar.with(getApplicationContext())
                    .text("External Storage Permission is Grant")
                    .show(this);
            return true;
        }
    }
}