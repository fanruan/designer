/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.mainframe;

import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UINumberField;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.scrollruler.*;
import com.fr.general.FRScreen;
import com.fr.general.Inter;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-10-13
 * Time: 下午5:08
 */
public class ChartArea extends JComponent implements ScrollRulerComponent {

    private static final int TOPGAP = 8;
    private static final int MIN_WIDTH = 36;
    private static final int MIN_HEIGHT = 21;
    private static final double SLIDER_FLOAT = 120.0;
    private static final double SLIDER_MIN = 60.0;
    private static final double DEFAULT_SLIDER = 100.0;
    private static final int ROTATIONS = 50;
    private int designerwidth = 810;
    private int designerheight = 500;
    private int customWidth = 810;
    private int customHeight = 500;
    private ChartDesigner designer;
    private int horizontalValue = 0;
    private int verticalValue = 0;
    private int verticalMax = 0;
    private int horicalMax = 0;
    private FormScrollBar verScrollBar;
    private FormScrollBar horScrollBar;
    //显示和设置图表界面大小的控件
    private UINumberField widthPane;
    private UINumberField heightPane;
    private boolean isValid = true;
    private double START_VALUE = DEFAULT_SLIDER;

    public ChartArea(ChartDesigner designer) {
        this(designer, true);
    }

    public ChartArea(ChartDesigner designer, boolean useScrollBar) {
        this.designer = designer;
        this.designer.setParent(this);
        this.customWidth = designer.getTarget().getWidth();
        this.customHeight = designer.getTarget().getHeight();
        this.designerwidth = this.customWidth;
        this.designerheight = this.customHeight;
        isValid = useScrollBar;
        verScrollBar = new FormScrollBar(Adjustable.VERTICAL, this);
        horScrollBar = new FormScrollBar(Adjustable.HORIZONTAL, this);
        if (useScrollBar) {
            this.setLayout(new FormRulerLayout());
            designer.setBorder(new LineBorder(new Color(198, 198, 198)));
            this.add(FormRulerLayout.CENTER, designer);
            addFormSize();
            this.add(FormRulerLayout.VERTICAL, verScrollBar);
            this.add(FormRulerLayout.HIRIZONTAL, horScrollBar);
            enableEvents(AWTEvent.MOUSE_WHEEL_EVENT_MASK);
        } else {
            // 报表参数界面只要标尺和中心pane
            this.setLayout(new RulerLayout());
            this.add(RulerLayout.CENTER, designer);
            addFormRuler();
        }
        this.setFocusTraversalKeysEnabled(false);
    }

    /**
     * 增加表单的页面大小控制界面，包括手动修改和滑块拖动
     */
    private void addFormSize() {
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        double[] rowSize = {f};
        double[] columnSize = {p, f, p, p, p, p, p,f,p};
        UILabel tipsPane = new UILabel("chart");
        tipsPane.setPreferredSize(new Dimension(200, 0));
        widthPane = new UINumberField();
        widthPane.setPreferredSize(new Dimension(60, 0));
        heightPane = new UINumberField();
        heightPane.setPreferredSize(new Dimension(60, 0));
        JPanel panel = new JPanel(){
              public Dimension getPreferredSize(){
                  return new Dimension(200,0);
              }
        };
        JPanel resizePane = TableLayoutHelper.createCommonTableLayoutPane(new JComponent[][]{
                        {tipsPane, new UILabel(), widthPane, new UILabel(Inter.getLocText("Indent-Pixel")), new UILabel("x"),
                                heightPane, new UILabel(Inter.getLocText("Indent-Pixel")),new UILabel(),panel}},
                rowSize, columnSize, 8
        );
        this.add(FormRulerLayout.BOTTOM, resizePane);
        setWidgetsConfig();
        // 先初始话滑块及对应事件，然后获取分辨率调整容器的显示大小
        initCalculateSize();
    }

    private void setWidgetsConfig() {
        widthPane.setHorizontalAlignment(widthPane.CENTER);
        heightPane.setHorizontalAlignment(heightPane.CENTER);
        widthPane.setMaxDecimalLength(0);
        heightPane.setMaxDecimalLength(0);
        //控件初始值就是根节点组件初始的宽和高
        widthPane.setValue(designerwidth);
        heightPane.setValue(designerheight);
        addWidthPaneListener();
        addHeightPaneListener();
    }

    private void initCalculateSize() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension scrnsize = toolkit.getScreenSize();
        double value = FRScreen.getByDimension(scrnsize).getValue();
        if (value != DEFAULT_SLIDER) {
            reCalculateRoot(value, true);
        }
    }

    //设置宽度的控件及响应事件
    private void addWidthPaneListener() {
        widthPane.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        reCalculateWidth((int) ((UINumberField) evt.getSource()).getValue());
                    }
                }
        );
        widthPane.addFocusListener(
                new FocusAdapter() {
                    public void focusLost(FocusEvent e) {
                        // 失去焦点时，可以认为输入结束
                        reCalculateWidth((int) ((UINumberField) e.getSource()).getValue());
                    }
                }
        );
    }

    private void addHeightPaneListener() {
        heightPane.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        reCalculateHeight((int) ((UINumberField) evt.getSource()).getValue());
                    }
                }
        );
        heightPane.addFocusListener(
                new FocusAdapter() {
                    public void focusLost(FocusEvent e) {
                        // 失去焦点时，可以认为输入结束
                        reCalculateHeight((int) ((UINumberField) e.getSource()).getValue());
                    }
                }
        );
    }

    private void reCalculateWidth(int width) {
        int dW = width - designerwidth;
        if (dW == 0) {
            return;
        }
        // 图表设计器先设大小为实际的高和当前的宽，然后按此调整内部的组件
        designer.setSize(width, designerheight);
        designerwidth = width;
        customWidth = width;
        designer.getTarget().setWidth(width);
        ChartArea.this.validate();
        designer.fireTargetModified();
    }

    private void reCalculateHeight(int height) {
        int dW = height - designerwidth;
        if (dW == 0) {
            return;
        }
        // 图表设计器先设大小为实际的高和当前的宽，然后按此调整内部的组件
        designer.setSize(designerwidth, height);
        designerheight = height;
        customHeight = height;
        this.designer.getTarget().setHeight(height);
        ChartArea.this.validate();
        designer.fireTargetModified();
    }

    /**
     * 按照界面大小的百分比值调整root大小
     *
     * @param needCalculateParaHeight 是否需要调整参数界面高度
     * @param value
     */
    private void reCalculateRoot(double value, boolean needCalculateParaHeight) {
        if (value == START_VALUE) {
            return;
        }
        double percent = (value - START_VALUE) / START_VALUE;
        Dimension d = new Dimension(designerwidth, designerheight);
        // 调整自适应布局大小后，同步调整参数界面和border大小，此时刷新下formArea
        ChartArea.this.validate();
        START_VALUE = value;
    }

    /**
     * 增加刻度条
     */
    public void addFormRuler() {
        BaseRuler vRuler = new VerticalRuler(this);
        BaseRuler hRuler = new HorizontalRuler(this);
        this.add(RulerLayout.VRULER, vRuler);
        this.add(RulerLayout.HRULER, hRuler);
    }

    /**
     * 鼠标滚轮事件
     * 由于表单设计界面要求： 容器大小大于界面时，滚动条才可以拖动，所以不支持滚动无限往下滚
     */
    @Override
    protected void processMouseWheelEvent(java.awt.event.MouseWheelEvent evt) {
        int id = evt.getID();
        switch (id) {
            case MouseEvent.MOUSE_WHEEL: {
                int rotations = evt.getWheelRotation();
                int value = this.verScrollBar.getValue() + rotations * ROTATIONS;
                value = Math.min(value, verticalMax);
                value = Math.max(0, value);
                doLayout(); //加dolayout是因为每次滚动都要重置 Max的大小
                this.verScrollBar.setValue(value);
                break;
            }
        }
    }

    /**
     * 容器布局
     */
    public void doLayout() {
        layout();
        if (isValid) {
            setScrollBarProperties(customWidth - designer.getWidth(), horScrollBar);
            //计算滚动条值的时候应该算上参数面板的高度
            setScrollBarProperties(customHeight - designer.getHeight(), verScrollBar);
        }
    }

    /**
     * 设置滚动条的属性
     */
    private void setScrollBarProperties(int value, FormScrollBar bar) {
        if (value <= 0) {
            // 界面有滚动条时，手动缩小容器宽度到界面内，重置滚动条值和max
            setScrollBarMax(0, bar);
            bar.setMaximum(0);
            bar.setValue(0);
            bar.setEnabled(false);
        } else {
            //参数面板拖拽过程中value一直为当前value
            int oldValue = verticalValue;
            setScrollBarMax(value, bar);
            bar.setEnabled(true);
            bar.setMaximum(value);
            bar.setValue(value);
            bar.setValue(oldValue);
        }
    }

    private boolean isScrollNotVisible(FormScrollBar bar) {
        if (bar.getOrientation() == Adjustable.VERTICAL) {
            return verticalMax == 0;
        } else {
            return horicalMax == 0;
        }
    }

    private void setScrollBarMax(int max, FormScrollBar bar) {
        if (bar.getOrientation() == Adjustable.VERTICAL) {
            verticalMax = max;
        } else {
            horicalMax = max;
        }
    }

    /**
     * 返回designer的最小高度
     *
     * @return int
     */
    public int getMinHeight() {
        return MIN_HEIGHT;
    }

    /**
     * 返回designer的最小宽度
     *
     * @return int
     */
    public int getMinWidth() {
        return MIN_WIDTH;
    }

    /**
     * getRulerLengthUnit
     *
     * @return short
     */
    public short getRulerLengthUnit() {
        return -1;
    }

    /**
     * 返回水平滚动条的value
     *
     * @return int
     */
    public int getHorizontalValue() {
        return horizontalValue;
    }

    /**
     * 设置水平滚动条的value
     *
     * @param newValue
     */
    public void setHorizontalValue(int newValue) {
        this.horizontalValue = newValue;
    }

    /**
     * 返回竖直滚动条的value
     *
     * @return
     */
    public int getVerticalValue() {
        return verticalValue;
    }

    /**
     * 竖直滚动条赋值
     *
     * @param newValue
     */
    public void setVerticalValue(int newValue) {
        this.verticalValue = newValue;
    }

    /**
     * 返回当前designer的高度
     *
     * @return height
     */
    public int getDesignerHeight() {
        return designer.getHeight();
    }

    /**
     * 返回当前designer的宽度
     *
     * @return
     */
    public int getDesignerWidth() {
        return designer.getWidth();
    }

    /**
     * 返回宽度控件的value
     *
     * @return 宽度
     */
    public double getWidthPaneValue() {
        return widthPane.getValue();
    }

    /**
     * 设置宽度值
     *
     * @param value 值
     */
    public void setWidthPaneValue(int value) {
        widthPane.setValue(value);
    }

    /**
     * 设置高度值
     *
     * @param value 值
     */
    public void setHeightPaneValue(int value) {
        heightPane.setValue(value);
    }

    /**
     * 返回高度控件的value
     *
     * @return 高度
     */
    public double getHeightPaneValue() {
        return heightPane.getValue();
    }

    /**
     * 返回界面区域大小
     *
     * @return Dimension
     */
    public Dimension getAreaSize() {
        return new Dimension(horScrollBar.getMaximum(), verScrollBar.getMaximum());
    }

    /**
     * setAreaSize
     *
     * @param totalSize
     * @param horizontalValue
     * @param verticalValue
     */
    public void setAreaSize(Dimension totalSize, int horizontalValue, int verticalValue, double width, double height, double slide) {
        horScrollBar.setMaximum((int) totalSize.getWidth());
        verScrollBar.setMaximum((int) totalSize.getHeight());
        horScrollBar.setValue(horizontalValue);
        verScrollBar.setValue(verticalValue);
        // 撤销会refresh底层容器，需要按照之前的宽高和百分比重置下容器size
        if (width != widthPane.getValue()) {
            widthPane.setValue(width);
            reCalculateWidth((int) width);
        }
        if (height != heightPane.getValue()) {
            heightPane.setValue(height);
            reCalculateHeight((int) height);
        }
        // undo时会重新refreshRoot，需要再次按照百分比调整下
        START_VALUE = DEFAULT_SLIDER;
        reCalculateRoot(slide, true);
    }

    public int getCustomWidth(){
        return this.customWidth;
    }

    public int getCustomHeight(){
       return this.customHeight;
    }

    /**
     * 计算滚动条的值和max
     *
     * @param oldmax      之前最大值
     * @param max         当前最大值
     * @param newValue    当前value
     * @param oldValue    之前value
     * @param visi        designer的大小
     * @param orientation 滚动条方向
     * @return 计算后的值和max
     */
    @Override
    public Point calculateScroll(int oldmax, int max, int newValue, int oldValue, int visi, int orientation) {
        int scrollMax = orientation == 1 ? verticalMax : horicalMax;
        //防止滚动条到达低端还可以继续点击移动(滚动条最大范围不变时，newValue要在范围之内)
        if (oldmax == scrollMax + visi && newValue > scrollMax) {
            return new Point(oldValue, oldmax);
        }
        return new Point(newValue, max);
    }

    private class FormRulerLayout extends RulerLayout {
        public FormRulerLayout() {
            super();
        }

        /**
         * 表单用的layout，当前不需要标尺
         */
        public void layoutContainer(Container target) {
            synchronized (target.getTreeLock()) {
                Insets insets = target.getInsets();
                int top = insets.top;
                int left = insets.left;
                int bottom = target.getHeight() - insets.bottom;
                int right = target.getWidth() - insets.right;
                Dimension resize = resizePane.getPreferredSize();
                Dimension hbarPreferredSize = null;
                Dimension vbarPreferredSize = null;

                resizePane.setBounds(left, bottom - resize.height, right, resize.height);
                if (horScrollBar != null) {
                    hbarPreferredSize = horScrollBar.getPreferredSize();
                    vbarPreferredSize = verScrollBar.getPreferredSize();
                    horScrollBar.setBounds(left, bottom - hbarPreferredSize.height - resize.height, right - BARSIZE, hbarPreferredSize.height);
                    verScrollBar.setBounds(right - vbarPreferredSize.width, top, vbarPreferredSize.width, bottom - BARSIZE - resize.height);
                }
                ChartDesigner dg = ((ChartDesigner) designer);
                Rectangle rec = new Rectangle(left + (right - designerwidth) / 2, TOPGAP, right, bottom);
                //是否为图表
                if (isValid) {
                    int maxHeight = bottom - hbarPreferredSize.height - resize.height - TOPGAP * 2;
                    int maxWidth = right - vbarPreferredSize.width;
                    designerwidth  = designerwidth> maxWidth ? maxWidth : designerwidth;
                    designerheight = designerheight > maxHeight ? maxHeight : designerheight;
                    int designerLeft = left + (verScrollBar.getX() - designerwidth) / 2;
                    rec = new Rectangle(designerLeft, TOPGAP, designerwidth, designerheight);
                }
                // designer是整个表单设计界面中的面板部分，目前只放自适应布局和参数界面。
                designer.setBounds(rec);
            }
        }

    }

}