package com.fr.design.widget.ui.designer.mobile;

import com.fr.base.iofile.attr.FormTabPaddingAttrMark;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.designer.IntervalConstants;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.WidgetPropertyPane;
import com.fr.design.mainframe.widget.accessibles.AccessibleTemplateStyleEditor;
import com.fr.design.mainframe.widget.accessibles.MobileTemplateStylePane;
import com.fr.design.widget.ui.designer.mobile.component.MobileComponentMarginPane;
import com.fr.design.widget.ui.designer.mobile.component.MobileComponentLayoutIntervalPane;
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
    private MobileComponentMarginPane marginPane;
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
        if (!shouldHidePadding(designer)) {
            marginPane = new MobileComponentMarginPane(FormTabPaddingAttrMark.XML_TAG);
            intervalPane = new MobileComponentLayoutIntervalPane(FormTabPaddingAttrMark.XML_TAG);
            UIExpandablePane advancePane = new UIExpandablePane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Advanced"), 280, 20, marginPane);
            UIExpandablePane layoutPane = new UIExpandablePane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Layout"), 280, 20, intervalPane);
            //高级
            holder.add(advancePane, BorderLayout.CENTER);
            //布局
            holder.add(layoutPane, BorderLayout.SOUTH);
        }

        this.add(holder, BorderLayout.NORTH);
    }

    @Override
    public void populate(FormDesigner designer) {
        templateStyleEditor.setValue(((WCardTagLayout) xCreator.toData()).getMobileTemplateStyle());
        // 数据 populate 完成后，再设置监听
        this.bindListeners2Widgets();
        this.addAttributeChangeListener(changeListener);
        if (marginPane != null) {//业务层面可以写成shouldHidePadding但是这样写应该性能差点
            marginPane.populate((WCardTagLayout) xCreator.toData());
        }
        if (intervalPane != null) {
            intervalPane.populate((WCardTagLayout) xCreator.toData());
        }
    }

    @Override
    public void update() {
        ((WCardTagLayout) xCreator.toData()).setMobileTemplateStyle((MobileTemplateStyle) templateStyleEditor.getValue());
        DesignerContext.getDesignerFrame().getSelectedJTemplate().fireTargetModified(); // 触发设计器保存按钮亮起来
        if (marginPane != null) {
            marginPane.update((WCardTagLayout) xCreator.toData());
        }
        if (intervalPane != null) {
            intervalPane.update((WCardTagLayout) xCreator.toData());
        }
    }
}
