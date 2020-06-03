# BGStart
lib for miui 、AndroidQ  background startActivity

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
	      implementation 'org.zhx.common:bgStart:1.0.0'
	}
```
注意：appcompat （未使用Androidx）引用：
在gradle.properties 添加：
```
android.useAndroidX=true
android.enableJetifier=true
```
## 程序使用：
1、使用之前 先申请权限：
```
 BgStart bgStart = new IBgStartImpl();
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
```
2、当需要 后台启动页面时 调用以下代码：
```
  Intent intent = new Intent(MainActivity.this, B.class);
                bgStart.startActivity(MainActivity.this, intent, B.class.getName());
```
