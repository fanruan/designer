package com.fr.design.gui.ibutton;

import com.fr.design.constants.UIConstants;
import com.fr.design.event.GlobalNameListener;
import com.fr.design.event.GlobalNameObserver;
import com.fr.stable.ArrayUtils;
import com.fr.stable.StringUtils;
import sun.swing.SwingUtilities2;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.text.View;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UIButtonGroup<T> extends JPanel implements GlobalNameObserver {
    private static final long serialVersionUID = 1L;
    protected List<UIToggleButton> labelButtonList;
    protected int selectedIndex = -1;
    private List<T> objectList;// 起到一个render的作用
    private GlobalNameListener globalNameListener = null;
    private String buttonGroupName = StringUtils.EMPTY;
    private boolean isToolBarComponent = false;
    private boolean isClick;

    public UIButtonGroup(String[] textArray) {
        this(textArray, null);
    }

    public UIButtonGroup(Icon[] iconArray) {
        this(iconArray, null);
    }

    public UIButtonGroup(Icon[][] iconArray) {
        this(iconArray, null);
    }

    public UIButtonGroup(Icon[] iconArray, T[] objects) {
        if (!ArrayUtils.isEmpty(objects) && iconArray.length == objects.length) {
            this.objectList = Arrays.asList(objects);
        }
        labelButtonList = new ArrayList<UIToggleButton>(iconArray.length);
        this.setLayout(getGridLayout(iconArray.length));
        this.setBorder(getGroupBorder());
        for (int i = 0; i < iconArray.length; i++) {
            final int index = i;
            Icon icon = iconArray[i];
            final UIToggleButton labelButton = new UIToggleButton(icon) {
                @Override
                protected MouseListener getMouseListener() {
                    return new MouseAdapter() {
                        @Override
                        public void mousePressed(MouseEvent e) {
                            isClick = true;
                            if (!isEnabled()) {
                                return;
                            }
                            if (globalNameListener != null) {
                                globalNameListener.setGlobalName(buttonGroupName);
                            }
                            setSelectedWithFireChanged(index);
                        }
                    };
                }

                public boolean shouldResponseNameListener() {
                    return false;
                }
            };
            initButton(labelButton);
        }
    }

    public UIButtonGroup(Icon[][] iconArray, T[] objects) {
        if (!ArrayUtils.isEmpty(objects) && iconArray.length == objects.length) {
            this.objectList = Arrays.asList(objects);
        }
        labelButtonList = new ArrayList<UIToggleButton>(iconArray.length);
        this.setLayout(getGridLayout(iconArray.length));
        this.setBorder(getGroupBorder());
        for (int i = 0; i < iconArray.length; i++) {
            final int index = i;
            Icon[] icon = iconArray[i];
            final UIToggleButton labelButton = new UIToggleButton(icon) {
                @Override
                protected MouseListener getMouseListener() {
                    return new MouseAdapter() {
                        @Override
                        public void mousePressed(MouseEvent e) {
                            isClick = true;
                            if (!isEnabled()) {
                                return;
                            }
                            if (globalNameListener != null) {
                                globalNameListener.setGlobalName(buttonGroupName);
                            }
                            setSelectedWithFireChanged(index);
                        }
                    };
                }

                public boolean shouldResponseNameListener() {
                    return false;
                }
            };
            initButton(labelButton);
        }
    }

    public boolean hasClick() {
        return isClick;
    }

    public void setClickState(boolean changeFlag) {
        isClick = changeFlag;
    }

    public void setForToolBarButtonGroup(boolean isToolBarComponent) {
        this.isToolBarComponent = isToolBarComponent;
        if (isToolBarComponent) {
            for (int i = 0; i < labelButtonList.size(); i++) {
                labelButtonList.get(i).set4ToolbarButton();
            }
        }
        repaint();

    }


    /**
     * setEnabled
     *
     * @param enabled
     */
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        for (int i = 0; i < labelButtonList.size(); i++) {
            labelButtonList.get(i).setEnabled(enabled);
        }
    }

    public UIButtonGroup(String[] textArray, T[] objects) {
        if (!ArrayUtils.isEmpty(objects) && textArray.length == objects.length) {
            this.objectList = Arrays.asList(objects);
        }
        labelButtonList = new ArrayList<UIToggleButton>(textArray.length);
        this.setLayout(getGridLayout(textArray.length));
        this.setBorder(getGroupBorder());
        for (int i = 0; i < textArray.length; i++) {
            final int index = i;
            final UIToggleButton labelButton = new UIToggleButton(textArray[i]) {
                @Override
                protected MouseListener getMouseListener() {
                    return new MouseAdapter() {
                        @Override
                        public void mousePressed(MouseEvent e) {
                            if (!isEnabled()) {
                                return;
                            }
                            if (globalNameListener != null) {
                                globalNameListener.setGlobalName(buttonGroupName);
                            }
                            setSelectedWithFireChanged(index);
                        }
                    };
                }

                @Override
                public Insets getInsets() {
                    return new Insets(0, 2, 0, 2);
                }

                public boolean shouldResponseNameListener() {
                    return false;
                }

            };
            labelButton.setUI(new UIButtonUI() {
                protected void paintText(Graphics g, AbstractButton b, String text, Rectangle textRec) {
                    View v = (View) b.getClientProperty(BasicHTML.propertyKey);
                    if (v != null) {
                        v.paint(g, textRec);
                        return;
                    }
                    FontMetrics fm = SwingUtilities2.getFontMetrics(b, g);
                    int mnemonicIndex = b.getDisplayedMnemonicIndex();
                    if (isPressed(b)) {
                        g.setColor(Color.white);
                    } else {
                        g.setColor(b.isEnabled() ? Color.black : UIConstants.LINE_COLOR);
                    }

                    SwingUtilities2.drawStringUnderlineCharAt(b, g, text, mnemonicIndex, textRec.x + getTextShiftOffset(), textRec.y + fm.getAscent() + getTextShiftOffset());
                }
            });
            initButton(labelButton);
        }
    }

    public void setGlobalName(String name) {
        buttonGroupName = name;
    }

    protected void initButton(UIToggleButton labelButton) {
        labelButton.setBorderPainted(false);
        labelButtonList.add(labelButton);
        this.add(labelButton);
    }

    protected Border getGroupBorder() {
        return BorderFactory.createEmptyBorder(1, 1, 1, 1);
    }

    protected LayoutManager getGridLayout(int number) {
        return new GridLayout(0, number, 1, 0);
    }

    /**
     * paintComponent
     *
     * @param g
     */
    public void paintComponents(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Shape oldClip = g2d.getClip();
        g2d.clip(new RoundRectangle2D.Double(1, 1, getWidth(), getHeight(), UIConstants.ARC, UIConstants.ARC));
        super.paintComponents(g);
        g2d.setClip(oldClip);
    }

    /**
     * setSelectedItem
     *
     * @param ob
     */
    public void setSelectedItem(T ob) {
        if (objectList == null) {
            setSelectedIndex(-1);
            return;
        }
        setSelectedIndex(objectList.indexOf(ob));
    }

    /**
     * getSelectedItem
     *
     * @return
     */
    public T getSelectedItem() {
        if (selectedIndex == -1) {
            return null;
        }
        return objectList.get(selectedIndex);
    }

    /**
     * getSelectedIndex
     *
     * @return
     */
    public int getSelectedIndex() {
        return selectedIndex;
    }

    protected void setSelectedWithFireChanged(int newSelectedIndex) {
        selectedIndex = newSelectedIndex;
        for (int i = 0; i < labelButtonList.size(); i++) {
            if (i == selectedIndex) {
                labelButtonList.get(i).setSelectedWithFireListener(true);
            } else {
                labelButtonList.get(i).setSelected(false);
            }
        }
    }

    /**
     * setSelectedIndex
     *
     * @param newSelectedIndex
     */
    public void setSelectedIndex(int newSelectedIndex) {
        selectedIndex = newSelectedIndex;
        for (int i = 0; i < labelButtonList.size(); i++) {
            labelButtonList.get(i).setSelected(i == selectedIndex);
        }
    }

    private void fireStateChanged() {
        Object[] listeners = listenerList.getListenerList();
        ChangeEvent e = null;

        for (int i = 0; i < listeners.length; i++) {
            if (listeners[i] == ChangeListener.class) {
                if (e == null) {
                    e = new ChangeEvent(this);
                }
                ((ChangeListener) listeners[i + 1]).stateChanged(e);
            }
        }
    }

    /**
     * getButton
     *
     * @param index
     * @return
     */
    public UIToggleButton getButton(int index) {
        return labelButtonList.get(index);
    }

    /**
     * 给所有的Button添加Tips
     *
     * @param tips
     */
    public void setAllToolTips(String[] tips) {
        for (int i = 0; i < labelButtonList.size(); i++) {
            labelButtonList.get(i).setToolTipText(tips[i]);
        }
    }


    /**
     * 注册全局属性名字监听器
     *
     * @param listener 观察者监听事件
     */
    public void registerNameListener(GlobalNameListener listener) {
        globalNameListener = listener;
    }

    /**
     * 是否响应名字监听事件
     *
     * @return
     */
    public boolean shouldResponseNameListener() {
        return true;
    }


    /**
     * @param l
     */
    public void addChangeListener(ChangeListener l) {
        for (int i = 0; i < labelButtonList.size(); i++) {
            labelButtonList.get(i).addChangeListener(l);
            listenerList.add(ChangeListener.class, l);
        }
    }

    /**
     * @param l
     */
    public void removeChangeListener(ChangeListener l) {
        this.listenerList.remove(ChangeListener.class, l);
    }


    /**
     * @param l
     */
    public void addActionListener(ActionListener l) {
        for (int i = 0; i < labelButtonList.size(); i++) {
            labelButtonList.get(i).addActionListener(l);
        }
    }


    /**
     * @param l
     */
    public void removeActionListener(ActionListener l) {
        for (int i = 0; i < labelButtonList.size(); i++) {
            labelButtonList.get(i).removeActionListener(l);
        }
    }

    /**
     * populate
     */
    public void populateBean() {
        fireStateChanged();
    }

    /**
     * 重载Border画法
     *
     * @param g
     */
    @Override
    protected void paintBorder(Graphics g) {
        if (isToolBarComponent) {
            return;
        }
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(UIConstants.SHADOW_GREY);

        int width = 0;
        for (int i = 0; i < labelButtonList.size() - 1; i++) {
            width += labelButtonList.get(i).getWidth() + 1;
            int height = labelButtonList.get(i).getHeight();
            g.drawLine(width, 0, width, height);
        }

        width += labelButtonList.get(labelButtonList.size() - 1).getWidth() + 1;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawRoundRect(0, 0, width, getHeight() - 1, UIConstants.BUTTON_GROUP_ARC, UIConstants.BUTTON_GROUP_ARC);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

    }

    /**
     * main
     *
     * @param args
     */
    public static void main(String... args) {
//        JFrame jf = new JFrame("test");
//        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        JPanel content = (JPanel) jf.getContentPane();
//        content.setLayout(new BorderLayout());
//        Icon[] a1 = {BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/h_left_normal.png"), BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/h_center_normal.png"),
//                BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/h_right_normal.png")};
//        Integer[] a2 = new Integer[]{Constants.LEFT, Constants.CENTER, Constants.RIGHT};
//        UIButtonGroup<Integer> bb = new UIButtonGroup<Integer>(a1, a2);
//        bb.setBounds(20, 20, bb.getPreferredSize().width, bb.getPreferredSize().height);
//        bb.setSelectedIndex(0);
//        bb.setEnabled(false);
//        content.add(bb);
//        GUICoreUtils.centerWindow(jf);
//        jf.setSize(400, 400);
//        jf.setVisible(true);
    }


}