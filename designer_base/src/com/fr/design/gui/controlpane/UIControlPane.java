package com.fr.design.gui.controlpane;

import com.fr.base.chart.BasePlot;
import com.fr.design.constants.UIConstants;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itoolbar.UIToolBarUI;
import com.fr.design.gui.itoolbar.UIToolbar;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.menu.ShortCut;
import com.fr.design.menu.ToolBarDef;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.stable.ArrayUtils;
import com.fr.stable.Nameable;
import com.fr.stable.StringUtils;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by plough on 2017/7/21.
 */
public abstract class UIControlPane extends BasicPane implements UnrepeatedNameHelper {
    protected static final int SHORT_WIDTH = 30; //每加一个short Divider位置加30
    protected JPanel controlUpdatePane;
    private ShortCut4JControlPane[] shorts;
    private NameableCreator[] creators;
    private ToolBarDef toolbarDef;
    private UIToolbar toolBar;
    private UIToolbar topToolBar;
    protected Window popupEditDialog;
    // peter:这是整体的一个cardLayout Pane
    protected CardLayout cardLayout;
    protected JPanel cardPane;
    protected BasePlot plot;
    private static final int TOP_TOOLBAR_HEIGHT = 20;
    private static final int TOP_TOOLBAR_WIDTH = 156;  // 可能因为用了tablelayout，要比其他地方多一个像素，看起来才正常
    private static final int TOP_TOOLBAR_WIDTH_SHORT = 76;

    public UIControlPane() {
        this.initComponentPane();
    }

    public UIControlPane(BasePlot plot) {
        this.plot = plot;
        this.initComponentPane();
    }

    /**
     * 生成添加按钮的NameableCreator
     *
     * @return 按钮的NameableCreator
     */
    public abstract NameableCreator[] createNameableCreators();

    public ShortCut4JControlPane[] getShorts() {
        return shorts;
    }

    public void setShorts(ShortCut4JControlPane[] shorts) {
        this.shorts = shorts;
    }

    public void setCreators(NameableCreator[] creators) {
        this.creators = creators;
    }

    public ToolBarDef getToolbarDef() {
        return toolbarDef;
    }

    public void setToolbarDef(ToolBarDef toolbarDef) {
        this.toolbarDef = toolbarDef;
    }

    public UIToolbar getToolBar() {
        return toolBar;
    }

    public void setToolBar(UIToolbar toolBar) {
        this.toolBar = toolBar;
    }

    public CardLayout getCardLayout() {
        return cardLayout;
    }

    public void setCardLayout(CardLayout cardLayout) {
        this.cardLayout = cardLayout;
    }

    public JPanel getCardPane() {
        return cardPane;
    }

    public void setCardPane(JPanel cardPane) {
        this.cardPane = cardPane;
    }

    public abstract void saveSettings();

    // 是否使用新样式
    protected boolean isNewStyle() {
        return true;
    }

    protected void initComponentPane() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.creators = this.createNameableCreators();
        this.controlUpdatePane = createControlUpdatePane();

        // p: edit card layout
        this.cardLayout = new CardLayout();
        cardPane = FRGUIPaneFactory.createCardLayout_S_Pane();
        cardPane.setLayout(this.cardLayout);
        // p:选择的Label
        UILabel selectLabel = new UILabel();
        cardPane.add(selectLabel, "SELECT");
        cardPane.add(controlUpdatePane, "EDIT");
        if (isNewStyle()) {
            getPopupEditDialog(cardPane);
            this.add(getLeftPane(), BorderLayout.CENTER);
            this.setBorder(BorderFactory.createEmptyBorder(10, 10, 15, 10));
        } else {
            // 增加边框
            JPanel leftPaneWrapper = new JPanel(new BorderLayout());
            leftPaneWrapper.add(getLeftPane(), BorderLayout.CENTER);
            leftPaneWrapper.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
            JPanel rightPaneWrapper = new JPanel(new BorderLayout());
            rightPaneWrapper.add(cardPane, BorderLayout.CENTER);
            rightPaneWrapper.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
            // SplitPane
            JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, leftPaneWrapper, rightPaneWrapper);
            mainSplitPane.setBorder(BorderFactory.createLineBorder(GUICoreUtils.getTitleLineBorderColor()));
            mainSplitPane.setOneTouchExpandable(true);
            this.add(mainSplitPane, BorderLayout.CENTER);
            mainSplitPane.setDividerLocation(getLeftPreferredSize());
        }

        this.checkButtonEnabled();
    }

    protected void getPopupEditDialog(JPanel cardPane) {
        popupEditDialog = new PopupEditDialog(cardPane);
    }

    protected abstract JPanel createControlUpdatePane();

    protected JPanel getLeftPane() {
        // LeftPane
        JPanel leftPane = FRGUIPaneFactory.createBorderLayout_S_Pane();

        JPanel leftContentPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        initLeftPane(leftContentPane);
        leftPane.add(leftContentPane, BorderLayout.CENTER);

        shorts = this.createShortcuts();
        if (ArrayUtils.isEmpty(shorts)) {
            return leftPane;
        }

        toolbarDef = new ToolBarDef();
        for (ShortCut4JControlPane sj : shorts) {
            toolbarDef.addShortCut(sj.getShortCut());
        }
        toolBar = ToolBarDef.createJToolBar();
        toolBar.setUI(new UIToolBarUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(Color.WHITE);
                g2.fillRect(0, 0, c.getWidth(), c.getHeight());
            }
        });
        toolbarDef.updateToolBar(toolBar);
        // 封装一层，加边框
        JPanel toolBarPane = new JPanel(new BorderLayout());
        toolBarPane.add(toolBar, BorderLayout.CENTER);
        toolBarPane.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 1, UIConstants.RULER_LINE_COLOR));

        leftContentPane.add(toolBarPane, BorderLayout.NORTH);

        //  顶部标签及add按钮
        topToolBar = new UIToolbar(FlowLayout.LEFT, new UIToolBarUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(UIConstants.SELECT_TAB);
                g2.fillRect(0, 0, c.getWidth(), c.getHeight());
            }
        });
        topToolBar.setBorder(null);
        topToolBar.setLayout(new BorderLayout());
        ShortCut addItem = addItemShortCut().getShortCut();
        addItem.intoJToolBar(topToolBar);

        JPanel leftTopPane = getLeftTopPane(topToolBar);

        leftTopPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));
        leftPane.add(leftTopPane, BorderLayout.NORTH);

        return leftPane;
    }

    protected JPanel getLeftTopPane(UIToolbar topToolBar) {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {p, f, isNewStyle() ? TOP_TOOLBAR_WIDTH : TOP_TOOLBAR_WIDTH_SHORT};
        double[] rowSize = {TOP_TOOLBAR_HEIGHT};
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(getAddItemText()), new JPanel(), topToolBar},
        };
        return TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
    }

    /**
     * 子类重写此方法，可以改变标签内容
     */
    protected String getAddItemText() {
        return "add item ";
    }

    /**
     * 初始化左边面板
     */
    protected void initLeftPane(JPanel leftPane) {

    }

    protected int getLeftPreferredSize() {
        return shorts.length * SHORT_WIDTH;
    }


    protected ShortCut4JControlPane[] createShortcuts() {
        return new ShortCut4JControlPane[]{
                copyItemShortCut(),
                moveUpItemShortCut(),
                moveDownItemShortCut(),
                sortItemShortCut(),
                removeItemShortCut()
        };
    }

    protected abstract ShortCut4JControlPane addItemShortCut();

    protected abstract ShortCut4JControlPane removeItemShortCut();

    protected abstract ShortCut4JControlPane copyItemShortCut();

    protected abstract ShortCut4JControlPane moveUpItemShortCut();

    protected abstract ShortCut4JControlPane moveDownItemShortCut();

    protected abstract ShortCut4JControlPane sortItemShortCut();

    public abstract Nameable[] update();


    public void populate(Nameable[] nameableArray) {
    }

    /**
     * 检查按钮可用状态 Check button enabled.
     */
    public void checkButtonEnabled() {
    }

    protected void doBeforeRemove() {
    }

    protected void doAfterRemove() {
    }

    public NameableCreator[] creators() {
        return creators == null ? new NameableCreator[0] : creators;
    }

    protected abstract boolean hasInvalid(boolean isAdd);

    /**
     * 刷新 NameableCreator
     *
     * @param creators 生成器
     */
    public void refreshNameableCreator(NameableCreator[] creators) {
        this.creators = creators;
        shorts = this.createShortcuts();
        toolbarDef.clearShortCuts();
        for (ShortCut4JControlPane sj : shorts) {
            toolbarDef.addShortCut(sj.getShortCut());
        }

        toolbarDef.updateToolBar(toolBar);
        toolBar.validate();
        toolBar.repaint();


        // 顶部按钮
        topToolBar.removeAll();
        ShortCut addItem = addItemShortCut().getShortCut();
        addItem.intoJToolBar(topToolBar);
        topToolBar.validate();
        this.controlUpdatePane = createControlUpdatePane();//REPORT-4841 刷新一下编辑面板
        cardPane.add(controlUpdatePane, "EDIT");

        this.repaint();
    }

    // 点击"编辑"按钮，弹出面板
    protected class PopupEditDialog extends JDialog {
        private JComponent editPane;
        private PopupToolPane popupToolPane;
        private static final int WIDTH = 570;
        private static final int HEIGHT = 490;

        public PopupEditDialog(JComponent pane) {
            super(DesignerContext.getDesignerFrame());
            setUndecorated(true);
            pane.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
            this.editPane = pane;
            JPanel editPaneWrapper = new JPanel(new BorderLayout());
            popupToolPane = new PopupToolPane(this);
            editPaneWrapper.add(popupToolPane, BorderLayout.NORTH);
            editPaneWrapper.add(editPane, BorderLayout.CENTER);
            editPaneWrapper.setBorder(BorderFactory.createLineBorder(UIConstants.POP_DIALOG_BORDER, 1));
            this.getContentPane().add(editPaneWrapper, BorderLayout.CENTER);
            setSize(WIDTH, HEIGHT);
//            pack();
            this.setVisible(false);
            initListener();
        }

        @Override
        public void setTitle(String title) {
            popupToolPane.setTitle(title);
        }

        private void hideDialog() {
            // 要隐藏 先检查有没有非法输入
            try {
                checkValid();
            } catch (Exception exp) {
                // 存在非法输入 拒绝隐藏
                JOptionPane.showMessageDialog(UIControlPane.this, exp.getMessage());
                this.requestFocus();
                return;
            }
            // 检查是否有子弹窗，如果有，则不隐藏
            for (Window window : getOwnedWindows()) {
                if (window.isVisible()) {
                    return;
                }
            }
            // 如果有可见模态对话框，则不隐藏
            for (Window window : DesignerContext.getDesignerFrame().getOwnedWindows()) {
                if (window instanceof JDialog && window.isVisible() && ((JDialog) window).isModal()) {
                    return;
                }
            }
            saveSettings();
            setVisible(false);
        }

        private void initListener() {
            addWindowFocusListener(new WindowAdapter() {
                @Override
                public void windowLostFocus(WindowEvent e) {
                    hideDialog();
                }
            });
        }
    }

    // 移动弹出编辑面板的工具条
    private class PopupToolPane extends JPanel {
        private JDialog parentDialog;  // 如果不在对话框中，值为null
        private Color originColor;  // 初始背景
        private JPanel contentPane;
        private UILabel titleLabel;
        private Point mouseDownCompCoords;  // 存储按下左键的位置，移动对话框时会用到

        private static final int MIN_X = -150;
        private static final int MAX_X_SHIFT = 50;
        private static final int MAX_Y_SHIFT = 50;

        private MouseListener mouseListener = new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(Cursor.getDefaultCursor());
                if (mouseDownCompCoords == null) {
                    contentPane.setBackground(originColor);
                }
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mouseDownCompCoords = null;
                if (!getBounds().contains(e.getPoint())) {
                    contentPane.setBackground(originColor);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                mouseDownCompCoords = e.getPoint();
            }
        };

        private MouseMotionListener mouseMotionListener = new MouseMotionListener() {
            @Override
            public void mouseMoved(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                contentPane.setBackground(UIConstants.POPUP_TITLE_BACKGROUND);
                repaint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (mouseDownCompCoords != null) {
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
        };

        public PopupToolPane(JDialog parentDialog) {
            this(StringUtils.EMPTY, parentDialog);
        }

        public PopupToolPane(String title, JDialog parentDialog) {
            super();
            this.parentDialog = parentDialog;
            originColor = UIConstants.DIALOG_TITLEBAR_BACKGROUND;

            contentPane = new JPanel();
            contentPane.setBackground(originColor);
            contentPane.setLayout(new BorderLayout());
            titleLabel = new UILabel(title);
            Font font = new Font("SimSun", Font.PLAIN, 12);
            titleLabel.setFont(font);
            contentPane.add(titleLabel, BorderLayout.WEST);
            contentPane.setBorder(new EmptyBorder(5, 14, 6, 0));

            setLayout(new BorderLayout());
            add(contentPane, BorderLayout.CENTER);
            setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UIConstants.TOOLBAR_BORDER_COLOR));

            addMouseListener(mouseListener);
            addMouseMotionListener(mouseMotionListener);
        }

        public void setTitle(String title) {
            titleLabel.setText(title);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(super.getPreferredSize().width, 28);
        }
    }
}