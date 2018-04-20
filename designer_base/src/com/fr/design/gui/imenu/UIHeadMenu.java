package com.fr.design.gui.imenu;

import com.fr.design.constants.UIConstants;
import com.fr.design.utils.gui.GUIPaintUtils;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.MenuElement;
import javax.swing.border.Border;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Shape;

/**
 * @author null
 */
public class UIHeadMenu extends UIMenu {
    private static final float REC = 8f;
    private JPopupMenu popupMenu;

    public UIHeadMenu(String name) {
        super(name);
    }

    @Override
    public JPopupMenu getPopupMenu() {
        ensurePopupMenuCreated();
        popupMenu.setBackground(UIConstants.NORMAL_BACKGROUND);
        popupMenu.setBorder(new Border() {

            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                Graphics2D g2d = (Graphics2D) g;
                int rec = (int) REC;
                GUIPaintUtils.paintShapeBorder(g2d, x, y, width, height, rec);
                if (!(UIHeadMenu.this.getParent() instanceof JPopupMenu)) {
                    g.setColor(UIConstants.NORMAL_BACKGROUND);
                    g.drawLine(1, 0, UIHeadMenu.this.getWidth() - 2, 0);
                }
            }

            @Override
            public boolean isBorderOpaque() {
                return false;
            }

            @Override
            public Insets getBorderInsets(Component c) {
                return new Insets(5, 2, 10, 10);
            }
        });
        return popupMenu;
    }

    @Override
    protected void ensurePopupMenuCreated() {
        if (popupMenu == null) {
            this.popupMenu = new JPopupMenu() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g;
                    float width = getWidth();
                    float height = getHeight();

                    Shape shape = GUIPaintUtils.paintShape(g2d, width, height, REC);
                    g2d.setClip(shape);
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    super.paintComponent(g2d);
                }
            };
            popupMenu.setInvoker(this);
            popupListener = createWinListener(popupMenu);
        }
    }

    /**
     * 画界面
     */
    @Override
    public void updateUI() {
        setUI(new UIMenuUI());
    }

    /**
     * 判断popupmeu是否隐藏
     *
     * @return 如果隐藏 返回true
     */
    @Override
    public boolean isPopupMenuVisible() {
        ensurePopupMenuCreated();
        return popupMenu.isVisible();
    }


    /**
     * 设置popupmenu位置
     *
     * @param x x
     * @param y y
     */
    @Override
    public void setMenuLocation(int x, int y) {
        super.setMenuLocation(x, y);
        if (popupMenu != null) {
            popupMenu.setLocation(x, y);
        }
    }

    /**
     * 向popupmenu添加 JMenuItem
     *
     * @param menuItem 菜单项
     * @return 菜单项
     */
    @Override
    public JMenuItem add(JMenuItem menuItem) {
        ensurePopupMenuCreated();
        return popupMenu.add(menuItem);
    }

    /**
     * 添加组件
     *
     * @param c 组件
     * @return 组件
     */
    @Override
    public Component add(Component c) {
        ensurePopupMenuCreated();
        popupMenu.add(c);
        return c;
    }

    /**
     * 向指定位置添加组件
     *
     * @param c     组件
     * @param index 位置
     * @return 组件
     */
    @Override
    public Component add(Component c, int index) {
        ensurePopupMenuCreated();
        popupMenu.add(c, index);
        return c;
    }


    /**
     * 添加分隔符
     */
    @Override
    public void addSeparator() {
        ensurePopupMenuCreated();
        popupMenu.addSeparator();
    }

    /**
     * 添加menuitem到指定位置
     *
     * @param s   字符
     * @param pos 位置
     */
    @Override
    public void insert(String s, int pos) {
        if (pos < 0) {
            throw new IllegalArgumentException("index less than zero.");
        }

        ensurePopupMenuCreated();
        popupMenu.insert(new JMenuItem(s), pos);
    }

    /**
     * 添加么会特么到指定位置
     *
     * @param mi  菜单项
     * @param pos 位置
     * @return 菜单项
     */
    @Override
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
     *
     * @param a   事件
     * @param pos 位置
     * @return 菜单项
     */
    @Override
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
     * 添加分隔符到指定位置
     *
     * @param index 指定位置
     */
    @Override
    public void insertSeparator(int index) {
        if (index < 0) {
            throw new IllegalArgumentException("index less than zero.");
        }

        ensurePopupMenuCreated();
        popupMenu.insert(new JPopupMenu.Separator(), index);
    }


    /**
     * 移除
     *
     * @param item 菜单项
     */
    @Override
    public void remove(JMenuItem item) {
        if (popupMenu != null) {
            popupMenu.remove(item);
        }
    }

    /**
     * 移除指定位置菜单项
     *
     * @param pos 指定位置
     */
    @Override
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
     *
     * @param c 组件
     */
    @Override
    public void remove(Component c) {
        if (popupMenu != null) {
            popupMenu.remove(c);
        }
    }

    /**
     * 移除所有
     */
    @Override
    public void removeAll() {
        if (popupMenu != null) {
            popupMenu.removeAll();
        }
    }

    /**
     * 组件总数
     *
     * @return 组件总数
     */
    @Override
    public int getMenuComponentCount() {
        return (popupMenu == null) ? 0 : popupMenu.getComponentCount();
    }

    /**
     * 指定位置组件
     *
     * @param n 指定位置
     * @return 组件
     */
    @Override
    public Component getMenuComponent(int n) {
        return (popupMenu == null) ? null : popupMenu.getComponent(n);
    }

    /**
     * 所有组件
     *
     * @return 所有组件
     */
    @Override
    public Component[] getMenuComponents() {
        return (popupMenu == null) ? new Component[0] : popupMenu.getComponents();
    }


    @Override
    public MenuElement[] getSubElements() {
        if (popupMenu == null) {
            return new MenuElement[0];
        } else {
            MenuElement[] result = new MenuElement[1];
            result[0] = popupMenu;
            return result;
        }
    }
}
