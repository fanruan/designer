package com.fr.design.env;

import com.fr.common.report.ReportState;
import com.fr.concurrent.NamedThreadFactory;
import com.fr.design.EnvChangeEntrance;
import com.fr.design.dialog.FineJOptionPane;
import com.fr.design.i18n.Toolkit;
import com.fr.design.mainframe.messagecollect.StartErrorMessageCollector;
import com.fr.design.mainframe.messagecollect.entity.DesignerErrorMessage;
import com.fr.general.IOUtils;
import com.fr.log.FineLoggerFactory;
import com.fr.process.ProcessEventPipe;
import com.fr.process.engine.core.CarryMessageEvent;
import com.fr.process.engine.core.FineProcessContext;
import com.fr.stable.StringUtils;
import com.fr.workspace.WorkContext;
import com.fr.workspace.Workspace;
import com.fr.workspace.connect.WorkspaceClient;

import javax.swing.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 根据配置生成运行环境
 */
public class DesignerWorkspaceGenerator {

    private static final int WAIT_FREQ = 60;
    private static ExecutorService service = Executors.newCachedThreadPool(
            new NamedThreadFactory("DesignerWorkspaceGenerator"));

    public static Workspace generate(final DesignerWorkspaceInfo config) throws Exception {

        if (config == null || config.getType() == null) {
            return null;
        }

        Workspace workspace = null;
        switch (config.getType()) {
            case Local: {
                workspace = WorkContext.getFactory().build(config.getPath());
                break;
            }
            case Remote: {
                Future<WorkspaceClient> future = service.submit(new Callable<WorkspaceClient>() {
                    @Override
                    public WorkspaceClient call() throws Exception {
                        return WorkContext.getConnector().connect(config.getConnection());
                    }
                });
                WorkspaceClient client = null;
                try {
                     client = future.get(WAIT_FREQ, TimeUnit.SECONDS);
                } catch (Exception e) {
                    FineLoggerFactory.getLogger().error(e.getMessage(), e);
                    RemoteHandler.handle(config);
                }
                if (client != null) {
                    workspace = new RemoteWorkspace(client, config.getConnection());
                }
                break;
            }
        }
        return workspace;
    }

    enum  RemoteHandler {
        SELF;
        public static void handle(DesignerWorkspaceInfo config) {
            ProcessEventPipe eventPipe = FineProcessContext.getParentPipe();
            if (eventPipe != null) {
                eventPipe.fire(new CarryMessageEvent(ReportState.STOP.getValue()));
            }
            StartErrorMessageCollector.getInstance().record(DesignerErrorMessage.REMOTE_DESIGN_NO_RESPONSE.getId(),
                                                            DesignerErrorMessage.REMOTE_DESIGN_NO_RESPONSE.getMessage(),
                                                            StringUtils.EMPTY);
            int result = FineJOptionPane.showOptionDialog(null,
                                                          Toolkit.i18nText("Fine-Design_Error_Remote_No_Response_Tip"),
                                                          Toolkit.i18nText("Fine-Design_Basic_Error_Tittle"),
                                                          JOptionPane.YES_NO_OPTION,
                                                          JOptionPane.ERROR_MESSAGE,
                                                          IOUtils.readIcon("com/fr/design/images/error/error2.png"),
                                                          new Object[] {Toolkit.i18nText("Fine-Design_Error_Remote_No_Response_Wait"), Toolkit.i18nText("Fine-Design_Error_Remote_No_Response_Switch")},
                                                          null);
            if (result == JOptionPane.YES_OPTION) {
                try {
                    generate(config);
                } catch (Exception e) {
                    FineLoggerFactory.getLogger().error(e.getMessage(), e);
                }
            } else {
            }
        }
    }

    public static void stop() {
        service.shutdown();
    }
}
