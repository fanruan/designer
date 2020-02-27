package com.fr.start.module;

import com.fr.design.DesignerEnvManager;
import com.fr.design.EnvChangeEntrance;
import com.fr.design.constants.DesignerLaunchStatus;
import com.fr.design.env.DesignerWorkspaceGenerator;
import com.fr.design.env.DesignerWorkspaceInfo;
import com.fr.design.env.LocalDesignerWorkspaceInfo;
import com.fr.event.Event;
import com.fr.event.EventDispatcher;
import com.fr.event.Listener;
import com.fr.event.Null;
import com.fr.log.FineLoggerFactory;
import com.fr.module.Activator;
import com.fr.stable.StringUtils;
import com.fr.value.NotNullLazyValue;
import com.fr.workspace.WorkContext;
import com.fr.workspace.Workspace;
import org.jetbrains.annotations.NotNull;


/**
 * Created by juhaoyu on 2018/1/8.
 * 设计器启动时的环境相关模块activator
 */
public class DesignerWorkspaceProvider extends Activator {

    private static final String SPECIFY_WORKSPACE = "fr.designer.workspace";

    private NotNullLazyValue<StartupArgs> startupArgs = new NotNullLazyValue<StartupArgs>() {
        @NotNull
        @Override
        protected StartupArgs compute() {
            return findSingleton(StartupArgs.class);
        }
    };

    @Override
    public void start() {
        //检查环境
        DesignerEnvManager.checkNameEnvMap();

        if (startupArgs.getValue().isDemo()) {
            DesignerEnvManager.getEnvManager().setCurrentEnv2Default();
        } else {
            try {
                String current = DesignerEnvManager.getEnvManager().getCurEnvName();
                String workspacePath;
                DesignerWorkspaceInfo workspaceInfo;
                if (StringUtils.isNotEmpty(workspacePath = System.getProperty(SPECIFY_WORKSPACE))) {
                    workspaceInfo = LocalDesignerWorkspaceInfo.create(StringUtils.EMPTY, workspacePath);
                } else {
                    workspaceInfo = DesignerEnvManager.getEnvManager().getWorkspaceInfo(current);
                }
                Workspace workspace = DesignerWorkspaceGenerator.generate(workspaceInfo);
                boolean checkValid = workspace != null && workspaceInfo.checkValid();
                if (!checkValid) {
                    EnvChangeEntrance.getInstance().dealEvnExceptionWhenStartDesigner();
                } else {
                    WorkContext.switchTo(workspace);
                    //在设计器完全启动完成后，对初始环境进行一次服务检测，对主要功能无影响，异常仅做日志提示即可
                    final DesignerWorkspaceInfo selectEnv = workspaceInfo;
                    EventDispatcher.listen(DesignerLaunchStatus.STARTUP_COMPLETE, new Listener<Null>() {
                        @Override
                        public void on(Event event, Null aNull) {
                            try {
                                EnvChangeEntrance.getInstance().serviceDialog(selectEnv);
                            } catch (Exception e) {
                                FineLoggerFactory.getLogger().warn("Check Service Failed");
                            }
                        }
                    });
                }
            } catch (Throwable e) {
                FineLoggerFactory.getLogger().error(e.getMessage(), e);
                EnvChangeEntrance.getInstance().dealEvnExceptionWhenStartDesigner();
            }
        }
    }

    @Override
    public void stop() {
        // void
    }

    @Override
    public void afterAllStart() {
        DesignerLaunchStatus.setStatus(DesignerLaunchStatus.WORKSPACE_INIT_COMPLETE);
    }
}
