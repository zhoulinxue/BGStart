package org.zhx.common.bgstart.library.api;

import android.app.Activity;

/**
 * @ProjectName: BGStart
 * @Package: org.zhx.common.bgstart.library.api
 * @ClassName: PermissionServer
 * @Description:java类作用描述
 * @Author: zhouxue
 * @CreateDate: 2020/7/16 10:01
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/7/16 10:01
 * @UpdateRemark: 更新说明
 * @Version:1.0
 */
public interface PermissionServer {
    public void checkPermisstion(Activity activity, PermissionLisenter lisenter,String...params);
}
