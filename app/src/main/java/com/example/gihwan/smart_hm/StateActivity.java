package com.example.gihwan.smart_hm;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * Created by GiHwan on 2017. 12. 27..
 */

public class StateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state);


    }

    public void State_ToggleBtn_click(View v){
        switch (v.getId()){
            case R.id.switch_L_r1:      // LED 토글버튼 클릭시
                ToggleButton tb1 = (ToggleButton) findViewById(R.id.switch_L_r1);
                if(tb1.isChecked()){
                    Toast.makeText(getApplicationContext(), "LED 토글버튼이 눌렸습니다1 ", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.switch_L_r2:      // LED 토글버튼 클릭시
                ToggleButton tb2 = (ToggleButton) findViewById(R.id.switch_L_r2);
                if(tb2.isChecked()){
                    Toast.makeText(getApplicationContext(), "LED 토글버튼이 눌렸습니다2 ", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.switch_L_r3:      // LED 토글버튼 클릭시
                ToggleButton tb3 = (ToggleButton) findViewById(R.id.switch_L_r3);
                if(tb3.isChecked()){
                    Toast.makeText(getApplicationContext(), "LED 토글버튼이 눌렸습니다3 ", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.switch_L_r4:      // LED 토글버튼 클릭시
                ToggleButton tb4 = (ToggleButton) findViewById(R.id.switch_L_r4);
                if(tb4.isChecked()){
                    Toast.makeText(getApplicationContext(), "LED 토글버튼이 눌렸습니다4 ", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.switch_G_r1:      // 가스 토글 버튼 클릭시
                ToggleButton tb5 = (ToggleButton) findViewById(R.id.switch_G_r1);
                if(tb5.isChecked()){
                    Toast.makeText(getApplicationContext(), "LED 토글버튼이 눌렸습니다 g1 ", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.switch_G_r2:      // 가스 토글 버튼 클릭시
                ToggleButton tb6 = (ToggleButton) findViewById(R.id.switch_G_r2);
                if(tb6.isChecked()){
                    Toast.makeText(getApplicationContext(), "LED 토글버튼이 눌렸습니다 g2 ", Toast.LENGTH_SHORT).show();
                }
                break;


        }
    }
}