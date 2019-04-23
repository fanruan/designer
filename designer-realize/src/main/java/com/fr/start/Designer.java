package com.fr.start;

import com.fr.base.BaseUtils;
import com.fr.base.vcs.DesignerMode;
import com.fr.design.DesignerEnvManager;
import com.fr.design.RestartHelper;
import com.fr.design.actions.core.ActionFactory;
import com.fr.design.actions.file.WebPreviewUtils;
import com.fr.design.actions.file.newReport.NewPolyReportAction;
import com.fr.design.actions.file.newReport.NewWorkBookAction;
import com.fr.design.actions.server.ServerConfigManagerAction;
import com.fr.design.actions.server.StyleListAction;
import com.fr.design.actions.server.WidgetManagerAction;
import com.fr.design.constants.UIConstants;
import com.fr.design.file.HistoryTemplateListCache;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.file.MutilTempalteTabPane;
import com.fr.design.fun.MenuHandler;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ibutton.UIPreviewButton;
import com.fr.design.gui.imenu.UIMenuItem;
import com.fr.design.gui.imenu.UIPopupMenu;
import com.fr.design.gui.itoolbar.UILargeToolbar;
import com.fr.design.mainframe.ActiveKeyGenerator;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.DesignerFrameFileDealerPane;
import com.fr.design.mainframe.InformationCollector;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.mainframe.JWorkBook;
import com.fr.design.mainframe.alphafine.component.AlphaFinePane;
import com.fr.design.mainframe.bbs.UserInfoLabel;
import com.fr.design.mainframe.bbs.UserInfoPane;
import com.fr.design.mainframe.template.info.TemplateInfoCollector;
import com.fr.design.mainframe.toolbar.ToolBarMenuDockPlus;
import com.fr.design.mainframe.vcs.proxy.VcsCacheFileNodeFile;
import com.fr.design.mainframe.vcs.ui.FileVersionTable;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.menu.MenuDef;
import com.fr.design.menu.SeparatorDef;
import com.fr.design.menu.ShortCut;
import com.fr.design.module.DesignModuleFactory;
import com.fr.design.utils.DesignUtils;
import com.fr.design.utils.concurrent.ThreadFactoryBuilder;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.ComparatorUtils;
import com.fr.log.FineLoggerFactory;
import com.fr.module.Module;
import com.fr.module.ModuleContext;
import com.fr.runtime.FineRuntime;
import com.fr.stable.BuildContext;
import com.fr.stable.OperatingSystem;
import com.fr.stable.ProductConstants;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;
import com.fr.stable.lifecycle.LifecycleFatalError;
import com.fr.stable.project.ProjectConstants;
import com.fr.stable.xml.XMLTools;
import com.fr.start.fx.SplashFx;
import com.fr.start.jni.SplashMac;
import com.fr.start.module.StartupArgs;
import com.fr.start.preload.ImagePreLoader;
import com.fr.start.server.ServerTray;
import com.fr.workspace.WorkContext;
import com.fr.workspace.server.vcs.VcsOperator;
import com.fr.design.mainframe.vcs.common.VcsHelper;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Designer extends BaseDesigner {

    private static final int TOOLBARPANEVGAP = -4;
    private static final int PREVIEW_DOWN_X_GAP = 92;
    private static final int GAP = 7;
    private static final String OLD_ENV_FOLDER_71 = ".FineReport71";
    private static final String OLD_ENV_FOLDER_70 = ".FineReport70";

    private UserInfoPane userInfoPane;
    private UIButton saveButton;
    private UIButton undo;
    private UIButton redo;
    private UIPreviewButton run;

    /**
     * 设计器启动的Main方法
     *
     * @param args 参数
     */
    public static void main(String[] args) {
    
        //启动运行时
        FineRuntime.start();
        BuildContext.setBuildFilePath("/com/fr/stable/build.properties");
        // 如果端口被占用了 说明程序已经运行了一次,也就是说，已经建立一个监听服务器，现在只要给服务器发送命令就好了
        if (DesignUtils.isStarted()) {
            DesignUtils.clientSend(args);
            FineLoggerFactory.getLogger().error("Designer port not available.");
            System.exit(0);
            return;
        }
        RestartHelper.deleteRecordFilesWhenStart();

        preloadResource();

        SplashContext.getInstance().registerSplash(createSplash());

        SplashContext.getInstance().show();
        Module designerRoot = ModuleContext.parseRoot("designer-startup.xml");
        //传递启动参数
        designerRoot.setSingleton(StartupArgs.class, new StartupArgs(args));
        try {
            designerRoot.start();
        } catch (LifecycleFatalError fatal) {
            SplashContext.getInstance().hide();
            JOptionPane.showMessageDialog(null, fatal.getMessage(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Error"), JOptionPane.ERROR_MESSAGE);
            FineLoggerFactory.getLogger().error(fatal.getMessage(), fatal);
            System.exit(0);
        }

        if (WorkContext.getCurrent().isLocal()) {
            //初始化一下serverTray
            ServerTray.init();
        }

    }

    private static void preloadResource() {
        ExecutorService service = Executors.newCachedThreadPool();

        service.submit(new Runnable() {
            @Override
            public void run() {
                new ImagePreLoader();
            }
        });

        service.submit(new Runnable() {
            @Override
            public void run() {
                TemplateInfoCollector.getInstance();
            }
        });
        service.shutdown();
    }

    private static SplashStrategy createSplash() {
        // 这里可以开接口加载自定义启动画面
        if (OperatingSystem.isWindows()) {
            return new SplashFx();
        } else if (OperatingSystem.isMacOS()) {
            return new SplashMac();
        }
        return new SplashFx();
    }

    public Designer(String[] args) {
        super(args);
    }


    /**
     * 创建新建文件的快捷方式数组。
     *
     * @return 返回快捷方式的数组
     */
    @Override
    public ShortCut[] createNewFileShortCuts() {
        ArrayList<ShortCut> shortCuts = new ArrayList<ShortCut>();
//        shortCuts.add(new NewWorkBookXAction());
        shortCuts.add(new NewWorkBookAction());
        shortCuts.add(new NewPolyReportAction());
        try {
            if (DesignModuleFactory.getNewFormAction() != null) {
                shortCuts.add((ShortCut) DesignModuleFactory.getNewFormAction().newInstance());
            }
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
        return shortCuts.toArray(new ShortCut[shortCuts.size()]);
    }

    @Override
    protected MenuDef createServerMenuDef(ToolBarMenuDockPlus plus) {
        MenuDef menuDef = super.createServerMenuDef(plus);

        if (WorkContext.getCurrent() == null) {
            return menuDef;
        }

        if (!DesignerMode.isAuthorityEditing()) {
            menuDef.addShortCut(SeparatorDef.DEFAULT);

            if (WorkContext.getCurrent().isRoot()) {
                menuDef.addShortCut(new ServerConfigManagerAction(), new StyleListAction(), new WidgetManagerAction());
                if (ActionFactory.getChartPreStyleAction() != null) {
                    menuDef.addShortCut(ActionFactory.getChartPreStyleAction());
                }
                if (ActionFactory.getChartEmptyDataStyleAction() != null) {
                    menuDef.addShortCut(ActionFactory.getChartEmptyDataStyleAction());
                }
                if (ActionFactory.getChartMapEditorAction() != null) {
                    menuDef.addShortCut(ActionFactory.getChartMapEditorAction());
                }
            }

            insertMenu(menuDef, MenuHandler.SERVER);
        }

        return menuDef;
    }


    /**
     * 创建设计器上几个比较大的图标：新建cpt，保存，前进，后退，运行。
     *
     * @return 返回大图标对应的工具栏
     */
    @Override
    public UILargeToolbar createLargeToolbar() {
        UILargeToolbar largeToolbar = super.createLargeToolbar();
        largeToolbar.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 4));
        largeToolbar.add(generateEmptyGap(1));
        createRunButton(largeToolbar);
        largeToolbar.add(run);
        largeToolbar.add(generateEmptyGap(GAP));
        largeToolbar.addSeparator(new Dimension(2, 42));
        largeToolbar.setBorder(new MatteBorder(new Insets(0, 0, 1, 0), UIConstants.LINE_COLOR));
        return largeToolbar;
    }

    private JPanel generateEmptyGap(final int width) {
        JPanel panel = new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                Dimension dim = super.getPreferredSize();
                dim.width = width;
                return dim;
            }
        };
        panel.setBackground(null);
        panel.setOpaque(false);
        return panel;
    }

    /**
     * 创建上面一排的工具栏按钮
     *
     * @return 按钮
     */
    @Override
    public UIButton[] createUp() {
        return new UIButton[]{createSaveButton(), createUndoButton(), createRedoButton(), };
    }


    private UIButton createSaveButton() {
        saveButton = new UIButton(BaseUtils.readIcon("/com/fr/design/images/buttonicon/save.png"));
        saveButton.setToolTipText(KeySetUtils.SAVE_TEMPLATE.getMenuKeySetName());
        saveButton.set4ToolbarButton();
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTemplate<?, ?> jt = HistoryTemplateListCache.getInstance().getCurrentEditingTemplate();
                jt.stopEditing();
                jt.saveTemplate();
                jt.requestFocus();
                String fileName = VcsHelper.getEdittingFilename();
                int latestFileVersion = 0;
                try {
                    latestFileVersion = WorkContext.getCurrent().get(VcsOperator.class).getLatestFileVersion(fileName);

                } catch (Exception e1) {
                    FineLoggerFactory.getLogger().error(e1.getMessage());
                }
                try {
                    if (jt.getEditingFILE() instanceof VcsCacheFileNodeFile) {
                        WorkContext.getCurrent().get(VcsOperator.class).saveVersionFromCache(VcsHelper.CURRENT_USERNAME, fileName, StringUtils.EMPTY, latestFileVersion + 1);
                        String path = DesignerFrameFileDealerPane.getInstance().getSelectedOperation().getFilePath();
                        FileVersionTable.getInstance().updateModel(1, WorkContext.getCurrent().get(VcsOperator.class).getVersions(path.replaceFirst("/", "")));

                    } else {
                        WorkContext.getCurrent().get(VcsOperator.class).saveVersion(VcsHelper.CURRENT_USERNAME, fileName, StringUtils.EMPTY, latestFileVersion + 1);
                    }
                } catch (Exception e1) {
                    FineLoggerFactory.getLogger().error(e1.getMessage());
                }

            }
        });
        return saveButton;
    }

    private UIButton createUndoButton() {
        undo = new UIButton(BaseUtils.readIcon("/com/fr/design/images/buttonicon/undo.png"));
        undo.setToolTipText(KeySetUtils.UNDO.getMenuKeySetName());
        undo.set4ToolbarButton();
        undo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTemplate<?, ?> jt = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
                if (jt != null) {
                    jt.undo();
                }
            }
        });
        return undo;
    }

    private UIButton createRedoButton() {
        redo = new UIButton(BaseUtils.readIcon("/com/fr/design/images/buttonicon/redo.png"));
        redo.setToolTipText(KeySetUtils.REDO.getMenuKeySetName());
        redo.set4ToolbarButton();
        redo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTemplate<?, ?> jt = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
                if (jt != null) {
                    jt.redo();
                }
            }
        });
        return redo;
    }

    private void createRunButton(UILargeToolbar largeToolbar) {
        run = new UIPreviewButton(new UIButton(UIConstants.PAGE_BIG_ICON) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(34, 34);
            }
        }, new UIButton(UIConstants.PREVIEW_DOWN) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(34, 10);
            }
        }
        ) {
            @Override
            protected void upButtonClickEvent() {
                JTemplate<?, ?> jt = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
                if (jt == null) {
                    return;
                }
                WebPreviewUtils.preview(jt);
            }

            @Override
            protected void downButtonClickEvent() {
                final JTemplate<?, ?> jt = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
                if (jt == null) {
                    return;
                }

                UIPopupMenu menu = new UIPopupMenu();

                UIMenuItem[] items = jt.createMenuItem4Preview();
                for (int i = 0; i < items.length; i++) {
                    menu.add(items[i]);
                }
                GUICoreUtils.showPopupMenu(menu, MutilTempalteTabPane.getInstance(), MutilTempalteTabPane.getInstance().getX() - PREVIEW_DOWN_X_GAP, MutilTempalteTabPane.getInstance().getY() - 1 + MutilTempalteTabPane.getInstance().getHeight());
            }

            @Override
            public Dimension getPreferredSize() {
                // TODO Auto-generated method stub
                return new Dimension(34, 46);
            }
        };
        run.setExtraPainted(false);
        run.set4Toolbar();
        run.getUpButton().setToolTipText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Preview"));
        run.getDownButton().setToolTipText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Dropdown_More_Preview"));
    }

    @Override
    protected void refreshLargeToolbarState() {
        JTemplate<?, ?> jt = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
        if (jt == null) {
            return;
        }
        saveButton.setEnabled(!jt.isSaved() && !DesignerMode.isVcsMode());
        MutilTempalteTabPane.getInstance().refreshOpenedTemplate(HistoryTemplateListPane.getInstance().getHistoryList());
        MutilTempalteTabPane.getInstance().repaint();
        if (DesignerEnvManager.getEnvManager().isSupportUndo()) {
            undo.setEnabled(jt.canUndo());
            redo.setEnabled(jt.canRedo());
        } else {
            undo.setEnabled(false);
            redo.setEnabled(false);
        }

        run.getUpButton().setIcon(jt.getPreviewLargeIcon());

    }

    /**
     * 生成工具栏
     *
     * @param toolbarComponent 工具栏
     * @param plus             对象
     * @return 更新后的toolbar
     */
    @Override
    public JComponent resetToolBar(JComponent toolbarComponent, ToolBarMenuDockPlus plus) {
        //如果是处于权限编辑状态
        if (DesignerMode.isAuthorityEditing()) {
            if (plus instanceof JWorkBook && plus.toolbars4Target() == null) {
                //聚合块编辑
                return super.polyToolBar(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Privilege_Poly_Block_Edit"));
            } else {
                return plus.toolBar4Authority();
            }
        }

        if (plus.toolbarPanes4Form().length == 0) {
            return super.resetToolBar(toolbarComponent, plus);
        } else {
            JPanel toolbarPane;
            toolbarPane = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, TOOLBARPANEVGAP));
            Dimension dim = new Dimension();
            dim.height = plus.getToolBarHeight();
            toolbarPane.setPreferredSize(dim);
            toolbarPane.setFocusable(true);
            JPanel[] paneArray = plus.toolbarPanes4Form();
            for (int i = 0; i < paneArray.length; i++) {
                toolbarPane.add(paneArray[i]);
            }
            return toolbarPane;
        }
    }


    /**
     * 生成报表设计和表单设计的编辑区域
     *
     * @return 返回编辑区域
     */
    @Override
    public JTemplate<?, ?> createNewTemplate() {
        return new JWorkBook();
    }

    /**
     * 创建论坛登录面板, chart那边不需要
     *
     * @return 面板组件
     */
    @Override
    public Component createBBSLoginPane() {
        if (userInfoPane == null) {
            userInfoPane = UserInfoPane.getInstance();
        }
        return userInfoPane;
    }

    /**
     * 创建alphafine打开面板
     *
     * @return 面板组件
     */
    @Override
    public Component createAlphaFinePane() {
        return AlphaFinePane.getAlphaFinePane();
    }

    /**
     * 收集用户信息吗
     */
    @Override
    protected void collectUserInformation() {
        //定制的就不弹出来了
        if (!ComparatorUtils.equals(ProductConstants.APP_NAME, ProductConstants.DEFAULT_APP_NAME)) {
            return;
        }

        DesignerEnvManager envManager = DesignerEnvManager.getEnvManager();
        final String key = envManager.getActivationKey();
        //本地验证通过
        if (ActiveKeyGenerator.localVerify(key)) {
            onLineVerify(envManager, key);
            UserInfoLabel.showBBSDialog();
            return;
        }

        if (StableUtils.checkDesignerActive(readOldKey())) {
            //只要有老的key, 就不弹窗, 下次启动的时候, 在线验证下就行.
            String newKey = ActiveKeyGenerator.generateActiveKey();
            envManager.setActivationKey(newKey);
            UserInfoLabel.showBBSDialog();
            return;
        }

        CollectUserInformationDialog activeDialog = new CollectUserInformationDialog(
                DesignerContext.getDesignerFrame());
        activeDialog.setVisible(true);
    }

    private void onLineVerify(DesignerEnvManager envManager, final String key) {
        int status = envManager.getActiveKeyStatus();
        //没有联网验证过
        if (status != 0) {
            ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                    .setNameFormat("net-verify-thread-%s").build();
            ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                    1, 1,
                    0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<Runnable>(1),
                    namedThreadFactory);
            threadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    ActiveKeyGenerator.onLineVerify(key);
                }
            });
        }
    }

    private File getOldEnvFile(String folderName) {
        String userHome = System.getProperty("user.home");
        if (userHome == null) {
            userHome = System.getProperty("userHome");
        }
        String filePath = StableUtils.pathJoin(userHome, folderName, ProductConstants.APP_NAME + "Env.xml");
        return new File(filePath);
    }

    private String getOldActiveKeyFromFile(File envFile) {
        if (!envFile.exists()) {
            return StringUtils.EMPTY;
        }

        DesignerEnvManager temp = new DesignerEnvManager();
        try {
            XMLTools.readFileXML(temp, envFile);
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
        return temp.getActivationKey();
    }

    //默认只从7.0和711的设计器里读取key
    private String readOldKey() {
        File file71 = getOldEnvFile(OLD_ENV_FOLDER_71);
        if (!file71.exists()) {
            File file70 = getOldEnvFile(OLD_ENV_FOLDER_70);
            return getOldActiveKeyFromFile(file70);
        }

        return getOldActiveKeyFromFile(file71);
    }

    /**
     * 设计器退出时, 做的一些操作.
     */
    @Override
    public void shutDown() {
        InformationCollector collector = InformationCollector.getInstance();
        collector.collectStopTime();
        collector.saveXMLFile();
    }

}
