package com.fr.design.widget.ui.designer.mobile;

import com.fr.base.iofile.attr.FormTabPaddingAttrMark;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.designer.IntervalConstants;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.form.util.FormDesignerUtils;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.WidgetPropertyPane;
import com.fr.design.mainframe.widget.accessibles.AccessibleTemplateStyleEditor;
import com.fr.design.mainframe.widget.accessibles.MobileTemplateStylePane;
import com.fr.design.widget.ui.designer.mobile.component.MobileComponentAdvancePane;
import com.fr.design.widget.ui.designer.mobile.component.MobileComponentLayoutIntervalPane;
import com.fr.form.ui.container.WBodyLayoutType;
import com.fr.form.ui.container.WFitLayout;
import com.fr.form.ui.container.cardlayout.WCardTagLayout;
import com.fr.general.cardtag.mobile.MobileTemplateStyle;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;

public class TabMobileWidgetDefinePane extends MobileWidgetDefinePane {
    private XCreator xCreator;
    private FormDesigner designer; // 当前设计器
    private AccessibleTemplateStyleEditor templateStyleEditor;
    private AttributeChangeListener changeListener;
    private MobileComponentAdvancePane advancePane;
    private MobileComponentLayoutIntervalPane intervalPane;

    public TabMobileWidgetDefinePane(XCreator xCreator) {
        this.xCreator = xCreator;
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
    public void initPropertyGroups(Object source) {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.designer = WidgetPropertyPane.getInstance().getEditingFormDesigner();
        UILabel label = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Tab_Style_Template"));
        templateStyleEditor = new AccessibleTemplateStyleEditor(new MobileTemplateStylePane((WCardTagLayout) xCreator.toData()));
        JPanel jPanel = TableLayoutHelper.createGapTableLayoutPane(new Component[][]{new Component[]{label, templateStyleEditor}, {new UILabel()}}, TableLayoutHelper.FILL_LASTCOLUMN, IntervalConstants.INTERVAL_L1, LayoutConstants.VGAP_LARGE);
        JPanel holder = FRGUIPaneFactory.createBorderLayout_S_Pane();
        holder.add(jPanel, BorderLayout.NORTH);
        if (!shouldHidePadding()) {
            advancePane = new MobileComponentAdvancePane(FormTabPaddingAttrMark.XML_TAG);
            intervalPane = new MobileComponentLayoutIntervalPane(FormTabPaddingAttrMark.XML_TAG);
            //高级
            holder.add(advancePane, BorderLayout.CENTER);
            //布局
            holder.add(intervalPane, BorderLayout.SOUTH);
        }

        this.add(holder, BorderLayout.NORTH);
    }

    @Override
    public void populate(FormDesigner designer) {
        templateStyleEditor.setValue(((WCardTagLayout) xCreator.toData()).getMobileTemplateStyle());
        // 数据 populate 完成后，再设置监听
        this.bindListeners2Widgets();
        this.addAttributeChangeListener(changeListener);
        if (advancePane != null) {//业务层面可以写成shouldHidePadding但是这样写应该性能差点
            advancePane.populate((WCardTagLayout) xCreator.toData());
        }
        if (intervalPane != null) {
            intervalPane.populate((WCardTagLayout) xCreator.toData());
        }
    }

    @Override
    public void update() {
        ((WCardTagLayout) xCreator.toData()).setMobileTemplateStyle((MobileTemplateStyle) templateStyleEditor.getValue());
        DesignerContext.getDesignerFrame().getSelectedJTemplate().fireTargetModified(); // 触发设计器保存按钮亮起来
        if (advancePane != null) {
            advancePane.update((WCardTagLayout) xCreator.toData());
        }
        if (intervalPane != null) {
            intervalPane.update((WCardTagLayout) xCreator.toData());
        }
    }

    // body是否是绝对布局
    private boolean isBodyAbsoluteRelayout() {
        WFitLayout root = ((WFitLayout) designer.getRootComponent().toData());
        return root.getBodyLayoutType() == WBodyLayoutType.ABSOLUTE;
    }

    /**
     * 绝对布局且不勾选手机重布局 的时候不支持边距设置
     *
     * @return
     */
    private boolean shouldHidePadding() {
        return !FormDesignerUtils.isAppRelayout(designer) && isBodyAbsoluteRelayout();
    }
}
