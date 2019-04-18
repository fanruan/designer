package com.fr.design.upm.loader;

import com.fr.decision.webservice.bean.plugin.store.ProjectInfoBean;
import com.fr.decision.webservice.v10.plugin.helper.category.impl.BaseResourceLoader;

/**
 * @author richie
 * @version 10.0
 * Created by richie on 2019-04-18
 */
public class UpmDesignResourceLoader extends BaseResourceLoader {

    @Override
    public String getPluginPath() {
        return "upm/plugin_design.html";
    }

    @Override
    public void checkResourceExist(ProjectInfoBean projectInfoBean) throws Exception {

    }

    @Override
    public String getDownloadPath() throws Exception {
        return "http://fanruan-market.oss-cn-shanghai.aliyuncs.com/upm/1.0/upm-10.0.zip";
    }
}
