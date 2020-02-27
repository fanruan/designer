package com.fr.design.widget.ui.designer.mobile;

import com.fr.design.designer.creator.XCreator;
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.widget.ui.designer.mobile.component.MobileBookMarkUsePane;

import javax.swing.JPanel;
import java.awt.BorderLayout;

/**
 * @author hades
 * @version 10.0
 * Created by hades on 2020/2/12
 */
public class MobileBookMarkDefinePane extends MobileWidgetDefinePane {

    private XCreator xCreator;
    private MobileBookMarkUsePane mobileBookMarkUsePane;

    public MobileBookMarkDefinePane(XCreator xCreator) {
        this.xCreator = xCreator;
    }

    @Override
    public void initPropertyGroups(Object source) {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        JPanel panel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        this.mobileBookMarkUsePane = new MobileBookMarkUsePane();
        UIExpandablePane uiExpandablePane = new UIExpandablePane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Advanced"), 280, 20, mobileBookMarkUsePane);
        panel.add(uiExpandablePane);
        this.add(panel, BorderLayout.NORTH);
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
        this.bindListeners2Widgets();
    }

    @Override
    public void update() {
        this.mobileBookMarkUsePane.update(xCreator);
    }
}
