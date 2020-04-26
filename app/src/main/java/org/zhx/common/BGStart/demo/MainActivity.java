package org.zhx.common.BGStart.demo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import org.zhx.common.bgstart.library.IBgStartImpl;
import org.zhx.common.bgstart.library.Miui;
import org.zhx.common.bgstart.library.SystemAlertWindow;
import org.zhx.common.bgstart.library.PermissionUtil;
import org.zhx.common.bgstart.library.api.BgStart;
import org.zhx.common.bgstart.library.api.PermissionLisenter;

public class MainActivity extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BgStart bgStart = new IBgStartImpl();
        Log.e("RAG", Miui.isAllowed(this)+"");
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

    //    @Override
    protected void onStop() {
        super.onStop();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, B.class);
                new IBgStartImpl().startActivity(MainActivity.this, intent, B.class.getName());
            }
        }, 1000);
    }
}