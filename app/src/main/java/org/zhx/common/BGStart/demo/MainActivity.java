package org.zhx.common.BGStart.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
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
        //机型适配  可以使用 如下方式 完成 适配（当发现不能正常弹出 权限弹窗，又不能正常调起页面）

        //   比如 oppo 手机  直接在 方法后面加参数 如 【"huawei"， "oppo"， "vivo"，"meizu"】

        //检查 是否开启权限 （机型未覆盖 慎用 1.1.5 版本）
        boolean hasPermission = BgStart.getInstance().hasBgStartPermission(this);
        Log.e(TAG, hasPermission + "");
        //-------------------------------------------
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
        }, "vivo");
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
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                BgStart.getInstance().startActivity(getApplicationContext(), intent, TargetActivity.class.getName());
            }
        }, 1000);
    }


}
