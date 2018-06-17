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
            Workspace workspace = DesignerWorkspaceGenerator.generate(DesignerEnvManager.getEnvManager().getWorkspaceInfo(current));
            if (workspace == null) {
                TemplatePane.getInstance().dealEvnExceptionWhenStartDesigner();
            } else {
                WorkContext.switchTo(workspace);
            }
        } catch (Throwable e) {
            TemplatePane.getInstance().dealEvnExceptionWhenStartDesigner();
        }
    }
    
    /**
     * 找不到默认的工作空间时，让用户手动选择一个
     */
    private Workspace chooseWorkspace() {
        
        return null;
    }
    
}
