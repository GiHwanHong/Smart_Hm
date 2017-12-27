package com.example.gihwan.smart_hm;

import android.app.Activity;
import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TabHost;
import android.widget.Toast;

/**
 * Created by GiHwan on 2017. 12. 27..
 */

public class LoginActivity extends ActivityGroup {

    TabHost tabhost1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tabhost1 = (TabHost) findViewById(R.id.tabhost1);
        tabhost1.setup(getLocalActivityManager());

        tabhost1.addTab(tabhost1.newTabSpec("tab1tag")                      // newTabSpec - TabSpec 객체는 탭버튼의 형태와 탭의 내용을 관리하는 객체입니다.
                .setIndicator(getString(R.string.tab1))                     // TabSpec 객체를 생성할때 사용할 tag 이름을 지정합니다.
                .setContent(new Intent(this, Tab1Activity.class)));
        tabhost1.addTab(tabhost1.newTabSpec("tab2tag")
                .setIndicator(getString(R.string.tab2))
                .setContent(new Intent(this, Tab2Activity.class)));
        tabhost1.addTab(tabhost1.newTabSpec("tab3tag")
                .setIndicator(getString(R.string.tab3))
                .setContent(new Intent(this, MypageActivity.class)));

        createTab();


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

}