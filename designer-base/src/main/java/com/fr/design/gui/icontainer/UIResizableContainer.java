package com.fr.design.gui.icontainer;

import com.fr.base.BaseUtils;
import com.fr.base.vcs.DesignerMode;
import com.fr.design.constants.UIConstants;
import com.fr.design.mainframe.DesignerContext;
import com.fr.stable.Constants;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.stable.collections.utils.MathUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class UIResizableContainer extends JPanel {
    private static final long serialVersionUID = 1854340560790476907L;
    private static final int MAX_PARA_HEIGHT = 240;
    private int containerWidth = 240;
    private int preferredWidth = 240;
    private int toolPaneY = 300;
    private int toolPaneHeight = 10;
    private int bottomHeight = 30;

    private JComponent upPane;
    private JComponent downPane;
    //放参数面板
    private JComponent parameterPane = new JPanel();

    private HorizotalToolPane horizontToolPane;
    private VerticalToolPane verticalToolPane;

    private int direction;
    private boolean hasParameterPane;

    private static final int MAX_WIDTH = 300;
    private static final int MIN_WIDTH = 165;

    private static final int ARROW_MARGIN = 15;
    private static final int ARROW_MARGIN_VERTICAL = 7;
    private static final int ARROW_RANGE = 35;
    private static final int ARROW_RANGE_VERTICAL = 25;

    private boolean isLeftRightDragEnabled = true;
    private boolean isDownPaneVisible = true ;
    private int paraHeight;

    public UIResizableContainer(int direction) {
        this(new JPanel(), new JPanel(), direction);
    }

    /**
     * 设置是否允许拖拽
     *
     * @param isEnabled
     */
    public void setVerticalDragEnabled(boolean isEnabled) {
        isLeftRightDragEnabled = isEnabled;
    }

    /**
     * 设置面板宽度
     *
     * @param width
     */
    public void setContainerWidth(int width) {
        this.containerWidth = width;
        this.preferredWidth = width;
    }


    public void setDownPaneVisible(boolean isVisible){
        this.isDownPaneVisible = isVisible;
    }

    private void setPreferredWidth(int width) {
        this.preferredWidth = width;
    }

    public UIResizableContainer(JComponent upPane, JComponent downPane, int direction) {
        setBackground(UIConstants.NORMAL_BACKGROUND);

        this.upPane = upPane;
        this.direction = direction;
        this.downPane = downPane;

        this.horizontToolPane = new HorizotalToolPane();
        this.verticalToolPane = new VerticalToolPane();

        setLayout(containerLayout);
        add(upPane);
        add(horizontToolPane);
        add(downPane);
        add(verticalToolPane);
    }

    public UIResizableContainer(JComponent upPane, int direction) {
        setBackground(UIConstants.NORMAL_BACKGROUND);

        this.upPane = upPane;
        this.direction = direction;

        this.horizontToolPane = new HorizotalToolPane();
        setLayout(containerLayout);
        add(upPane);
        add(horizontToolPane);

    }

    public void setDownPane(JComponent downPane) {
        if (this.downPane != null){
            return;
        }
        this.verticalToolPane = new VerticalToolPane();
        this.downPane = downPane;
        add(downPane);
        add(verticalToolPane);
    }

    /**
     * 将面板设置成最佳的宽度
     */
    public void setWindow2PreferWidth() {
        if (containerWidth == toolPaneHeight) {
            containerWidth = preferredWidth;
            refreshContainer();
        }
    }

    /**
     * 获取参数面板高度
     */
    public int getParameterPaneHeight() {

        return paraHeight;

    }

    /**
     * 设置参数面板高度
     *
     * @param height
     */
    public void setParameterHeight(int height) {
        paraHeight = hasParameterPane? Math.min(height, MAX_PARA_HEIGHT) : 0;
        refreshContainer();

    }

    /**
     * 得到上下子面板的高度
     *
     * @return
     */
    public int getToolPaneY() {
        return this.toolPaneY;
    }

    /**
     * 设置关闭设计器前最后一次上下子面板的高度
     *
     * @param toolPaneY
     */
    public void setLastToolPaneY(int toolPaneY) {
        this.toolPaneY = toolPaneY;
    }

    /**
     * 得到容器的宽度
     *
     * @return
     */
    public int getContainerWidth() {
        return this.containerWidth;
    }

    /**
     * 设置关闭设计器前最后一次面板的宽度
     *
     * @param containerWidth
     */
    public void setLastContainerWidth(int containerWidth) {
        this.containerWidth = containerWidth;
    }

    private LayoutManager containerLayout = new LayoutManager() {

        @Override
        public void removeLayoutComponent(Component comp) {
            // TODO Auto-generated method stub

        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            return parent.getPreferredSize();
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            return null;
        }

        @Override
        public void layoutContainer(Container parent) {
            if (verticalToolPane == null || downPane == null) {
                return;
            }

            if (direction == Constants.RIGHT) {
                if(isDownPaneVisible){
                    upPane.setBounds(0, 0, containerWidth - toolPaneHeight, toolPaneY);
                    horizontToolPane.setBounds(0, toolPaneY, containerWidth - toolPaneHeight, toolPaneHeight);
                    downPane.setBounds(0, toolPaneY + toolPaneHeight, containerWidth - toolPaneHeight, parent.getHeight() - toolPaneY - toolPaneHeight - bottomHeight);
                    verticalToolPane.setBounds(containerWidth - toolPaneHeight, 0, toolPaneHeight, getHeight());
                }else{
                    upPane.setBounds(0, 0, containerWidth - toolPaneHeight, getHeight());
                    verticalToolPane.setBounds(containerWidth - toolPaneHeight, 0, toolPaneHeight, getHeight());
                }
            } else if (direction == Constants.LEFT) {
                if(isDownPaneVisible){
                    if (toolPaneY > getHeight() - toolPaneHeight - getParameterPaneHeight()) {
                        toolPaneY = getHeight() - toolPaneHeight - getParameterPaneHeight();
                    }
                    parameterPane.setBounds(20, 0, 230, getParameterPaneHeight());
                    upPane.setBounds(toolPaneHeight, getParameterPaneHeight(), containerWidth - toolPaneHeight, toolPaneY);
                    horizontToolPane.setBounds(toolPaneHeight, toolPaneY + getParameterPaneHeight(), containerWidth - toolPaneHeight, toolPaneHeight);
                    downPane.setBounds(toolPaneHeight, toolPaneY + toolPaneHeight + getParameterPaneHeight(), containerWidth - toolPaneHeight, parent.getHeight() - toolPaneY - toolPaneHeight - getParameterPaneHeight());
                    verticalToolPane.setBounds(0, 0, toolPaneHeight, getHeight());
                } else {
                    parameterPane.setBounds(20, 0, 230, getParameterPaneHeight());
                    upPane.setBounds(toolPaneHeight, getParameterPaneHeight(), containerWidth - toolPaneHeight, getHeight() - getParameterPaneHeight());
                    verticalToolPane.setBounds(0, 0, toolPaneHeight, getHeight());
                }
            }
        }

        @Override
        public void addLayoutComponent(String name, Component comp) {
        }
    };

    @Override
    /**
     * 得到最佳大小
     */
    public Dimension getPreferredSize() {
        return new Dimension(containerWidth, 400);
    }

    /**
     * 替换上子面板
     *
     * @param pane 面板
     */
    public void replaceUpPane(JComponent pane) {
        remove(pane);
        remove(this.upPane);
        add(this.upPane = pane);
        refreshContainer();
    }


    /**
     * 替换下子面板
     *
     * @param pane 面板
     */
    public void replaceDownPane(JComponent pane) {
        remove(pane);
        remove(this.downPane);
        add(this.downPane = pane);
        refreshContainer();
    }

    public void addParameterPane(JComponent pane) {
        add(this.parameterPane = pane);
        hasParameterPane = true;
        refreshContainer();
    }

    public void removeParameterPane() {
        remove(this.parameterPane);
        setParameterHeight(0);
        hasParameterPane = false;
        refreshContainer();

    }

    /**
     * 得到上子面板
     *
     * @return
     */
    public JComponent getUpPane() {
        return this.upPane;
    }

    /**
     * 得到下子面板
     *
     * @return
     */
    public JComponent getDownPane() {
        return this.downPane;
    }

    /**
     * 得到参数面板
     *
     * @return
     */
    public JComponent getParameterPane() {
        return this.parameterPane;
    }

    /**
     * 刷新下面板
     */
    public void refreshDownPane() {

    }

    private void refreshContainer() {
        validate();
        repaint();
        revalidate();
    }


    private class HorizotalToolPane extends JPanel {
        private int upModel = UIConstants.MODEL_NORMAL;
        private int downModel = UIConstants.MODEL_NORMAL;

        public HorizotalToolPane() {
            super();
            addMouseMotionListener(new MouseMotionListener() {

                @Override
                public void mouseMoved(MouseEvent e) {
                    if (e.getX() <= ARROW_RANGE) {
                        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                        upModel = UIConstants.MODEL_PRESS;
                    } else if (e.getX() >= getWidth() - ARROW_RANGE) {
                        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                        downModel = UIConstants.MODEL_PRESS;
                    } else {
                        resetModel();
                        setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
                    }
                    repaint();
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    toolPaneY = e.getYOnScreen() - UIResizableContainer.this.getLocationOnScreen().y;
                    toolPaneY = toolPaneY < 0 ? 0 : toolPaneY;
                    toolPaneY = toolPaneY > UIResizableContainer.this.getHeight() - toolPaneHeight ? UIResizableContainer.this.getHeight() - toolPaneHeight - getParameterPaneHeight() : toolPaneY - getParameterPaneHeight();
                    refreshContainer();
                }
            });

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    setCursor(Cursor.getDefaultCursor());
                    resetModel();
                    repaint();
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getX() <= ARROW_RANGE) {
                        toolPaneY = 0;
                    } else if (e.getX() >= getWidth() - ARROW_RANGE) {
                        toolPaneY = UIResizableContainer.this.getHeight() - toolPaneHeight - getParameterPaneHeight();
                    } else {
                        return;
                    }
                    refreshContainer();
                }
            });
        }

        private void resetModel() {
            upModel = UIConstants.MODEL_NORMAL;
            downModel = UIConstants.MODEL_NORMAL;
        }

        @Override
        public void paint(Graphics g) {
            Image upButton = (upModel == UIConstants.MODEL_NORMAL ? UIConstants.DRAG_UP_NORMAL : UIConstants.DRAG_UP_PRESS);
            Image downButton = (downModel == UIConstants.MODEL_NORMAL ? UIConstants.DRAG_DOWN_NORMAL : UIConstants.DRAG_DOWN_PRESS);

            g.drawImage(UIConstants.DRAG_BAR_LIGHT, 0, 0, getWidth(), getHeight(), null);
            g.drawImage(UIConstants.DRAG_LINE, (getWidth() - toolPaneHeight) / 2, 3, toolPaneHeight, 5, null);
            g.drawImage(upButton, ARROW_MARGIN, 3, toolPaneHeight, 5, null);
            g.drawImage(downButton, getWidth() - toolPaneHeight - ARROW_MARGIN, 3, toolPaneHeight, 5, null);
        }
    }

    private class VerticalToolPane extends JPanel {
        private int model = UIConstants.MODEL_NORMAL;

        public VerticalToolPane() {
            super();
            addMouseMotionListener(new MouseMotionListener() {

                @Override
                public void mouseMoved(MouseEvent e) {
                    if (e.getY() <= ARROW_RANGE_VERTICAL) {
                        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                        model = UIConstants.MODEL_PRESS;
                    } else if (isLeftRightDragEnabled) {
                        setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
                    }
                    repaint();
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    if (!isLeftRightDragEnabled) {
                        return;
                    }
                    upPane.setVisible(true);
                    downPane.setVisible(true);
                    if (direction == Constants.RIGHT) {
                        containerWidth = e.getXOnScreen() - UIResizableContainer.this.getLocationOnScreen().x;
                    } else if (direction == Constants.LEFT) {
                        containerWidth = UIResizableContainer.this.getWidth() + (UIResizableContainer.this.getLocationOnScreen().x - e.getXOnScreen());
                    }

                    containerWidth = containerWidth > MAX_WIDTH ? MAX_WIDTH : containerWidth;
                    containerWidth = containerWidth < MIN_WIDTH ? MIN_WIDTH : containerWidth;
                    refreshContainer();
                    if (DesignerMode.isAuthorityEditing()) {
                        DesignerContext.getDesignerFrame().doResize();
                    }


                }
            });
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (!isLeftRightDragEnabled) {
                        return;
                    }
                    setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    setCursor(Cursor.getDefaultCursor());
                    model = UIConstants.MODEL_NORMAL;
                    repaint();
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getY() <= ARROW_RANGE_VERTICAL) {
                        if (containerWidth == toolPaneHeight) {
                            containerWidth = preferredWidth;
                        } else {
                            setPreferredWidth(containerWidth);
                            containerWidth = toolPaneHeight;
                        }
                        refreshContainer();
                        if (DesignerMode.isAuthorityEditing()) {
                            DesignerContext.getDesignerFrame().doResize();
                        }
                    }
                }
            });
        }

        @Override
        public void paint(Graphics g) {
            Image button;
            if (direction == Constants.RIGHT) {
                g.drawImage(UIConstants.DRAG_BAR_LIGHT, 0, 0, toolPaneHeight, getHeight(), null);
                if (containerWidth == toolPaneHeight) {
                    if (model == UIConstants.MODEL_NORMAL) {
                        button = UIConstants.DRAG_RIGHT_NORMAL;
                    } else {
                        button = UIConstants.DRAG_RIGHT_PRESS;
                    }
                } else {
                    if (model == UIConstants.MODEL_NORMAL) {
                        button = UIConstants.DRAG_LEFT_NORMAL;
                    } else {
                        button = UIConstants.DRAG_LEFT_PRESS;
                    }
                }
                g.drawImage(button, 3, ARROW_MARGIN_VERTICAL, 5, toolPaneHeight, null);
            } else {
                g.drawImage(UIConstants.DRAG_BAR_LIGHT, 0, 0, toolPaneHeight, getHeight(), null);
                if (containerWidth == toolPaneHeight) {
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
                g.drawImage(button, 2, ARROW_MARGIN_VERTICAL, 5, toolPaneHeight, null);
            }
            if (isLeftRightDragEnabled) {
                g.drawImage(UIConstants.DRAG_DOT_VERTICAL, 2, getHeight() / 2, 5, toolPaneHeight, null);
            }
        }
    }

    /**
     * 主函数
     * @param args  参数
     */
    public static void main(String... args) {
        JFrame jf = new JFrame("test");
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel content = (JPanel) jf.getContentPane();
        content.setLayout(new BorderLayout());
        UIResizableContainer bb = new UIResizableContainer(Constants.RIGHT);
        JPanel cc = new JPanel();
        cc.setBackground(Color.blue);
        content.add(bb, BorderLayout.EAST);
        content.add(cc, BorderLayout.CENTER);
        GUICoreUtils.centerWindow(jf);
        jf.setSize(500, 500);
        jf.setVisible(true);
    }
}