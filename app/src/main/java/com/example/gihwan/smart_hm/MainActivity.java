package com.example.gihwan.smart_hm;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.ybq.android.spinkit.style.DoubleBounce;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.Manifest.permission.READ_PHONE_STATE;
import static android.support.v7.widget.StaggeredGridLayoutManager.TAG;

public class MainActivity extends Activity {

    RelativeLayout main_show , show1 , show2 , show3;           // main 레이아웃 제어하기 위함
    EditText usr_Id, usr_Pw , usr_Code;                         // 로그인 버튼 클릭시 main edittext의 값을 초기화 해주기 위함


    // 뒤로 가기 버튼 제어사용 변수
    long first_time;
    long second_time;

    String usr_id = "";
    String usr_pw = "";
    String usr_code ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boolean isGrantStorage = grantExternalStoragePermission();
        main_show = (RelativeLayout) findViewById(R.id.main_show);
        show1 = (RelativeLayout)findViewById(R.id.show1);
        show2 = (RelativeLayout)findViewById(R.id.show2);
        show3 = (RelativeLayout)findViewById(R.id.show3);

        usr_Id = (EditText)findViewById(R.id.house_holderid);       // 세대주의 아이디를 사용하기 위함.
        usr_Pw = (EditText)findViewById(R.id.house_holderpw);       // 세대주의 비밀번호를 사용하기 위함.
        usr_Code = (EditText)findViewById(R.id.member_code);        // 구성원의 입력 코드를 사용하기 위함.
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
                usr_id = usr_Id.getText().toString();
                usr_pw = usr_Pw.getText().toString();
                if(usr_id.length() !=0 && usr_pw.length() !=0 ){
                    ChkData_Main login_C = new ChkData_Main();
                    login_C.execute(usr_id,usr_pw,null);
                }
                else{
                    com.nispok.snackbar.Snackbar.with(getApplicationContext())
                            .text("로그인 정보를 다시 확인해주세요")
                            .show(MainActivity.this);
                }
                break;

            case R.id.login_member:             // 구성원 화면의 로그인 버튼 클릭시 처리
                usr_code = usr_Code.getText().toString();
                if(usr_code.length() != 0){
                    ChkData_Main login_Code = new ChkData_Main();
                    login_Code.execute(null,null,usr_code);
                }
                else{
                    com.nispok.snackbar.Snackbar.with(getApplicationContext())
                            .text("코드 정보를 입력해주세요")
                            .show(MainActivity.this);
                }
                break;
        }
    }

    public class ChkData_Main extends AsyncTask<String, Void, String>{                //////// 로그인 처리

        private CustomProgressDialog progressBar;
        private AlertDialog.Builder alertBuilder;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar = new CustomProgressDialog(MainActivity.this);
            progressBar.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            progressBar.show(); // 보여주기

            try {
                    Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        @Override
        protected String doInBackground(String... params) {

            String usr_id = (String)params[0]; // 사용자의 ID를 가져옴
            String usr_pw = (String)params[1]; // 사용자의 PW를 가져옴
            String usr_code = (String) params[2]; // 사용자의 CODE를 가져옴

            String server_URL = "http://13.124.195.151/hlogin.php";
            String postParameters = "usr_id="+usr_id + "&usr_pw="+usr_pw+"&usr_code="+usr_code;

            try {

                URL url = new URL(server_URL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(ContentValues.TAG, "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }
                  /* 서버 -> 안드로이드 파라메터값 전달 */
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                bufferedReader.close();
                return sb.toString();

            } catch (Exception e) {
                Log.d(ContentValues.TAG, "RecvData: Error ", e);
                return new String("Error: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);
            alertBuilder = new AlertDialog.Builder(MainActivity.this);
            Log.e("서버로 부터 받은 결과 값 : ",res);

            if(res.equals("1")){          // 세대주 버튼을 눌렀을때 로그인 실행시 서버의 값을 php를 통해 받고 성공하면 다음 로그인 창으로 이동하게 된다

                Log.e("RESULT","성공적으로 처리되었습니다!");
                // 만약, 비밀번호가 서버 DB에 있는 것과 일치하여 로그인에 성공하였을 때,
                // 입력창 초기화
                // 액티비티 이동
                usr_Id.setText("");
                usr_Pw.setText("");
                // Mypage를 처리할 때 사용될 데이터 값을 위해 Intent로 데이터를 날려주는 것을 신경쓰자

                startActivity(new Intent(MainActivity.this,LoginActivity.class).putExtra("usr_id",usr_id).putExtra("usr_code",""));
                finish();

            }

            else if (res.equals("2")){              // 구성원 버튼을 눌렀을때 로그인 실행시 서버의 값을 php를 통해 받고 성공하면 다음 로그인 창으로 이동하게 된다
                Log.e("RESULT","성공적으로 처리되었습니다!");
                usr_Code.setText("");
                startActivity(new Intent(MainActivity.this,LoginActivity.class).putExtra("usr_code",usr_code).putExtra("usr_id",""));
            }

            else if(res.equals("0")){
                Log.e("RESULT","비밀번호가 일치하지 않습니다.");
                alertBuilder
                        .setTitle("알림")
                        .setMessage("없는 정보 입니다. \n확인 후 다시 시도해주세요")
                        .setCancelable(true)
                        .setPositiveButton("확인",null);
                AlertDialog dialog = alertBuilder.create();
                dialog.show();
            }
            else{
                Log.e("RESULT","에러 발생! ERRCODE = " + res);
                alertBuilder
                        .setTitle("알림")
                        .setMessage("등록중 에러가 발생했습니다! ErrCode")
                        .setCancelable(true)
                        .setPositiveButton("확인",null);
                AlertDialog dialog = alertBuilder.create();
                dialog.show();
            }
            progressBar.dismiss();  // 그만 보여주기.
        }
    }

    // 핸드폰 연락처를 가져오기 위해서는 퍼미션 확인을 설정해야한다
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