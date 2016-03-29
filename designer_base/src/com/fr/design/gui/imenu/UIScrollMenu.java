package com.fr.design.gui.imenu;

/**
 * Created with IntelliJ IDEA.
 * User: richie
 * Date: 13-12-5
 * Time: 上午11:11
 */

import javax.swing.*;
import java.awt.*;


public class UIScrollMenu extends UIMenu {
    // Covers the one in the JMenu because the method that creates it in JMenu is private
    /**
     * The popup menu portion of the menu.
     */
    private JPopupMenu popupMenu;


    /**
     * Constructs a new <code>JMenu</code> with no text.
     */
    public UIScrollMenu() {
        this("");
    }

    /**
     * Constructs a new <code>JMenu</code> with the supplied string as its text.
     *
     * @param s the text for the menu label
     */
    public UIScrollMenu(String s) {
        super(s);
    }

    /**
     * Constructs a menu whose properties are taken from the <code>Action</code> supplied.
     *
     * @param a an <code>Action</code>
     */
    public UIScrollMenu(Action a) {
        this();
        setAction(a);
    }


    /**
     * Lazily creates the popup menu. This method will create the popup using the <code>JScrollPopupMenu</code> class.
     */
    protected void ensurePopupMenuCreated() {
        if (popupMenu == null) {
            this.popupMenu = new UIScrollPopUpMenu();
            popupMenu.setInvoker(this);
            popupListener = createWinListener(popupMenu);
        }
    }

    //////////////////////////////
//// All of these methods are necessary because ensurePopupMenuCreated() is private in JMenu
//////////////////////////////

    /**
     *画界面
     */
    public void updateUI() {
      setUI(new UIMenuUI());
    }


    /**
     * 判断popupmeu是否隐藏
     * @return  如果隐藏 返回true
     */
    public boolean isPopupMenuVisible() {
        ensurePopupMenuCreated();
        return popupMenu.isVisible();
    }


    /**
     * 设置popupmenu位置
     * @param x
     * @param y
     */
    public void setMenuLocation(int x, int y) {
        super.setMenuLocation(x, y);
        if (popupMenu != null) {
            popupMenu.setLocation(x, y);
        }
    }

    /**
     * 向popupmenu添加 JMenuItem
     * @param menuItem 菜单项
     * @return    菜单项
     */
    public JMenuItem add(JMenuItem menuItem) {
        ensurePopupMenuCreated();
        return popupMenu.add(menuItem);
    }

    /**
     * 添加组件
     * @param c   组件
     * @return    组件
     */
    public Component add(Component c) {
        ensurePopupMenuCreated();
        popupMenu.add(c);
        return c;
    }

    /**
     * 向指定位置添加组件
     * @param c       组件
     * @param index     位置
     * @return   组件
     */
    public Component add(Component c, int index) {
        ensurePopupMenuCreated();
        popupMenu.add(c, index);
        return c;
    }


    /**
     * 添加分隔符
     */
    public void addSeparator() {
        ensurePopupMenuCreated();
        popupMenu.addSeparator();
    }

    /**
     * 添加menuitem到指定位置
     * @param s      字符
     * @param pos     位置
     */
    public void insert(String s, int pos) {
        if (pos < 0) {
            throw new IllegalArgumentException("index less than zero.");
        }

        ensurePopupMenuCreated();
        popupMenu.insert(new JMenuItem(s), pos);
    }

    /**
     * 添加么会特么到指定位置
     * @param mi     菜单项
     * @param pos   位置
     * @return       菜单项
     */
    public JMenuItem insert(JMenuItem mi, int pos) {
        if (pos < 0) {
            throw new IllegalArgumentException("index less than zero.");
        }
        ensurePopupMenuCreated();
        popupMenu.insert(mi, pos);
        return mi;
    }

    /**
     * 添加到指定位置
     * @param a      事件
     * @param pos   位置
     * @return       菜单项
     */
    public JMenuItem insert(Action a, int pos) {
        if (pos < 0) {
            throw new IllegalArgumentException("index less than zero.");
        }

        ensurePopupMenuCreated();
        JMenuItem mi = new JMenuItem(a);
        mi.setHorizontalTextPosition(JButton.TRAILING);
        mi.setVerticalTextPosition(JButton.CENTER);
        popupMenu.insert(mi, pos);
        return mi;
    }

    /**
     *  添加分隔符到指定位置
     * @param index  指定位置
     */
    public void insertSeparator(int index) {
        if (index < 0) {
            throw new IllegalArgumentException("index less than zero.");
        }

        ensurePopupMenuCreated();
        popupMenu.insert(new JPopupMenu.Separator(), index);
    }


    /**
     * 移除
     * @param item   菜单项
     */
    public void remove(JMenuItem item) {
        if (popupMenu != null) {
            popupMenu.remove(item);
        }
    }

    /**
     * 移除指定位置菜单项
     * @param pos  指定位置
     */
    public void remove(int pos) {
        if (pos < 0) {
            throw new IllegalArgumentException("index less than zero.");
        }
        if (pos > getItemCount()) {
            throw new IllegalArgumentException("index greater than the number of items.");
        }
        if (popupMenu != null) {
            popupMenu.remove(pos);
        }
    }

    /**
     * 移除组件
     * @param c  组件
     */
    public void remove(Component c) {
        if (popupMenu != null) {
            popupMenu.remove(c);
        }
    }

    /**
     * 移除所有
     */
    public void removeAll() {
        if (popupMenu != null) {
            popupMenu.removeAll();
        }
    }

    /**
     *  组件总数
     * @return   组件总数
     */
    public int getMenuComponentCount() {
        return (popupMenu == null) ? 0 : popupMenu.getComponentCount();
    }

    /**
     * 指定位置组件
     * @param n   指定位置
     * @return    组件
     */
    public Component getMenuComponent(int n) {
        return (popupMenu == null) ? null : popupMenu.getComponent(n);
    }

    /**
     * 所有组件
     * @return   所有组件
     */
    public Component[] getMenuComponents() {
        return (popupMenu == null) ? new Component[0] : popupMenu.getComponents();
    }

    /**
     * 取得弹出菜单
     * @return  菜单
     */
    public JPopupMenu getPopupMenu() {
        ensurePopupMenuCreated();
        return popupMenu;
    }

    /**
     * 得到子元素
     * @return  子元素
     */
    public MenuElement[] getSubElements() {
        return popupMenu == null ? new MenuElement[0] : new MenuElement[]{popupMenu};
    }


    /**
     *   设置所有组件方位
     * @param o 方位
     */
    public void applyComponentOrientation(ComponentOrientation o) {
        super.applyComponentOrientation(o);

        if (popupMenu != null) {
            int ncomponents = getMenuComponentCount();
            for (int i = 0; i < ncomponents; ++i) {
                getMenuComponent(i).applyComponentOrientation(o);
            }
            popupMenu.setComponentOrientation(o);
        }
    }

    /**
     * 设置所有组件方位
     * @param o 方位
     */
    public void setComponentOrientation(ComponentOrientation o) {
        super.setComponentOrientation(o);
        if (popupMenu != null) {
            popupMenu.setComponentOrientation(o);
        }
    }
}