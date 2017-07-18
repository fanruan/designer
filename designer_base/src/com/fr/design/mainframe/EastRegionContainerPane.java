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
import com.fr.general.Inter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class EastRegionContainerPane extends UIEastResizableContainer {
    private static EastRegionContainerPane THIS;
    private Map<String, PropertyItem> propertyItemMap;
    private CardLayout propertyCard;
    private JPanel leftPane;
    private JPanel rightPane;
    private static final int CONTAINER_WIDTH = 260;
    private static final int TAB_WIDTH = 40;
    private static final int CONTENT_WIDTH = CONTAINER_WIDTH - TAB_WIDTH;
    private static final int POPUP_TOOLPANE_HEIGHT = 25;
    private static final int ARROW_RANGE_START = CONTENT_WIDTH - 30;
    private static final String KEY_CELL_ELEMENT = "cellElement";
    private static final String KEY_CELL_ATTR = "cellAttr";
    private static final String KEY_FLOAT_ELEMENT = "floatElement";
    private static final String KEY_WIDGET_SETTINGS = "widgetSettings";
    private static final String KEY_CONDITION_ATTR = "conditionAttr";
    private static final String KEY_HYPERLINK = "hyperlink";
    private static final String KEY_WIDGET_LIB = "widgetLib";
    private static final String KEY_AUTHORITY_EDITION = "authorityEdition";
    private static final String KEY_CONFIGURED_ROLES = "editedRoles";
    private static final String DEFAULT_PANE = "defaultPane";  // "无可用配置项"面板

    public enum PropertyMode {
        REPORT,  // 报表
        REPORT_PARA,  // 报表参数面板
        REPORT_FLOAT,  // 报表悬浮元素
        FORM,  // 表单
        FORM_REPORT,  // 表单报表块
        POLY,  // 聚合报表
        POLY_REPORT,  // 聚合报表-报表块
        POLY_CHART,  // 聚合报表-图表块
        AUTHORITY_EDITION  // 权限编辑
    }
    private PropertyMode currentMode;  // 当前模式（根据不同模式，显示不同的可用面板）


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
        switchMode(PropertyMode.REPORT);
//        initContentPane();
//        super(leftPane, rightPane);
        setContainerWidth(CONTAINER_WIDTH);
    }

    private void initPropertyItemList() {
        propertyItemMap = new LinkedHashMap<>();  // 有序map
        // 单元格元素
        PropertyItem cellElement = new PropertyItem(KEY_CELL_ELEMENT, Inter.getLocText("FR-Designer_Cell_Element"),
                "/com/fr/design/images/buttonicon/add.png", new PropertyMode[]{PropertyMode.REPORT, PropertyMode.REPORT_PARA, PropertyMode.REPORT_FLOAT, PropertyMode.POLY, PropertyMode.POLY_CHART},
                new PropertyMode[]{PropertyMode.REPORT, PropertyMode.FORM_REPORT, PropertyMode.POLY_REPORT});
        // 单元格属性
        PropertyItem cellAttr = new PropertyItem(KEY_CELL_ATTR, Inter.getLocText("FR-Designer_Cell_Attributes"),
                "/com/fr/design/images/buttonicon/add.png", new PropertyMode[]{PropertyMode.REPORT, PropertyMode.REPORT_PARA, PropertyMode.REPORT_FLOAT, PropertyMode.POLY, PropertyMode.POLY_CHART},
                new PropertyMode[]{PropertyMode.REPORT, PropertyMode.FORM_REPORT, PropertyMode.POLY_REPORT});
        // 悬浮元素
        PropertyItem floatElement = new PropertyItem(KEY_FLOAT_ELEMENT, Inter.getLocText("FR-Designer_Float_Element"),
                "/com/fr/design/images/buttonicon/add.png", new PropertyMode[]{PropertyMode.REPORT, PropertyMode.REPORT_PARA, PropertyMode.REPORT_FLOAT, PropertyMode.POLY, PropertyMode.POLY_CHART},
                new PropertyMode[]{PropertyMode.REPORT, PropertyMode.REPORT_FLOAT, PropertyMode.POLY_REPORT});
        // 控件设置
        PropertyItem widgetSettings = new PropertyItem(KEY_WIDGET_SETTINGS, Inter.getLocText("FR-Designer-Widget_Settings"),
                "/com/fr/design/images/buttonicon/add.png", new PropertyMode[]{PropertyMode.REPORT, PropertyMode.REPORT_PARA, PropertyMode.REPORT_FLOAT, PropertyMode.FORM, PropertyMode.POLY},
                new PropertyMode[]{PropertyMode.REPORT, PropertyMode.REPORT_PARA, PropertyMode.FORM, PropertyMode.POLY_REPORT, PropertyMode.POLY_CHART});
        // 条件属性
        PropertyItem conditionAttr = new PropertyItem(KEY_CONDITION_ATTR, Inter.getLocText("FR-Designer_Condition_Attributes"),
                "/com/fr/design/images/buttonicon/add.png", new PropertyMode[]{PropertyMode.REPORT, PropertyMode.REPORT_PARA, PropertyMode.REPORT_FLOAT, PropertyMode.POLY, PropertyMode.POLY_CHART},
                new PropertyMode[]{PropertyMode.REPORT, PropertyMode.FORM_REPORT, PropertyMode.POLY_REPORT});
        // 超级链接
        PropertyItem hyperlink = new PropertyItem(KEY_HYPERLINK, Inter.getLocText("FR-Designer_Hyperlink"),
                "/com/fr/design/images/buttonicon/add.png", new PropertyMode[]{PropertyMode.REPORT, PropertyMode.REPORT_PARA, PropertyMode.REPORT_FLOAT, PropertyMode.POLY, PropertyMode.POLY_CHART},
                new PropertyMode[]{PropertyMode.REPORT, PropertyMode.FORM_REPORT, PropertyMode.POLY_REPORT});
        // 组件库
        PropertyItem widgetLib = new PropertyItem(KEY_WIDGET_LIB, Inter.getLocText("FR-Designer_Widget_Library"),
                "/com/fr/design/images/buttonicon/add.png", new PropertyMode[]{PropertyMode.FORM},
                new PropertyMode[]{PropertyMode.FORM});
        // 权限编辑
        PropertyItem authorityEdition = new PropertyItem(KEY_AUTHORITY_EDITION, Inter.getLocText("FR-Designer_Permissions_Edition"),
                "/com/fr/design/images/buttonicon/add.png", new PropertyMode[]{PropertyMode.AUTHORITY_EDITION},
                new PropertyMode[]{PropertyMode.AUTHORITY_EDITION});
        // 已配置角色
        PropertyItem configuredRoles = new PropertyItem(KEY_CONFIGURED_ROLES, Inter.getLocText("FR-Designer_Configured_Roles"),
                "/com/fr/design/images/buttonicon/add.png", new PropertyMode[]{PropertyMode.AUTHORITY_EDITION},
                new PropertyMode[]{PropertyMode.AUTHORITY_EDITION});

        propertyItemMap.put(KEY_CELL_ELEMENT, cellElement);
        propertyItemMap.put(KEY_CELL_ATTR, cellAttr);
        propertyItemMap.put(KEY_FLOAT_ELEMENT, floatElement);
        propertyItemMap.put(KEY_WIDGET_SETTINGS, widgetSettings);
        propertyItemMap.put(KEY_CONDITION_ATTR, conditionAttr);
        propertyItemMap.put(KEY_HYPERLINK, hyperlink);
        propertyItemMap.put(KEY_WIDGET_LIB, widgetLib);
        propertyItemMap.put(KEY_AUTHORITY_EDITION, authorityEdition);
        propertyItemMap.put(KEY_CONFIGURED_ROLES, configuredRoles);
    }

    // "无可用配置项"面板
    private JPanel getDefaultPane() {
        JPanel defaultPane = new JPanel();
        UILabel label = new UILabel(Inter.getLocText("FR-Designer_No_Settings_Available"));
        defaultPane.setLayout(new BorderLayout());
        defaultPane.add(label, BorderLayout.CENTER);
        return defaultPane;
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
        for (PropertyItem item : propertyItemMap.values()) {
            if (item.isPoppedOut() || !item.isVisible()) {
                continue;
            }
            rightPane.add(item.getName(), item.getPropertyPanel());
        }
        rightPane.add(DEFAULT_PANE, getDefaultPane());

        replaceRightPane(rightPane);
        refreshRightPane();
    }

    // 左侧按钮面板
    private void initLeftPane() {
        leftPane = new JPanel();
        leftPane.setLayout(new VerticalFlowLayout(VerticalFlowLayout.TOP, 0, 0));
        for (PropertyItem item : propertyItemMap.values()) {
            if (item.isPoppedOut() || !item.isVisible()) {
                continue;
            }
            leftPane.add(item.getButton());
        }

//        leftPane.setLayout(new BoxLayout(leftPane, BoxLayout.Y_AXIS));
        leftPane.setBackground(Color.yellow);
        replaceLeftPane(leftPane);
    }

    public void switchMode(PropertyMode mode) {
        if (currentMode != null && currentMode.equals(mode)) {
            return;
        }
        currentMode = mode;
        updateAllPropertyPane();
    }

    public void updateAllPropertyPane() {
        updatePropertyItemMap();
        initContentPane();
    }

    private void updatePropertyItemMap() {
        for (PropertyItem item : propertyItemMap.values()) {
            item.updateStatus();
        }
    }

    // 弹出面板时，更新框架内容
    private void removeItem(PropertyItem propertyItem) {
        leftPane.remove(propertyItem.getButton());
        rightPane.remove(propertyItem.getPropertyPanel());
        refreshRightPane();
        refreshContainer();
    }

    @Override
    public void onResize() {
        for (PropertyItem item : propertyItemMap.values()) {
            item.onResize();
        }
    }

    public EastRegionContainerPane(JPanel leftPane, JPanel rightPane) {
        super(leftPane, rightPane);
//        setVerticalDragEnabled(false);
//        setContainerWidth(260);
    }

    public void replaceUpPane(JComponent pane) {
        replaceCellElementPane(pane);
    }

    public void replaceDownPane(JComponent pane) {
        replaceCellAttrPane(pane);
    }

    public JComponent getUpPane() {
        return getCellElementPane();
    }

    public JComponent getDownPane() {
        return getCellAttrPane();
    }

    public void replaceCellElementPane(JComponent pane) {
        propertyItemMap.get(KEY_CELL_ELEMENT).replaceContentPane(pane);
    }

    public JComponent getCellElementPane() {
        return propertyItemMap.get(KEY_CELL_ELEMENT).getContentPane();
    }

    public void replaceCellAttrPane(JComponent pane) {
        propertyItemMap.get(KEY_CELL_ATTR).replaceContentPane(pane);
    }

    public JComponent getCellAttrPane() {
        return propertyItemMap.get(KEY_CELL_ATTR).getContentPane();
    }

    public void replaceFloatElementPane(JComponent pane) {
        propertyItemMap.get(KEY_FLOAT_ELEMENT).replaceContentPane(pane);
    }

    public JComponent getFloatElementPane() {
        return propertyItemMap.get(KEY_FLOAT_ELEMENT).getContentPane();
    }

    public void replaceWidgetSettingsPane(JComponent pane) {
        propertyItemMap.get(KEY_WIDGET_SETTINGS).replaceContentPane(pane);
    }

    public JComponent getWidgetSettingsPane() {
        return propertyItemMap.get(KEY_WIDGET_SETTINGS).getContentPane();
    }

    public void replaceWidgetLibPane(JComponent pane) {
        propertyItemMap.get(KEY_WIDGET_LIB).replaceContentPane(pane);
    }

    public JComponent getWidgetLibPane() {
        return propertyItemMap.get(KEY_WIDGET_LIB).getContentPane();
    }

    public void replaceAuthorityEditionPane(JComponent pane) {
        propertyItemMap.get(KEY_AUTHORITY_EDITION).replaceContentPane(pane);
    }

    public JComponent getAuthorityEditionPane() {
        return propertyItemMap.get(KEY_AUTHORITY_EDITION).getContentPane();
    }

    public void replaceConfiguredRolesPane(JComponent pane) {
        propertyItemMap.get(KEY_CONFIGURED_ROLES).replaceContentPane(pane);
    }

    public JComponent getConfiguredRolesPane() {
        return propertyItemMap.get(KEY_CONFIGURED_ROLES).getContentPane();
    }

    public void addParameterPane(JComponent paraPane) {
//        propertyItemMap.get(KEY_HYPERLINK).replaceContentPane(paraPane);
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
        boolean hasAvailableTab = false;
        for (String name : propertyItemMap.keySet()) {
            PropertyItem propertyItem = propertyItemMap.get(name);
            if (propertyItem.isVisible() && !propertyItem.isPoppedOut() && propertyItem.isEnabled()) {
                propertyCard.show(rightPane, name);  // 显示第一个可用tab
                hasAvailableTab = true;
                break;
            }
        }
        if (!hasAvailableTab) {
            propertyCard.show(rightPane, DEFAULT_PANE);
        }

//        if (this.getRightPane() instanceof DockingView) {
//            ((DockingView) this.getRightPane()).refreshDockingView();
//        }
    }

    public void refreshDownPane() {
//        JComponent pane = propertyItemList.get(1).getContentPane();
//        if (pane instanceof DockingView) {
//            ((DockingView) pane).refreshDockingView();
//        }
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
        private UIButton button;
        private String name;  // 用于 card 切换
        private String title;  // 用于显示
        private JComponent propertyPanel;
        private JComponent contentPane;
        private FixedPopupPane popupPane;  // 左侧固定弹出框
        private PopupToolPane popupToolPane;  // 弹出工具条
        private PopupDialog popupDialog;  // 弹出框
        private boolean isPoppedOut = false;  // 是否弹出
        private boolean isVisible = true;  // 是否可见
        private boolean isEnabled = true;  // 是否可用
        private Set<PropertyMode> visibleModes;
        private Set<PropertyMode> enableModes;

        public PropertyItem(String name, String title, String btnUrl, PropertyMode[] visibleModes, PropertyMode[] enableModes) {
            this.name = name;
            this.title = title;
            initButton(btnUrl);
            initPropertyPanel();
//            this.visibleModes = new ArrayList<PropertyMode>(visibleModes);
            initModes(visibleModes, enableModes);
        }

        private void initModes(PropertyMode[] visibleModes, PropertyMode[] enableModes) {
            this.enableModes = new HashSet<>();
            this.visibleModes = new HashSet<>();
            for (PropertyMode enableMode : enableModes) {
                this.enableModes.add(enableMode);
            }
            for (PropertyMode visibleMode : visibleModes) {
                this.visibleModes.add(visibleMode);
            }
            this.visibleModes.addAll(this.enableModes);  // 可用必可见
        }

        public void updateStatus() {
            setEnabled(enableModes.contains(currentMode));
            setVisible(visibleModes.contains(currentMode));
        }

        public boolean isVisible() {
            return isVisible;
        }

        public void setVisible(boolean isVisible) {
            this.isVisible = isVisible;
        }

        public boolean isEnabled() {
            return isEnabled;
        }

        // 选项不可用
        public void setEnabled(boolean isEnabled) {
            this.isEnabled = isEnabled;
            button.setEnabled(isEnabled);
        }

        private void initPropertyPanel() {
            propertyPanel = new JPanel();
            propertyPanel.setBackground(Color.pink);
            contentPane = generateContentPane();
            popupToolPane = new PopupToolPane(this, PopupToolPane.DOWN_BUTTON);
            propertyPanel.setLayout(new BorderLayout());
            propertyPanel.add(popupToolPane, BorderLayout.NORTH);
            propertyPanel.add(contentPane, BorderLayout.CENTER);
        }

        public boolean isPoppedOut() {
            return isPoppedOut;
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

        public String getTitle() {
            return title;
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
        private String title;
        private JComponent contentPane;
        private PropertyItem propertyItem;
        private String buttonType;
        private JDialog parentDialog;  // 如果不在对话框中，值为null
        private Color originColor;  // 初始背景
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
            this.title = propertyItem.getTitle();
            this.contentPane = propertyItem.getContentPane();
            originColor = getBackground();
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

            if (buttonType.equals(DOWN_BUTTON)) {

            } else if (buttonType.equals(UP_BUTTON)) {

            } else {
                throw new IllegalArgumentException("unknown button type: " + buttonType);
            }

            addMouseMotionListener(new MouseMotionListener() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    if (e.getX() >= ARROW_RANGE_START) {
                        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                        model = UIConstants.MODEL_PRESS;
                    } else if (isMovable) {
                        setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                        setBackground(Color.pink);
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
                    if (mouseDownCompCoords == null) {
                        setBackground(originColor);
                    }
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
                    if (!getBounds().contains(e.getPoint())) {
                        setBackground(originColor);
                    }
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
            if (buttonType.equals(DOWN_BUTTON)) {
                propertyItem.popupDialog();
            } else if (buttonType.equals(UP_BUTTON)) {
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
            if (buttonType.equals(DOWN_BUTTON)) {
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
            PopupToolPane popupToolPane = new PopupToolPane(propertyItem, PopupToolPane.UP_BUTTON);
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