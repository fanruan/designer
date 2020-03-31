/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.style.color;

import com.fr.design.DesignerEnvManager;
import com.fr.design.border.UIRoundedBorder;
import com.fr.design.constants.UIConstants;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.utils.gui.GUICoreUtils;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.ArrayList;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * The pane used to select color
 */
public class ColorSelectPane extends TransparentPane implements ColorSelectable {
    private static final long serialVersionUID = -8634152305687249392L;

    private Color color = null; //color
    //color setting action.
    private ArrayList<ChangeListener> colorChangeListenerList = new ArrayList<ChangeListener>();


    /* default */ ColorSelectDetailPane pane;

    /**
     * Constructor.
     */
    public ColorSelectPane() {
        super(true);
        initialCompents(true);
    }

    public ColorSelectPane(boolean isSupportTransparent) {
        super(isSupportTransparent);
        initialCompents(isSupportTransparent);
    }

    private void initialCompents(boolean isSupportTransparent) {

        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.setBorder(new UIRoundedBorder(UIConstants.LINE_COLOR, 1, 5));
        if (isSupportTransparent) {
            UIButton transpanrentButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_ChartF_Transparency"));
            this.add(transpanrentButton, BorderLayout.NORTH);
            transpanrentButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    doTransparent();
                }
            });
        }

        // center
        JPanel centerPane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_S_Pane();
        this.add(centerPane, BorderLayout.CENTER);

        centerPane.add(getRow1Pane());

        JPanel menuColorPane1 = getMenuColorPane();
        centerPane.add(menuColorPane1);
        Color[] colorArray = this.getColorArray();
        for (int i = 0; i < colorArray.length; i++) {
            Color color = colorArray[i] == null ? UsedColorPane.DEFAULT_COLOR : colorArray[i];
            menuColorPane1.add(new ColorCell(color, this));
        }

        centerPane.add(Box.createVerticalStrut(1));

        UIButton customButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_More_Color"));

        customButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                customButtonPressed();
            }
        });
        customButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JPanel centerPane1 = FRGUIPaneFactory.createBorderLayout_S_Pane();
        centerPane1.setBorder(BorderFactory.createEmptyBorder(2, 8, 8, 8));
        centerPane1.add(customButton, BorderLayout.NORTH);
        centerPane.add(centerPane1);
    }

    protected JPanel getMenuColorPane() {
        JPanel menuColorPane = new JPanel();
        menuColorPane.setLayout(new GridLayout(5, 8, 1, 1));
        menuColorPane.setBorder(BorderFactory.createEmptyBorder(8, 8, 0, 8));

        return menuColorPane;
    }

    // 第一行，1个取色按钮 + 1个留空的单元格 + 6个最近使用的颜色
    protected JPanel getRow1Pane() {
        JPanel row1Pane = new JPanel(FRGUIPaneFactory.createBorderLayout());
        row1Pane.setBorder(BorderFactory.createEmptyBorder(8, 8, 0, 0));
        row1Pane.setPreferredSize(new Dimension(135, 24));  // 宽度为 16 * 8 + 7

        // 最近使用
        UsedColorPane usedColorPane = new UsedColorPane(1, 8, 1, this, true, true);
        usedColorPane.getPane().setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
        row1Pane.add(usedColorPane.getPane());
        return row1Pane;
    }

    protected Color[] getColorArray() {
        return ColorFactory.MenuColors;
    }


    /**
     * Add change listener.
     * 增加监听
     *
     * @param changeListener
     */
    public void addChangeListener(ChangeListener changeListener) {
        this.colorChangeListenerList.add(changeListener);
    }

    @Override
    protected String title4PopupWindow() {
        return "Color";
    }

    /**
     * Return the color.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Set the color.
     *
     * @param color the new color.
     */
    public void setColor(Color color) {
        this.color = color;

        //fire color change.
        if (!colorChangeListenerList.isEmpty()) {
            ChangeEvent evt = new ChangeEvent(this);

            for (int i = 0; i < colorChangeListenerList.size(); i++) {
                this.colorChangeListenerList.get(i).stateChanged(evt);
            }
        }

        DesignerEnvManager.getEnvManager().getColorConfigManager().addToColorQueue(color);
        this.repaint();
    }

    /**
     * 选中颜色
     */
    @Override
    public void colorSetted(ColorCell colorCell) {
        colorCell.repaint();
    }

    /**
     * 初始化中央面板
     *
     * @param centerPane 中央面板
     */
    @Override
    public void initCenterPaneChildren(JPanel centerPane) {
        GUICoreUtils.initCenterPaneChildren(centerPane, this);
    }

    /**
     * 透明
     */
    @Override
    public void doTransparent() {
        this.setColor(null);
    }

    /**
     * 更多颜色
     */
    @Override
    public void customButtonPressed() {
        pane = new ColorSelectDetailPane(Color.WHITE);
        ColorSelectDialog.showDialog(DesignerContext.getDesignerFrame(), pane, Color.WHITE, this, true);
    }
}
