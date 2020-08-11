# BGStart
lib for miui 、AndroidQ  background startActivity

### 新增 自定义权限弹窗（1.1.3）
```
  BgManager.getInstance().init(this, new ShowSource() {
            @Override
            public void show(Activity context, final PermissionLisenter permissionListener) {
                // 弹出 权限提醒 弹窗
                new AlertDialog.Builder(context).setCancelable(false)
                        .setTitle(org.zhx.common.bgstart.library.R.string.title_dialog)
                        .setMessage(org.zhx.common.bgstart.library.R.string.message_overlay_failed)
                        .setPositiveButton(org.zhx.common.bgstart.library.R.string.setting, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //TODO
                                if (permissionListener != null) {
                                    permissionListener.onGranted();
                                }
                            }
                        })
                        .setNegativeButton(org.zhx.common.bgstart.library.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (permissionListener != null)
                                    permissionListener.cancel();
                            }
                        })
                        .show();
            }
        });
```

## 集成：
```
allprojects {
    repositories {      
        jcenter()
    }
}
```
Androidx:
```
	dependencies {
	      implementation 'org.zhx.common:bgStart:1.1.3'
	}
```
注意：appcompat （未使用Androidx）引用：
在gradle.properties 添加：
```
android.useAndroidX=true
android.enableJetifier=true
```
## 程序使用：

1、AndroidManifest 中
```
  <activity android:name="org.zhx.common.bgstart.library.BridgeActivity" />
```

2、初始化 Application  onCreat中
```
 @Override
    public void onCreate() {
        super.onCreate();
        BgManager.getInstance().init(this);
    }
```
注：如果你的app  实现了 Application.ActivityLifecycleCallbacks 接口那边 这样初始化
```
@Override
    public void onCreate() {
        super.onCreate();
      BgManager.getInstance().init(this,activityLifecycleCallbacks);
    }
```

3、使用之前 先申请权限：
```
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
```
4、机型适配  可以使用 如下方式 完成 适配（当发现不能正常弹出 权限弹窗，又不能正常调起页面）

   比如 oppo 手机  直接在 方法后面加参数 如 【"huawei"， "oppo"， "vivo"，"meizu"】

```
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
        },"oppo");
```
4、当需要 后台启动页面时 调用以下代码：
```
   Intent intent = new Intent(getApplicationContext(), TargetActivity.class);
   BgStart.getInstance().startActivity(getApplicationContext(), intent, TargetActivity.class.getName());
```
