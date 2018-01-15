package com.example.gihwan.smart_hm;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Window;

/**
 * Created by GiHwan on 2018. 1. 8..
 */

public class CustomProgressDialog extends Dialog{
    public CustomProgressDialog(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 지저분한(?) 다이얼 로그 제목을 날림
        setContentView(R.layout.custom_dialog); // 다이얼로그에 박을 레이아웃
    }
}
