package com.fr.start.module;

import com.fr.design.DesignerEnvManager;
import com.fr.design.RestartHelper;
import com.fr.design.utils.DesignUtils;
import com.fr.general.CloudCenter;
import com.fr.general.ComparatorUtils;
import com.fr.general.GeneralContext;
import com.fr.module.Activator;
import com.fr.stable.ProductConstants;

import java.io.File;

/**
 * Created by juhaoyu on 2018/1/8.
 */
public class PreStartActivator extends Activator {

    private static final int MESSAGE_PORT = 51462;

    private static final int DEBUG_PORT = 51463;

    @Override
    public void start() {

        RestartHelper.deleteRecordFilesWhenStart();
        CloudCenter.getInstance();
        if (checkMultiStart()) {
            return;
        }
        initLanguage();
    }

    private boolean checkMultiStart() {

        if (isDebug()) {
            setDebugEnv();
        } else {
            DesignUtils.setPort(getStartPort());
        }

        return false;
    }

    private int getStartPort() {

        return MESSAGE_PORT;
    }


    //在VM options里加入-Ddebug=true激活
    private boolean isDebug() {

        return ComparatorUtils.equals("true", System.getProperty("debug"));
    }


    //端口改一下，环境配置文件改一下。便于启动两个设计器，进行对比调试
    private void setDebugEnv() {

        DesignUtils.setPort(DEBUG_PORT);
        DesignerEnvManager.setEnvFile(new File(ProductConstants.getEnvHome() + File.separator + ProductConstants.APP_NAME + "Env_debug.xml"));
    }

    private void initLanguage() {
        //这两句的位置不能随便调换，因为会影响语言切换的问题
        GeneralContext.setLocale(DesignerEnvManager.getEnvManager(false).getLocale());
    }

    @Override
    public void stop() {

    }
}
