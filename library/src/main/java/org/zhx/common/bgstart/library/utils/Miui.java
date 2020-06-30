package org.zhx.common.bgstart.library.utils;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.Properties;

/**
 * Copyright (C), 2015-2020
 * FileName: MIUIUtils
 * Author: zx
 * Date: 2020/4/17 16:09
 * Description:
 */
public class Miui {
    private static String TAG = Miui.class.getSimpleName();
    // 检测MIUI
    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";
    private static final String miui = "ro.miui.ui.version.name";
    private static final String miui5 = "V5";
    private static final String miui6 = "V6";
    private static final String miui7 = "V7";
    private static final String miui8 = "V8";
    private static final String miui9 = "V9";

    public static boolean isMIUI() {
        Properties prop = new Properties();
        boolean isMIUI;
        try {
            prop.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
        } catch (IOException e) {
            return isMIUI2("Xiaomi") ;
        }
        isMIUI = prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null
                || prop.getProperty(miui, null) != null
                || prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null;
        return isMIUI;
    }

    private static boolean isMIUI2(String manufact) {
        String manufacturer = Build.MANUFACTURER;
        //这个字符串可以自己定义,例如判断华为就填写huawei,魅族就填写meizu
        if (manufact.equalsIgnoreCase(manufacturer)) {
            return true;
        }
        return false;
    }

    private static String getProp() {
        return getProp(miui);
    }

    /**
     * 悬浮窗跳转
     * @param context
     */
    public static void jump(Context context) {
        switch (getProp()) {
            case miui5:
                reqForMiui5(context);
                break;
            case miui6:
            case miui7:
                reqForMiui67(context);
                break;
            case miui8:
            case miui9:
                reqForMiui89(context);
                break;
        }
    }


    private static void reqForMiui5(Context context) {
        String packageName = context.getPackageName();
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", packageName, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (isIntentAvailable(intent, context)) {
            context.startActivity(intent);
        } else {
            Log.e(TAG, "intent is not available!");
        }
    }

    private static void reqForMiui67(Context context) {
        Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
        intent.setClassName("com.miui.securitycenter",
                "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
        intent.putExtra("extra_pkgname", context.getPackageName());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (isIntentAvailable(intent, context)) {
            context.startActivity(intent);
        } else {
            Log.e(TAG, "intent is not available!");
        }
    }

    static boolean isIntentAvailable(Intent intent, Context context) {
        return intent != null && context.getPackageManager().queryIntentActivities(
                intent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0;
    }

    private static void reqForMiui89(Context context) {
        Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
        intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
        intent.putExtra("extra_pkgname", context.getPackageName());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (isIntentAvailable(intent, context)) {
            context.startActivity(intent);
        } else {
            intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
            intent.setPackage("com.miui.securitycenter");
            intent.putExtra("extra_pkgname", context.getPackageName());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (isIntentAvailable(intent, context)) {
                context.startActivity(intent);
            } else {
                Log.e(TAG, "intent is not available!");
            }
        }
    }



    public static boolean isAllowed(Context context) {
        AppOpsManager ops = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        try {
            int op = 10021;
            Method method = ops.getClass().getMethod("checkOpNoThrow", new Class[]{int.class, int.class, String.class});
            Integer result = (Integer) method.invoke(ops, op, android.os.Process.myUid(), context.getPackageName());
            return result == AppOpsManager.MODE_ALLOWED;

        } catch (Exception e) {
            Log.e(TAG, "not support");
        }
        return false;
    }

    static String getProp(String name) {
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + name);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            String line = input.readLine();
            input.close();
            return line;
        } catch (IOException ex) {
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
