package com.fr.design.widget.ui.designer.layout;

import com.fr.design.data.DataCreatorUI;
import com.fr.design.designer.IntervalConstants;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.widget.accessibles.AccessibleWLayoutBorderStyleEditor;
import com.fr.design.widget.ui.designer.AbstractDataModify;
import com.fr.form.ui.AbstractBorderStyleWidget;
import com.fr.form.ui.LayoutBorderStyle;


import javax.swing.*;
import java.awt.*;

/**
 * Created by kerry on 2017/8/29.
 */
public class BorderStyleWidgetDefinePane extends AbstractDataModify<AbstractBorderStyleWidget> {
    private AccessibleWLayoutBorderStyleEditor borderStyleEditor;

    public BorderStyleWidgetDefinePane(XCreator xCreator) {
        super(xCreator);
        initComponent();
    }


    public void initComponent() {
        borderStyleEditor = new AccessibleWLayoutBorderStyleEditor();
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        JPanel advancePane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        JPanel jPanel = TableLayoutHelper.createGapTableLayoutPane(new Component[][]{
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("FR-Designer-Widget_Style")), borderStyleEditor}}, TableLayoutHelper.FILL_LASTCOLUMN, IntervalConstants.INTERVAL_W3, IntervalConstants.INTERVAL_L1);
        jPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        advancePane.add(jPanel, BorderLayout.CENTER);

        UIExpandablePane layoutExpandablePane = new UIExpandablePane(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Advanced"), 280, 20, advancePane );

        this.add(layoutExpandablePane, BorderLayout.CENTER);
    }


    @Override
    public String title4PopupWindow() {
        return "borderStyleWidget";
    }

    @Override
    public void populateBean(AbstractBorderStyleWidget ob) {
        borderStyleEditor.setValue(ob.getBorderStyle());
    }


    @Override
    public AbstractBorderStyleWidget updateBean() {
        AbstractBorderStyleWidget abstractBorderStyleWidget = (AbstractBorderStyleWidget)creator.toData();
        abstractBorderStyleWidget.setBorderStyle((LayoutBorderStyle) borderStyleEditor.getValue());
        return abstractBorderStyleWidget;

    }


    @Override
    public DataCreatorUI dataUI() {
        return null;
    }

}