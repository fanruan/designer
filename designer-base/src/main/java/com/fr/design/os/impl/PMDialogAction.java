package com.fr.design.os.impl;

import com.fr.config.ServerPreferenceConfig;
import com.fr.design.extra.WebViewDlgHelper;
import com.fr.design.upm.UpmFinder;
import com.fr.design.utils.DesignUtils;
import com.fr.stable.os.Arch;
import com.fr.stable.os.OperatingSystem;
import com.fr.stable.os.support.OSBasedAction;

/**
 * 插件管理窗口
 * @author pengda
 * @date 2019/10/9
 */
public class PMDialogAction implements OSBasedAction {
    private static String PLUGIN_MANAGER_ROUTE = "#management/plugin";
    @Override
    public void execute(Object... objects) {
         if(Arch.getArch() == Arch.ARM){
             DesignUtils.visitEnvServerByParameters( PLUGIN_MANAGER_ROUTE,null,null);
             return;
         }
        if (ServerPreferenceConfig.getInstance().isUseOptimizedUPM() && !OperatingSystem.isLinux()) {
            UpmFinder.showUPMDialog();
        } else {
            WebViewDlgHelper.createPluginDialog();
        }
    }
}
