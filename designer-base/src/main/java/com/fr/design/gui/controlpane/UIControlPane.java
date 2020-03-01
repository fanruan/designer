package com.fr.design.gui.controlpane;

import com.fr.design.constants.UIConstants;
import com.fr.design.dialog.FineJOptionPane;
import com.fr.design.gui.controlpane.shortcutfactory.ShortCutFactory;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itoolbar.UIToolBarUI;
import com.fr.design.gui.itoolbar.UIToolbar;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.menu.ShortCut;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.design.widget.FRWidgetFactory;
import com.fr.invoke.Reflect;
import com.fr.stable.ArrayUtils;
import com.fr.stable.StringUtils;
import com.fr.stable.os.OperatingSystem;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
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
abstract class UIControlPane extends JControlPane {
    private UIToolbar topToolBar;
    protected Window popupEditDialog;
    private static final int TOP_TOOLBAR_HEIGHT = 20;
    private static final int TOP_TOOLBAR_WIDTH = 156;  // 可能因为用了tablelayout，要比其他地方多一个像素，看起来才正常
    private static final int TOP_TOOLBAR_WIDTH_SHORT = 76;

    UIControlPane() {
        super();
    }

    public abstract void saveSettings();

    @Override
    protected void initShortCutFactory() {
        this.shortCutFactory = ShortCutFactory.newInstance(this);
    }

    // 是否使用新样式
    protected boolean isNewStyle() {
        return true;
    }

    protected void initComponentPane() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.creators = this.createNameableCreators();

        initCardPane();
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

    private void getPopupEditDialog(JPanel cardPane) {
        popupEditDialog = new PopupEditDialog(cardPane);
    }

    @Override
    protected void initToolBar() {
        super.initToolBar();
        toolBar.setUI(new UIToolBarUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(Color.WHITE);
                g2.fillRect(0, 0, c.getWidth(), c.getHeight());
            }
        });
    }

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

        initToolBar();

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
        ShortCut addItem = shortCutFactory.addItemShortCut().getShortCut();
        addItem.intoJToolBar(topToolBar);
        JPanel leftTopPane = getLeftTopPane(topToolBar);
        leftTopPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));
        leftPane.add(leftTopPane, BorderLayout.NORTH);

        return leftPane;
    }

    protected JPanel getLeftTopPane(UIToolbar topToolBar) {
        UILabel addItemLabel = FRWidgetFactory.createLineWrapLabel(getAddItemText());

        topToolBar.setPreferredSize(
                new Dimension(
                        isNewStyle() ? TOP_TOOLBAR_WIDTH : TOP_TOOLBAR_WIDTH_SHORT,
                        TOP_TOOLBAR_HEIGHT
                ));
        JPanel toolBarPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        toolBarPane.add(topToolBar, BorderLayout.NORTH);

        JPanel leftTopPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        leftTopPane.add(toolBarPane, BorderLayout.EAST);
        leftTopPane.add(addItemLabel, BorderLayout.CENTER);
        return leftTopPane;
    }

    /**
     * 子类重写此方法，可以改变标签内容
     */
    protected String getAddItemText() {
        return "add item ";
    }

    protected ShortCut4JControlPane[] createShortcuts() {
//        return AbstractShortCutFactory.getInstance(this).createNewShortCuts();
        return shortCutFactory.createShortCuts();
    }

    /**
     * 刷新 NameableCreator
     *
     * @param creators 生成器
     */
    public void refreshNameableCreator(NameableCreator[] creators) {
        super.refreshNameableCreator(creators);

        // 顶部按钮
        topToolBar.removeAll();
        ShortCut addItem = shortCutFactory.addItemShortCut().getShortCut();
        addItem.intoJToolBar(topToolBar);
        topToolBar.validate();
        this.controlUpdatePane = createControlUpdatePane();//REPORT-4841 刷新一下编辑面板
        cardPane.add(controlUpdatePane, "EDIT");
        this.repaint();
    }

    // 点击"编辑"按钮，弹出面板
    class PopupEditDialog extends JDialog {
        private JComponent editPane;
        private PopupToolPane popupToolPane;
        private static final int WIDTH = 570;
        private static final int HEIGHT = 490;

        PopupEditDialog(JComponent pane) {
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

            try {
                //没有指定owner的弹出框用的是SwingUtilities.getSharedOwnerFrame()
                Frame sharedOwnerFrame = Reflect.on(SwingUtilities.class).call("getSharedOwnerFrame").get();
                for (Window window : sharedOwnerFrame.getOwnedWindows()) {
                    if (window instanceof JDialog && window.isVisible() && ((JDialog) window).isModal()) {
                        // 如果有可见模态对话框，则不隐藏
                        return;
                    }
                }
            } catch (Exception ignore) {
                //do nothing
            }

            // 要隐藏 先检查有没有非法输入
            // 非法输入检查放在最后，因为可能出现面板弹出新弹框而失去焦点的情况，比如 输入公式时，弹出公式编辑对话框
            try {
                checkValid();
            } catch (Exception exp) {
                // 存在非法输入 拒绝隐藏
                this.setAlwaysOnTop(true);
                FineJOptionPane.showMessageDialog(this, exp.getMessage());
                this.requestFocus();
                return;
            }
            saveSettings();
            setVisible(false);
        }

        private void initListener() {
            addWindowFocusListener(new WindowAdapter() {
                @Override
                public void windowLostFocus(WindowEvent e) {
                    //在Linux上拉回焦点，不然导致一些面板关不掉
                    if(OperatingSystem.isLinux()) {
                        requestFocus();
                    }
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

        PopupToolPane(JDialog parentDialog) {
            this(StringUtils.EMPTY, parentDialog);
        }

        PopupToolPane(String title, JDialog parentDialog) {
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