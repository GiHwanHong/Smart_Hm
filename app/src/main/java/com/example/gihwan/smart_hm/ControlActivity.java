package com.example.gihwan.smart_hm;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by GiHwan on 2017. 12. 27..
 */
public class ControlActivity extends AppCompatActivity {

    TextView TMP , Humidity;
    TextView State_Gas , State_Fire;

    ToggleButton tb_living , tb_kitchen,tb_room,tb_gasvalve;

    String LED_State1="";       // 받아온 센서 값을 저장하기 위함
    String LED_State2="";
    String LED_State3="";
    String VALVE_State="";

    String TMP_val ="";         // 받아온 온도 , 습도 값을 저장하기 위함
    String Hum_val ="";

    String Gas_val = "";        // 받아온 가스 , 화재 , 문값을 저장하기 위함
    String Fire_val = "";
    String Door_val = "";

    private SwipeRefreshLayout refreshLayout_Control;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        TMP = (TextView)findViewById(R.id.Tmp_r1);                   // 온도 상태를 숫자로 나타내기 위한 Textview
        Humidity = (TextView)findViewById(R.id.Tmp_r2);              // 습도 상태를 숫자로 나타내기 위한 Textview

        State_Gas = (TextView)findViewById(R.id.txt_gas_state);      // 가스 상태를 나타내는 Textview
        State_Fire = (TextView)findViewById(R.id.txt_fire_state);    // 화재 상태를 나타내는 Textview

        tb_living = (ToggleButton) findViewById(R.id.switch_L_1);       // 토글 버튼 - 거실
        tb_kitchen = (ToggleButton) findViewById(R.id.switch_L_2);      //         - 부엌
        tb_room = (ToggleButton) findViewById(R.id.switch_L_3);         //         - 방
        tb_gasvalve = (ToggleButton) findViewById(R.id.switch_G_1);     //         - 가스벨브

        refreshLayout_Control = (SwipeRefreshLayout)findViewById(R.id.swipeRefreshLo);
        refreshLayout_Control.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {                 // 호출 시점 Refresh
            @Override
            public void onRefresh() {
                new Control_recv_LED().execute();

                refreshLayout_Control.setRefreshing(false);
            }
        });
        new Control_recv_LED().execute();
    }

    public void State_ToggleBtn_click(View v){
        switch (v.getId()){
            case R.id.switch_L_1:      // LED 토글버튼 클릭시

                if(tb_living.isChecked()){
                    new Control_send().execute("1",LED_State2,LED_State3,VALVE_State);
                    LED_State1="1";
                }
                else{
                    new Control_send().execute("0",LED_State2,LED_State3,VALVE_State);
                    LED_State1="0";
                }
                break;

            case R.id.switch_L_2:      // LED 토글버튼 클릭시
                if(tb_kitchen.isChecked()){
                    new Control_send().execute(LED_State1,"1",LED_State3,VALVE_State);
                    LED_State2="1";

                }
                else{
                    new Control_send().execute(LED_State1,"0",LED_State3,VALVE_State);
                    LED_State2="0";
                }
                break;

            case R.id.switch_L_3:      // LED 토글버튼 클릭시
                if(tb_room.isChecked()){
                    new Control_send().execute(LED_State1,LED_State2,"1",VALVE_State);
                    LED_State3="1";
                }
                else{
                    new Control_send().execute(LED_State1,LED_State2,"0",VALVE_State);
                    LED_State3="0";
                }
                break;

            case R.id.switch_G_1:      // 가스 토글 버튼 클릭시
                if(tb_gasvalve.isChecked()){
                    new Control_send().execute(LED_State1,LED_State2,LED_State3,"1");
                    VALVE_State="1";
                }
                else{
                   new Control_send().execute(LED_State1,LED_State2,LED_State3,"0");
                    VALVE_State="0";
                }
                break;
        }
    }



    // LED 센서의 값을 읽어올 때 처리
    public class Control_recv_LED extends AsyncTask<String, Void,String>{

        private ProgressDialog progressDialog = new ProgressDialog(ControlActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // 프로그래스바를 이용
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("잠시만 기다려 주세요.");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);
            Log.e("모든 LED, 온도, 습도 , 가스 , 화재 정보를 얻어 온다!", res);
            progressDialog.dismiss();
        }

        @Override
        protected String doInBackground(String... sensors) {
            try {
                for (int i = 0; i < 3; i++) {
                    progressDialog.setProgress(i * 30);
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return getJsonText_LED();

        }
    }
    public String getJsonText_LED() {
        StringBuffer sb = new StringBuffer();

        try {
            String jsonPage = getStringFromUrl("http://52.78.22.237/sensor.php");       // 전등 조작 버튼
            String jsonPage_hut = getStringFromUrl("http://52.78.22.237/hutemp.php");   // 온도 습도 조작
            String jsonPage_push = getStringFromUrl("http://52.78.22.237/push.php");         // 가스 , 화재 , 문을 알려주기 위한 것 (푸쉬알림)


            // 읽어들인 JSON포맷의 데이터를 JSON객체로 변환
            JSONObject json_sensor = new JSONObject(jsonPage);
            Log.e("json_sensor : ", json_sensor.toString());

            JSONObject json_hut = new JSONObject(jsonPage_hut);
            Log.e("json_hut : ", json_hut.toString());

            JSONObject json_push = new JSONObject(jsonPage_push);
            Log.e("json_push : ", json_push.toString());

            // 배열로 구성 되어있는 JSON 배열생성
            JSONArray jArr_sensor = json_sensor.getJSONArray("sensorData");           // json tag get sensor
            JSONArray jArr_hut = json_hut.getJSONArray("hutemp");                     // json tag get hutemp
            JSONArray jArr_push = json_push.getJSONArray("pushData");                 // json tag get pushData


            Log.e("JArray_Sensor : ", jArr_sensor.toString());
            Log.e("JArray_Hut : ", jArr_hut.toString());
            Log.e("jArr_push : ", jArr_push.toString());

            if(jArr_sensor.length()!=0){
                //배열의 크기만큼 반복하면서, 현재 켜있는 LED의 값을 추출함
                for (int i = 0; i < jArr_sensor.length(); i++) {
                    //i번째 배열 할당
                    json_sensor = jArr_sensor.getJSONObject(i);

                    //ksNo,korName의 값을 추출함
                    LED_State1 = json_sensor.getString("LED1");     // tag 이름 안에 있는 데이터 LED1의  value를 가져온다
                    LED_State2 = json_sensor.getString("LED2");     // tag 이름 안에 있는 데이터 LED2의  value를 가져온다
                    LED_State3 = json_sensor.getString("LED3");     // tag 이름 안에 있는 데이터 LED3의  value를 가져온다
                    VALVE_State = json_sensor.getString("VALVE");   // tag 이름 안에 있는 데이터 VALVE의  value를 가져온다

                    Log.e("LED 1: " ,LED_State1);
                    Log.e("LED 2: " ,LED_State2);
                    Log.e("LED 3: " ,LED_State3);
                    Log.e("LED 4: " ,VALVE_State);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(LED_State1.equals("1")){
                                tb_living.setChecked(true);
                            }
                            if(LED_State2.equals("1")){
                                tb_kitchen.setChecked(true);
                            }
                            if(LED_State3.equals("1")){
                                tb_room.setChecked(true);
                            }
                            if(VALVE_State.equals("1")){
                                tb_gasvalve.setChecked(true);
                            }
                            if(LED_State1.equals("0")){
                                tb_living.setChecked(false);
                            }
                            if(LED_State2.equals("0")){
                                tb_kitchen.setChecked(false);
                            }
                            if(LED_State3.equals("0")){
                                tb_room.setChecked(false);
                            }
                            if(VALVE_State.equals("0")){
                                tb_gasvalve.setChecked(false);
                            }
                        }});

                    sb.append("[ " + LED_State1 + " ]\n");
                    sb.append("[" + LED_State2 + "]\n");
                    sb.append("[" + LED_State3 + "]\n");
                    sb.append("[" + VALVE_State + "]\n");
                    sb.append("\n");

                }
            }

            if(jArr_hut.length() !=0){      // 온도, 습도의 데이터가 있는지 없는지 확인 한다
                //배열의 크기만큼 반복하면서, 현재 켜있는 온도의 값을 추출함
                for (int i = 0; i < jArr_hut.length(); i++) {
                    //i번째 배열 할당
                    json_hut = jArr_hut.getJSONObject(i);

                    //Temp,Humidity의 값을 추출함
                    TMP_val = json_hut.getString("temp");
                    Hum_val = json_hut.getString("hu");

                    Log.e("temp: " ,TMP_val);
                    Log.e("hu: " ,Hum_val);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TMP.setText(TMP_val+" 도");
                            TMP.setGravity(Gravity.CENTER);
                            Humidity.setText(Hum_val+" 도");
                            Humidity.setGravity(Gravity.CENTER);
                        }});
                    sb.append("[" + TMP_val + "]\n");
                    sb.append("[" + Hum_val + "]\n");
                    sb.append("\n");
                }
            }
            if(jArr_push.length() !=0){         // 가스 , 화재 , 문 데이터가 있는지 없는지 확인 한다
                //배열의 크기만큼 반복하면서, 현재의 가스 , 화재 , 문의 데이터를 추출하기 위함
                for (int i = 0; i < jArr_push.length(); i++) {
                    //i번째 배열 할당
                    json_push = jArr_push.getJSONObject(i);

                    //Temp,Humidity의 값을 추출함
                    Gas_val = json_push.getString("GAS");
                    Fire_val = json_push.getString("FIRE");
                    Door_val = json_push.getString("DOOR");

                    Log.e("gas: " ,Gas_val);
                    Log.e("fire: " ,Fire_val);
                    Log.e("door: " ,Door_val);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(Gas_val.equals("1")){
                                State_Gas.setText("가스가 누출 되었습니다.");
                                State_Gas.setTextColor(Color.RED);
                            }
                            if(Fire_val.equals("1")){
                                State_Fire.setText("화재 위험이 감지되었습니다.");
                                State_Fire.setTextColor(Color.RED);
                            }
                            if(Gas_val.equals("0")){
                                State_Gas.setText("현재 가스는 안전합니다.");
                                State_Gas.setTextColor(Color.parseColor("#388E3C"));
                            }
                            if(Fire_val.equals("0")){
                                State_Fire.setText("현재 화재는 안전합니다.");
                                State_Fire.setTextColor(Color.parseColor("#388E3C"));
                            }
                        }});
                    sb.append("[" + Gas_val + "]\n");
                    sb.append("[" + Fire_val + "]\n");
                    sb.append("[" + Door_val + "]\n");
                    sb.append("\n");
                }
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        return sb.toString();
    }

    // getStringFromUrl : 주어진 URL의 문서의 내용을 문자열로 반환
    public String getStringFromUrl(String pUrl) {

        BufferedReader bufreader = null;
        HttpURLConnection urlConnection = null;

        StringBuffer page = new StringBuffer(); //읽어온 데이터를 저장할 StringBuffer객체 생성

        try {
            URL url = new URL(pUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream contentStream = urlConnection.getInputStream();

            bufreader = new BufferedReader(new InputStreamReader(contentStream, "UTF-8"));
            String line = null;

            //버퍼의 웹문서 소스를 줄단위로 읽어(line), Page에 저장함
            while ((line = bufreader.readLine()) != null) {
                Log.d("my_data:", line);
                page.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //자원해제
            try {
                bufreader.close();
                urlConnection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return page.toString();
    }

    // LED버튼을 제어 할 값을 보낼 때 처리
    public class Control_send extends AsyncTask<String, Void,String>{
        private ProgressDialog progressDialog = new ProgressDialog(ControlActivity.this);

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
            Log.e("내가 버튼 제어할 정보를 보여준다", res);
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

            String LED1 = params[0];
            String LED2 = params[1];
            String LED3 = params[2];
            String VALVE = params[3];
            Log.e("내가 전송하는 값 : " , LED1+LED2+LED3+VALVE);

            String server_URL = "http://52.78.22.237/sensor.php";
            String postParameters = "LED1="+LED1 + "&LED2="+LED2+"&LED3="+LED3+"&VALVE="+VALVE;

            try {

                URL url = new URL(server_URL+"?"+postParameters);
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
    }
}