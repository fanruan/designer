package com.fr.design.widget.ui.designer.mobile;

import com.fr.design.designer.beans.events.DesignerEvent;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.MobileWidgetListPane;
import com.fr.design.mainframe.WidgetPropertyPane;
import com.fr.form.ui.container.WSortLayout;


import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;

/**
 * Created by plough on 2018/2/5.
 */
public class ParaMobileDefinePane extends MobileWidgetDefinePane {
    private XCreator paraCreator;
    private FormDesigner designer;
    private AttributeChangeListener changeListener;
    private MobileWidgetListPane mobileWidgetListPane;

    public ParaMobileDefinePane(XCreator xCreator) {
        this.paraCreator = xCreator;
    }

    @Override
    public void initPropertyGroups(Object source) {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.designer = WidgetPropertyPane.getInstance().getEditingFormDesigner();
        this.add(getMobileWidgetListPane(), BorderLayout.CENTER);
        this.repaint();
    }

    // 控件顺序
    private UIExpandablePane getMobileWidgetListPane() {
        mobileWidgetListPane = new MobileWidgetListPane(designer, (WSortLayout) paraCreator.toData());
        mobileWidgetListPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        JPanel panelWrapper = FRGUIPaneFactory.createBorderLayout_S_Pane();
        panelWrapper.add(mobileWidgetListPane, BorderLayout.CENTER);

        return new UIExpandablePane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Widget_Order"), 280, 20, panelWrapper);
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

        // 设置监听
        this.bindListeners2Widgets();
        this.addAttributeChangeListener(changeListener);
    }

    @Override
    public void update() {
        mobileWidgetListPane.updateToDesigner();
        designer.getEditListenerTable().fireCreatorModified(DesignerEvent.CREATOR_EDITED);
    }
}
