package com.fr.design.widget.ui.designer.mobile;

import com.fr.design.designer.creator.XCreator;
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.widget.ui.designer.mobile.component.MobileAdvanceInnerPane;
import com.fr.design.widget.ui.designer.mobile.component.MobileBookMarkUsePane;

import javax.swing.*;
import java.awt.*;

/**
 * @author hades
 * @version 10.0
 * Created by hades on 2020/4/18
 */
public class MobileBookMarkCombinePane extends MobileWidgetDefinePane {

    private MobileBookMarkUsePane mobileBookMarkUsePane;
    private MobileAdvanceInnerPane mobileAdvanceInnerPane;
    private XCreator xCreator;


    public MobileBookMarkCombinePane(XCreator xCreator) {
        this.xCreator = xCreator;
    }

    @Override
    public void initPropertyGroups(Object source) {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        JPanel panel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        this.mobileAdvanceInnerPane = new MobileAdvanceInnerPane(xCreator);
        this.mobileBookMarkUsePane = new MobileBookMarkUsePane();
        panel.add(this.mobileBookMarkUsePane, BorderLayout.NORTH);
        panel.add(this.mobileAdvanceInnerPane, BorderLayout.CENTER);
        UIExpandablePane uiExpandablePane = new UIExpandablePane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Advanced"), 280, 20, panel);
        this.add(uiExpandablePane, BorderLayout.NORTH);
    }

    private void bindListeners2Widgets() {
        reInitAllListeners();
        AttributeChangeListener changeListener = new AttributeChangeListener() {
            @Override
            public void attributeChange() {
                update();
            }
        };
        this.addAttributeChangeListener(changeListener);
    }

    private void reInitAllListeners() {
        initListener(this);
    }

    @Override
    public void populate(FormDesigner designer) {
        this.mobileBookMarkUsePane.populate(xCreator);
        this.mobileAdvanceInnerPane.populate();
        this.bindListeners2Widgets();
    }

    @Override
    public void update() {
        this.mobileBookMarkUsePane.update(xCreator);
        this.mobileAdvanceInnerPane.update();
    }
}
