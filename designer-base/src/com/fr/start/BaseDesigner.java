/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.start;

import com.fr.design.DesignerEnvManager;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.file.MutilTempalteTabPane;
import com.fr.design.file.TemplateTreePane;
import com.fr.design.fun.DesignerStartOpenFileProcessor;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.DesignerFrame;
import com.fr.design.mainframe.toolbar.ToolBarMenuDock;
import com.fr.design.utils.DesignUtils;
import com.fr.event.EventDispatcher;
import com.fr.file.FILE;
import com.fr.file.FILEFactory;
import com.fr.file.FileFILE;
import com.fr.general.ComparatorUtils;
import com.fr.locale.InterProviderFactory;
import com.fr.log.FineLoggerFactory;
import com.fr.module.ModuleEvent;
import com.fr.stable.OperatingSystem;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.lang.reflect.Method;

/**
 * The main class of Report Designer.
 */
public abstract class BaseDesigner extends ToolBarMenuDock {

    private static final int LOAD_TREE_MAXNUM = 10;
    
    public BaseDesigner(String[] args) {
    
        init(args);
    }
    
    private void init(String[] args) {
        //初始化
        EventDispatcher.fire(ModuleEvent.MajorModuleStarting, InterProviderFactory.getProvider().getLocText("FR-Designer_Initializing"));
        // 初始化look and feel.这个在预加载之前执行是因为lookAndFeel里的东西，预加载时也要用到
        DesignUtils.initLookAndFeel();
    
        // 初始化Log Handler
        DesignerEnvManager.loadLogSetting();
        createDesignerFrame();
    }
    
    public void show(String[] args) {
        
        collectUserInformation();
        showDesignerFrame(args, DesignerContext.getDesignerFrame(), false);
        for (int i = 0; !TemplateTreePane.getInstance().getTemplateFileTree().isTemplateShowing() && i < LOAD_TREE_MAXNUM; i++) {
            TemplateTreePane.getInstance().getTemplateFileTree().refresh();
        }
    }
    
    
    private void createDesignerFrame() {
        
        new DesignerFrame(this);
    }


    private void showDesignerFrame(String[] args, final DesignerFrame df,
                                   boolean isException) {
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
                file = FILEFactory.createFILE(FILEFactory.ENV_PREFIX
                        + DesignerEnvManager.getEnvManager().getLastOpenFile());
            }
            isException = openFile(df, isException, file);
            df.fireDesignerOpened();
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
            if (!isException) {
                showDesignerFrame(args, df, true);
            } else {
                System.exit(0);
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
        df.addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent e) {
                df.getSelectedJTemplate().requestGridFocus();
            }
        });
        df.setVisible(true);
        return isException;
    }
    
    
    /**
     * @param window
     */
    private void enableFullScreenMode(Window window) {
        String className = "com.apple.eawt.FullScreenUtilities";
        String methodName = "setWindowCanFullScreen";

        try {
            Class<?> clazz = Class.forName(className);
            Method method = clazz.getMethod(methodName, new Class<?>[]{
                    Window.class, boolean.class});
            method.invoke(null, window, true);
        } catch (Throwable t) {
            FineLoggerFactory.getLogger().error("Full screen mode is not supported");
        }
    }


    protected abstract String module2Start();

    // 收集用户信息码
    protected void collectUserInformation() {

    }
}