package com.example.gihwan.smart_hm;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.util.KakaoParameterException;

/**
 * Created by GiHwan on 2017. 12. 27..
 */

public class MypageActivity extends Activity {


    private KakaoLink kakaoLink; // 카카오톡 메신저를 사용하기 위해 선언해놓은 변수
    EditText Code_Val ;          // 입력한 코드 번호를 공유하기 위해 선언해놓은 변수
    private String str = null;
    private TextView Address;
    //String Code_Val_send = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        Code_Val = (EditText) findViewById(R.id.Mypage_Code);
        Address = (TextView) findViewById(R.id.Mypage_Addr_Show);


        try {
            kakaoLink = KakaoLink.getKakaoLink(MypageActivity.this);
        } catch (KakaoParameterException e) {
            e.printStackTrace();
        }

    }

    public void Mypage_Btn_click(View v){
        switch (v.getId()){
            case R.id.Mypage_Btn:    // 카카오 버튼을 통해 코드번호를 보내기 위함
                //Code_Val_send = Code_Val.getText().toString();
                final KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder
                        = kakaoLink.createKakaoTalkLinkMessageBuilder();
                try {
                    kakaoTalkLinkMessageBuilder.addText("전송된 코드는 \n => "+Code_Val.getText().toString());
                    kakaoLink.sendMessage(kakaoTalkLinkMessageBuilder, this);   // 메시지 전송
                } catch (KakaoParameterException e) {
                    e.printStackTrace();
                }
                finish();
                break;
            case R.id.Mypage_Cancel:    // 취소 버튼 클릭시
                startActivity(new Intent(this,LoginActivity.class));
                finish();
                break;
            case R.id.Mypage_Delete:     // 회원 탈퇴 버튼 클릭시
                com.nispok.snackbar.Snackbar.with(getApplicationContext())
                        .text(" DataBase Delete 처리가 되어야합니다")
                        .show(MypageActivity.this);
                break;
            case R.id.Mypage_Update:    // 회원 정보 변경 버튼 클릭시
                com.nispok.snackbar.Snackbar.with(getApplicationContext())
                        .text(" DataBase Update 처리가 되어야합니다")
                        .show(MypageActivity.this);
                break;

            case R.id.Mypage_Addr_Btn:        // 회원 주소 찾기 버튼 클릭시
                Intent in_getData = new Intent(MypageActivity.this, AdselectActivity.class);
                startActivityForResult(in_getData, 0);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {       // startActivityForResult에서 넘긴 값을 처리하기 위함
            case 0:                  // Daum에서 받은 주소 값을 받기 위함.
                if (resultCode == RESULT_OK) {
                    str = data.getStringExtra("address");
                    Log.e("잘 받았어 고마워 : ", str);
                    Address.setText(str);
                }
                break;

        }
    }
}