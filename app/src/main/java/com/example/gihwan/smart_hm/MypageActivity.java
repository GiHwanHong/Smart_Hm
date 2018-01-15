package com.example.gihwan.smart_hm;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.util.KakaoParameterException;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.ContentValues.TAG;

/**
 * Created by GiHwan on 2017. 12. 27..
 */

public class MypageActivity extends Activity {


    private KakaoLink kakaoLink; // 카카오톡 메신저를 사용하기 위해 선언해놓은 변수

    EditText Mypage_ID, Mypage_PN, Mypage_CODE;          // 입력한 코드 번호를 공유하기 위해 선언해놓은 변수
    EditText Mypage_PW1, Mypage_PW2, Mypage_NAME;
    TextView Mypage_ADDR_Show;

    private String str = "";         // 주소 값을 전달 받아 수정하기 위함
    private String usr_id = "";       // 사용자의 값을 전달 하기 위함
    private String usr_code = "";

    String usr_id_recv = "";        // 인텐트로 값을 전달 받기 위함
    String usr_code_recv = "";

    private String usr_id_json = "";      //json으로 받은 값을 사용하기 위함
    private String usr_pw_json = "";
    private String usr_pw_r_json = "";
    private String usr_name_json = "";
    private String usr_addr_json = "";
    private String usr_phone_json = "";
    private String usr_code_json = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        Mypage_ID = (EditText) findViewById(R.id.Mypage_Id);
        Mypage_PW1 = (EditText) findViewById(R.id.Mypage_Pw1);
        Mypage_PW2 = (EditText) findViewById(R.id.Mypage_Pw2);
        Mypage_NAME = (EditText) findViewById(R.id.Mypage_Name);
        Mypage_ADDR_Show = (TextView) findViewById(R.id.Mypage_Addr_Show);
        Mypage_PN = (EditText) findViewById(R.id.Mypage_Pn);
        Mypage_CODE = (EditText) findViewById(R.id.Mypage_Code);

        try {

            Intent intent_I = getIntent();
            usr_id_recv = intent_I.getStringExtra("usr_id_recv");
            usr_code_recv = intent_I.getStringExtra("usr_code_recv");
            Log.e("usr_id_recv_Mypage", usr_id_recv);
            Log.e("usr_code_recv_Mypage", usr_code_recv);

            kakaoLink = KakaoLink.getKakaoLink(MypageActivity.this);

            //만들어놓은 Select_Login를 선언 및 실행
            // 인텐트를 통해 전달 받은 값을 execute해야 함
            new MypageActivity.Json_select().execute(usr_id_recv, usr_code_recv);

        } catch (KakaoParameterException e) {
            e.printStackTrace();
        }

        //코드 자동 생성을 위하여 입력을 방지.
        Mypage_CODE.setClickable(false);
        Mypage_CODE.setFocusable(false);
    }

    public void Mypage_Btn_click(View v) {
        switch (v.getId()) {
            case R.id.Mypage_Btn:    // 카카오 버튼을 통해 코드번호를 보내기 위함
                final KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder
                        = kakaoLink.createKakaoTalkLinkMessageBuilder();
                try {
                    kakaoTalkLinkMessageBuilder.addText("전송된 코드는 \n => " + Mypage_CODE.getText().toString());
                    kakaoLink.sendMessage(kakaoTalkLinkMessageBuilder, this);   // 메시지 전송
                } catch (KakaoParameterException e) {
                    e.printStackTrace();
                }
                finish();
                break;
            case R.id.Mypage_Cancel:    // 취소 버튼 클릭시
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;

            case R.id.Mypage_Delete:     // 회원 탈퇴 버튼 클릭 시
                new Json_Delete().execute(usr_code_json);
                break;

            case R.id.Mypage_Update:    // 회원 정보 변경 버튼 클릭시
                usr_id_json = Mypage_ID.getText().toString();
                usr_pw_json = Mypage_PW1.getText().toString();
                usr_pw_r_json =  Mypage_PW2.getText().toString();
                usr_name_json =  Mypage_NAME.getText().toString();
                usr_phone_json = Mypage_PN.getText().toString();
                usr_code_json =Mypage_CODE.getText().toString();
                new Json_Update().execute(usr_id_json,usr_pw_json,usr_pw_r_json,usr_name_json,usr_addr_json,usr_phone_json,usr_code_json);
                break;

            case R.id.Mypage_Addr_Btn:        // 회원 주소 변경 버튼 클릭시
                Intent in_getData = new Intent(MypageActivity.this, AdselectActivity.class);
                startActivityForResult(in_getData, 0);
                break;
        }
    }
    public class Json_Delete extends AsyncTask<String,Void,String> {

        private ProgressDialog Back_dialog = new ProgressDialog(MypageActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Back_dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            Back_dialog.setMessage("잠시만 기다려 주세요.");
            Back_dialog.show();
        }

        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);
            Back_dialog.dismiss();
            Log.e("Delete_Post Value : ", res);
            com.nispok.snackbar.Snackbar.with(getApplicationContext())
                    .text(res)
                    .show(MypageActivity.this);
            Mypage_ID.setText("");
            Mypage_PW1.setText("");
            Mypage_PW2.setText("");
            Mypage_NAME.setText("");
            Mypage_ADDR_Show.setText("");
            Mypage_PN.setText("");
            Mypage_CODE.setText("");

        }

        @Override
        protected String doInBackground(String... params) {

            String usr_code = (String) params[0];

            String server_URL = "http://13.124.195.151/hdelete.php";

            String postParameters = "usr_code=" + usr_code;

            Log.e("Delete_postParameters : ", postParameters);
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
    ///////////////////////////----Delete AsyncTask 끝----////////////////////////////////

    public class Json_Update extends AsyncTask<String,Void,String>{
        private ProgressDialog Back_dialog = new ProgressDialog(MypageActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Back_dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            Back_dialog.setMessage("잠시만 기다려 주세요.");
            Back_dialog.show();
        }

        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);
            Back_dialog.dismiss();
            Log.e("Update_Post Value : ", res);
            com.nispok.snackbar.Snackbar.with(getApplicationContext())
                    .text(res)
                    .show(MypageActivity.this);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                for (int i = 0; i < 3; i++) {
                    Back_dialog.setProgress(i * 30);
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
            String usr_code = (String) params[6];

            String server_URL = "http://13.124.195.151/hupdate.php";
            String postParameters = "usr_id=" + usr_id + "&usr_pw1=" + usr_pw1 + "&usr_pw2=" + usr_pw2 + "&usr_name=" + usr_name + "&usr_addr=" + usr_addr + "&usr_phone="
                    + usr_phone + "&usr_code=" + usr_code;

            Log.e("Update_postParameters : ", postParameters);

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

                Log.d(TAG, "Update Data: Error ", e);

                return new String("Error: " + e.getMessage());
            }
        }
    }

    ///////////////////////////----Update AsyncTask 끝----////////////////////////////////
    public class Json_select extends AsyncTask<String, Void, String> {

        private ProgressDialog progressDialog = new ProgressDialog(MypageActivity.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // 프로그래스바를 이용
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("잠시만 기다려 주세요.");
            progressDialog.show();
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

            // Login한 사용자를 얻기 위해서는 Intent로 사용자의 Id값을 전달 받아서 post방식으로 날리는 방식이 좋을 것 같다.
            // -> MainActivity -> LoginActivity -> MypageActivity 순으로 Intent로 값을 전달해 주어 해결하였다.
            // Login한 사용자의 정보를 서버 DB를 통해 받아와서 Mypage의 각각 EditText에 뿌려주는 역할을 하는 부분
            // 인텐트로 값(아이디,비밀번호1,비밀번호2, 이름, 주소, 연락처, 코드번호)을 넘겨주고 MypageActivity에서 받는다.
            // -> 이 방법은 상당히 비효율적이라 생각한다. MypageActivity에서 처리하도록 하자.

            usr_id = (String) params[0]; // 사용자의 ID를 가져옴
            usr_code = (String) params[1]; // 사용자의 CODE를 가져옴

            return getJsonText();
        }

        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);
            // 프로그래스바를 종료시킬 것
            Log.e("모든 정보를 얻어 온다", res);
            progressDialog.dismiss();
        }
    }       // 서버 -> 안드로이드로 세밀하게 받아오기 위한 코드

    public String getJsonText() {
        StringBuffer sb = new StringBuffer();

        try {
            //주어진 URL 문서의 내용을 문자열로 얻는다.
            String jsonPage = getStringFromUrl("http://13.124.195.151/hselect.php");

            //읽어들인 JSON포맷의 데이터를 JSON객체로 변환
            JSONObject json = new JSONObject(jsonPage);
            Log.e("json : ", json.toString());

            Gson gson = new Gson();
            String json_g = gson.toJson(json);
            Log.e("Gson_name : ", json_g.substring(20, 24));

            String str_1 = json_g.substring(20, 24);

            if (str_1.equals("Code")) {
                JSONArray jArr_code = json.getJSONArray("Code");
                if (jArr_code != null) {
                    try {
                        for (int i = 0; i < jArr_code.length(); i++) {
                            json = jArr_code.getJSONObject(i);

                            // Json으로 서버에 저장되어있는 회원 정보를 불러오기 위함.
                            usr_id_json = json.getString("usr_id");
                            usr_pw_json = json.getString("usr_pw");
                            usr_pw_r_json = json.getString("usr_pw_r");
                            usr_name_json = json.getString("usr_name");
                            usr_addr_json = json.getString("usr_addr");
                            usr_phone_json = json.getString("usr_phone");
                            usr_code_json = json.getString("usr_code");

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Mypage_ID.setText(usr_id_json);
                                    Mypage_PW1.setText(usr_pw_json);
                                    Mypage_PW2.setText(usr_pw_r_json);
                                    Mypage_NAME.setText(usr_name_json);
                                    Mypage_ADDR_Show.setText(usr_addr_json);
                                    Mypage_PN.setText(usr_phone_json);
                                    Mypage_CODE.setText(usr_code_json);

                                }
                            });
                        }
                    } catch (Exception e) {
                        Log.e("Error msg : ", e.toString());
                    }
                }
                Log.e("코드로 로그인 했다 ; ", json_g.substring(20, 24));
            }

            Log.e("Gson_name : ", json_g.substring(20, 28));
            String str1 = json_g.substring(20, 28);

            if (str1.equals("Userinfo")) {
                JSONArray jArr_usr = json.getJSONArray("Userinfo");
                if (jArr_usr != null) {
                    for (int i = 0; i < jArr_usr.length(); i++) {
                        json = jArr_usr.getJSONObject(i);

                        // Json으로 서버에 저장되어있는 회원 정보를 불러오기 위함.
                        usr_id_json = json.getString("usr_id");
                        usr_pw_json = json.getString("usr_pw");
                        usr_pw_r_json = json.getString("usr_pw_r");
                        usr_name_json = json.getString("usr_name");
                        usr_addr_json = json.getString("usr_addr");
                        usr_phone_json = json.getString("usr_phone");
                        usr_code_json = json.getString("usr_code");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Mypage_ID.setText(usr_id_json);
                                Mypage_PW1.setText(usr_pw_json);
                                Mypage_PW2.setText(usr_pw_r_json);
                                Mypage_NAME.setText(usr_name_json);
                                Mypage_ADDR_Show.setText(usr_addr_json);
                                Mypage_PN.setText(usr_phone_json);
                                Mypage_CODE.setText(usr_code_json);
                            }
                        });

                    }
                }
                Log.e("이 사람이다 ; ", json_g.substring(20, 28));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    // getStringFromUrl : 주어진 URL의 문서의 내용을 문자열로 반환
    public String getStringFromUrl(String pUrl) {

        String postParameters = "usr_id=" + usr_id + "&usr_code=" + usr_code;

        try {
            URL url = new URL(pUrl);
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
                Log.d("my_data:", line);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {       // startActivityForResult에서 넘긴 값을 처리하기 위함
            case 0:                  // Daum에서 받은 주소 값을 받기 위함.
                if (resultCode == RESULT_OK) {
                    str = data.getStringExtra("myaddr");
                    usr_addr_json=str;
                    Log.e("잘 받았어 고마워 : ", str);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Mypage_ADDR_Show.setText(str);
                        }});
                }
                break;
        }
    }

}