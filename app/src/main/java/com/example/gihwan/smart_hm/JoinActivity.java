package com.example.gihwan.smart_hm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by GiHwan on 2017. 12. 27..
 */

public class JoinActivity extends Activity {

    TextView Addrshow_view ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);


        Addrshow_view = (TextView)findViewById(R.id.Join_Addr_Show);


    }



    public void Join_Btn_Click(View v){
        switch (v.getId()){
            case R.id.Join_Cancel:          // 취소 버튼 클릭시
                startActivity(new Intent(this,MainActivity.class));
                finish();
                break;
            case R.id.Join_Ok:              // 가입 버튼 클릭시
                Toast.makeText(getApplicationContext(), "가입이 된 후 DB에 입력이 됩니다", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this,MainActivity.class));
                finish();
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
                    Addrshow_view.setText(intent.getStringExtra("address").toString());
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }
}
