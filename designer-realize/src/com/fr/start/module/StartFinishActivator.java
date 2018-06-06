package com.fr.start.module;

import com.fr.design.fun.impl.GlobalListenerProviderManager;
import com.fr.design.utils.DesignUtils;
import com.fr.module.Activator;

/**
 * Created by juhaoyu on 2018/1/8.
 */
public class StartFinishActivator extends Activator {
    
    private static final int MESSAGE_PORT = 51462;
    
    @Override
    public void start() {
        
        DesignUtils.creatListeningServer(getStartPort(), startFileSuffix());
        GlobalListenerProviderManager.getInstance().init();
    }
    
    private int getStartPort() {
        
        return MESSAGE_PORT;
    }
    
    
    private String[] startFileSuffix() {
        
        return new String[]{".cpt", ".xls", ".xlsx", ".frm", ".form", ".cht", ".chart"};
    }
    
    @Override
    public void stop() {
    
    }
}
