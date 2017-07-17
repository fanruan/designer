package com.fr.design.mainframe;

import com.fr.base.BaseUtils;
import com.fr.design.DesignerEnvManager;
import com.fr.design.constants.UIConstants;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icontainer.UIEastResizableContainer;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.VerticalFlowLayout;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.FRFont;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class EastRegionContainerPane extends UIEastResizableContainer {
    private static EastRegionContainerPane THIS;
    private List<PropertyItem> propertyItemList;
    private CardLayout propertyCard;
    private JPanel leftPane;
    private JPanel rightPane;
    private static final int CONTAINER_WIDTH = 260;
    private static final int TAB_WIDTH = 40;
    private static final int CONTENT_WIDTH = CONTAINER_WIDTH - TAB_WIDTH;
    private static final int POPUP_TOOLPANE_HEIGHT = 25;
    private static final int ARROW_RANGE_START = CONTENT_WIDTH - 30;

    /**
     * 得到实例
     *
     * @return
     */
    public static final EastRegionContainerPane getInstance() {
        if (THIS == null) {
            THIS = new EastRegionContainerPane();
//            THIS.setLastToolPaneY(DesignerEnvManager.getEnvManager().getLastEastRegionToolPaneY());
            THIS.setLastContainerWidth(DesignerEnvManager.getEnvManager().getLastEastRegionContainerWidth());
        }
        return THIS;
    }

    public EastRegionContainerPane() {
        super();
//        setVerticalDragEnabled(false);
        initPropertyItemList();
        initContentPane();
//        super(leftPane, rightPane);
        setContainerWidth(CONTAINER_WIDTH);
    }

    private void initPropertyItemList() {
        propertyItemList = new ArrayList<>();

        // 单元格元素
        PropertyItem cellElement = new PropertyItem("cellElement", "/com/fr/design/images/buttonicon/add.png");
        // 单元格属性
        PropertyItem cellAttr = new PropertyItem("cellAttr", "com/fr/design/images/toolbarbtn/close.png");
        // 悬浮元素
        PropertyItem floatElement = new PropertyItem("floatElement", "com/fr/design/images/toolbarbtn/close.png");
        // 控件设置
        PropertyItem widgetSettings = new PropertyItem("widgetSettings", "com/fr/design/images/toolbarbtn/close.png");
        // 条件属性
        PropertyItem conditionAttr = new PropertyItem("conditionAttr", "com/fr/design/images/toolbarbtn/close.png");
        // 超级链接
        PropertyItem hyperlink = new PropertyItem("hyperlink", "com/fr/design/images/toolbarbtn/close.png");
        // 组件库
        PropertyItem widgetLib = new PropertyItem("widgetLib", "com/fr/design/images/toolbarbtn/close.png");
        propertyItemList.add(cellElement);
        propertyItemList.add(cellAttr);
        propertyItemList.add(floatElement);
        propertyItemList.add(widgetSettings);
        propertyItemList.add(conditionAttr);
        propertyItemList.add(hyperlink);
        propertyItemList.add(widgetLib);
    }

    private void initContentPane() {
        initRightPane();
        initLeftPane();
    }

    // 右侧属性面板
    private void initRightPane() {
        rightPane = new JPanel();
        propertyCard = new CardLayout();
        rightPane.setBackground(Color.green);
        rightPane.setLayout(propertyCard);
        for (PropertyItem item : propertyItemList) {
            rightPane.add(item.getName(), item.getPropertyPanel());
        }

        replaceRightPane(rightPane);
    }

    // 左侧按钮面板
    private void initLeftPane() {
        leftPane = new JPanel();
        leftPane.setLayout(new VerticalFlowLayout(VerticalFlowLayout.TOP, 0, 0));
        for (PropertyItem item : propertyItemList) {
            leftPane.add(item.getButton());
        }

//        leftPane.setLayout(new BoxLayout(leftPane, BoxLayout.Y_AXIS));
        leftPane.setBackground(Color.yellow);
        replaceLeftPane(leftPane);
    }

    // 弹出面板时，更新框架内容
    private void removeItem(PropertyItem propertyItem) {
        leftPane.remove(propertyItem.getButton());
        rightPane.remove(propertyItem.getPropertyPanel());
        refreshContainer();
    }

    @Override
    public void onResize() {
        for (PropertyItem item : propertyItemList) {
            item.onResize();
        }
    }

    public EastRegionContainerPane(JPanel leftPane, JPanel rightPane) {
        super(leftPane, rightPane);
//        setVerticalDragEnabled(false);
//        setContainerWidth(260);
    }

    public void replaceUpPane(JComponent pane) {
        propertyItemList.get(0).replaceContentPane(pane);
    }

    public void replaceDownPane(JComponent pane) {
        propertyItemList.get(1).replaceContentPane(pane);
    }

    public JComponent getUpPane() {
        return propertyItemList.get(0).getContentPane();
    }

    public JComponent getDownPane() {
        return propertyItemList.get(1).getContentPane();
    }

    public void addParameterPane(JComponent paraPane) {
        propertyItemList.get(2).replaceContentPane(paraPane);
    }

    public void setParameterHeight(int height) {
        // stub
    }

    public static void main(String[] args){
        JFrame jf = new JFrame("test");
//        jf = new JFrame("test");

        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel cc = new JPanel();
        cc.setBackground(Color.white);
//        JPanel leftPane = new JPanel();
//        leftPane.setBackground(Color.yellow);
//        JPanel rightPane = new JPanel();
//        rightPane.setBackground(Color.green);
//
//        JButton b1, b2;
//        b1 = new JButton("b1");
//        b2 = new JButton("b2");
//        b1.setPreferredSize(new Dimension(40, 40));
//        b2.setPreferredSize(new Dimension(40, 40));
//        leftPane.add(b1);
//        leftPane.add(b2);
//        leftPane.setLayout(new BoxLayout(leftPane, BoxLayout.Y_AXIS));

        JPanel content = (JPanel)jf.getContentPane();
//        content.setLayout(null);
        content.add(cc, BorderLayout.CENTER);
        content.add(new EastRegionContainerPane(), BorderLayout.EAST);
        GUICoreUtils.centerWindow(jf);
        jf.setSize(400, 400);
        jf.setVisible(true);
    }

    public void removeParameterPane() {

    }

    /**
     * 刷新右面板
     */
    public void refreshRightPane() {

        if (this.getRightPane() instanceof DockingView) {
            ((DockingView) this.getRightPane()).refreshDockingView();
        }
    }

    public void refreshDownPane() {
        JComponent pane = propertyItemList.get(1).getContentPane();
        if (pane instanceof DockingView) {
            ((DockingView) pane).refreshDockingView();
        }
    }

    private void refreshContainer() {
        validate();
        repaint();
        revalidate();
    }

    public int getToolPaneY() {
        return 0;
    }



    class PropertyItem {
        //        private UIButton button;
        private UIButton button;
        private String name;
        private JComponent propertyPanel;
        private JComponent contentPane;
        private FixedPopupPane popupPane;  // 左侧固定弹出框
        private PopupToolPane popupToolPane;  // 弹出工具条
        private PopupDialog popupDialog;  // 弹出框
        private int x, y;  // 弹出框的坐标
        private int height; // 弹出框的高度
        private boolean isPoppedOut = false;  // 是否弹出
        private Dimension fixedSize;

        public PropertyItem(String name, String btnUrl) {
            this.name = name;
            initButton(btnUrl);
            initPropertyPanel();
        }

        // 选项不可用
        public void setEnabled(boolean enabled) {
            button.setEnabled(enabled);
        }

        private void initPropertyPanel() {
            propertyPanel = new JPanel();
            propertyPanel.setBackground(Color.pink);
            contentPane = generateContentPane();
            popupToolPane = new PopupToolPane(this, PopupToolPane.UP_BUTTON);
            propertyPanel.setLayout(new BorderLayout());
            propertyPanel.add(popupToolPane, BorderLayout.NORTH);
            propertyPanel.add(contentPane, BorderLayout.CENTER);
        }

        public void setIsPoppedOut(boolean isPoppedOut) {
            this.isPoppedOut = isPoppedOut;
        }

        public JComponent generateContentPane() {
            JComponent contentPane = new JPanel();
            JButton testBtn = new JButton(name);
            testBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setEnabled(!button.isEnabled());
                }
            });
            contentPane.add(testBtn);
            return contentPane;
        }

        public void replaceContentPane(JComponent pane) {
            propertyPanel.remove(this.contentPane);
            propertyPanel.add(this.contentPane = pane);
            if (popupDialog != null && isPoppedOut) {
                popupDialog.replaceContentPane(contentPane);
            }
            if (popupPane != null && !isRightPaneVisible()) {
                popupPane.replaceContentPane(contentPane);
            }

            refreshContainer();
        }

        public JComponent getContentPane() {
            return contentPane;
        }

        public void onResize() {
            if (isRightPaneVisible()) {
                replaceContentPane(contentPane);
            } else if(popupPane != null) {
                popupPane.replaceContentPane(contentPane);
            }
        }

//        private void refreshContainer() {
//            propertyPanel.validate();
//            propertyPanel.repaint();
//            propertyPanel.revalidate();
//        }

        private void initButton(String btnUrl) {
            button = new UIButton(BaseUtils.readIcon(btnUrl)) {
                public Dimension getPreferredSize() {
                    return new Dimension(TAB_WIDTH, TAB_WIDTH);
                }
            };
            button.set4LargeToolbarButton();
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (isRightPaneVisible()) {
                        propertyCard.show(rightPane, name);
                    } else {
                        popupFixedPane();
                    }
                }
            });
        }

        public UIButton getButton() {
            return button;
        }

        public String getName() {
            return name;
        }

        public JComponent getPropertyPanel() {
            return propertyPanel;
        }

        // 固定弹窗
        public void popupFixedPane() {
            if (popupPane == null) {
                popupPane = new FixedPopupPane(this);
            }
            GUICoreUtils.showPopupMenu(popupPane, button, -popupPane.getPreferredSize().width, 0);
        }

        // 弹出对话框
        public void popupDialog() {
//            setIsPoppedOut(true);
            if (isPoppedOut) {
                return;
            }
            isPoppedOut = true;
            if (popupDialog == null) {
                popupDialog = new PopupDialog(this);
            } else {
                popupDialog.replaceContentPane(contentPane);
                popupDialog.setVisible(true);
            }
//            initContentPane();
//            refreshContainer();
            removeItem(this);
        }

        public void popToFrame() {
            if (isPoppedOut) {
                isPoppedOut = false;
//                popupDialog.dispose();
                popupDialog.setVisible(false);
                initContentPane();
                onResize();
                refreshContainer();
            }
        }
    }

    private class FixedPopupPane extends JPopupMenu {
        private JComponent contentPane;
//        private PopupToolPane popupToolPane;
        private int fixedHeight;
        FixedPopupPane(PropertyItem propertyItem) {
            contentPane = propertyItem.getContentPane();
            this.setLayout(new BorderLayout());
//            popupToolPane = ;
            this.add(new PopupToolPane(propertyItem), BorderLayout.NORTH);
            this.add(contentPane, BorderLayout.CENTER);
            this.setOpaque(false);
            fixedHeight = getPreferredSize().height - contentPane.getPreferredSize().height;
            updateSize();
        }

        private void updateSize() {
            int newHeight = fixedHeight + contentPane.getPreferredSize().height;
            this.setPreferredSize(new Dimension(CONTAINER_WIDTH - TAB_WIDTH, newHeight));
        }

        public JComponent getContentPane() {
            return contentPane;
        }

        public void replaceContentPane(JComponent pane) {
//            remove(pane);
            this.remove(this.contentPane);
            this.add(this.contentPane = pane);
            updateSize();
            refreshContainer();
        }

        private void refreshContainer() {
            validate();
            repaint();
            revalidate();
        }
    }

    // 弹出属性面板的工具条
    private class PopupToolPane extends JPanel {
        private int model = UIConstants.MODEL_NORMAL;
        private String title = "单元格元素";
        private JComponent contentPane;
        private PropertyItem propertyItem;
        private String buttonType;
        private JDialog parentDialog;  // 如果不在对话框中，值为null
        private boolean isMovable = false;
        private Point mouseDownCompCoords;  // 存储按下左键的位置，移动对话框时会用到

        private static final int MIN_X = -150;
        private static final int MAX_X_SHIFT = 50;
        private static final int MAX_Y_SHIFT = 50;


        private static final String NO_BUTTON = "NoButton";
        private static final String UP_BUTTON = "UpButton";
        private static final String DOWN_BUTTON = "DownButton";

        public PopupToolPane(PropertyItem propertyItem) {
            this(propertyItem, NO_BUTTON);
        }

        public PopupToolPane(PropertyItem propertyItem, String buttonType) {
            super();
            this.propertyItem = propertyItem;
            this.contentPane = propertyItem.getContentPane();
            setLayout(new BorderLayout());
            UILabel label = new UILabel(title);
            label.setForeground(new Color(69, 135, 255));
            add(label, BorderLayout.WEST);
            setBorder(new EmptyBorder(5, 10, 0, 0));

            initToolButton(buttonType);
        }

        public void setParentDialog(JDialog parentDialog) {
            this.parentDialog = parentDialog;
            isMovable = true;
        }

        private void initToolButton(final String buttonType) {
            this.buttonType = buttonType;
            if (buttonType.equals(NO_BUTTON)) {
                return;
            }

            if (buttonType.equals(UP_BUTTON)) {

            } else if (buttonType.equals(DOWN_BUTTON)) {

            } else {
                throw new IllegalArgumentException("unknown button type: " + buttonType);
            }

            addMouseMotionListener(new MouseMotionListener() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    if (e.getX() >= ARROW_RANGE_START) {
                        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                        model = UIConstants.MODEL_PRESS;
                    } else {
                        setCursor(Cursor.getDefaultCursor());
                        model = UIConstants.MODEL_NORMAL;
                    }
                    repaint();
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    if (isMovable && mouseDownCompCoords != null) {
                        Point currCoords = e.getLocationOnScreen();
                        int x = currCoords.x - mouseDownCompCoords.x;
                        int y = currCoords.y - mouseDownCompCoords.y;
                        //屏幕可用区域
                        Rectangle screen = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();

                        int minY = screen.y;
                        int maxX = Toolkit.getDefaultToolkit().getScreenSize().width - MAX_X_SHIFT;
                        int maxY = Toolkit.getDefaultToolkit().getScreenSize().height - MAX_Y_SHIFT;
                        if (x < MIN_X) {
                            x = MIN_X;
                        } else if (x > maxX) {
                            x = maxX;
                        }
                        if (y < minY) {
                            y = minY;
                        } else if (y > maxY) {
                            y = maxY;
                        }
                        // 移动到屏幕边缘时，需要校正位置
                        parentDialog.setLocation(x, y);
                    }
                }
            });
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseExited(MouseEvent e) {
                    setCursor(Cursor.getDefaultCursor());
                    model = UIConstants.MODEL_NORMAL;
                    repaint();
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getX() >= ARROW_RANGE_START) {
                        onPop();
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    mouseDownCompCoords = null;
                }
                @Override
                public void mousePressed(MouseEvent e) {
                    if (e.getX() < ARROW_RANGE_START) {
                        mouseDownCompCoords = e.getPoint();
                    }
                }

            });
        }

        // 触发弹入、弹出
        private void onPop() {
            if (buttonType.equals(UP_BUTTON)) {
                propertyItem.popupDialog();
            } else if (buttonType.equals(DOWN_BUTTON)) {
                propertyItem.popToFrame();
            }
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(super.getPreferredSize().width, POPUP_TOOLPANE_HEIGHT);
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            Image button;
            g.setColor(new Color(69, 135, 255));
            g.setFont(FRFont.getInstance().applySize(14));
//            g.drawString(title, 5, 20);
//            g.drawImage(UIConstants.DRAG_BAR, 0, 0, CONTENT_WIDTH, POPUP_TOOLPANE_HEIGHT, null);

            if (buttonType.equals(NO_BUTTON)) {
                return;
            }
            if (buttonType.equals(UP_BUTTON)) {
                if (model == UIConstants.MODEL_NORMAL) {
                    button = UIConstants.DRAG_LEFT_NORMAL;
                } else {
                    button = UIConstants.DRAG_LEFT_PRESS;
                }
            } else {
                if (model == UIConstants.MODEL_NORMAL) {
                    button = UIConstants.DRAG_RIGHT_NORMAL;
                } else {
                    button = UIConstants.DRAG_RIGHT_PRESS;
                }
            }
//                g.drawImage(button, 2, ARROW_MARGIN_VERTICAL, 5, toolPaneHeight, null);
            g.drawImage(button, ARROW_RANGE_START + 12, 7, 5, 5, null);
        }
    }

    private class PopupDialog extends JDialog {
        private Container container;
        private static final int RESIZE_RANGE = 4;
        private Cursor originCursor;
        private Cursor southResizeCursor = Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR);
        private Point mouseDownCompCoords;
        private int minHeight;  // 对话框最小高度
        private JComponent contentPane;
        public PopupDialog(PropertyItem propertyItem) {
            super(DesignerContext.getDesignerFrame());
            container = getContentPane();
            setUndecorated(true);
            PopupToolPane popupToolPane = new PopupToolPane(propertyItem, PopupToolPane.DOWN_BUTTON);
            popupToolPane.setParentDialog(this);
            contentPane = propertyItem.getContentPane();
            container.add(popupToolPane, BorderLayout.NORTH);
            container.add(contentPane, BorderLayout.CENTER);
            minHeight = container.getPreferredSize().height;
            setSize(CONTENT_WIDTH, minHeight);
//            validate();
            Point btnCoords = propertyItem.getButton().getLocationOnScreen();
            this.setLocation(btnCoords.x - CONTENT_WIDTH, btnCoords.y);

            initListener();
            this.setVisible(true);
        }
        public void replaceContentPane(JComponent contentPane) {
            container.remove(this.contentPane);
            container.add(this.contentPane = contentPane);
//            pack();
            if (getSize().height < container.getPreferredSize().height) {
                setSize(CONTENT_WIDTH, container.getPreferredSize().height);
            }
            refreshContainer();
        }

        private void refreshContainer() {
            validate();
            repaint();
            revalidate();
        }

        private void initListener() {
            addMouseMotionListener(new MouseMotionListener() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    if (mouseDownCompCoords != null) {
                        Rectangle bounds = getBounds();
                        Point currCoords = e.getLocationOnScreen();
                        bounds.height = currCoords.y - mouseDownCompCoords.y + bounds.height;
                        // 校正位置
                        if (bounds.height < minHeight) {
                            bounds.height = minHeight;
                        }
                        mouseDownCompCoords.y = currCoords.y;
                        setBounds(bounds);
                    }
                }

                @Override
                public void mouseMoved(MouseEvent e) {
                    if (originCursor == null) {  // 记录最初的光标
                        originCursor = getCursor();
                    }
                    if (e.getY() > getHeight() - RESIZE_RANGE) {
                        setCursor(southResizeCursor);
                    } else {
                        // 还原
                        if (mouseDownCompCoords == null && getCursor().equals(southResizeCursor)) {
                            setCursor(originCursor);
                        }
                    }

                    repaint();
                }
            });
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (getCursor().equals(southResizeCursor)) {
                        mouseDownCompCoords = e.getLocationOnScreen();
                    }
                }
                @Override
                public void mouseReleased(MouseEvent e) {
                    mouseDownCompCoords = null;
                }
            });
        }
    }
}