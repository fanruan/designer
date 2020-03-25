package com.fr.design.menu;

import com.fr.base.BaseUtils;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.imenu.*;
import com.fr.design.gui.iscrollbar.UIScrollBar;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.stable.StringUtils;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.ContainerListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Define Menu.
 */
public class MenuDef extends ShortCut {

    private static final int MENU_DEFAULTWDITH = 156;
    private static final int BLANK_WIDTH = 30;
    protected String name;
    //右侧属性表弹出框重绘
    protected Boolean isEastAttr = false;
    protected char mnemonic;
    protected String iconPath;
    protected String tooltip;
    //item List.
    private List<ShortCut> shortcutList = new ArrayList<ShortCut>();
    // peter:产生的JMenu, UIButton以及enabled变量都是为由MenuDef产生的控件所用的
    protected boolean enabled = true;
    protected UIMenu createdJMenu;
    protected UIButton createdButton;
    protected JPopupMenu popupMenu;
    protected boolean hasScrollSubMenu;
    protected boolean isHeadMenu;

    private String anchor;

    public MenuDef() {
    }

    public MenuDef(String name) {
        this.setName(name);
    }

    public MenuDef(Boolean rePaint) {
        this.setRePaint(rePaint);
    }

    public MenuDef(String name, char mnemonic) {
        this.setName(name);
        this.setMnemonic(mnemonic);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getRePaint() {
        return isEastAttr;
    }

    public void setRePaint(boolean rePaint) {
        this.isEastAttr = rePaint;
    }

    public char getMnemonic() {
        return mnemonic;
    }

    public void setMnemonic(char mnemonic) {
        this.mnemonic = mnemonic;
    }

    public void setHasScrollSubMenu(boolean scrollSubMenu) {
        this.hasScrollSubMenu = scrollSubMenu;
    }

    public void setHasRecMenu(boolean headMenu) {
        this.isHeadMenu = headMenu;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public int getShortCutCount() {
        return this.shortcutList.size();
    }

    public ShortCut getShortCut(int index) {
        return this.shortcutList.get(index);
    }

    public String getAnchor() {
        return anchor == null ? StringUtils.EMPTY : anchor;
    }

    public void setAnchor(String anchor) {
        this.anchor = anchor;
    }

    public List<ShortCut> getShortcutList() {
        return this.shortcutList;
    }

    /**
     * 插入菜单项
     *
     * @param index    插入的位置
     * @param shortCut 菜单信息
     */
    public void insertShortCut(int index, ShortCut shortCut) {
        int size = this.shortcutList.size();
        index = Math.min(index, size);
        this.shortcutList.add(index, shortCut);
    }

    /**
     * 用可变参数，方便添加数组
     *
     * @param shortcut 参数   存储菜单项信息
     */
    public void addShortCut(ShortCut... shortcut) {
        for (ShortCut i : shortcut) {
            this.shortcutList.add(i);
        }
    }

    public void removeShortCut(ShortCut shortCut) {

        this.shortcutList.remove(shortCut);
    }

    /**
     * 清理
     */
    public void clearShortCuts() {
        this.shortcutList.clear();
    }

    /**
     * 生成UIButton
     *
     * @return 菜单按钮
     */
    public UIButton createUIButton() {
        if (createdButton == null) {
            if (iconPath != null) {
                createdButton = new UIButton(BaseUtils.readIcon(iconPath));
                createdButton.set4ToolbarButton();
            } else {
                createdButton = new UIButton(name);
            }
            // 添加名字以作自动化测试
            createdButton.setName(name);
            createdButton.setToolTipText(tooltip);
            createdButton.addMouseListener(mouseListener);
        }

        return createdButton;
    }

    public void setTooltip(String text) {
        this.tooltip = text;
    }

    /**
     * 生成JMenu
     *
     * @return 菜单
     */
    public UIMenu createJMenu() {
        if (createdJMenu == null) {
            createdJMenu = createJMenu0();
            createdJMenu.setMnemonic(this.getMnemonic());
            if (this.iconPath != null) {
                createdJMenu.setIcon(BaseUtils.readIcon(this.iconPath));
            }
            MenuListener menuListener = createMenuListener();
            createdJMenu.addMenuListener(menuListener);
            ContainerListener listener = getContainerListener();
            if (listener != null) {
                createdJMenu.getPopupMenu().addContainerListener(listener);
            }
        }

        return createdJMenu;
    }
    
    protected UIMenu createJMenu0() {
        
        UIMenu createdJMenu;
        if (hasScrollSubMenu) {
            createdJMenu = new UIScrollMenu(this.getName());
        } else if (isHeadMenu){
            createdJMenu = new UIHeadMenu(this.getName());
        } else {
            createdJMenu = new UIMenu(this.getName());
        }
        return createdJMenu;
    }
    
    protected ContainerListener getContainerListener() {
        return null;
    }

    /**
     * 生成 JPopupMenu
     *
     * @return 弹出菜单
     */
    public JPopupMenu createJPopupMenu() {
        UIMenu menu = createJMenu();
        updateMenu();
        return menu.getPopupMenu();
    }

    /**
     * 设置是否可用
     *
     * @param b 布尔型
     */
    @Override
    public void setEnabled(boolean b) {
        this.enabled = b;

        if (createdButton != null) {
            createdButton.setEnabled(enabled);
        }

        if (createdJMenu != null) {
            createdJMenu.setEnabled(enabled);
        }
    }

    /**
     * 按钮状态
     *
     * @return 状态
     */
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * 更新菜单
     */
    public void updateMenu() {
        //peter:这个方法用来产生JMenu的孩子控件,但是不update,action.
        this.updatePopupMenu(this.createJMenu().getPopupMenu());

        //peter:需要设置JMenu的enabled属性.
        if (createdJMenu != null) {
            createdJMenu.setEnabled(createdJMenu.getPopupMenu().getComponentCount() > 0 && enabled);
            createdJMenu.repaint(10);
        }
    }

    /**
     * 更新菜单
     *
     * @param popupMenu 菜单
     */
    protected void updatePopupMenu(JPopupMenu popupMenu) {
        removeComponent(popupMenu);
        this.popupMenu = popupMenu;
        // 一开始是不能插入分隔符的
        boolean nec_seperator = false;
        boolean isFirstItem = true;
        int actionCount = this.getShortCutCount();
        for (int i = 0; i < actionCount; i++) {
            ShortCut shortcut = this.getShortCut(i);

            // 如果shortcut是SeparatorDef,先不加,先标记一下nec_seperator为true,等下一个shortcut需要加到PopupMenu时再加
            if (shortcut instanceof SeparatorDef) {
                nec_seperator = true;
                continue;
            }
            if (nec_seperator) {
                if (!isFirstItem) {
                    SeparatorDef.DEFAULT.intoJPopupMenu(popupMenu);
                }
                nec_seperator = false;
            }

            shortcut.intoJPopupMenu(popupMenu);
            isFirstItem = false;
        }

        if (createdJMenu != null && createdJMenu.getPopupMenu() != null) {
            setEnabled(createdJMenu.getPopupMenu().getComponentCount() > 0 && enabled);
        }
    }

    /**
     * 更新右侧属性面板菜单
     *
     * @param popupMenu 菜单
     */
    protected void updateEastPopupMenu(JPopupMenu popupMenu) {
        removeComponent(popupMenu);
        this.popupMenu = popupMenu;
        // 一开始是不能插入分隔符的
        boolean nec_seperator = false;
        boolean isFirstItem = true;
        int actionCount = this.getShortCutCount();
        for (int i = 0; i < actionCount; i++) {
            ShortCut shortcut = this.getShortCut(i);

            // 如果shortcut是SeparatorDef,先不加,先标记一下nec_seperator为true,等下一个shortcut需要加到PopupMenu时再加
            if (shortcut instanceof SeparatorDef) {
                nec_seperator = true;
                continue;
            }
            if (nec_seperator) {
                if (!isFirstItem) {
                    SeparatorDef.DEFAULT.intoJPopupMenu(popupMenu);
                }
                nec_seperator = false;
            }
            shortcut.intoJPopupMenu(popupMenu);
            isFirstItem = false;
        }

        if (createdJMenu != null && createdJMenu.getPopupMenu() != null) {
            setEnabled(createdJMenu.getPopupMenu().getComponentCount() > 0 && enabled);
        }
    }

    /**
     * 删除所有组件 除了滚动条
     *
     * @param popupMenu 菜单
     */
    public void removeComponent(JPopupMenu popupMenu) {
        UIScrollBar uiScrollBar = new UIScrollBar();
        if (hasScrollSubMenu) {
            for (Component comp : popupMenu.getComponents()) {
                if (comp instanceof UIScrollBar) {
                    uiScrollBar = (UIScrollBar) comp;
                }
            }
        }
        popupMenu.removeAll();
        if (hasScrollSubMenu) {
            popupMenu.add(uiScrollBar);
        }
    }

    /**
     * 添加菜单项
     *
     * @param menu 菜单
     */
    @Override
    public void intoJPopupMenu(JPopupMenu menu) {
        updateMenu();

        menu.add(this.createJMenu());
    }

    /**
     * 添加
     *
     * @param toolBar 菜单条
     */
    @Override
    public void intoJToolBar(JToolBar toolBar) {
        toolBar.add(this.createUIButton());
    }
    
    protected MenuListener createMenuListener() {
        
        return menuDefListener;
    }

    private MenuListener menuDefListener = new MenuListener() {

        @Override
        public void menuCanceled(MenuEvent evt) {
        }

        @Override
        public void menuDeselected(MenuEvent evt) {
        }

        @Override
        public void menuSelected(MenuEvent evt) {
            Object source = evt.getSource();
            if (!(source instanceof JMenu)) {
                return;
            }

            MenuDef.this.updateMenu();
        }
    };
    private MouseListener mouseListener = new MouseAdapter() {

        @Override
        public void mouseReleased(MouseEvent evt) {
            Object source = evt.getSource();
            UIButton button = (UIButton) source;
            if (!button.isEnabled()) {
                return;
            }
            if (isEastAttr) {
                popupMenu = new UIPopupEastAttrMenu();
                popupMenu.setInvoker(button);
                MenuDef.this.updateEastPopupMenu(popupMenu);
                updatePopupMenuSize();
                GUICoreUtils.showPopupMenu(popupMenu, button, 0, button.getSize().height);
            } else {
                popupMenu = new UIPopupMenu();
                popupMenu.setInvoker(button);
                MenuDef.this.updatePopupMenu(popupMenu);
                GUICoreUtils.showPopupMenu(popupMenu, button, 0, button.getSize().height);
            }
        }
    };

    private void updatePopupMenuSize() {
        int preferredWidth = popupMenu.getPreferredSize().width - BLANK_WIDTH;  // 减少行尾的空白部分
        int popupMenuWidth = preferredWidth > MENU_DEFAULTWDITH ? preferredWidth : MENU_DEFAULTWDITH;
        popupMenu.setPopupSize(new Dimension(popupMenuWidth, popupMenu.getPreferredSize().height));
    }


    //ben: for ui test
    public JPopupMenu getPopupMenu() {
        return popupMenu;
    }

    public void setPopupMenu(JPopupMenu popupMenu) {
        this.popupMenu = popupMenu;
    }


}