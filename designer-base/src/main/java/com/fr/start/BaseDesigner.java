/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.start;

import com.fr.common.report.ReportState;
import com.fr.design.DesignerEnvManager;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.constants.DesignerLaunchStatus;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.file.MutilTempalteTabPane;
import com.fr.design.file.TemplateTreePane;
import com.fr.design.fun.DesignerStartOpenFileProcessor;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.DesignerFrame;
import com.fr.design.mainframe.toolbar.ToolBarMenuDock;
import com.fr.design.ui.util.UIUtil;
import com.fr.event.Event;
import com.fr.event.EventDispatcher;
import com.fr.event.Listener;
import com.fr.event.Null;
import com.fr.exit.DesignerExiter;
import com.fr.file.FILE;
import com.fr.file.FILEFactory;
import com.fr.file.FileFILE;
import com.fr.general.ComparatorUtils;
import com.fr.log.FineLoggerFactory;
import com.fr.process.ProcessEventPipe;
import com.fr.process.engine.core.CarryMessageEvent;
import com.fr.process.engine.core.FineProcessContext;
import com.fr.stable.OperatingSystem;

import java.awt.Window;
import java.io.File;
import java.lang.reflect.Method;

/**
 * The main class of Report Designer.
 */
public abstract class BaseDesigner extends ToolBarMenuDock {

    private static final int LOAD_TREE_MAXNUM = 10;

    private final String[] args;

    public BaseDesigner(String[] args) {

        this.args = args;
        init();
    }

    private void init() {
        prepare();
        // 初始化Log Handler
        DesignerEnvManager.loadLogSetting();
        createDesignerFrame();
    }

    /**
     * 准备一些订阅
     */
    private void prepare() {
        EventDispatcher.listen(DesignerLaunchStatus.DESIGNER_INIT_COMPLETE, new Listener<Null>() {
            @Override
            public void on(Event event, Null param) {
                EventDispatcher.stopListen(this);
                UIUtil.invokeLaterIfNeeded(new Runnable() {
                    @Override
                    public void run() {

                        // 打开上次的文件
                        showDesignerFrame(false);
                        DesignerLaunchStatus.setStatus(DesignerLaunchStatus.OPEN_LAST_FILE_COMPLETE);
                    }
                });
            }
        });
        EventDispatcher.listen(DesignerLaunchStatus.STARTUP_COMPLETE, new Listener<Null>() {
            @Override
            public void on(Event event, Null param) {
                EventDispatcher.stopListen(this);
                // 启动完成 停止监听
                ProcessEventPipe eventPipe = FineProcessContext.getParentPipe();
                if (eventPipe != null) {
                    eventPipe.fire(new CarryMessageEvent(ReportState.STOP.getValue()));
                }
                collectUserInformation();
            }
        });
    }

    public void show() {
        UIUtil.invokeLaterIfNeeded(new Runnable() {
            @Override
            public void run() {
                refreshTemplateTree();
            }
        });
    }

    private void refreshTemplateTree() {
        //TODO: 2019-06-14  这里有啥作用？
        DesignerContext.getDesignerFrame().refreshEnv();
        for (int i = 0; !TemplateTreePane.getInstance().getTemplateFileTree().isTemplateShowing() && i < LOAD_TREE_MAXNUM; i++) {
            TemplateTreePane.getInstance().getTemplateFileTree().refresh();
        }
    }

    private void createDesignerFrame() {
        new DesignerFrame(this);
    }

    private void showDesignerFrame(boolean isException) {
        try {
            FILE file = null;
            if (args != null && args.length > 0) {
                // p:需要打开这个报表文件,这个代码不能删除.
                for (String arg : args) {
                    if (ComparatorUtils.equals("demo", arg)) {
                        file = FILEFactory.createFILE(FILEFactory.ENV_PREFIX + DesignerEnvManager.getEnvManager().getLastOpenFile());
                        break;
                    }
                    File f = new File(arg);
                    String path = f.getAbsolutePath();
                    boolean pathends1 = path.endsWith(".cpt")
                            || path.endsWith(".xls");
                    boolean pathends2 = path.endsWith(".xlsx")
                            || path.endsWith(".frm");
                    boolean pathends3 = path.endsWith(".form")
                            || path.endsWith(".cht");
                    boolean pathends4 = pathends1 || pathends2 || pathends3;
                    if (pathends4 || path.endsWith(".chart")) {
                        file = new FileFILE(f);
                    }
                }
            } else {
                file = FILEFactory.createFILE(FILEFactory.ENV_PREFIX + DesignerEnvManager.getEnvManager().getLastOpenFile());
            }
            DesignerFrame df = DesignerContext.getDesignerFrame();
            isException = openFile(df, isException, file);
            df.fireDesignerOpened();
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
            if (!isException) {
                showDesignerFrame(true);
            } else {
                DesignerExiter.getInstance().execute();
            }
        }
    }

    private boolean openFile(final DesignerFrame df, boolean isException, FILE file) {

        //启动时打开指定文件的接口
        DesignerStartOpenFileProcessor processor = ExtraDesignClassManager.getInstance().getSingle(DesignerStartOpenFileProcessor.XML_TAG);
        if (processor != null) {
            FILE f = processor.fileToShow();
            if (f != null) {
                file = f;//避免null
            } else {
                isException = true;//此时有文件nullpointer异常，执行打开空文件
            }
        }
        if (file.exists() && !isException) {
            df.openTemplate(file);
        } else {
            df.addAndActivateJTemplate();
            MutilTempalteTabPane.getInstance().setTemTemplate(HistoryTemplateListPane.getInstance().getCurrentEditingTemplate());
        }
        if (OperatingSystem.isMacOS()) {
            enableFullScreenMode(df);
        }
        df.getSelectedJTemplate().requestGridFocus();
        return isException;
    }

    private void enableFullScreenMode(Window window) {
        String className = "com.apple.eawt.FullScreenUtilities";
        String methodName = "setWindowCanFullScreen";

        try {
            Class<?> clazz = Class.forName(className);
            Method method = clazz.getMethod(methodName, Window.class, boolean.class);
            method.invoke(null, window, true);
        } catch (Throwable t) {
            FineLoggerFactory.getLogger().error("Full screen mode is not supported");
        }
    }

    // 收集用户信息码
    protected void collectUserInformation() {

    }
}
