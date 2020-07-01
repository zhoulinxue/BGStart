package org.zhx.common.bgstart.library.api;

/**
 * @ProjectName: BGStart
 * @Package: org.zhx.common.bgstart.library.api
 * @ClassName: OnStartResultLisenter
 * @Description:java类作用描述
 * @Author: zhouxue
 * @CreateDate: 2020/7/1 11:32
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/7/1 11:32
 * @UpdateRemark: 更新说明
 * @Version:1.0
 */
public interface OnStartResultLisenter {

    public void onSuc();

    public void onNotifycation();

    public void onError(String error);
}
