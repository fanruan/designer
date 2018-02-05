package com.fr.design.widget.ui.designer.mobile;

import com.fr.design.designer.beans.events.DesignerEvent;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.mainframe.MobileWidgetListPane;
import com.fr.design.mainframe.WidgetPropertyPane;
import com.fr.general.Inter;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Method;

/**
 * Created by plough on 2018/2/1.
 */
public class BodyMobileDefinePane extends MobileWidgetDefinePane {
    private XCreator bodyCreator;
    private FormDesigner designer;
    private AttributeChangeListener changeListener;
    private UICheckBox appRelayoutCheck;
    private MobileWidgetListPane mobileWidgetListPane;

    public BodyMobileDefinePane(XCreator xCreator) {
        this.bodyCreator = xCreator;
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
        appRelayoutCheck = new UICheckBox(Inter.getLocText("FR-Designer-App_ReLayout"), true);
        appRelayoutCheck.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        panel.add(appRelayoutCheck);

        final JPanel panelWrapper = FRGUIPaneFactory.createBorderLayout_S_Pane();
        panelWrapper.add(panel, BorderLayout.NORTH);

        return new UIExpandablePane(Inter.getLocText("FR-Designer_Properties_Mobile"), 280, 20, panelWrapper);
    }

    // 控件顺序
    private UIExpandablePane getMobileWidgetListPane() {
        mobileWidgetListPane = new MobileWidgetListPane(designer);
        mobileWidgetListPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        JPanel panelWrapper = FRGUIPaneFactory.createBorderLayout_S_Pane();
        panelWrapper.add(mobileWidgetListPane, BorderLayout.CENTER);

        return new UIExpandablePane(Inter.getLocText("FR-Designer_WidgetOrder"), 280, 20, panelWrapper);
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

    // body是否开启手机重布局
    private boolean isAppRelayout() {
        boolean result = false;
        try {
            Method m = bodyCreator.toData().getClass().getMethod("isAppRelayout");
            result = (boolean)m.invoke(bodyCreator.toData());
        } catch (Exception e) {
            // do nothing
        }
        return result;
    }

    private void setAppRelayout(boolean appRelayoutSeleted) {
        if (appRelayoutSeleted == isAppRelayout()) {
            return;
        }
        try {
            Method m = bodyCreator.toData().getClass().getMethod("setAppRelayout", boolean.class);
            m.invoke(bodyCreator.toData(), appRelayoutSeleted);
        } catch (Exception e) {
            // do nothing
        }
    }

    @Override
    public void populate(FormDesigner designer) {
        this.designer = designer;
        appRelayoutCheck.setSelected(isAppRelayout());

        // 数据 populate 完成后，再设置监听
        this.bindListeners2Widgets();
        this.addAttributeChangeListener(changeListener);
    }

    @Override
    public void update() {
        setAppRelayout(appRelayoutCheck.isSelected());
        mobileWidgetListPane.updateToDesigner();
        designer.getEditListenerTable().fireCreatorModified(DesignerEvent.CREATOR_EDITED);
    }
}
