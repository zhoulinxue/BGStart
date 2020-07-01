package org.zhx.common.bgstart.library;

import android.app.Activity;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (C), 2015-2020
 * FileName: CustomActivityManager
 * Author: zx
 * Date: 2020/4/16 16:08
 * Description:
 */
public class CustomActivityManager {
    private static final String SP_KEY_ACTIVITY_STACK_TOP = "sp_key_activity_stack_top";
    static Map<String, String> map = new HashMap<>();

    public static String getTopActivity() {
        // 这里从SP中读取栈顶Activity名字
        return map.get(SP_KEY_ACTIVITY_STACK_TOP);
    }

    public static void setTopActivity(Activity topActivity) {
        if (topActivity != null) {
            // 这里把栈顶Activity名字存入SP
            map.put(SP_KEY_ACTIVITY_STACK_TOP, topActivity.getClass().getName());
        }
    }

    public static void clearTopActivity() {
        // 这里清除SP数据
        map.clear();
    }

    /**
     * 应用 是否 在后台
     * @return
     */
    public static boolean isAppBackGround() {
        return map == null || map.isEmpty();
    }

}
