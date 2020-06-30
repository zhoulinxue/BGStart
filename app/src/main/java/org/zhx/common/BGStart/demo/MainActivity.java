package org.zhx.common.BGStart.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import org.zhx.common.bgstart.library.impl.OverLayImpl;
import org.zhx.common.bgstart.library.utils.Miui;
import org.zhx.common.bgstart.library.api.BgStart;
import org.zhx.common.bgstart.library.api.PermissionLisenter;

public class MainActivity extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BgStart bgStart = new OverLayImpl();
        Log.e("RAG", Miui.isAllowed(this) + "");
        bgStart.requestStartPermisstion(this, new PermissionLisenter() {
            @Override
            public void onGranted() {
                Log.e(TAG, "onGranted");
            }

            @Override
            public void cancel() {
                Log.e(TAG, "cancel");
            }

            @Override
            public void onDenied() {
                Log.e(TAG, "onDenied");
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        //模拟 后台 启动页面  当 按下 home 建 1秒后 弹出 界面
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, B.class);
                new OverLayImpl().startActivity(MainActivity.this, intent, B.class.getName());
            }
        }, 1000);
    }
}
