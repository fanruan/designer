package com.fr.design.mainframe;

import com.fr.base.ScreenResolution;
import com.fr.common.inputevent.InputEventBaseOnOS;
import com.fr.design.designer.beans.events.DesignerEvent;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.XWBorderLayout;
import com.fr.design.designer.creator.XWFitLayout;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UIBasicSpinner;
import com.fr.design.gui.itextfield.UINumberField;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.scrollruler.BaseRuler;
import com.fr.design.scrollruler.HorizontalRuler;
import com.fr.design.scrollruler.RulerLayout;
import com.fr.design.scrollruler.ScrollRulerComponent;
import com.fr.design.scrollruler.VerticalRuler;
import com.fr.design.utils.ComponentUtils;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.form.main.mobile.FormMobileAttr;
import com.fr.form.ui.container.WBorderLayout;
import com.fr.general.FRScreen;
import com.fr.stable.AssistUtils;


import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.AWTEvent;
import java.awt.Adjustable;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class FormArea extends JComponent implements ScrollRulerComponent {

    private static final double SLIDER_FLOAT = 400.0;
    private static final double SLIDER_MIN = 10.0;
    public static final double DEFAULT_SLIDER = 100.0;
    private static final int ROTATIONS = 50;
    private static final int SHOWVALMAX = 400;
    private static final int SHOWVALMIN = 10;
    private static final int RESIZE_PANE_GAP = 8;
    private static final int MOBILE_ONLY_WIDTH = 375;
    private static final int MOBILE_ONLY_HEIGHT = 560;
    private FormDesigner designer;
    private int horizontalValue = 0;
    private int verticalValue = 0;
    private int verticalMax = 0;
    private int horicalMax = 0;
    private FormScrollBar verScrollBar;
    private FormScrollBar horScrollBar;
    //显示和设置表单界面大小的控件
    private UINumberField widthPane;
    private UINumberField heightPane;
    private JFormSliderPane slidePane;
    private boolean isValid = true;
    // 初始时滑块值为100，托动后的值设为START_VALUE;
    private double START_VALUE = DEFAULT_SLIDER;
    private int resolution = ScreenResolution.getScreenResolution();
    private double screenValue;

    public FormScrollBar getHorScrollBar() {
        return horScrollBar;
    }

    public void setHorScrollBar(FormScrollBar horScrollBar) {
        this.horScrollBar = horScrollBar;
    }

    public FormArea(FormDesigner designer) {
        this(designer, true);
    }

    public FormArea(FormDesigner designer, boolean useScrollBar) {
        this.designer = designer;
        this.designer.setParent(this);
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
            widthPane.setEnabled(!designer.getTarget().getFormMobileAttr().isMobileOnly());
        } else {
            // 报表参数界面只要标尺和中心pane
            this.setLayout(new RulerLayout());
            this.add(RulerLayout.CENTER, designer);
            addFormRuler();
        }
        this.setFocusTraversalKeysEnabled(false);
        this.designer.addMouseWheelListener(showValSpinnerMouseWheelListener);
    }

    public void onMobileAttrModified() {
        FormMobileAttr formMobileAttr = designer.getTarget().getFormMobileAttr();
        if (formMobileAttr.isMobileOnly()) {
            widthPane.setValue(MOBILE_ONLY_WIDTH);
            changeWidthPaneValue(MOBILE_ONLY_WIDTH);
            heightPane.setValue(MOBILE_ONLY_HEIGHT);
            changeHeightPaneValue(MOBILE_ONLY_HEIGHT);
        }
        widthPane.setEnabled(!formMobileAttr.isMobileOnly());
    }

    MouseWheelListener showValSpinnerMouseWheelListener = new MouseWheelListener() {
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            if (InputEventBaseOnOS.isControlDown(e)) {
                int dir = e.getWheelRotation();
                int old_resolution = (int) slidePane.getShowVal().getValue();
                slidePane.getShowVal().setValue(old_resolution - (dir * SHOWVALMIN));
            }
        }
    };

    /**
     * 增加表单的页面大小控制界面，包括手动修改和滑块拖动
     */
    private void addFormSize() {
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        double[] rowSize = {f};
        double[] columnSize = {p, f, p, p, p, p, p, f, p};
        UILabel tipsPane = new UILabel("form");
        tipsPane.setPreferredSize(new Dimension(200, 0));
        widthPane = new UINumberField();
        widthPane.setPreferredSize(new Dimension(60, 0));
        heightPane = new UINumberField();
        heightPane.setPreferredSize(new Dimension(60, 0));

//    	slidePane = new UINumberSlidePane(SLIDER_MIN, SLIDER_FLOAT);
//    	slidePane.setPreferredSize(new Dimension(260,20));
        slidePane = JFormSliderPane.getInstance();
        slidePane.setPreferredSize(new Dimension(200, 20));

        JPanel resizePane = TableLayoutHelper.createCommonTableLayoutPane(new JComponent[][]{{ tipsPane, new UILabel(), widthPane, new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Indent_Pixel")), new UILabel("x"), heightPane, new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Indent_Pixel")), new UILabel(), slidePane }}, rowSize, columnSize, RESIZE_PANE_GAP);
        this.add(FormRulerLayout.BOTTOM, resizePane);
        setWidgetsConfig();
        // 先初始话滑块及对应事件，然后获取分辨率调整容器的显示大小
        slidePane.setEnabled(false);
        slidePane.setVisible(false);
//        initTransparent();
        initCalculateSize();
    }

    private void setWidgetsConfig() {
        widthPane.setHorizontalAlignment(widthPane.CENTER);
        heightPane.setHorizontalAlignment(heightPane.CENTER);
        widthPane.setMaxDecimalLength(0);
        heightPane.setMaxDecimalLength(0);
        //控件初始值就是根节点组件初始的宽和高
        widthPane.setValue(designer.getRootComponent().getWidth());
        heightPane.setValue(designer.getRootComponent().getHeight());
        addWidthPaneListener();
        addHeightPaneListener();
    }

    private void initTransparent() {
        initCalculateSize();
        slidePane.getShowVal().addChangeListener(showValSpinnerChangeListener);
    }

    ChangeListener showValSpinnerChangeListener = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            double value = (int) ((UIBasicSpinner) e.getSource()).getValue();
            value = value > SHOWVALMAX ? SHOWVALMAX : value;
            value = value < SHOWVALMIN ? SHOWVALMIN : value;
            JForm jf = (JForm) HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
            jf.setResolution((int) value);
            jf.getFormDesign().setResolution((int) value);
            jf.getFormDesign().getArea().resolution = (int) value;
            reCalculateRoot(value, true);
            JTemplate form = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
            if (form != null) {
                form.fireTargetModified();
            }
        }
    };

    /**
     * 返回当前的屏幕分辨率对应的百分比值
     *
     * @return 缩放的百分比值
     */
    public double getScreenValue() {
        return screenValue;
    }

    /**
     * 设置屏幕分辨率对应的百分比值
     *
     * @param screenValue 百分比值
     */
    public void setScreenValue(double screenValue) {
        this.screenValue = screenValue;
    }

    private void initCalculateSize() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension scrnsize = toolkit.getScreenSize();
        this.screenValue = FRScreen.getByDimension(scrnsize).getValue();
        XLayoutContainer root = FormArea.this.designer.getRootComponent();
        // 7.1.1不放缩放滑块，但表单大小仍按屏幕分辨率调整
//        slidePane.populateBean(screenValue);
        if (root.acceptType(XWFitLayout.class)) {
            XWFitLayout layout = (XWFitLayout) root;
            if ( !AssistUtils.equals(screenValue, DEFAULT_SLIDER) ) {
                reCalculateRoot(screenValue, true);
            } else {
                // 组件间隔啊
                // REPORT-2585 原有的逻辑导致嵌套的tab中的间隔加不上去，会在后续拖动的过程中出问题
                reCalculateDefaultRoot(screenValue, true);
            }
        }
        LayoutUtils.layoutContainer(root);
    }

    //设置宽度的控件及响应事件
    private void addWidthPaneListener() {
        widthPane.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        int width = (int) ((UINumberField) evt.getSource()).getValue();
                        changeWidthPaneValue(width);
                    }
                });
        widthPane.addFocusListener(
                new FocusAdapter() {
                    public void focusLost(FocusEvent e) {
                        // 失去焦点时，可以认为输入结束
                        int width = (int) ((UINumberField) e.getSource()).getValue();
                        changeWidthPaneValue(width);
                    }
                });
    }

    private void changeWidthPaneValue(int width) {
        XWFitLayout layout = (XWFitLayout) designer.getRootComponent();
        if (width != layout.toData().getContainerWidth()) {
            reCalculateWidth(width);
            designer.getEditListenerTable().fireCreatorModified(DesignerEvent.CREATOR_EDITED);
        }
    }

    private void addHeightPaneListener() {
        heightPane.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        int height = (int) ((UINumberField) evt.getSource()).getValue();
                        changeHeightPaneValue(height);
                    }
                });
        heightPane.addFocusListener(
                new FocusAdapter() {
                    public void focusLost(FocusEvent e) {
                        // 失去焦点时，可以认为输入结束
                        int height = (int) ((UINumberField) e.getSource()).getValue();
                        changeHeightPaneValue(height);
                    }
                });
    }

    private void changeHeightPaneValue(int height) {
        XWFitLayout layout = (XWFitLayout) designer.getRootComponent();
        if (height != layout.toData().getContainerHeight()) {
            reCalculateHeight(height);
            designer.getEditListenerTable().fireCreatorModified(DesignerEvent.CREATOR_EDITED);
        }
    }

    private void reCalculateWidth(int width) {
        XLayoutContainer root = FormArea.this.designer.getRootComponent();
        if (root.acceptType(XWFitLayout.class)) {
            XWFitLayout layout = (XWFitLayout) root;
            Dimension d = new Dimension(layout.toData().getContainerWidth(), layout.toData().getContainerHeight());
            Rectangle rec = new Rectangle(d);
            // 容器大小改变时，设下backupBound为其之前的实际大小,以便调整贴边界的组件
            layout.setBackupBound(rec);
            int dW = width - rec.width;
            if (dW == 0) {
                return;
            }
            double percent = (double) dW / rec.width;
            if (percent < 0 && !layout.canReduce(percent)) {
                widthPane.setValue(rec.width);
                return;
            }
            // 布局容器先设大小为实际的高和当前的宽，然后按此调整内部的组件
            layout.setSize(width, rec.height);
            layout.adjustCreatorsWidth(percent);
            if (layout.getNeedAddWidth() > 0) {
                widthPane.setValue(layout.getWidth());
                // 调整之后清零调整量，否则再次缩放到最小值会产生干扰
                layout.setNeedAddWidth(0);
            }
            doReCalculateRoot(width, rec.height, layout);
        }
    }

    private void reCalculateHeight(int height) {
        XLayoutContainer root = FormArea.this.designer.getRootComponent();
        if (root.acceptType(XWFitLayout.class)) {
            XWFitLayout layout = (XWFitLayout) root;
            Dimension d = new Dimension(layout.toData().getContainerWidth(), layout.toData().getContainerHeight());
            Rectangle rec = new Rectangle(d);
            // 容器大小改变时，设下backupBound为其之前的实际大小
            layout.setBackupBound(rec);
            int dH = height - rec.height;
            if (dH == 0) {
                return;
            }
            double percent = (double) dH / rec.height;
            if (percent < 0 && !layout.canReduce(percent)) {
                heightPane.setValue(rec.height);
                return;
            }
            layout.setSize(rec.width, height);
            layout.adjustCreatorsHeight(percent);
            if (layout.getNeedAddHeight() > 0) {
                heightPane.setValue(layout.getHeight());
                // 调整之后清零调整量，否则再次缩放到最小值会产生干扰
                layout.setNeedAddHeight(0);
            }
            doReCalculateRoot(rec.width, height, layout);
        }
    }

    /**
     * 修改大小后，再根据屏幕分辨率调整下
     */
    private void doReCalculateRoot(int width, int height, XWFitLayout layout) {
//    	double value = slidePane.updateBean();
        //重置滑块的值为默认值100
        START_VALUE = DEFAULT_SLIDER;
        if ( AssistUtils.equals(screenValue, DEFAULT_SLIDER) ) {
            layout.getParent().setSize(width, height + designer.getParaHeight());
            FormArea.this.validate();
        } else {
            layout.setBackupGap(screenValue / DEFAULT_SLIDER);
            reCalculateRoot(screenValue, false);
        }
    }

    /**
     * 按照界面大小的百分比值调整root大小
     *
     * @param needCalculateParaHeight 是否需要调整参数界面高度
     * @param value
     */
    private void reCalculateRoot(double value, boolean needCalculateParaHeight) {
        if ( AssistUtils.equals(value, START_VALUE) ) {
            return;
        }
        double percent = (value - START_VALUE) / START_VALUE;
        XLayoutContainer root = FormArea.this.designer.getRootComponent();
        if (root.acceptType(XWFitLayout.class)) {
            XWFitLayout layout = (XWFitLayout) root;
            layout.setContainerPercent(value / DEFAULT_SLIDER);
            traverAndAdjust(layout, percent);
            layout.adjustCreatorsWhileSlide(percent);

            // 拖动滑块，先将内部组件百分比大小计算，再计算容器大小

            Dimension d = new Dimension(layout.getWidth(), layout.getHeight());
            // 自适应布局的父层是border
            if (layout.getParent() != null) {
                int paraHeight = designer.getParaHeight();
                if (needCalculateParaHeight && paraHeight > 0) {
                    designer.setParaHeight(paraHeight);
                    XWBorderLayout parent = (XWBorderLayout) layout.getParent();
                    parent.toData().setNorthSize(paraHeight);
                    parent.removeAll();
                    parent.add(designer.getParaComponent(), WBorderLayout.NORTH);
                    parent.add(designer.getRootComponent(), WBorderLayout.CENTER);
                }
                layout.getParent().setSize(d.width, d.height + paraHeight);
                // 调整自适应布局大小后，同步调整参数界面和border大小，此时刷新下formArea
                FormArea.this.validate();
            }
            START_VALUE = value;
        }
    }

    /**
     * 按照界面大小的百分比值调整root大小
     *
     * @param needCalculateParaHeight 是否需要调整参数界面高度
     * @param value
     */
    private void reCalculateDefaultRoot(double value, boolean needCalculateParaHeight) {
        XLayoutContainer root = FormArea.this.designer.getRootComponent();
        if (root.acceptType(XWFitLayout.class)) {
            XWFitLayout layout = (XWFitLayout) root;
            layout.setContainerPercent(1.0);
            traverAndAdjust(layout, 0.0);
            layout.adjustCreatorsWhileSlide(0.0);

            // 拖动滑块，先将内部组件百分比大小计算，再计算容器大小

            Dimension d = new Dimension(layout.getWidth(), layout.getHeight());
            // 自适应布局的父层是border
            if (layout.getParent() != null) {
                int paraHeight = designer.getParaHeight();
                if (needCalculateParaHeight && paraHeight > 0) {
                    designer.setParaHeight(paraHeight);
                    XWBorderLayout parent = (XWBorderLayout) layout.getParent();
                    parent.toData().setNorthSize(paraHeight);
                    parent.removeAll();
                    parent.add(designer.getParaComponent(), WBorderLayout.NORTH);
                    parent.add(designer.getRootComponent(), WBorderLayout.CENTER);
                }
                layout.getParent().setSize(d.width, d.height + paraHeight);
                // 调整自适应布局大小后，同步调整参数界面和border大小，此时刷新下formArea
                FormArea.this.validate();
            }
        }
    }

    //循环遍历布局，按百分比调整子组件大小
    private void traverAndAdjust(XCreator creator, double percent) {
        for (int i = 0; i < creator.getComponentCount(); i++) {
            Object object = creator.getComponent(i);
            if (object instanceof XCreator) {
                XCreator temp = (XCreator) object;
                temp.adjustCompSize(percent);
                traverAndAdjust(temp, percent);
            }
        }

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
                onMouseWheelScroll(evt);
                break;
            }
            default:
        }
    }

    private void onMouseWheelScroll(MouseWheelEvent evt) {
        int value = this.verScrollBar.getValue() + evt.getWheelRotation() * ROTATIONS;
        value = Math.max(0, Math.min(value, verticalMax));
        doLayout(); //加dolayout是因为每次滚动都要重置 Max的大小
        this.verScrollBar.setValue(value);
    }

    /**
     * 返回表单容器的中心designer
     * getFormEditor.
     */
    public FormDesigner getFormEditor() {
        return designer;
    }

    private boolean shouldSetScrollValue(XCreator creator) {
        return !isValid || designer.isRoot(creator) || getDesignerWidth() >= designer.getRootComponent().getWidth();
    }

    /**
     * 设置界面内的组件可见以及水平垂直滚动条的值
     * （除了根容器，拖入组件进来时如果大小超过当前界面大小，必须设置滚动条值，否则滚动条默认不显示）
     *
     * @param creator 控件
     */
    public void scrollPathToVisible(XCreator creator) {
        creator.seleteRelatedComponent(creator);

        if (!ComponentUtils.isComponentVisible(creator) && !designer.isRoot(creator)) {
            designer.makeVisible(creator);
        }

        if (shouldSetScrollValue(creator)) {
            return;
        }
        //获取在容器的绝对位置
        Rectangle rec = ComponentUtils.getRelativeBounds(creator);
        int dWidth = getDesignerWidth();
        if (rec.width <= dWidth && rec.x < getHorizontalValue()) {
            //在边界内部且x位置小于水平滚动条的值
            horScrollBar.setValue(rec.x);
        } else if (rec.x + rec.width > dWidth + horizontalValue) {
            //超出边界宽度
            horScrollBar.setValue(rec.x + rec.width - dWidth);
        }
        int dHeight = getDesignerHeight();
        if (rec.height < dHeight && rec.y < getVerticalValue()) {
            //在边界内部且y位置小于竖直滚动条的值
            verScrollBar.setValue(rec.y);
        } else if (rec.y + rec.height > dHeight + verticalValue) {
            //超出边界高度
            verScrollBar.setValue(rec.y + rec.height - dHeight);
        }
    }


    /**
     * 容器布局
     */
    public void doLayout() {
        layout();
        if (isValid) {
            XLayoutContainer root = designer.getRootComponent();
            setScrollBarProperties(root.getWidth() - designer.getWidth(), horScrollBar, horizontalValue);
            //计算滚动条值的时候应该算上参数面板的高度
            setScrollBarProperties(designer.getParaHeight() + root.getHeight() - designer.getHeight(), verScrollBar, verticalValue);
        }
    }

    /**
     * 设置滚动条的属性
     */
    private void setScrollBarProperties(int value, FormScrollBar bar, int oldValue) {
        if (value == 0 && isScrollNotVisible(bar)) {
            return;
        }
        if (value <= 0) {
            // 界面有滚动条时，手动缩小容器宽度到界面内，重置滚动条值和max
            setScrollBarMax(0, bar);
            bar.setMaximum(0);
            bar.setValue(0);
            bar.setEnabled(false);
        } else {
            setScrollBarMax(value, bar);
            bar.setEnabled(true);
            bar.setMaximum(value);
            bar.setValue(value);
            //参数面板拖拽过程中value一直为当前value
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
        return designer.getDesignerMode().getMinDesignHeight();
    }

    /**
     * 返回designer的最小宽度
     *
     * @return int
     */
    public int getMinWidth() {
        return designer.getDesignerMode().getMinDesignWidth();
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
     * 返回界面大小的百分比值
     *
     * @return 百分比值
     */
    public double getSlideValue() {
//    	return slidePane.updateBean();
        //7.1.1不加缩放滑块
        return this.screenValue;
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
        this.verticalMax = (int) totalSize.getHeight();
        this.horicalMax = (int) totalSize.getHeight();
        // 撤销时会refreshRoot，导致layout大小变为默认大小
        // 按照之前设置的宽高和百分比重置下容器size
        if ( !AssistUtils.equals(width, widthPane.getValue()) ) {
            widthPane.setValue(width);
            reCalculateWidth((int) width);
        }
        if ( !AssistUtils.equals(height, heightPane.getValue()) ) {
            heightPane.setValue(height);
            reCalculateHeight((int) height);
        }
        if (designer.getRootComponent().acceptType(XWFitLayout.class) && AssistUtils.equals(slide, DEFAULT_SLIDER) ) {
            XWFitLayout layout = (XWFitLayout) designer.getRootComponent();
            // 撤销时先refreshRoot了，此处去掉内边距再增加间隔
            layout.moveContainerMargin();
            layout.addCompInterval(layout.getAcualInterval());
        } else if (designer.getRootComponent().acceptType(XWFitLayout.class)) {
            START_VALUE = DEFAULT_SLIDER;
            reCalculateRoot(slide, true);
//    		slidePane.populateBean(slide);
        }
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
        private static final int DESIGNER_WIDTH = 960;
        private static final int DESIGNER_HEIGHT = 540;
        private static final int TOPGAP = 8;

        private int DESIGNERWIDTH = DESIGNER_WIDTH;
        private int DESIGNERHEIGHT = DESIGNER_HEIGHT;

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
                FormDesigner dg = ((FormDesigner) designer);
                XLayoutContainer root = dg.getRootComponent();
                if (root.acceptType(XWFitLayout.class)) {
                    DESIGNERWIDTH = root.getWidth();
                    DESIGNERHEIGHT = dg.hasWAbsoluteLayout() ? root.getHeight() + dg.getParaHeight() : root.getHeight();
                }
                Rectangle rec = new Rectangle(left + (right - DESIGNERWIDTH) / 2, TOPGAP, right, bottom);
                //是否为表单
                if (isValid) {
                    if (hbarPreferredSize == null) {
                        throw new IllegalArgumentException("hbarPreferredSize can not be null!");
                    }
                    int maxHeight = bottom - hbarPreferredSize.height - resize.height - TOPGAP * 2;
                    int maxWidth = right - vbarPreferredSize.width;
                    DESIGNERWIDTH = DESIGNERWIDTH > maxWidth ? maxWidth : DESIGNERWIDTH;
                    DESIGNERHEIGHT = DESIGNERHEIGHT > maxHeight ? maxHeight : DESIGNERHEIGHT;
                    int designerLeft = left + (verScrollBar.getX() - DESIGNERWIDTH) / 2;
                    rec = new Rectangle(designerLeft, TOPGAP, DESIGNERWIDTH, DESIGNERHEIGHT);
                }
                // designer是整个表单设计界面中的面板部分，目前只放自适应布局和参数界面。
                designer.setBounds(rec);
            }
        }

    }

}
