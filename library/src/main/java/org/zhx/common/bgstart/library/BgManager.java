package org.zhx.common.bgstart.library;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * @ProjectName: BGStart
 * @Package: org.zhx.common.bgstart.library
 * @ClassName: BgManager
 * @Description:java类作用描述
 * @Author: zhouxue
 * @CreateDate: 2020/6/28 10:27
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/6/28 10:27
 * @UpdateRemark: 更新说明
 * @Version:1.0
 */
public class BgManager {
    private String TAG = BgManager.class.getSimpleName();
    private Map<String, Object> models = new HashMap<>();

    public <T> T with(Class<T> service) {
        if (service != null) {
            Object object = models.get(service.getSimpleName());
            if (object == null) {
                Log.e(TAG, "creat  new Model" + service.getSimpleName());
                try {
                    object = service.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                models.put(service.getSimpleName(), object);
            }
            return (T) object;
        }
        return null;
    }
}
