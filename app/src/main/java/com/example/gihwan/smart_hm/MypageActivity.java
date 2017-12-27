package com.example.gihwan.smart_hm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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
   // String Code_Val_send = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        Code_Val = (EditText) findViewById(R.id.code_val);

        try {
            kakaoLink = KakaoLink.getKakaoLink(MypageActivity.this);
        } catch (KakaoParameterException e) {
            e.printStackTrace();
        }

    }

    public void Mypage_Btn_click(View v){
        switch (v.getId()){
            case R.id.code_send:    // 카카오 버튼을 통해 코드번호를 보내기 위함
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
            case R.id.Mypage_Leave:     // 회원 탈퇴 버튼 클릭시
                Toast.makeText(getApplicationContext(), " DataBase Delete 처리가 되어야합니다", Toast.LENGTH_SHORT).show();
                break;
            case R.id.Mypage_Update:    // 회원 정보 변경 버튼 클릭시
                Toast.makeText(getApplicationContext(), " DataBase Update 처리가 되어야합니다", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
