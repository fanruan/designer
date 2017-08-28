package com.fr.design.widget.ui;

import com.fr.design.constants.LayoutConstants;
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.form.ui.CheckBox;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;

public class CheckBoxDefinePane extends AbstractDataModify<CheckBox> {
    private UITextField text;

    public CheckBoxDefinePane() {
        this.iniComoponents();
    }

    private void iniComoponents() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        text = new UITextField();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(text, BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 35, 0, 0));

        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("FR-Designer_Text")), panel},
        };
        double[] rowSize = {p};
        double[] columnSize = {p, f};
        int[][] rowCount = {{1, 1}};
        JPanel pane = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, LayoutConstants.VGAP_LARGE, LayoutConstants.VGAP_LARGE);

        UIExpandablePane uiExpandablePane = new UIExpandablePane(Inter.getLocText("FR-Designer_Advanced"), 280, 24, pane);
        pane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 15));

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