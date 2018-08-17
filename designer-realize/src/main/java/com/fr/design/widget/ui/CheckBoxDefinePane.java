package com.fr.design.widget.ui;

import com.fr.design.designer.IntervalConstants;
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.form.ui.CheckBox;


import javax.swing.*;
import java.awt.*;

public class CheckBoxDefinePane extends AbstractDataModify<CheckBox> {
    private UITextField text;

    public CheckBoxDefinePane() {
        this.iniComponents();
    }

    private void iniComponents() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        text = new UITextField();
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Text")), text},
        };
        double[] rowSize = {p};
        double[] columnSize = {p, f};
        int[][] rowCount = {{1, 1}};
        JPanel pane = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount,  IntervalConstants.INTERVAL_W3, IntervalConstants.INTERVAL_L1);

        UIExpandablePane uiExpandablePane = new UIExpandablePane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Advanced"), 280, 24, pane);
        pane.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 0));

        this.add(uiExpandablePane);
    }

    @Override
    protected String title4PopupWindow() {
        return "CheckBox";
    }

    @Override
    public void populateBean(CheckBox check) {
        text.setText(check.getText());
    }

    @Override
    public CheckBox updateBean() {
        CheckBox box = new CheckBox();
        box.setText(text.getText());
        return box;
    }
}
