package com.fr.design.fun.impl;

import com.fr.design.fun.DesignerEnvProcessor;

/**
 * Created by Administrator on 2016/3/31/0031.
 */
public abstract class AbstractDesignerEnvProcessor implements DesignerEnvProcessor {

    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

    public String changeEnvPathBeforeConnect(String userName, String password, String path){
        return path;
    }

    /**
     * web端预览的时候不需要jsessionid, 他会默认跳转到登陆页面, 如果带上了, 返回会因为里面资源文件加载不到而出问题.
     *
     * @return web端预览地址
     */
    public String getWebBrowserURL(String envPath){
        return envPath;
    }

}
