package com.fr.design.fun;

import com.fr.stable.fun.Level;

/**
 * 在env加载之前, 修改env参数的接口
 * 如: https [需要在加载之前设置系统证书];
 * cas [需要动态获取sessionid来修改path]
 *
 * Created by Administrator on 2016/3/31/0031.
 */
public interface DesignerEnvProcessor extends Level{

    String XML_TAG = "DesignerEnvProcessor";
    int CURRENT_LEVEL = 1;

    /**
     * 在远程env连接之前, 修改env的path, 以通过权限认证. 如果之前没有jsessionid, 那么就加上, 如果有了, 就更新成新的.
     * 如: localhost:8080/WebReport/ReportServer? -> localhost:8080/WebReport/ReportServer?jsessionid=abcdegf;
     *
     * @return 修改后的jsessionid
     */
    String changeEnvPathBeforeConnect(String userName, String password, String path);
}
