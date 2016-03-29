/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.mainframe.toolbar;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.design.DesignState;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.actions.UpdateAction;
import com.fr.design.actions.file.CloseCurrentTemplateAction;
import com.fr.design.actions.file.ExitDesignerAction;
import com.fr.design.actions.file.OpenRecentReportMenuDef;
import com.fr.design.actions.file.OpenTemplateAction;
import com.fr.design.actions.file.PreferenceAction;
import com.fr.design.actions.file.SwitchExistEnv;
import com.fr.design.actions.help.AboutAction;
import com.fr.design.actions.help.FeedBackAction;
import com.fr.design.actions.help.ForumAction;
import com.fr.design.actions.help.SupportQQAction;
import com.fr.design.actions.help.TutorialAction;
import com.fr.design.actions.help.WebDemoAction;
import com.fr.design.actions.server.ConnectionListAction;
import com.fr.design.actions.server.FunctionManagerAction;
import com.fr.design.actions.server.GlobalParameterAction;
import com.fr.design.actions.server.GlobalTableDataAction;
import com.fr.design.actions.server.PlatformManagerAction;
import com.fr.design.actions.server.PluginManagerAction;
import com.fr.design.file.NewTemplatePane;
import com.fr.design.fun.MenuHandler;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.imenu.UIMenu;
import com.fr.design.gui.imenu.UIMenuBar;
import com.fr.design.gui.itoolbar.UILargeToolbar;
import com.fr.design.gui.itoolbar.UIToolbar;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.menu.MenuDef;
import com.fr.design.menu.SeparatorDef;
import com.fr.design.menu.ShortCut;
import com.fr.design.menu.ToolBarDef;
import com.fr.general.ComparatorUtils;
import com.fr.general.GeneralContext;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import com.fr.stable.ProductConstants;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

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
    private static final String FINEREPORT = "FineReport";
    private static final int MENUBAR_HEIGHT = 22;
    public static final int PANLE_HEIGNT = 26;
    private MenuDef[] menus;
    private ToolBarDef toolBarDef;

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
            @Override
            public Dimension getPreferredSize() {
                Dimension dim = super.getPreferredSize();
                dim.height = MENUBAR_HEIGHT;
                return dim;
            }
        };

        this.menus = menus(plus);
        for (int i = 0; i < menus.length; i++) {
            UIMenu subMenu = menus[i].createJMenu();
            jMenuBar.add(subMenu);
            menus[i].updateMenu();
        }
        return jMenuBar;
    }

    /**
     * 生成报表设计和表单设计的编辑区域
     *
     * @return 模板
     */
    public JTemplate<?, ?> createNewTemplate() {
        return null;
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////menu below/////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////

    private MenuDef[] menus(final ToolBarMenuDockPlus plus) {
        java.util.List<MenuDef> menuList = new java.util.ArrayList<MenuDef>();
        // 添加文件菜单
        menuList.add(createFileMenuDef(plus));

        MenuDef[] menuDefs = createTemplateShortCuts(plus);
        insertTemplateExtendMenu(plus, menuDefs);

        // 添加模板菜单
        menuList.addAll(Arrays.asList(menuDefs));

        // 添加服务器菜单
        menuList.add(createServerMenuDef(plus));
        // 添加帮助菜单
        menuList.add(createHelpMenuDef());

        return menuList.toArray(new MenuDef[menuList.size()]);
    }

    private void insertTemplateExtendMenu(ToolBarMenuDockPlus plus, MenuDef[] menuDefs) {
        // 给菜单加插件入口
        for (MenuDef m : menuDefs) {
            switch (m.getAnchor()) {
                case MenuHandler.TEMPLATE :
                    insertMenu(m, MenuHandler.TEMPLATE, new TemplateTargetAction(plus));
                    break;
                case MenuHandler.INSERT :
                    insertMenu(m, MenuHandler.INSERT);
                    break;
                case MenuHandler.CELL :
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

    private MenuDef createFileMenuDef(ToolBarMenuDockPlus plus) {
        MenuDef menuDef = new MenuDef(Inter.getLocText("FR-Designer_File"), 'F');

        ShortCut[] scs = new ShortCut[0];
        if (!BaseUtils.isAuthorityEditing()) {
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

        menuDef.addShortCut( new ExitDesignerAction());

        insertMenu(menuDef, MenuHandler.FILE);
        return menuDef;
    }

    protected void addCloseCurrentTemplateAction(MenuDef menuDef) {
        if (!BaseUtils.isAuthorityEditing()) {
            menuDef.addShortCut(new CloseCurrentTemplateAction());
        }
    }

    protected void addPreferenceAction(MenuDef menuDef) {
        if (!BaseUtils.isAuthorityEditing()) {
            menuDef.addShortCut(new PreferenceAction());
        }
    }

    protected void addSwitchExistEnvAction(MenuDef menuDef) {
        menuDef.addShortCut(new SwitchExistEnv());
    }

    protected ShortCut openTemplateAction(){
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
	 * 
	 */
    public Component createBBSLoginPane(){
    	return new UILabel();
    }


    protected MenuDef createServerMenuDef(ToolBarMenuDockPlus plus) {
        MenuDef menuDef = new MenuDef(Inter.getLocText("FR-Designer_M-Server"), 'S');

        if (FRContext.getCurrentEnv() == null || !FRContext.getCurrentEnv().isRoot()) {
            menuDef.addShortCut(new ConnectionListAction());
            return menuDef;
        }

        if (!BaseUtils.isAuthorityEditing()) {
            menuDef.addShortCut(
                    new ConnectionListAction(),
                    new GlobalTableDataAction()
            );
        }


        menuDef.addShortCut(
                new PlatformManagerAction()
        );

        if (!BaseUtils.isAuthorityEditing()) {
            if (shouldShowPlugin()){
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

    private boolean shouldShowPlugin() {
        return FRContext.isChineseEnv() || ComparatorUtils.equals(GeneralContext.getLocale(), Locale.TAIWAN);
    }

    /**
     * 创建帮助子菜单
     * @return 帮组菜单的子菜单
     */
    public ShortCut[] createHelpShortCuts() {
        java.util.List<ShortCut> shortCuts = new ArrayList<ShortCut>();
        shortCuts.add(new WebDemoAction());
        shortCuts.add(SeparatorDef.DEFAULT);
        shortCuts.add(new TutorialAction());
        shortCuts.add(SeparatorDef.DEFAULT);
        if (ComparatorUtils.equals(ProductConstants.APP_NAME,FINEREPORT)) {
            shortCuts.add(new FeedBackAction());
            shortCuts.add(SeparatorDef.DEFAULT);
            shortCuts.add(new SupportQQAction());
            shortCuts.add(SeparatorDef.DEFAULT);
            shortCuts.add(new ForumAction());
        }
        shortCuts.add(SeparatorDef.DEFAULT);
        shortCuts.add(new AboutAction());

        return shortCuts.toArray(new ShortCut[shortCuts.size()]);
    }


    private MenuDef createHelpMenuDef() {
        MenuDef menuDef = new MenuDef(Inter.getLocText("FR-Designer_Help"), 'H');
        ShortCut[] otherHelpShortCuts = createHelpShortCuts();
        for (ShortCut shortCut : otherHelpShortCuts) {
            menuDef.addShortCut(shortCut);
        }
        insertMenu(menuDef, MenuHandler.HELP);
        return menuDef;
    }

    /**
     * 生成工具栏
     *
     * @param toolbarComponent 工具栏
     * @param plus             对象
     * @return 工具栏
     */
    public JComponent resetToolBar(JComponent toolbarComponent, ToolBarMenuDockPlus plus) {
        ToolBarDef[] plusToolBarDefs = plus.toolbars4Target();
        UIToolbar toolBar;
        if (toolbarComponent instanceof UIToolbar) {
            toolBar = (UIToolbar) toolbarComponent;
            toolBar.removeAll();
        } else {
            toolBar = ToolBarDef.createJToolBar();
        }

        toolBar.setFocusable(true);
        toolBarDef = new ToolBarDef();

        if (plusToolBarDefs != null) {
            for (int i = 0; i < plusToolBarDefs.length; i++) {
                ToolBarDef def = plusToolBarDefs[i];
                for (int di = 0, dlen = def.getShortCutCount(); di < dlen; di++) {
                    toolBarDef.addShortCut(def.getShortCut(di));
                }
                toolBarDef.addShortCut(SeparatorDef.DEFAULT);
            }
            UIManager.getDefaults().put("ToolTip.hideAccelerator", Boolean.TRUE);
            toolBarDef.updateToolBar(toolBar);
            return toolBar;

        } else {
            return polyToolBar(Inter.getLocText(new String[]{"Polybolck", "Edit"}));
        }
    }


    protected JPanel polyToolBar(String text) {
        JPanel panel = new JPanel(new BorderLayout()) {
            public Dimension getPreferredSize() {
                Dimension dim = super.getPreferredSize();
                dim.height = PANLE_HEIGNT;
                return dim;
            }
        };
        UILabel uiLabel = new UILabel(text);
        uiLabel.setHorizontalAlignment(SwingConstants.CENTER);
        uiLabel.setFont(new Font(Inter.getLocText("FR-Designer-All_MSBold"), 0, 14));
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

        public JComponent[] toolBarButton4Form() {
            return new JComponent[0];
        }

        public JComponent toolBar4Authority() {
            return new JPanel();
        }

        @Override
        public int getMenuState() {
            return DesignState.WORK_SHEET;
        }
        public int getToolBarHeight(){
            return PANLE_HEIGNT;
        }

        /**
         * 导出菜单的子菜单 ，目前用于图表设计器
         *
         * @return 子菜单
         */
    	public ShortCut[] shortcut4ExportMenu(){
            return new ShortCut[0];
        }

    };

    public NewTemplatePane getNewTemplatePane(){
       return new NewTemplatePane() {
           @Override
           public Icon getNew() {
               return BaseUtils.readIcon("/com/fr/design/images/buttonicon/addicon.png");
           }

           @Override
           public Icon getMouseOverNew() {
               return BaseUtils.readIcon("/com/fr/design/images/buttonicon/add_press.png");
           }

           @Override
           public Icon getMousePressNew() {
               return BaseUtils.readIcon("/com/fr/design/images/buttonicon/add_press.png");
           }
       };
    }

    protected void insertMenu(MenuDef menuDef, String anchor) {
        insertMenu(menuDef, anchor, new NoTargetAction());
    }

    protected void insertMenu(MenuDef menuDef, String anchor, ShortCutMethodAction action) {
        // 下面是插件接口接入点
        MenuHandler[] menuHandlers = ExtraDesignClassManager.getInstance().getMenuHandlers(anchor);
        for (MenuHandler handler : menuHandlers) {
            int insertPosition = handler.insertPosition(menuDef.getShortCutCount());
            ShortCut shortCut = action.methodAction(handler);
            if (shortCut == null){
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
                    insertPosition ++;
                }
                if (handler.insertSeparatorAfter()) {
                    insertPosition ++;
                    menuDef.insertShortCut(insertPosition, SeparatorDef.DEFAULT);
                }
            }
        }
    }
    
    /**
	 * 设计器退出时, 做的一些操作.
	 * 
	 */
    public void shutDown(){
    	
    }

    private interface ShortCutMethodAction{

        public ShortCut methodAction(MenuHandler handler);
    }

    private abstract class AbstractShortCutMethodAction implements ShortCutMethodAction{

        public ShortCut methodAction(MenuHandler handler){
           return handler.shortcut();
        }
    }

    //不需要编辑对象的菜单, 比如文件, 服务器, 关于
    private class NoTargetAction extends AbstractShortCutMethodAction{

    }

    //模板为对象的菜单, 比如模板, 后续如果单元格也要, 直接加个CellTargetAction即可.
    //在methodAction中做handler.shortcut(cell), 不需要修改handler中原有接口, 加个shortcut(cell).
    private class TemplateTargetAction extends AbstractShortCutMethodAction{

        private ToolBarMenuDockPlus plus;

        public TemplateTargetAction(ToolBarMenuDockPlus plus){
            this.plus = plus;
        }

        public ShortCut methodAction(MenuHandler handler) {
            return handler.shortcut(plus);
        }
    }
}