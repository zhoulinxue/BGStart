package org.zhx.common.BGStart.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import org.zhx.common.bgstart.library.api.PermissionLisenter;
import org.zhx.common.bgstart.library.impl.BgStart;

public class MainActivity extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BgStart.getInstance().requestStartPermisstion(this, new PermissionLisenter() {
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
        mHandler.removeCallbacks(null);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e("BgStart", "开始跳转 " + System.currentTimeMillis());
                Intent intent = new Intent(MainActivity.this, TargetActivity.class);
                BgStart.getInstance().startActivity(MainActivity.this, intent, TargetActivity.class.getName());
            }
        }, 1000);
    }


}
