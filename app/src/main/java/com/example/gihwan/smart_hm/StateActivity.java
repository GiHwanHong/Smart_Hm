package com.example.gihwan.smart_hm;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * Created by GiHwan on 2017. 12. 27..
 */

public class StateActivity extends AppCompatActivity {

    TextView TMP , Humidity;
    TextView State_Gas , State_Fire;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state);

        TMP = (TextView)findViewById(R.id.Tmp_r1);           // 온도 상태를 숫자로 나타내기 위한 Textview
        Humidity = (TextView)findViewById(R.id.Tmp_r2);      // 습도 상태를 숫자로 나타내기 위한 Textview

        State_Gas = (TextView)findViewById(R.id.txt_gas_state); // 가스 상태를 나타내는 Textview
        State_Fire = (TextView)findViewById(R.id.txt_fire_state); // 화재 상태를 나타내는 Textview

    }

    public void State_ToggleBtn_click(View v){
        switch (v.getId()){
            case R.id.switch_L_1:      // LED 토글버튼 클릭시
                ToggleButton tb1 = (ToggleButton) findViewById(R.id.switch_L_1);
                if(tb1.isChecked()){
                    Toast.makeText(getApplicationContext(), "LED 토글버튼이 눌렸습니다1 ", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.switch_L_2:      // LED 토글버튼 클릭시
                ToggleButton tb2 = (ToggleButton) findViewById(R.id.switch_L_2);
                if(tb2.isChecked()){
                    Toast.makeText(getApplicationContext(), "LED 토글버튼이 눌렸습니다2 ", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.switch_L_3:      // LED 토글버튼 클릭시
                ToggleButton tb3 = (ToggleButton) findViewById(R.id.switch_L_3);
                if(tb3.isChecked()){
                    Toast.makeText(getApplicationContext(), "LED 토글버튼이 눌렸습니다3 ", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.switch_G_1:      // 가스 토글 버튼 클릭시
                ToggleButton tb5 = (ToggleButton) findViewById(R.id.switch_G_1);
                if(tb5.isChecked()){
                    Toast.makeText(getApplicationContext(), "LED 토글버튼이 눌렸습니다 g1 ", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }
}