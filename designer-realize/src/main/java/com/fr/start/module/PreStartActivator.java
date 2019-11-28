package com.fr.start.module;

import com.fr.design.DesignerEnvManager;
import com.fr.design.RestartHelper;
import com.fr.design.i18n.Toolkit;
import com.fr.design.utils.DesignUtils;
import com.fr.event.EventDispatcher;
import com.fr.file.TmpFileUtils;
import com.fr.general.CloudCenter;
import com.fr.general.GeneralContext;
import com.fr.module.Activator;
import com.fr.module.ModuleEvent;

/**
 * Created by juhaoyu on 2018/1/8.
 */
public class PreStartActivator extends Activator {

    @Override
    public void start() {
        //清空临时文件
        TmpFileUtils.cleanUpInnerTmpFiles();
        RestartHelper.deleteRecordFilesWhenStart();
        //初始化
        EventDispatcher.fire(ModuleEvent.MajorModuleStarting, Toolkit.i18nText("Fine-Design_Basic_Initializing"));
        // 完成初始化
        //noinspection ResultOfMethodCallIgnored
        CloudCenter.getInstance();

        // 创建监听服务
        DesignUtils.createListeningServer(DesignUtils.getPort(), startFileSuffix());

        initLanguage();
    }

    @Override
    public void stop() {
        // void
    }

    private void initLanguage() {
        //这两句的位置不能随便调换，因为会影响语言切换的问题
        GeneralContext.setLocale(DesignerEnvManager.getEnvManager(false).getLanguage());
    }

    private String[] startFileSuffix() {

        return new String[]{".cpt", ".xls", ".xlsx", ".frm", ".form", ".cht", ".chart"};
    }
}
