package com.fr.design.widget.ui.designer.mobile;

import com.fr.design.designer.creator.XCreator;
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.MobileWidgetListPane;
import com.fr.design.mainframe.WidgetPropertyPane;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Created by plough on 2018/2/1.
 */
public class BodyMobileDefinePane extends MobileWidgetDefinePane {
    private XCreator xCreator; // 当前选中控件的xCreator
    private FormDesigner designer;
    private AttributeChangeListener changeListener;

    public BodyMobileDefinePane(XCreator xCreator) {
        this.xCreator = xCreator;
    }

    @Override
    protected void initContentPane() {}

    @Override
    protected JPanel createContentPane() {
        return new JPanel();
    }

    @Override
    public String getIconPath() {
        return StringUtils.EMPTY;
    }

    @Override
    public String title4PopupWindow() {
        return StringUtils.EMPTY;
    }


    @Override
    public void initPropertyGroups(Object source) {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.designer = WidgetPropertyPane.getInstance().getEditingFormDesigner();
        this.add(getMobilePropertyPane(), BorderLayout.NORTH);
        this.add(getMobileWidgetListPane(), BorderLayout.CENTER);
        this.repaint();
    }

    // 手机属性
    private UIExpandablePane getMobilePropertyPane() {
        JPanel panel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        UICheckBox relayoutCheck = new UICheckBox("手机重布局", true);
        relayoutCheck.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        panel.add(relayoutCheck);

        final JPanel panelWrapper = FRGUIPaneFactory.createBorderLayout_S_Pane();
        panelWrapper.add(panel, BorderLayout.NORTH);

        return new UIExpandablePane("手机属性", 280, 20, panelWrapper);
    }

    // 控件顺序
    private UIExpandablePane getMobileWidgetListPane() {
        JPanel panel = new MobileWidgetListPane(designer);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        JPanel panelWrapper = FRGUIPaneFactory.createBorderLayout_S_Pane();
        panelWrapper.add(panel, BorderLayout.CENTER);

        return new UIExpandablePane("控件顺序", 280, 20, panelWrapper);
    }

    private void bindListeners2Widgets() {
        reInitAllListeners();
        this.changeListener = new AttributeChangeListener() {
            @Override
            public void attributeChange() {
                update();
            }
        };
    }

    /**
     * 后台初始化所有事件.
     */
    private void reInitAllListeners() {
        initListener(this);
    }

    @Override
    public void populate(FormDesigner designer) {
        this.designer = designer;

//        if (!isAppRelayout()) {
//            return;
//        }
//
//        BaseChartEditor chartEditor = (BaseChartEditor)xCreator.toData();
//        ChartMobileFitAttrStateProvider zoomOutAttr = chartEditor.getMobileAttr().getZoomOutAttr();
//
//        // 数据 populate 完成后，再设置监听
//        this.bindListeners2Widgets();
//        this.addAttributeChangeListener(changeListener);
    }

    @Override
    public void update() {
//        ChartMobileAttrProvider mobileAttr = ((BaseChartEditor)xCreator.toData()).getMobileAttr();
//        mobileAttr.setZoomInAttr(ChartMobileFitAttrState.PROPORTION);
//        mobileAttr.setZoomOutAttr((ChartMobileFitAttrState)((Item)zoomOutComboBox.getSelectedItem()).getValue());
//        DesignerContext.getDesignerFrame().getSelectedJTemplate().fireTargetModified(); // 触发设计器保存按钮亮起来
    }
}
