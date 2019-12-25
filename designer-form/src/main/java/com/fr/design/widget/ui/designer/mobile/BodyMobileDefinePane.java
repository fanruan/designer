package com.fr.design.widget.ui.designer.mobile;

import com.fr.base.iofile.attr.FormBodyPaddingAttrMark;
import com.fr.design.designer.beans.events.DesignerEvent;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.form.util.FormDesignerUtils;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.MobileWidgetListPane;
import com.fr.design.mainframe.WidgetPropertyPane;
import com.fr.design.widget.ui.designer.mobile.component.MobileBookMarkSettingPane;
import com.fr.design.widget.ui.designer.mobile.component.MobileComponentFrozenPane;
import com.fr.design.widget.ui.designer.mobile.component.MobileComponentMarginPane;
import com.fr.design.widget.ui.designer.mobile.component.MobileComponentLayoutIntervalPane;
import com.fr.form.ui.RichStyleWidgetProvider;
import com.fr.form.ui.container.WFitLayout;
import com.fr.form.ui.container.WSortLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;

/**
 * Created by plough on 2018/2/1.
 */
public class BodyMobileDefinePane extends MobileWidgetDefinePane {
    private XCreator bodyCreator;
    private FormDesigner designer;
    private AttributeChangeListener changeListener;
    private UICheckBox appRelayoutCheck;
    private MobileWidgetListPane mobileWidgetListPane;
    private MobileComponentMarginPane marginPane;
    private MobileComponentLayoutIntervalPane intervalPane;
    private MobileComponentFrozenPane frozenPane;
    private MobileBookMarkSettingPane bookMarkSettingPane;
    private UIExpandablePane advancePane;
    private UIExpandablePane layoutPane;

    public BodyMobileDefinePane(XCreator xCreator) {
        this.bodyCreator = xCreator;
    }

    public XCreator getBodyCreator() {
        return bodyCreator;
    }

    public void setBodyCreator(XCreator bodyCreator) {
        this.bodyCreator = bodyCreator;
    }

    public FormDesigner getDesigner() {
        return designer;
    }

    public void setDesigner(FormDesigner designer) {
        this.designer = designer;
    }

    @Override
    public void initPropertyGroups(Object source) {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.designer = WidgetPropertyPane.getInstance().getEditingFormDesigner();
        this.add(createNorthPane(), BorderLayout.NORTH);
        this.add(getMobileWidgetListPane(), BorderLayout.CENTER);
        this.repaint();
    }

    // 手机属性
    public UIExpandablePane getMobilePropertyPane() {
        JPanel panel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        appRelayoutCheck = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_App_ReLayout"), true);
        appRelayoutCheck.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        panel.add(appRelayoutCheck);

        final JPanel panelWrapper = FRGUIPaneFactory.createBorderLayout_S_Pane();
        panelWrapper.add(panel, BorderLayout.NORTH);

        return new UIExpandablePane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Properties_Mobile"), 280, 20, panelWrapper);
    }

    // 控件顺序
    public UIExpandablePane getMobileWidgetListPane() {
        mobileWidgetListPane = new MobileWidgetListPane(designer, (WSortLayout) bodyCreator.toData());
        mobileWidgetListPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        JPanel panelWrapper = FRGUIPaneFactory.createBorderLayout_S_Pane();
        panelWrapper.add(mobileWidgetListPane, BorderLayout.CENTER);

        return new UIExpandablePane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Widget_Order"), 280, 20, panelWrapper);
    }

    private JPanel createNorthPane() {
        JPanel holder = FRGUIPaneFactory.createBorderLayout_S_Pane();
        holder.add(getMobilePropertyPane(), BorderLayout.NORTH);
        marginPane = new MobileComponentMarginPane(FormBodyPaddingAttrMark.XML_TAG);
        intervalPane = new MobileComponentLayoutIntervalPane(FormBodyPaddingAttrMark.XML_TAG);
        frozenPane = new MobileComponentFrozenPane();
        bookMarkSettingPane = new MobileBookMarkSettingPane();
        JPanel wrapLayoutPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        JPanel wrapAdvancePane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        wrapLayoutPane.add(intervalPane, BorderLayout.NORTH);
        wrapLayoutPane.add(frozenPane, BorderLayout.CENTER);
        wrapAdvancePane.add(marginPane, BorderLayout.CENTER);
        wrapAdvancePane.add(bookMarkSettingPane, BorderLayout.SOUTH);
        advancePane = new UIExpandablePane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Advanced"), 280, 20, wrapAdvancePane);
        layoutPane = new UIExpandablePane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Layout"), 280, 20, wrapLayoutPane);
        //高级
        holder.add(advancePane, BorderLayout.CENTER);
        //布局
        holder.add(layoutPane, BorderLayout.SOUTH);

        boolean flag = !shouldHidePadding(designer);
        advancePane.setVisible(flag);
        layoutPane.setVisible(flag);
        frozenPane.setVisible(appRelayoutCheck.isSelected());
        bookMarkSettingPane.setVisible(appRelayoutCheck.isSelected());
        return holder;
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


    private void setAppRelayout(boolean appRelayoutSeleted) {
        if (appRelayoutSeleted == FormDesignerUtils.isAppRelayout(designer)) {
            return;
        }
        try {
            ((WFitLayout) designer.getRootComponent().toData()).setAppRelayout(appRelayoutSeleted);
        } catch (Exception e) {
            // do nothing
        }
    }

    @Override
    public void populate(FormDesigner designer) {
        this.designer = designer;
        XCreator xCreator = designer.getSelectionModel().getSelection().getSelectedCreator();
        appRelayoutCheck.setSelected(FormDesignerUtils.isAppRelayout(designer));
        // 数据 populate 完成后，再设置监听
        this.bindListeners2Widgets();
        this.addAttributeChangeListener(changeListener);
        marginPane.populate((RichStyleWidgetProvider) getBodyCreator().toData());
        intervalPane.populate((RichStyleWidgetProvider) getBodyCreator().toData());
        frozenPane.populate(xCreator);
        bookMarkSettingPane.populate(xCreator);
    }

    @Override
    public void update() {
        boolean appRelayout = appRelayoutCheck.isSelected();
        setAppRelayout(appRelayoutCheck.isSelected());
        boolean appPaddingVisible = appRelayout || !FormDesignerUtils.isBodyAbsolute(designer);
        advancePane.setVisible(appPaddingVisible);
        layoutPane.setVisible(appPaddingVisible);
        frozenPane.setVisible(appRelayout);
        mobileWidgetListPane.updateToDesigner();
        designer.getEditListenerTable().fireCreatorModified(DesignerEvent.CREATOR_EDITED);

        if (advancePane.isVisible()) {
            marginPane.update((RichStyleWidgetProvider) getBodyCreator().toData());
        }
        if (layoutPane.isVisible()) {
            intervalPane.update((RichStyleWidgetProvider) getBodyCreator().toData());
        }
        if (appRelayout) {
            XCreator xCreator = designer.getSelectionModel().getSelection().getSelectedCreator();
            frozenPane.update(xCreator);
            bookMarkSettingPane.update(xCreator);
        }
    }
}
