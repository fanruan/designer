package com.fr.start.module;

import com.fr.base.FRContext;
import com.fr.design.DesignerEnvManager;
import com.fr.design.RestartHelper;
import com.fr.design.module.DesignModule;
import com.fr.general.GeneralContext;
import com.fr.general.Inter;
import com.fr.general.SiteCenter;
import com.fr.module.Activator;
import com.fr.stable.BuildContext;

/**
 * Created by juhaoyu on 2018/1/8.
 */
public class PreStartActivator extends Activator {
    
    @Override
    public void start() {
        
        RestartHelper.deleteRecordFilesWhenStart();
        BuildContext.setBuildFilePath("/com/fr/stable/build.properties");
        SiteCenter.getInstance();
        initLanguage();
        
        // 在 initLanguage 之后加载设计器国际化文件，确保是正确的语言环境
        Inter.loadLocaleFile(GeneralContext.getLocale(), DesignModule.LOCALE_FILE_PATH);
    }
    
    private void initLanguage() {
        //这两句的位置不能随便调换，因为会影响语言切换的问题
        FRContext.setLanguage(DesignerEnvManager.getEnvManager(false).getLanguage());
        DesignerEnvManager.checkNameEnvMap();
    }
    
    @Override
    public void stop() {
    
    }
}
