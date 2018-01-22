package com.example.gihwan.smart_hm;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TreeSet;
import java.util.regex.Pattern;

import static android.content.ContentValues.TAG;

/**
 * Created by GiHwan on 2017. 12. 27..
 */

public class JoinActivity extends Activity {

    TextView Addrshow_view;

    EditText Join_ID, Join_NAME, Join_PN;
    TextView Join_CODE;
    EditText Join_PW1, Join_PW2;
    String str = null;      // 주소 값을 전달 받기위한 변수

    // 현재 날짜를 Format 받아와서 처리해주도록 한다.
    long now = System.currentTimeMillis();
    Date date = new Date(now);
    SimpleDateFormat Date_Format = new SimpleDateFormat("yyyy년 MM월 dd일 HH시mm분");
    String getTime = Date_Format.format(date);

    // 랜덤 값을 받기 위해 사용된 변수
    String sRnd = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        Addrshow_view = (TextView) findViewById(R.id.Join_Addr_Show);

        Join_ID = (EditText) findViewById(R.id.Join_Id);      // 아이디 입력 EditText
        Join_PW1 = (EditText) findViewById(R.id.Join_Pw1);    // 비밀번호1 입력 EditText
        Join_PW2 = (EditText) findViewById(R.id.Join_Pw2);    // 비밀번호2 입력 EditText
        Join_NAME = (EditText) findViewById(R.id.Join_Name);   // 이름 입력 EditText
        Join_PN = (EditText) findViewById(R.id.Join_Pn);      // 핸드폰 번호 입력 EditText
        Join_CODE = (TextView) findViewById(R.id.Join_CODE);  // 코드번호 입력 EditText

        Join_ID.setFilters(new InputFilter[] {filter_join});      // 입력할 때 데이터 베이스에 효율적으로 접근하기 위해 영어만 입력하도록 함
        Join_PW1.setFilters(new InputFilter[] {filter_join});     // 입력할 때 데이터 베이스에 효율적으로 접근하기 위해 영어만 입력하도록 함
        Join_PW2.setFilters(new InputFilter[] {filter_join});     // 입력할 때 데이터 베이스에 효율적으로 접근하기 위해 영어만 입력하도록 함

        Join_ID.requestFocus(); // 바로 올라오게 하기 위함.

        //핸드폰 번호 가져오기
        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        String phoneNum = telephonyManager.getLine1Number();

        if (phoneNum.startsWith("+82")) {
            phoneNum = phoneNum.replace("+82", "0");
        }
        // TextView Scrolling 가능하게 하기

        Join_PN.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        Join_PN.setText(phoneNum);

        getRandomCode();
        Addrshow_view.setMovementMethod(new ScrollingMovementMethod().getInstance());
    }


    public void Join_Btn_Click(View v) {
        switch (v.getId()) {
            case R.id.Join_Cancel:          // 취소 버튼 클릭시
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            case R.id.Join_Ok:              // 가입 버튼 클릭시
                chk_edit();                 // EditText에 입력 된 것을 받고, 서버 디비에 저장하도록 한다
                break;
            case R.id.Join_Addr_Btn:        // 주소 찾기 버튼 클릭시
                Intent in_getData = new Intent(JoinActivity.this, SearchActivity.class);
                startActivityForResult(in_getData, 0);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        switch (requestCode) {       // startActivityForResult에서 넘긴 값을 처리하기 위함
            case 0:                 // Daum에서 받은 주소 값을 받기 위함.
                if (resultCode == RESULT_OK) {
                    intent.getStringExtra("address");
                    Log.e("잘 받았어 고마워 : ", intent.getStringExtra("address").toString());
                    str = intent.getStringExtra("address").toString();
                    Addrshow_view.setText(str);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }

    public void chk_edit() {                    // 가입 버튼 클릭시 처리되는 부분

        String usr_id = Join_ID.getText().toString();
        String usr_pw1 = Join_PW1.getText().toString();
        String usr_pw2 = Join_PW2.getText().toString();
        String usr_name = Join_NAME.getText().toString();
        String usr_addr = str;
        String usr_phone = Join_PN.getText().toString();
        String usr_ip = "172.35.28.255";
        String usr_code = Join_CODE.getText().toString();

        try {
            if (usr_id.length() != 0 && usr_pw1.length() != 0 && usr_pw2.length() != 0 && usr_name.length() != 0 && usr_addr.length() != 0
                    && usr_phone.length() != 0 && usr_code.length() != 0) {
                if (usr_pw1.equals(usr_pw2) == false) {
                    Join_PW1.setError("다시한번 확인 해주세요");
                    Join_PW2.setError("다시한번 확인 해주세요");
                    com.nispok.snackbar.Snackbar.with(getApplicationContext())
                            .text("비밀번호를 다시한번 확인해 주세요")
                            .show(this);
                } else {
                    Insert_Join Task = new Insert_Join();
                    Task.execute(usr_id, usr_pw1, usr_pw2, usr_name, usr_addr, usr_phone, getTime, usr_ip, usr_code);
                    Join_ID.setText(null);
                    Join_PW1.setText(null);
                    Join_PW2.setText(null);
                    Join_NAME.setText(null);
                    Addrshow_view.setText("주소가 표시 됩니다.");
                    getRandomCode();
                }

            } else {
                com.nispok.snackbar.Snackbar.with(getApplicationContext())
                        .text("입력이 안된 곳이 있습니다 확인후 다시 누르세요")
                        .show(this);
            }
        } catch (Exception e) {
            com.nispok.snackbar.Snackbar.with(getApplicationContext())
                    .text("입력이 안된 곳이 있습니다 확인후 다시 누르세요")
                    .show(this);
        }

    }

    // 영어만 입력하기 위한 필터 처리 해주는 코드
    protected InputFilter filter_join= new InputFilter() {

        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Pattern ps = Pattern.compile("^[a-zA-Z0-9]+$");
            if (!ps.matcher(source).matches()) {
                return "";
            }
            return null;
        }
    };

    // 코드 생성하기 위함
    public void getRandomCode() {
        // size가 5이 되기 전에는 계속 루프를 돈다.
        // 다른 수 5개가 저장이 되면 루프를 탈출
        // Treeeset은 데이터를 집어 넣으면 기본적으로 오름차순(Ascending) 정렬
        // TreeSet은 중복된 값을 허용하지 않으므로

        TreeSet<Integer> lotto = new TreeSet<Integer>();
        while (lotto.size() < 5) {
            int num = (int) (Math.random() * 10) + 1;
            lotto.add(num);
            sRnd = lotto.toString();
            sRnd = sRnd.replaceAll("\\[", "");
            sRnd = sRnd.replaceAll("\\]", "");
            sRnd = sRnd.replaceAll(", ", "");
        }

        Log.e("랜덤 출력 ", sRnd);
        Join_CODE.setText(sRnd);
    }

    class Insert_Join extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog = new ProgressDialog(JoinActivity.this);

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
            com.nispok.snackbar.Snackbar.with(getApplicationContext())
                    .text(res)
                    .show(JoinActivity.this);
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

            String usr_id = (String) params[0];
            String usr_pw1 = (String) params[1];
            String usr_pw2 = (String) params[2];
            String usr_name = (String) params[3];
            String usr_addr = (String) params[4];
            String usr_phone = (String) params[5];
            String usr_joinday = (String) params[6];
            String usr_ip = (String) params[7];
            String usr_code = (String) params[8];

            String server_URL = "http://13.124.195.151/hinsert.php";
            String postParameters = "usr_id=" + usr_id + "&usr_pw1=" + usr_pw1 + "&usr_pw2=" + usr_pw2 + "&usr_name=" + usr_name + "&usr_addr=" + usr_addr + "&usr_phone="
                    + usr_phone + "&usr_joinday=" + usr_joinday + "&usr_ip=" + usr_ip + "&usr_code=" + usr_code;

            Log.e("Join_postParameters : ", postParameters);

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
                Log.d(TAG, "POST response code - " + responseStatusCode);

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

                Log.d(TAG, "InsertData: Error ", e);

                return new String("Error: " + e.getMessage());
            }
        }
    }
}