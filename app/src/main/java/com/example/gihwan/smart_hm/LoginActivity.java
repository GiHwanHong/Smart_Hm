package com.example.gihwan.smart_hm;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.Toast;

import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.util.KakaoParameterException;

/**
 * Created by GiHwan on 2017. 12. 27..
 */

public class LoginActivity extends ActivityGroup {

    TabHost tabhost1;
    private KakaoLink kakaoLink; // 카카오톡 메신저를 사용하기 위해 선언해놓은 변수

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tabhost1 = (TabHost) findViewById(R.id.tabhost1);
        tabhost1.setup(getLocalActivityManager());

        tabhost1.addTab(tabhost1.newTabSpec("tab1tag")                      // newTabSpec - TabSpec 객체는 탭버튼의 형태와 탭의 내용을 관리하는 객체입니다.
                .setIndicator(getString(R.string.tab1))                     // TabSpec 객체를 생성할때 사용할 tag 이름을 지정합니다.
                .setContent(new Intent(this, StateActivity.class)));
        tabhost1.addTab(tabhost1.newTabSpec("tab2tag")
                .setIndicator(getString(R.string.tab2))
                .setContent(new Intent(this, ChartActivity.class)));
        tabhost1.addTab(tabhost1.newTabSpec("tab3tag")
                .setIndicator(getString(R.string.tab3))
                .setContent(new Intent(this, MypageActivity.class)));

        createTab();

        try {
            kakaoLink = KakaoLink.getKakaoLink(LoginActivity.this);
        } catch (KakaoParameterException e) {
            e.printStackTrace();
        }

    }

    public void createTab() {

        tabhost1.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                switch (tabId) {
                    case "tab1tag":
                        Toast.makeText(getApplicationContext(), getString(R.string.tab1) + "탭입니다 ", Toast.LENGTH_SHORT).show();
                        break;
                    case "tab2tag":
                        Toast.makeText(getApplicationContext(), getString(R.string.tab2) + "탭입니다 ", Toast.LENGTH_SHORT).show();
                        break;
                    case "tab3tag":
                        Toast.makeText(getApplicationContext(), getString(R.string.tab3) + "탭입니다 ", Toast.LENGTH_SHORT).show();
                        break;

                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_kakao:
                final KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder
                        = kakaoLink.createKakaoTalkLinkMessageBuilder();
                try {
                    kakaoTalkLinkMessageBuilder.addText("전송된 코드는 \n => ");
                    kakaoLink.sendMessage(kakaoTalkLinkMessageBuilder, this);   // 메시지 전송
                } catch (KakaoParameterException e) {
                    e.printStackTrace();
                }
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}