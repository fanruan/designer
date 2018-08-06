package com.fr.start.module;

import com.fr.design.DesignerEnvManager;
import com.fr.design.env.DesignerWorkspaceGenerator;
import com.fr.design.env.DesignerWorkspaceInfo;
import com.fr.design.mainframe.TemplatePane;
import com.fr.module.Activator;

import com.fr.workspace.WorkContext;
import com.fr.workspace.Workspace;


/**
 * Created by juhaoyu on 2018/1/8.
 * 设计器启动时的环境相关模块activator
 */
public class DesignerWorkspaceProvider extends Activator {
    
    @Override
    public void start() {
        //检查环境
        DesignerEnvManager.checkNameEnvMap();
        
        if (getModule().leftFindSingleton(StartupArgs.class) != null && getModule().leftFindSingleton(StartupArgs.class).isDemo()) {
            DesignerEnvManager.getEnvManager().setCurrentEnv2Default();
        } else {
            try {
                String current = DesignerEnvManager.getEnvManager().getCurEnvName();
                DesignerWorkspaceInfo workspaceInfo = DesignerEnvManager.getEnvManager().getWorkspaceInfo(current);
                Workspace workspace = DesignerWorkspaceGenerator.generate(workspaceInfo);
                boolean checkValid = workspace == null ? false : workspaceInfo.checkValid();
                if (!checkValid) {
                    TemplatePane.getInstance().dealEvnExceptionWhenStartDesigner();
                } else {
                    WorkContext.switchTo(workspace);
                }
            } catch (Throwable e) {
                TemplatePane.getInstance().dealEvnExceptionWhenStartDesigner();
            }
        }
    }
    
    
    @Override
    public void stop() {
    
    }
    
    
}
