package com.fr.design.widget.ui.designer.mobile;

import com.fr.design.designer.creator.XCreator;
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.gui.frpane.AttributeChangeListener;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.FormDesigner;
import com.fr.design.widget.ui.designer.mobile.component.MobileAdvanceInnerPane;

import javax.swing.*;
import java.awt.*;

/**
 * @author hades
 * @version 10.0
 * Created by hades on 2019/12/24
 */
public class MobileAdvanceDefinePane extends MobileWidgetDefinePane {

    private XCreator xCreator;
    private MobileAdvanceInnerPane innerPane;

    public MobileAdvanceDefinePane(XCreator xCreator) {
        this.xCreator = xCreator;
    }

    @Override
    public void initPropertyGroups(Object source) {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.innerPane = new MobileAdvanceInnerPane(xCreator);
        UIExpandablePane uiExpandablePane = new UIExpandablePane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Mobile_BookMark"), 280, 20, innerPane);
        JPanel wrapPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        wrapPane.add(uiExpandablePane, BorderLayout.NORTH);
        this.add(wrapPane, BorderLayout.NORTH);
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
    public Dimension getPreferredSize() {
        return new Dimension(super.getPreferredSize().width, 80);
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(400, 200);
    }

    @Override
    public void populate(FormDesigner designer) {
        this.innerPane.populate();
        this.bindListeners2Widgets();
    }

    @Override
    public void update() {
        this.innerPane.update();
    }

}
