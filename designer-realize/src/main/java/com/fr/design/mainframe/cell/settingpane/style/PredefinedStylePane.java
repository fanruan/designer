package com.fr.design.mainframe.cell.settingpane.style;

import com.fr.base.NameStyle;
import com.fr.base.ScreenResolution;
import com.fr.base.Style;
import com.fr.config.ServerPreferenceConfig;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.DesignerBean;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.ComparatorUtils;


import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;

public class PredefinedStylePane extends FurtherBasicBeanPane<NameStyle> implements DesignerBean {

    private static final int LEFT_BORDER = 10;
    private static final int RIGHT_BORDER = 10;
    private DefaultListModel defaultListModel;
    private JList styleList;
    private ChangeListener changeListener;

    public PredefinedStylePane() {
        defaultListModel = new DefaultListModel();
        styleList = new JList(defaultListModel);
        DefaultListCellRenderer render = new DefaultListCellRenderer() {
            private Style nameStyle;

            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (value instanceof Style) {
                    this.nameStyle = (Style) value;
                    this.setText(" ");
                }
                this.setPreferredSize(new Dimension(210, 22));
                return this;
            }

            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);

                if (nameStyle == null) {
                    return;
                }
                String text = "abcedfgh";
                if (nameStyle instanceof NameStyle) {
                    text = ((NameStyle) nameStyle).getName();
                }
                Style.paintBackground((Graphics2D) g, nameStyle, getWidth() - 1, getHeight() - 1);
                Style.paintContent((Graphics2D) g, text, nameStyle, getWidth() - 1, getHeight() - 1, ScreenResolution.getScreenResolution());
                Style.paintBorder((Graphics2D) g, nameStyle, getWidth() - 1, getHeight() - 1);
            }

        };
        styleList.setCellRenderer(render);
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.add(styleList, BorderLayout.CENTER);
        this.setBorder(BorderFactory.createEmptyBorder(0 ,LEFT_BORDER, 0, RIGHT_BORDER));

        styleList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int clickedNumber = e.getClickCount();
                if (clickedNumber == 1) {
                    if (changeListener != null) {
                        changeListener.stateChanged(new ChangeEvent(styleList));
                    }
                }
                // 如果点击次数大于2认为发生双击，弹出编辑界面
                // if (clickedNumber >= 2) {
                // }
            }
        });

        DesignerContext.setDesignerBean("predefinedStyle", this);
    }

    /**
     * 添加改变监听
     *
     * @param changeListener 监听事件
     */
    public void addChangeListener(ChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    /**
     * 重置
     */
    public void reset() {

    }

    @Override
    public void populateBean(NameStyle ob) {
        refreshBeanElement();
        for (int i = 0; i < defaultListModel.getSize(); i++) {
            if (ComparatorUtils.equals(ob, defaultListModel.get(i))) {
                styleList.setSelectedIndex(i);
                break;
            }
        }
    }

    @Override
    public NameStyle updateBean() {
        return (NameStyle) styleList.getSelectedValue();
    }

    /**
     * 获取面板标题
     *
     * @return 标题
     */
    public String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Predefined_Style");
    }

    /**
     * 是否可以接纳对象
     *
     * @param ob 组件对象
     * @return 是否可以接纳对象
     */
    public boolean accept(Object ob) {
        return ob instanceof NameStyle;
    }

    /**
     * 刷新组件对象
     */
    public void refreshBeanElement() {
        defaultListModel.removeAllElements();
        if (ServerPreferenceConfig.getInstance().hasStyle()) {
            Iterator iterato = ServerPreferenceConfig.getInstance().getStyleNameIterator();
            while (iterato.hasNext()) {
                String name = (String) iterato.next();
                NameStyle nameStyle = NameStyle.getInstance(name);
                defaultListModel.addElement(nameStyle);
            }
        }
        styleList.setModel(defaultListModel);
        GUICoreUtils.repaint(this);

    }

}