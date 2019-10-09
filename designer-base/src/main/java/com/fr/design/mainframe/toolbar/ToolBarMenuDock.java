/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.mainframe.toolbar;

import com.fr.base.FRContext;
import com.fr.base.vcs.DesignerMode;
import com.fr.design.DesignState;
import com.fr.design.DesignerEnvManager;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.actions.UpdateAction;
import com.fr.design.actions.community.BBSAction;
import com.fr.design.actions.community.BugAction;
import com.fr.design.actions.community.CenterAction;
import com.fr.design.actions.community.CusDemandAction;
import com.fr.design.actions.community.FacebookFansAction;
import com.fr.design.actions.community.NeedAction;
import com.fr.design.actions.community.QuestionAction;
import com.fr.design.actions.community.SignAction;
import com.fr.design.actions.community.TechSolutionAction;
import com.fr.design.actions.community.VideoAction;
import com.fr.design.actions.file.CloseCurrentTemplateAction;
import com.fr.design.actions.file.ExitDesignerAction;
import com.fr.design.actions.file.OpenRecentReportMenuDef;
import com.fr.design.actions.file.OpenTemplateAction;
import com.fr.design.actions.file.PreferenceAction;
import com.fr.design.actions.file.SwitchExistEnv;
import com.fr.design.actions.help.AboutAction;
import com.fr.design.actions.help.FineUIAction;
import com.fr.design.actions.help.TutorialAction;
import com.fr.design.actions.help.WebDemoAction;
import com.fr.design.actions.help.alphafine.AlphaFineAction;
import com.fr.design.actions.help.alphafine.AlphaFineConfigManager;
import com.fr.design.actions.server.ConnectionListAction;
import com.fr.design.actions.server.FunctionManagerAction;
import com.fr.design.actions.server.GlobalParameterAction;
import com.fr.design.actions.server.GlobalTableDataAction;
import com.fr.design.actions.server.PlatformManagerAction;
import com.fr.design.actions.server.PluginManagerAction;
import com.fr.design.file.NewTemplatePane;
import com.fr.design.fun.MenuHandler;
import com.fr.design.fun.OemProcessor;
import com.fr.design.fun.TableDataPaneProcessor;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.imenu.UIMenu;
import com.fr.design.gui.imenu.UIMenuBar;
import com.fr.design.gui.itoolbar.UILargeToolbar;
import com.fr.design.gui.itoolbar.UIToolbar;
import com.fr.design.locale.impl.SupportLocaleImpl;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.mainframe.ToolBarNewTemplatePane;
import com.fr.design.menu.MenuDef;
import com.fr.design.menu.SeparatorDef;
import com.fr.design.menu.ShortCut;
import com.fr.design.menu.ToolBarDef;
import com.fr.design.os.impl.SupportOSImpl;
import com.fr.design.remote.action.RemoteDesignAuthManagerAction;
import com.fr.design.update.actions.SoftwareUpdateAction;
import com.fr.design.utils.ThemeUtils;
import com.fr.general.ComparatorUtils;
import com.fr.general.GeneralContext;
import com.fr.general.locale.LocaleAction;
import com.fr.general.locale.LocaleCenter;
import com.fr.general.os.OSBasedAction;
import com.fr.general.os.OSSupportCenter;
import com.fr.log.FineLoggerFactory;
import com.fr.plugin.context.PluginContext;
import com.fr.plugin.context.PluginRuntime;
import com.fr.plugin.manage.PluginFilter;
import com.fr.plugin.observer.PluginEvent;
import com.fr.plugin.observer.PluginEventListener;
import com.fr.plugin.observer.PluginEventType;
import com.fr.stable.ArrayUtils;
import com.fr.stable.StringUtils;
import com.fr.stable.os.OperatingSystem;
import com.fr.start.OemHandler;
import com.fr.workspace.WorkContext;
import org.jetbrains.annotations.Nullable;

import javax.swing.JComponent;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * @author richer
 * @since 6.5.5 创建于2011-6-13
 */
/*
 * TODO ALEX_SEP 从sheet1切换到sheet2,如果用到的Docking是一样的,期望位置不要发生变动,sheet1时操作显示的哪个docking的tab,在sheet2时也一样
 * 感觉用docking自己确定其位置的方式比较容易实现
 * 还有docking的状态的保存,下次打开设计器,也应该是这样的
 */
public abstract class ToolBarMenuDock {
    public static final int PANLE_HEIGNT = 26;
    public static final ToolBarMenuDockPlus NULLAVOID = new ToolBarMenuDockPlus() {

        @Override
        public ToolBarDef[] toolbars4Target() {
            return new ToolBarDef[0];
        }


        @Override
        public ShortCut[] shortcut4FileMenu() {
            return new ShortCut[0];
        }

        @Override
        public MenuDef[] menus4Target() {
            return new MenuDef[0];
        }

        @Override
        public JPanel[] toolbarPanes4Form() {
            return new JPanel[0];
        }

        @Override
        public JComponent[] toolBarButton4Form() {
            return new JComponent[0];
        }

        @Override
        public JComponent toolBar4Authority() {
            return new JPanel();
        }

        @Override
        public int getMenuState() {
            return DesignState.WORK_SHEET;
        }

        @Override
        public int getToolBarHeight() {
            return PANLE_HEIGNT;
        }

        /**
         * 导出菜单的子菜单 ，目前用于图表设计器
         *
         * @return 子菜单
         */
        @Override
        public ShortCut[] shortcut4ExportMenu() {
            return new ShortCut[0];
        }

    };
    private static final int MENUBAR_HEIGHT = 22;

    private static final List<PluginEventListener> PLUGIN_LISTENERS = new ArrayList<>();

    private MenuDef[] menus;
    private ToolBarDef toolBarDef;
    private List<UpdateActionModel> shortCutsList;

    /**
     * 更新菜单
     */
    public void updateMenuDef() {
        for (int i = 0, count = ArrayUtils.getLength(menus); i < count; i++) {
            menus[i].updateMenu();
        }
    }

    /**
     * 更新toolbar
     */
    public void updateToolBarDef() {
        if (toolBarDef == null) {
            return;
        }
        for (int j = 0, cc = toolBarDef.getShortCutCount(); j < cc; j++) {
            ShortCut shortCut = toolBarDef.getShortCut(j);
            if (shortCut instanceof UpdateAction) {
                ((UpdateAction) shortCut).update();
            }
        }

        refreshLargeToolbarState();
    }

    /**
     * 生成菜单栏
     *
     * @param plus 对象
     * @return 菜单栏
     */
    public final JMenuBar createJMenuBar(ToolBarMenuDockPlus plus) {
        UIMenuBar jMenuBar = new UIMenuBar() {
            private Dimension dim;

            @Override
            public Dimension getPreferredSize() {
                if (dim == null) {
                    dim = super.getPreferredSize();
                    dim.height = MENUBAR_HEIGHT;
                }
                return dim;
            }
        };
        resetJMenuBar(jMenuBar, plus);
        return jMenuBar;
    }


    /**
     * 重置菜单栏
     *
     * @param jMenuBar 当前菜单栏
     * @param plus     对象
     */
    public final void resetJMenuBar(JMenuBar jMenuBar, ToolBarMenuDockPlus plus) {
        jMenuBar.removeAll();
        this.menus = menus(plus);
        try {
            OemProcessor oemProcessor = OemHandler.findOem();
            if (oemProcessor != null) {
                this.menus = oemProcessor.dealWithMenuDef(this.menus);
                if (this.menus == null) {
                    this.menus = menus(plus);
                }
            }
        } catch (Throwable e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
            this.menus = menus(plus);
        }
        for (MenuDef menu : menus) {
            menu.setHasRecMenu(true);
            UIMenu subMenu = menu.createJMenu();
            jMenuBar.add(subMenu);
            menu.updateMenu();
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////menu below/////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 生成报表设计和表单设计的编辑区域
     *
     * @return 模板
     */
    public JTemplate<?, ?> createNewTemplate() {
        return null;
    }

    public MenuDef[] menus(final ToolBarMenuDockPlus plus) {
        //删除之前创建的插件菜单监听
        clearPluginListeners();
        final java.util.List<MenuDef> menuList = new java.util.ArrayList<MenuDef>();
        // 添加文件菜单
        menuList.add(createFileMenuDef(plus));

        MenuDef[] menuDefs = createTemplateShortCuts(plus);
        insertTemplateExtendMenu(plus, menuDefs);

        // 添加模板菜单
        menuList.addAll(Arrays.asList(menuDefs));

        // 添加服务器菜单
        if (WorkContext.getCurrent() != null && WorkContext.getCurrent().isRoot()) {
            menuList.add(createServerMenuDef(plus));
        }

        // 添加帮助菜单
        menuList.add(createHelpMenuDef());

        LocaleCenter.buildAction(new LocaleAction() {
            @Override
            public void execute() {
                addCommunityMenuDef(menuList);
            }
        }, SupportLocaleImpl.COMMUNITY);


        // 添加全部UpdateAction到actionmanager中
        addAllUpdateActionsToList(menuList);
        UpdateActionManager.getUpdateActionManager().setUpdateActions(shortCutsList);

        return menuList.toArray(new MenuDef[menuList.size()]);
    }

    //清空监听
    private static synchronized void clearPluginListeners() {

        for (PluginEventListener listener : PLUGIN_LISTENERS) {
            GeneralContext.stopListenPlugin(listener);
        }
        PLUGIN_LISTENERS.clear();
    }

    /**
     * 获取所有actionmodel
     *
     * @param menuList
     */
    private void addAllUpdateActionsToList(List<MenuDef> menuList) {
        shortCutsList = new ArrayList<>();
        for (MenuDef menuDef : menuList) {
            addUpdateActionToList(menuDef, 0);
        }
    }

    /**
     * 递归获取所有UpdateAction
     *
     * @param menuDef
     */
    private void addUpdateActionToList(MenuDef menuDef, int level) {
        if (menuDef instanceof OpenRecentReportMenuDef) {
            return;
        }
        String parentName = StringUtils.EMPTY;
        if (level > 0) {
            parentName = menuDef.getName();
        }
        level++;
        for (ShortCut shortCut : menuDef.getShortcutList()) {
            if (shortCut instanceof UpdateAction) {
                shortCutsList.add(new UpdateActionModel(parentName, (UpdateAction) shortCut));
            } else if (shortCut instanceof MenuDef) {
                addUpdateActionToList((MenuDef) shortCut, level);
            }
        }
    }

    public void addCommunityMenuDef(java.util.List<MenuDef> menuList) {
        Locale locale = GeneralContext.getLocale();
        Locale[] locales = supportCommunityLocales();
        for (int i = 0; i < locales.length; i++) {
            if (locale.equals(locales[i])) {
                menuList.add(createCommunityMenuDef());
                break;
            }
        }
    }

    public Locale[] supportCommunityLocales() {
        return new Locale[]{
                Locale.CHINA,
                Locale.TAIWAN,
                Locale.US
        };
    }

    public void insertTemplateExtendMenu(ToolBarMenuDockPlus plus, MenuDef[] menuDefs) {
        // 给菜单加插件入口
        for (MenuDef m : menuDefs) {
            switch (m.getAnchor()) {
                case MenuHandler.TEMPLATE:
                    insertMenu(m, MenuHandler.TEMPLATE, new TemplateTargetAction(plus));
                    break;
                case MenuHandler.INSERT:
                    insertMenu(m, MenuHandler.INSERT);
                    break;
                case MenuHandler.CELL:
                    insertMenu(m, MenuHandler.CELL);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 创建新建模板的菜单
     *
     * @param plus 对象
     * @return 菜单
     */
    public MenuDef[] createTemplateShortCuts(ToolBarMenuDockPlus plus) {
        return plus.menus4Target();
    }

    public MenuDef createFileMenuDef(ToolBarMenuDockPlus plus) {
        if (DesignerMode.isVcsMode()) {
            MenuDef menuDef = VcsScene.createFileMenuDef(plus);
            insertMenu(menuDef, MenuHandler.FILE);
            return menuDef;
        }
        MenuDef menuDef = new MenuDef(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_File"), 'F');

        ShortCut[] scs = new ShortCut[0];
        if (!DesignerMode.isAuthorityEditing()) {
            scs = createNewFileShortCuts();
        }
        if (!ArrayUtils.isEmpty(scs)) {
            menuDef.addShortCut(scs);
        }

        menuDef.addShortCut(openTemplateAction());

        menuDef.addShortCut(new OpenRecentReportMenuDef());

        addCloseCurrentTemplateAction(menuDef);

        scs = plus.shortcut4FileMenu();
        if (!ArrayUtils.isEmpty(scs)) {
            menuDef.addShortCut(SeparatorDef.DEFAULT);
            menuDef.addShortCut(scs);
            menuDef.addShortCut(SeparatorDef.DEFAULT);
        }

        addPreferenceAction(menuDef);

        addSwitchExistEnvAction(menuDef);

        menuDef.addShortCut(new ExitDesignerAction());

        insertMenu(menuDef, MenuHandler.FILE);
        return menuDef;
    }

    protected void addCloseCurrentTemplateAction(MenuDef menuDef) {
        if (!DesignerMode.isAuthorityEditing()) {
            menuDef.addShortCut(new CloseCurrentTemplateAction());
        }
    }

    protected void addPreferenceAction(MenuDef menuDef) {
        if (!DesignerMode.isAuthorityEditing()) {
            menuDef.addShortCut(new PreferenceAction());
        }
    }

    protected void addSwitchExistEnvAction(MenuDef menuDef) {
        menuDef.addShortCut(new SwitchExistEnv());
    }

    protected ShortCut openTemplateAction() {
        return new OpenTemplateAction();
    }

    /**
     * 创建新建文件的菜单
     *
     * @return 菜单
     */
    public abstract ShortCut[] createNewFileShortCuts();

    /**
     * 创建论坛登录面板, chart那边不需要
     *
     * @return 面板组件
     */
    public Component createBBSLoginPane() {
        return new UILabel();
    }

    public Component createAlphaFinePane() {
        return new UILabel();
    }

    protected MenuDef createServerMenuDef(ToolBarMenuDockPlus plus) {
        MenuDef menuDef = new MenuDef(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic__M_Server"), 'S');

        if (!DesignerMode.isAuthorityEditing()) {
            menuDef.addShortCut(
                    new ConnectionListAction(),
                    createGlobalTDAction()
            );
        }


        menuDef.addShortCut(
                new PlatformManagerAction()
        );

        if (shouldShowRemoteAuth()) {
            menuDef.addShortCut(new RemoteDesignAuthManagerAction());
        }

        if (!DesignerMode.isAuthorityEditing()) {
            if (shouldShowPlugin()) {
                menuDef.addShortCut(
                        new PluginManagerAction()
                );
            }
            menuDef.addShortCut(
                    new FunctionManagerAction(),
                    new GlobalParameterAction()
            );
        }


        return menuDef;
    }

    private ShortCut createGlobalTDAction() {
        TableDataPaneProcessor processor = ExtraDesignClassManager.getInstance().getSingle(TableDataPaneProcessor.XML_TAG);
        return processor == null ? new GlobalTableDataAction() : processor.createServerTDAction();
    }

    /**
     * 判断是否应该展示远程设计权限管理
     * 如果当前环境是远程环境，并且登录用户为管理员，那么应该展示；否则不予展示
     *
     * @return boolean
     */
    private boolean shouldShowRemoteAuth() {

        return WorkContext.getCurrent() != null && !WorkContext.getCurrent().isLocal() && WorkContext.getCurrent().isRoot();
    }

    private boolean shouldShowPlugin() {
        return WorkContext.getCurrent().isLocal() && FRContext.isChineseEnv();
    }

    /**
     * 创建帮助子菜单
     *
     * @return 帮组菜单的子菜单
     */
    public ShortCut[] createHelpShortCuts() {
        final java.util.List<ShortCut> shortCuts = new ArrayList<ShortCut>();
        shortCuts.add(new WebDemoAction());
        // 英文，把 video 和帮助文档放到 Help 下面
        if (GeneralContext.getLocale().equals(Locale.US)) {
            shortCuts.add(new VideoAction());
            shortCuts.add(new TutorialAction());
        }
        //远程不使用更新升级
        if (WorkContext.getCurrent().isLocal()) {
            shortCuts.add(new SoftwareUpdateAction());
        }
        if (AlphaFineConfigManager.isALPHALicAvailable()) {
            shortCuts.add(new AlphaFineAction());
        }

        shortCuts.add(SeparatorDef.DEFAULT);
        if (DesignerEnvManager.getEnvManager().isOpenDebug()) {
            OSSupportCenter.buildAction(new OSBasedAction() {
                @Override
                public void execute() {
                    shortCuts.add(new FineUIAction());
                }
            }, SupportOSImpl.FINEUI);

        }
        shortCuts.add(new AboutAction());

        return shortCuts.toArray(new ShortCut[0]);
    }

    /**
     * 创建社区子菜单
     *
     * @return 社区菜单的子菜单
     */
    public ShortCut[] createCommunityShortCuts() {
        final java.util.List<ShortCut> shortCuts = new ArrayList<ShortCut>();
        shortCuts.add(new BBSAction());
        shortCuts.add(new VideoAction());
        shortCuts.add(new TutorialAction());
        shortCuts.add(new QuestionAction());
        shortCuts.add(new TechSolutionAction());
        shortCuts.add(new BugAction());
        shortCuts.add(new NeedAction());
        shortCuts.add(new CusDemandAction());
        shortCuts.add(new CenterAction());
        shortCuts.add(new SignAction());
        LocaleCenter.buildAction(new LocaleAction() {
            @Override
            public void execute() {
                shortCuts.add(new FacebookFansAction());
            }
        }, SupportLocaleImpl.FACEBOOK);
        return shortCuts.toArray(new ShortCut[shortCuts.size()]);
    }

    public MenuDef createHelpMenuDef() {
        MenuDef menuDef = new MenuDef(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Help"), 'H');
        ShortCut[] otherHelpShortCuts = createHelpShortCuts();
        for (ShortCut shortCut : otherHelpShortCuts) {
            menuDef.addShortCut(shortCut);
        }
        insertMenu(menuDef, MenuHandler.HELP);
        return menuDef;
    }

    public MenuDef createCommunityMenuDef() {
        MenuDef menuDef = new MenuDef(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Community"), 'C');
        ShortCut[] otherCommunityShortCuts = createCommunityShortCuts();
        for (ShortCut shortCut : otherCommunityShortCuts) {
            menuDef.addShortCut(shortCut);
        }
        insertMenu(menuDef, MenuHandler.BBS);
        return menuDef;
    }

    /**
     * 生成工具栏
     *
     * @param toolbarComponent 工具栏
     * @param plus             对象
     * @return 工具栏
     */
    public JComponent resetToolBar(@Nullable JComponent toolbarComponent, ToolBarMenuDockPlus plus) {
        ToolBarDef[] plusToolBarDefs = plus.toolbars4Target();
        UIToolbar toolBar;
        if (toolbarComponent instanceof UIToolbar) {
            toolBar = (UIToolbar) toolbarComponent;
            toolBar.removeAll();
        } else {
            toolBar = ToolBarDef.createJToolBar(ThemeUtils.BACK_COLOR);
        }

        toolBar.setFocusable(true);
        toolBarDef = new ToolBarDef();

        if (plusToolBarDefs != null) {
            for (ToolBarDef def : plusToolBarDefs) {
                for (int di = 0, dlen = def.getShortCutCount(); di < dlen; di++) {
                    toolBarDef.addShortCut(def.getShortCut(di));
                }
                toolBarDef.addShortCut(SeparatorDef.DEFAULT);
            }
            UIManager.getDefaults().put("ToolTip.hideAccelerator", Boolean.TRUE);
            toolBarDef.updateToolBar(toolBar);
            return toolBar;

        } else {
            return polyToolBar(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Polyblock_Edit"));
        }
    }

    protected JPanel polyToolBar(String text) {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            public Dimension getPreferredSize() {
                Dimension dim = super.getPreferredSize();
                dim.height = PANLE_HEIGNT;
                return dim;
            }
        };
        UILabel uiLabel = new UILabel(text);
        uiLabel.setHorizontalAlignment(SwingConstants.CENTER);
        uiLabel.setFont(new Font(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_All_MSBold"), 0, 14));
        uiLabel.setForeground(new Color(150, 150, 150));
        panel.add(uiLabel, BorderLayout.CENTER);
        return panel;
    }

    /**
     * 重置上面的工具栏
     *
     * @param plus 对象
     * @return 工具栏
     */
    public JComponent[] resetUpToolBar(ToolBarMenuDockPlus plus) {
        return plus.toolBarButton4Form();
    }

    /**
     * 创建大的工具按钮
     *
     * @return 大的工具按钮
     */
    public UILargeToolbar createLargeToolbar() {
        return new UILargeToolbar(FlowLayout.LEFT);
    }

    /**
     * 创建上面的按钮
     *
     * @return 按钮
     */
    public UIButton[] createUp() {
        return new UIButton[0];
    }

    protected void refreshLargeToolbarState() {

    }

    public NewTemplatePane getNewTemplatePane() {
        return ToolBarNewTemplatePane.getInstance();
    }

    protected void insertMenu(MenuDef menuDef, String anchor) {
        insertMenu(menuDef, anchor, new NoTargetAction());
    }

    protected void insertMenu(MenuDef menuDef, String anchor, ShortCutMethodAction action) {

        listenPluginMenuChange(menuDef, anchor, action);
        Set<MenuHandler> set = ExtraDesignClassManager.getInstance().getArray(MenuHandler.MARK_STRING);
        addExtraMenus(menuDef, anchor, action, set);

    }

    private void listenPluginMenuChange(final MenuDef menuDef, final String anchor, final ShortCutMethodAction action) {

        PluginFilter filter = new PluginFilter() {

            @Override
            public boolean accept(PluginContext context) {

                return context.contain(MenuHandler.MARK_STRING);
            }
        };

        PluginEventListener beforeStop = new PluginEventListener() {

            @Override
            public void on(PluginEvent event) {
                PluginRuntime runtime = event.getContext().getRuntime();
                Set<MenuHandler> menuHandlers = runtime.get(MenuHandler.MARK_STRING);
                removeExtraMenus(menuDef, anchor, action, menuHandlers);
            }
        };

        PluginEventListener afterRun = new PluginEventListener() {

            @Override
            public void on(PluginEvent event) {

                PluginRuntime runtime = event.getContext().getRuntime();
                Set<MenuHandler> menuHandlers = runtime.get(MenuHandler.MARK_STRING);
                addExtraMenus(menuDef, anchor, action, menuHandlers);
            }
        };

        GeneralContext.listenPlugin(PluginEventType.BeforeStop, beforeStop, filter);
        GeneralContext.listenPlugin(PluginEventType.AfterRun, afterRun, filter);

        PLUGIN_LISTENERS.add(afterRun);
        PLUGIN_LISTENERS.add(beforeStop);
    }

    private void removeExtraMenus(MenuDef menuDef, String anchor, ShortCutMethodAction action, Set<MenuHandler> set) {


        List<MenuHandler> target = new ArrayList<>();
        for (MenuHandler handler : set) {
            if (ComparatorUtils.equals(handler.category(), anchor)) {
                target.add(handler);
            }
        }

        for (MenuHandler handler : target) {
            int insertPosition = handler.insertPosition(menuDef.getShortCutCount());
            if (insertPosition == MenuHandler.HIDE) {
                return;
            }
            ShortCut shortCut = action.methodAction(handler);
            if (shortCut == null) {
                continue;
            }
            menuDef.removeShortCut(shortCut);
        }
    }

    private void addExtraMenus(MenuDef menuDef, String anchor, ShortCutMethodAction action, Set<MenuHandler> set) {

        List<MenuHandler> target = new ArrayList<>();
        for (MenuHandler handler : set) {
            if (ComparatorUtils.equals(handler.category(), anchor)) {
                target.add(handler);
            }
        }

        for (MenuHandler handler : target) {
            int insertPosition = handler.insertPosition(menuDef.getShortCutCount());
            if (insertPosition == MenuHandler.HIDE) {
                return;
            }
            ShortCut shortCut = action.methodAction(handler);
            if (shortCut == null) {
                continue;
            }

            if (insertPosition == MenuHandler.LAST) {
                if (handler.insertSeparatorBefore()) {
                    menuDef.addShortCut(SeparatorDef.DEFAULT);
                }
                menuDef.addShortCut(shortCut);
            } else {
                menuDef.insertShortCut(insertPosition, shortCut);
                if (handler.insertSeparatorBefore()) {
                    menuDef.insertShortCut(insertPosition, SeparatorDef.DEFAULT);
                    insertPosition++;
                }
                if (handler.insertSeparatorAfter()) {
                    insertPosition++;
                    menuDef.insertShortCut(insertPosition, SeparatorDef.DEFAULT);
                }
            }
        }
    }

    /**
     * 设计器退出时, 做的一些操作.
     */
    public void shutDown() {

    }

    private interface ShortCutMethodAction {

        public ShortCut methodAction(MenuHandler handler);
    }

    private abstract class AbstractShortCutMethodAction implements ShortCutMethodAction {

        @Override
        public ShortCut methodAction(MenuHandler handler) {
            return handler.shortcut();
        }
    }

    //不需要编辑对象的菜单, 比如文件, 服务器, 关于
    private class NoTargetAction extends AbstractShortCutMethodAction {

    }

    //模板为对象的菜单, 比如模板, 后续如果单元格也要, 直接加个CellTargetAction即可.
    //在methodAction中做handler.shortcut(cell), 不需要修改handler中原有接口, 加个shortcut(cell).
    private class TemplateTargetAction extends AbstractShortCutMethodAction {

        private ToolBarMenuDockPlus plus;

        public TemplateTargetAction(ToolBarMenuDockPlus plus) {
            this.plus = plus;
        }

        @Override
        public ShortCut methodAction(MenuHandler handler) {
            return handler.shortcut(plus);
        }
    }
}
