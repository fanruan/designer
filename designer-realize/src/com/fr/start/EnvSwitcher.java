package com.fr.start;

import com.fr.base.Env;
import com.fr.base.env.EnvUpdater;
import com.fr.design.DesignerEnvManager;
import com.fr.design.env.EnvGenerator;
import com.fr.design.mainframe.TemplatePane;

/**
 * Created by juhaoyu on 2018/1/31.
 * 环境切换器
 */
public class EnvSwitcher {
    
    public void switch2LastEnv() {
        
        try {
            String current = DesignerEnvManager.getEnvManager().getCurEnvName();
            Env env = EnvGenerator.generate(DesignerEnvManager.getEnvManager().getEnv(current));
            EnvUpdater.updateEnv(env);
        } catch (Exception e) {
            TemplatePane.getInstance().dealEvnExceptionWhenStartDesigner();
        }
    }
    
}
