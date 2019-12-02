package com.fr.design.gui.icontainer;


import com.fr.design.base.mode.DesignModeContext;
import com.fr.design.constants.UIConstants;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.utils.gui.GUICoreUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 * Created by plough on 2017/7/7.
 */
public class UIEastResizableContainer extends JPanel {
    private static final long serialVersionUID = 1854340560790476907L;
    private int containerWidth = 240;
    private int preferredWidth = 240;
    private int topToolPaneHeight = 25;
    private int leftPaneWidth = 40;

    private JComponent leftPane;
    private JComponent rightPane;

    //    private HorizotalToolPane horizontToolPane;
    private TopToolPane topToolPane;


    private static final int ARROW_MARGIN = 15;
    private static final int ARROW_RANGE = 35;

//    private boolean isRightPaneVisible = true;

    public UIEastResizableContainer() {
        this(new JPanel(), new JPanel());
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

    public boolean isRightPaneVisible() {
        return containerWidth > leftPaneWidth;
    }


//    public void setRightPaneVisible(boolean isVisible){
//        this.isRightPaneVisible = isVisible;
//    }

    private void setPreferredWidth(int width) {
        this.preferredWidth = width;
    }

    public UIEastResizableContainer(JComponent leftPane, JComponent rightPane) {
        setBackground(UIConstants.PROPERTY_PANE_BACKGROUND);
        this.leftPane = leftPane;
        this.rightPane = rightPane;

        this.topToolPane = new TopToolPane();
        topToolPane.setBackground(UIConstants.PROPERTY_PANE_BACKGROUND);

        setLayout(containerLayout);
        add(topToolPane);
        add(leftPane);
        add(rightPane);
    }

    public static void main(String... args) {
        JFrame jf = new JFrame("test");
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel content = (JPanel) jf.getContentPane();
        content.setLayout(new BorderLayout());

        JPanel leftPane = new JPanel();
        leftPane.setBackground(Color.yellow);
        JPanel rightPane = new JPanel();
        rightPane.setBackground(Color.green);

        UIButton b1, b2;
        b1 = new UIButton("b1");
        b2 = new UIButton("b2");
        b1.setPreferredSize(new Dimension(40, 40));
        b2.setPreferredSize(new Dimension(40, 40));
        leftPane.add(b1);
        leftPane.add(b2);


        UIEastResizableContainer bb = new UIEastResizableContainer(leftPane, rightPane);

        JPanel cc = new JPanel();
        cc.setBackground(Color.WHITE);

        content.add(bb, BorderLayout.EAST);
        content.add(cc, BorderLayout.CENTER);
        GUICoreUtils.centerWindow(jf);
        jf.setSize(500, 500);
        jf.setVisible(true);
    }

    /**
     * 将面板设置成最佳的宽度
     */
    public void setWindow2PreferWidth() {
        if (containerWidth == leftPaneWidth) {
            containerWidth = preferredWidth;
            refreshContainer();
        }
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
        if (containerWidth == leftPaneWidth) {
            this.containerWidth = containerWidth;
        }
        // 忽略其他情况
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
            if (topToolPane == null || rightPane == null) {
                return;
            }

//            topToolPane.setBounds(0, 0, containerWidth, topToolPaneHeight);//0,0,10,462
            topToolPane.setBounds(0, 0, leftPaneWidth, topToolPaneHeight);//0,0,10,462
            leftPane.setBounds(0, topToolPaneHeight, leftPaneWidth, getHeight() - topToolPaneHeight);

//            parameterPane.setBounds(20, 0, 230, getParameterPaneHeight());//10,0,230,462
            rightPane.setBounds(leftPaneWidth, 0, containerWidth-leftPaneWidth, getHeight());//20,0,230,0
        }

        @Override
        public void addLayoutComponent(String name, Component comp) {
            // do nothing
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
     * 替换左子面板
     *
     * @param pane 面板
     */
    public void replaceLeftPane(JComponent pane) {
        remove(pane);
        remove(this.leftPane);
        add(this.leftPane = pane);
        refreshContainer();
    }


    /**
     * 替换右子面板
     *
     * @param pane 面板
     */
    public void replaceRightPane(JComponent pane) {
        remove(pane);
        remove(this.rightPane);
        add(this.rightPane = pane);
        refreshContainer();
    }

    /**
     * 得到左子面板
     *
     * @return
     */
    public JComponent getLeftPane() {
        return this.leftPane;
    }

    /**
     * 得到右子面板
     *
     * @return
     */
    public JComponent getRightPane() {
        return this.rightPane;
    }

    private void refreshContainer() {
        validate();
        repaint();
        revalidate();
    }

    /**
     * 伸缩右子面板时，触发此方法
     */
    public void onResize() {
        // do nothing here
    }

    private class TopToolPane extends JPanel {
        private int model = UIConstants.MODEL_NORMAL;

        public TopToolPane() {
            super();
            addMouseMotionListener(new MouseMotionListener() {

                @Override
                public void mouseMoved(MouseEvent e) {
                    if (e.getX() <= ARROW_RANGE) {
                        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                        model = UIConstants.MODEL_PRESS;
                    } else {
                        setCursor(Cursor.getDefaultCursor());
                        model = UIConstants.MODEL_NORMAL;
                    }
                    refreshContainer();
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    // do nothing
                }
            });
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseExited(MouseEvent e) {
                    setCursor(Cursor.getDefaultCursor());
                    model = UIConstants.MODEL_NORMAL;
                    refreshContainer();
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getX() <= ARROW_RANGE) {
                        if (containerWidth == leftPaneWidth) {
                            containerWidth = preferredWidth;
                        } else {
                            setPreferredWidth(containerWidth);
                            containerWidth = leftPaneWidth;
                        }
                        onResize();
                        refreshContainer();
                        if (DesignModeContext.isAuthorityEditing()) {
                            DesignerContext.getDesignerFrame().doResize();
                        }
                    }
                }
            });
        }

        @Override
        public void paint(Graphics g) {
            Image button;

            if (containerWidth == leftPaneWidth) {
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
            g.drawImage(button, 18, 7, 5, 10, null);
        }
    }

}