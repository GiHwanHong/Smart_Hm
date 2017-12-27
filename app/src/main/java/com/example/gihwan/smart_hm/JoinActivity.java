package com.example.gihwan.smart_hm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

/**
 * Created by GiHwan on 2017. 12. 27..
 */

public class JoinActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
    }



    public void Join_Btn_Click(View v){
        switch (v.getId()){
            case R.id.Join_Cancel:
                startActivity(new Intent(this,MainActivity.class));
                finish();
                break;
            case R.id.Join_Ok:
                Toast.makeText(getApplicationContext(), "가입이 된 후 DB에 입력이 됩니다", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this,MainActivity.class));
                finish();
                break;
        }
    }
}
