package com.fr.design.gui.frpane;

import com.fr.base.BaseUtils;
import com.fr.design.constants.UIConstants;
import com.fr.log.FineLoggerFactory;
import com.fr.general.GeneralUtils;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Coder: Sean
 * Date: 13-12-30
 * Time: 上午11:38
 */
public class UITabbedPaneUI extends BasicTabbedPaneUI {

    private int closeX = -1;
    private int closeY = -1;

    private static final Icon ADD_NORMAL = BaseUtils.readIcon("com/fr/design/images/gui/tab_add_normal.png");
    private static final Icon ADD_OVER = BaseUtils.readIcon("com/fr/design/images/gui/tab_add_hover.png");
    private static final Icon ADD_CLICK = BaseUtils.readIcon("com/fr/design/images/gui/tab_add_click.png");
    private Icon addBtn = ADD_NORMAL;
    private Icon closeIcon = BaseUtils.readIcon("com/fr/design/images/gui/tab_delete.png");
    private int addX = -1;
    private int addY = -1;
    private int rollover = -1;
    private Color tabBorderColor = new Color(143, 160, 183);
    private Color[] tabSelectedColor = {UIConstants.NORMAL_BLUE, UIConstants.NORMAL_BLUE, UIConstants.NORMAL_BLUE};

    /**
     * 创建UI对象
     *
     * @param c 容器
     * @return 返回UI对象
     */
    public static ComponentUI createUI(JComponent c) {
        return new UITabbedPaneUI();
    }

    /**
     * 初始化监听事件
     */
    protected void installListeners() {
        super.installListeners();
        tabPane.addMouseMotionListener(
                (MouseMotionListener) mouseListener);
    }

    protected MouseListener createMouseListener() {
        return new UIMouseHandler();
    }

    public class UIMouseHandler implements MouseListener, MouseMotionListener {
        /**
         * 鼠标按下
         *
         * @param e 事件
         */
        public void mousePressed(MouseEvent e) {
            if (!tabPane.isEnabled()) {
                return;
            }
            int x = e.getX(), y = e.getY();
            if (addX != -1 && isMouseInAdd(x, y)) {
                addBtn = ADD_CLICK;
                tabPane.repaint();
            }
            int tabIndex = getTabAtLocation(x, y);
            if (tabIndex >= 0 && tabPane.isEnabledAt(tabIndex)) {
                if (canClose() && isMouseInClose(x, y)) {
                    ((UITabbedPane) tabPane).doRemoveTab(tabIndex);
                } else if (tabIndex != tabPane.getSelectedIndex()) {
                    tabPane.setSelectedIndex(tabIndex);
                } else if (tabPane.isRequestFocusEnabled()) {
                    tabPane.requestFocus();
                }
            }
        }

        /**
         * 鼠标进入
         *
         * @param e 事件
         */
        public void mouseEntered(MouseEvent e) {
        }

        /**
         * 鼠标离开
         *
         * @param e 事件
         */
        public void mouseExited(MouseEvent e) {
            if (rollover >= tabPane.getTabCount()) {
                rollover = -1;
            }
            if (rollover != -1) {
                tabPane.repaint(getTabBounds(tabPane, rollover));
                rollover = -1;
            }
        }

        /**
         * 鼠标点击
         *
         * @param e 事件
         */
        public void mouseClicked(MouseEvent e) {
        }

        /**
         * 鼠标释放
         *
         * @param e 事件
         */
        public void mouseReleased(MouseEvent e) {
            if (addX != -1 && isMouseInAdd(e.getX(), e.getY())) {
                String classpath = ((UITabbedPane) tabPane).getClassPath();
                String tabName = ((UITabbedPane) tabPane).getTabName();
                try {
                    addBtn = ADD_NORMAL;
                    tabPane.addTab(tabName,
                            (Component) GeneralUtils.classForName(classpath).newInstance());
                } catch (Exception ex) {
                    FineLoggerFactory.getLogger().error(ex.getMessage(), ex);
                }
            }
        }

        /**
         * 鼠标拖拽
         *
         * @param e 事件
         */
        public void mouseDragged(MouseEvent e) {
        }

        /**
         * 鼠标移动
         *
         * @param e 事件
         */
        public void mouseMoved(MouseEvent e) {
            if (tabPane == null) {
                return;
            }
            if (!tabPane.isEnabled()) {
                return;
            }
            int x = e.getX(), y = e.getY();
            if (addX != -1 && isMouseInAdd(x, y)) {
                addBtn = ADD_OVER;
                tabPane.repaint();
            } else if (addBtn != ADD_NORMAL) {
                addBtn = ADD_NORMAL;
                tabPane.repaint();
            }
            checkRollOver(getTabAtLocation(x, y));
        }
    }

    /**
     * 判断鼠标是否在添加按钮上
     *
     * @param x 鼠标坐标x
     * @param y 鼠标坐标y
     * @return 返回鼠标是否在添加按钮上
     */
    private boolean isMouseInAdd(int x, int y) {
        int addWidth = addBtn.getIconWidth(), addHeight = addBtn.getIconHeight();
        return x >= addX && x <= addX + addWidth && y > addY && y <= addY + addHeight;
    }

    /**
     * 判断鼠标是否在关闭按钮上
     *
     * @param x 鼠标坐标x
     * @param y 鼠标坐标y
     * @return 返回鼠标是否在关闭按钮上
     */
    private boolean isMouseInClose(int x, int y) {
        int closeWidth = closeIcon.getIconWidth(), closeHeight = closeIcon.getIconHeight();
        return x >= closeX && x <= closeX + closeWidth && y > closeY && y <= closeY + closeHeight;
    }

    /**
     * 如果tab只剩下最后一个，则不画删除按钮
     *
     * @return 返回当前tab还可否关闭
     */
    private boolean canClose() {
        return tabPane.getTabCount() > 1 && ((UITabbedPane) tabPane).isClosable();
    }

    /**
     * tab轮转切换
     *
     * @param tabIndex tab索引
     */
    private void checkRollOver(int tabIndex) {
        if (rollover >= tabPane.getTabCount()) {
            rollover = -1;
        }
        if (tabIndex == rollover) {
            return;
        }
        if (rollover != -1) {
            tabPane.repaint(getTabBounds(tabPane, rollover));
            if (tabIndex == -1) {
                rollover = -1;
            }
        }
        if (tabIndex >= 0 && tabPane.isEnabledAt(tabIndex)) {
            rollover = tabIndex;
            tabPane.repaint(getTabBounds(tabPane, tabIndex));
        }
    }

    /**
     * 根据鼠标坐标获取tab
     *
     * @param x 鼠标坐标x
     * @param y 鼠标坐标y
     * @return 返回tab索引
     */
    private int getTabAtLocation(int x, int y) {
        return tabForCoordinate(tabPane, x, y);
    }

    /**
     * 绘制tab的边框
     */
    protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
        boolean isEnabled = (tabPane.isEnabledAt(tabIndex));
        if (!tabPane.isEnabled()) {
            isEnabled = false;
        }
        boolean isRollover = (rollover == tabIndex);
        drawUITabBorder(g, tabPlacement, x, y, w, h, isSelected, isEnabled, isRollover);
    }

    /**
     * 根据tab的状态绘制tab的边框
     */
    private void drawUITabBorder(Graphics g, int tabPlacement, int x, int y, int w, int h,
                                 boolean isSelected, boolean isEnabled, boolean isRollover) {
        if (!isEnabled) {
            drawUITabBorder(g, tabBorderColor, x, y, w, h, tabPlacement);
        } else if (isSelected || isRollover) {
            drawSelectedUITabBorder(g, tabBorderColor, x, y, w, h, tabPlacement);
            if (isRollover && canClose()) {
                closeX = x + w - closeIcon.getIconWidth() - 3;
                closeY = 0;
                switch (tabPlacement) {
                    case BOTTOM:
                        closeY = y;
                        break;
                    case TOP:
                        closeY = y + 3;
                        break;
                }
                closeIcon.paintIcon(tabPane, g, closeX, closeY);
            }
        } else {
            drawUITabBorder(g, tabBorderColor, x, y, w, h, tabPlacement);
        }
    }

    /**
     * 更新界面
     *
     * @param g
     * @param c
     */
    public void update(Graphics g, JComponent c) {
        Insets insets = tabPane.getInsets();
        int x = insets.left;
        int y = insets.top;
        int w = tabPane.getWidth() - insets.right - insets.left;
        int h = tabPane.getHeight() - insets.top - insets.bottom;

        if (c.isOpaque()) {
            g.setColor(UIConstants.NORMAL_BACKGROUND);
            g.fillRect(0, 0, c.getWidth(), c.getHeight());
        }

        int tabPlacement = tabPane.getTabPlacement();
        switch (tabPlacement) {
            case BOTTOM:
                h -= calculateTabAreaHeight(tabPlacement, runCount, maxTabHeight);
                break;
            case TOP:
            default:
                y += calculateTabAreaHeight(tabPlacement, runCount, maxTabHeight);
                h -= (y - insets.top);
        }
        drawUIContentBorder(g, x, y, w, h);
        if (((UITabbedPane) tabPane).isClosable()) {
            drawUITabAddBtn(g, tabPlacement, x, y, w, h);
        }
        super.paint(g, c);
    }

    private void drawUITabAddBtn(Graphics g, int tabPlacement, int x, int y, int w, int h) {
        addX = rects[tabPane.getTabCount() - 1].x + rects[tabPane.getTabCount() - 1].width + 4;
        switch (tabPlacement) {
            case BOTTOM:
                addY = y + h + 3;
                break;
            case TOP:
                addY = y - addBtn.getIconHeight() - 3;
                break;
        }
        addBtn.paintIcon(tabPane, g, addX, addY);
    }

    private void drawUIContentBorder(Graphics g, int x, int y, int w, int h) {
        g.setColor(tabBorderColor);
        g.drawRect(x, y, w - 3, h - 3);
        // Shadow
        g.setColor(new Color(204, 204, 204));
        g.drawLine(x + w - 2, y + 1, x + w - 2, y + h - 2); // right
        g.drawLine(x + 1, y + h - 2, x + w - 3, y + h - 2); // bottom
    }

    @Override
    protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
        // do nothing
    }

    /**
     * 绘制tab的边框
     */
    private void drawUITabBorder(
            Graphics g, Color c, int x, int y, int w, int h, int tabPlacement) {
        Color c2 = null;
        g.setColor(c);
        switch (tabPlacement) {
            case SwingConstants.BOTTOM:
                w -= 1;
                y -= 2;
                g.drawLine(x + 2, y + h - 1, x + w - 3, y + h - 1);
                g.drawLine(x, y, x, y + h - 3);
                g.drawLine(x + w - 1, y, x + w - 1, y + h - 3);
                c2 = new Color(c.getRed(), c.getGreen(), c.getBlue(), 56);
                g.setColor(c2);
                g.drawLine(x, y + h - 1, x, y + h - 1);
                g.drawLine(x + w - 1, y + h - 1, x + w - 1, y + h - 1);
                c2 = new Color(c.getRed(), c.getGreen(), c.getBlue(), 183);
                g.setColor(c2);
                g.drawLine(x, y + h - 2, x, y + h - 2);
                g.drawLine(x + 1, y + h - 1, x + 1, y + h - 1);
                g.drawLine(x + w - 2, y + h - 1, x + w - 2, y + h - 1);
                g.drawLine(x + w - 1, y + h - 2, x + w - 1, y + h - 2);
                c2 = new Color(c.getRed(), c.getGreen(), c.getBlue(), 76);
                g.setColor(c2);
                g.drawLine(x + 1, y + h - 2, x + 1, y + h - 2);
                g.drawLine(x + w - 2, y + h - 2, x + w - 2, y + h - 2);
                break;
            case SwingConstants.TOP:
            default:
                w -= 1;
                g.drawLine(x + 2, y, x + w - 3, y);
                g.drawLine(x, y + 2, x, y + h - 1);
                g.drawLine(x + w - 1, y + 2, x + w - 1, y + h - 1);
                c2 = new Color(c.getRed(), c.getGreen(), c.getBlue(), 56);
                g.setColor(c2);
                g.drawLine(x, y, x, y);
                g.drawLine(x + w - 1, y, x + w - 1, y);
                c2 = new Color(c.getRed(), c.getGreen(), c.getBlue(), 183);
                g.setColor(c2);
                g.drawLine(x + 1, y, x + 1, y);
                g.drawLine(x + w - 2, y, x + w - 2, y);
                g.drawLine(x, y + 1, x, y + 1);
                g.drawLine(x + w - 1, y + 1, x + w - 1, y + 1);
                c2 = new Color(c.getRed(), c.getGreen(), c.getBlue(), 76);
                g.setColor(c2);
                g.drawLine(x + 1, y + 1, x + 1, y + 1);
                g.drawLine(x + w - 2, y + 1, x + w - 2, y + 1);
        }
    }

    /**
     * 绘制被选中的tab
     */
    private void drawSelectedUITabBorder(Graphics g, Color c, int x, int y, int w, int h, int tabPlacement) {
        Color c1 = tabSelectedColor[0];
        Color c2 = tabSelectedColor[1];
        Color c3 = tabSelectedColor[2];
        g.setColor(c2);
        switch (tabPlacement) {
            case SwingConstants.BOTTOM:
                w -= 1;
                y -= 2;
                g.drawLine(x + 2, y + h - 1, x + w - 3, y + h - 1);//下梯形，4个点2横线
                g.drawLine(x + 1, y + h - 2, x + 1, y + h - 2);
                g.drawLine(x + w - 2, y + h - 2, x + w - 2, y + h - 2);
                g.drawLine(x, y + h - 3, x, y + h - 3);
                g.drawLine(x + w - 1, y + h - 3, x + w - 1, y + h - 3);
                g.setColor(c1);//梯形内部，2横线
                g.drawLine(x + 2, y + h - 2, x + w - 3, y + h - 2);
                g.drawLine(x + 1, y + h - 3, x + w - 2, y + h - 3);
                g.setColor(c);//左右的边框，2竖线
                g.drawLine(x, y, x, y + h - 4);
                g.drawLine(x + w - 1, y, x + w - 1, y + h - 4);
                g.setColor(c3);//角，4个点
                g.drawLine(x + 1, y + h - 1, x + 1, y + h - 1);
                g.drawLine(x, y + h - 2, x, y + h - 2);
                g.drawLine(x + w - 2, y + h - 1, x + w - 2, y + h - 1);
                g.drawLine(x + w - 1, y + h - 2, x + w - 1, y + h - 2);
                break;
            case SwingConstants.TOP:
            default:
                w -= 1;
                g.drawLine(x + 2, y, x + w - 3, y);
                g.drawLine(x + 1, y + 1, x + 1, y + 1);
                g.drawLine(x + w - 2, y + 1, x + w - 2, y + 1);
                g.drawLine(x, y + 2, x, y + 2);
                g.drawLine(x + w - 1, y + 2, x + w - 1, y + 2);
                g.setColor(c1);
                g.drawLine(x + 2, y + 1, x + w - 3, y + 1);
                g.drawLine(x + 1, y + 2, x + w - 2, y + 2);
                g.setColor(c3);
                g.drawLine(x + 1, y, x + 1, y);
                g.drawLine(x, y + 1, x, y + 1);
                g.drawLine(x + w - 2, y, x + w - 2, y);
                g.drawLine(x + w - 1, y + 1, x + w - 1, y + 1);
                g.setColor(c);
                g.drawLine(x, y + 3, x, y + h - 1);
                g.drawLine(x + w - 1, y + 3, x + w - 1, y + h - 1);
        }
    }

    protected LayoutManager createLayoutManager() {
        super.createLayoutManager();
        if (tabPane.getTabLayoutPolicy() == JTabbedPane.SCROLL_TAB_LAYOUT) {
            return super.createLayoutManager();
        } else {
            return new UITabbedPaneLayout();
        }
    }

    protected class UITabbedPaneLayout extends TabbedPaneLayout {
    }
}