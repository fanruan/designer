package com.fr.start;

import com.fr.design.DesignerEnvManager;
import com.fr.design.env.DesignerWorkspaceGenerator;
import com.fr.design.mainframe.TemplatePane;
import com.fr.workspace.WorkContext;
import com.fr.workspace.Workspace;

/**
 * Created by juhaoyu on 2018/1/31.
 * 环境切换器
 */
public class EnvSwitcher {
    
    public void switch2LastEnv() {
        
        try {
            String current = DesignerEnvManager.getEnvManager().getCurEnvName();
            Workspace workspace = DesignerWorkspaceGenerator.generate(DesignerEnvManager.getEnvManager().getEnv(current));
            WorkContext.switchTo(workspace);
        } catch (Exception e) {
            TemplatePane.getInstance().dealEvnExceptionWhenStartDesigner();
        }
    }
    
}
