package com.fr.design.fun;

import com.fr.stable.fun.mark.Immutable;

/**
 * 在env加载之前, 修改env参数的接口
 * 如: https [需要在加载之前设置系统证书];
 * cas [需要动态获取sessionid来修改path]
 *
 * Created by Administrator on 2016/3/31/0031.
 */
public interface DesignerEnvProcessor extends Immutable{

    String XML_TAG = "DesignerEnvProcessor";
    int CURRENT_LEVEL = 1;

    /**
     * 在远程env连接之前, 修改env的path, 以通过权限认证. 如果之前没有jsessionid, 那么就加上, 如果有了, 就更新成新的.
     * 如: localhost:8080/WebReport/ReportServer? -> localhost:8080/WebReport/ReportServer?jsessionid=abcdegf;
     *
     * @return 修改后的jsessionid
     */
    String changeEnvPathBeforeConnect(String userName, String password, String path);

    /**
     * web端预览的时候不需要jsessionid, 他会默认跳转到登陆页面, 如果带上了, 反而会因为里面资源文件加载不到而出问题.
     *
     * @return web端预览地址
     */
    String getWebBrowserURL(String envPath);
}
