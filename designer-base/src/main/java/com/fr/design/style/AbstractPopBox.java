package com.fr.design.style;

import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.style.background.BackgroundJComponent;
import com.fr.design.style.background.gradient.GradientBackgroundPane;
import com.fr.general.Background;

import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.AWTEvent;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2011-11-24 下午02:51:12
 *          类说明: 弹出box
 */
public abstract class AbstractPopBox extends JPanel {

    protected BackgroundJComponent displayComponent;

    private JWindow selectPopupWindow;
    private boolean isWindowEventInit = false;
    private static int GAP = 2;
    private static int GAP2 = 20;

    private List<ChangeListener> changeListenerList = new ArrayList<ChangeListener>();

    MouseAdapter mouseListener = new MouseAdapter() {
        public void mousePressed(MouseEvent evt) {
            showPopupMenu();
        }


    };

    AWTEventListener awt = new AWTEventListener() {
        public void eventDispatched(AWTEvent event) {
            if (event instanceof MouseEvent) {
                MouseEvent evt = (MouseEvent) event;
                if (evt.getClickCount() > 0) {
                    mouseClick(evt);
                }
            }
        }
    };

    protected void showPopupMenu() {
        if (selectPopupWindow != null && selectPopupWindow.isVisible()) {
            hidePopupMenu();
            return;
        }

        if (!this.isEnabled()) {
            return;
        }
        Toolkit.getDefaultToolkit().addAWTEventListener(awt, AWTEvent.MOUSE_EVENT_MASK);

        selectPopupWindow = this.getControlWindow();

        Point convertPoint = new Point(0, 0);

        // e: 将点(0,0)从ColorSelectionPane的坐标系统转换到屏幕坐标.
        SwingUtilities.convertPointToScreen(convertPoint, this);
        int y = convertPoint.y + this.getSize().height;
        int x = convertPoint.x;
        int h = y + selectPopupWindow.getHeight();
        int width = x + selectPopupWindow.getWidth();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        if (h > screenSize.height) {
            y = y - selectPopupWindow.getHeight() - GAP2;// 超过屏幕高度了
        }

        if (width > screenSize.width) {
            x = screenSize.width - selectPopupWindow.getWidth();
        }
        selectPopupWindow.setLocation(x, y);

        selectPopupWindow.setVisible(true);

        //wei : 为了点击别的地方下拉颜色窗口消失
        MouseAdapter parentMouseListener = new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                mouseClick(evt);
            }
        };
        if (!this.isWindowEventInit && SwingUtilities.getAncestorOfClass(GradientBackgroundPane.class, this) != null) {
            SwingUtilities.getAncestorOfClass(GradientBackgroundPane.class, this).addMouseListener(parentMouseListener);
            this.isWindowEventInit = true;
        }
    }

    private void mouseClick(MouseEvent evt) {
        Point point = new Point((int) (evt.getLocationOnScreen().getX()), (int) evt.getLocationOnScreen().getY());
        Dimension popBoxD = AbstractPopBox.this.getSize();
        try {
            Point popBoxP = AbstractPopBox.this.getLocationOnScreen();
            Dimension popMenuD = this.getControlWindow().getSize();
            Point popMenuP = this.getControlWindow().getLocation();
            Rectangle popBoxRect = new Rectangle(popBoxP, popBoxD);
            Rectangle popMenuRect = new Rectangle(popMenuP, popMenuD);
            if (!popBoxRect.contains(point) && !popMenuRect.contains(point)) {
                this.hidePopupMenu();
            }
        } catch (Exception ignore){
            this.hidePopupMenu();
        }
    }

    protected void hidePopupMenu() {
        if (selectPopupWindow != null) {
            selectPopupWindow.setVisible(false);
        }

        selectPopupWindow = null;
        Toolkit.getDefaultToolkit().removeAWTEventListener(awt);
    }

    protected JWindow getControlWindow() {
        //find parent.
        if (this.selectPopupWindow == null) {
            Window parentWindow = SwingUtilities.windowForComponent(this);
            if (parentWindow != null) {
                this.selectPopupWindow = new SelectControlWindow(parentWindow);
            }

            selectPopupWindow.addMouseListener(new MouseAdapter() {
                public void mouseExited(MouseEvent evt) {
                    int x = evt.getLocationOnScreen().x;
                    int y = evt.getLocationOnScreen().y;

                    if (selectPopupWindow != null) {
                        double desValue = 2;
                        Rectangle rectangle = selectPopupWindow.getBounds();
                        boolean b1 = x < rectangle.x + desValue || x >= rectangle.x + rectangle.width - desValue;
                        boolean b2 = y > rectangle.y + rectangle.height - desValue;//避免了鼠标下移刚进入selectPopupWindow的过程中弹出框隐藏,上移移出后由AbstractPopBox的mouseListener处理
                        if (b1 || b2) {
                            hidePopupMenu();
                        }
                    }
                }
            });
        }

        return selectPopupWindow;
    }

    /**
     * 添加事件
     *
     * @param changeListener 事件
     */
    public void addSelectChangeListener(ChangeListener changeListener) {
        this.changeListenerList.add(changeListener);
    }

    /**
     * 删除事件
     *
     * @param changeListener 事件
     */
    public void removeSelectChangeListener(ChangeListener changeListener) {
        this.changeListenerList.remove(changeListener);
    }

    /**
     * 响应事件
     */
    public void fireChangeListener() {
        if (!changeListenerList.isEmpty()) {
            ChangeEvent evt = new ChangeEvent(this);
            for (int i = 0; i < changeListenerList.size(); i++) {
                this.changeListenerList.get(i).stateChanged(evt);
            }
        }
    }

    /**
     * 待说明
     *
     * @param background 背景
     */
    public void fireDisplayComponent(Background background) {
        if (displayComponent != null) {
            displayComponent.setSelfBackground(background);
        }
        fireChangeListener();
        this.repaint();
    }

    /**
     * 初始化弹出框的面板
     *
     * @param preWidth 宽度
     * @return 弹出面板
     */
    public abstract JPanel initWindowPane(double preWidth);

    private class SelectControlWindow extends JWindow {
        private static final long serialVersionUID = -5776589767069105911L;

        public SelectControlWindow(Window paranet) {
            super(paranet);
            this.initComponents();
        }

        public void initComponents() {
            JPanel defaultPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
            this.setContentPane(defaultPane);

//            defaultPane.setBorder(UIManager.getBorder("PopupMenu.border"));

            if (displayComponent != null) {
                defaultPane.add(initWindowPane(displayComponent.getPreferredSize().getWidth()));
            } else {
                defaultPane.add(initWindowPane(20));
            }
            this.pack();
        }

        @Override
        public void setVisible(boolean b) {
            super.setVisible(b);
            AbstractPopBox.this.repaint();
        }
    }

    protected boolean isPopupVisible() {
        return selectPopupWindow == null ? false : selectPopupWindow.isVisible();
    }
}