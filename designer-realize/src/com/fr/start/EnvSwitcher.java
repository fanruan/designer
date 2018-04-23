package com.fr.start;

import com.fr.base.FRContext;
import com.fr.design.DesignerEnvManager;
import com.fr.design.mainframe.TemplatePane;
import com.fr.env.SignIn;
import com.fr.general.Inter;

/**
 * Created by juhaoyu on 2018/1/31.
 * 环境切换器
 */
public class EnvSwitcher {
    
    public void switch2LastEnv() {
        
        try {
            String current = DesignerEnvManager.getEnvManager().getCurEnvName();
            SignIn.signIn(DesignerEnvManager.getEnvManager().getEnv(current));
            if (!FRContext.getCurrentEnv().testServerConnectionWithOutShowMessagePane()) {
                throw new Exception(Inter.getLocText("Datasource-Connection_failed"));
            }
        } catch (Exception e) {
            TemplatePane.getInstance().dealEvnExceptionWhenStartDesigner();
        }
    }
    
}
