package com.fr.design.widget.ui.designer;

import com.fr.design.designer.creator.XCreator;
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.widget.ui.designer.component.FormWidgetValuePane;
import com.fr.form.ui.Label;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;


/**
 * Created by ibm on 2017/8/3.
 */
public class LabelDefinePane extends AbstractDataModify<Label> {

    public LabelDefinePane(XCreator xCreator) {
        super(xCreator);
        initComponent();
    }

    public void initComponent() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        JPanel advancePane = createAdvancePane();
        UIExpandablePane advanceExpandablePane = new UIExpandablePane(Inter.getLocText("FR-Designer_Advanced"), 280, 20, advancePane);
        this.add(advanceExpandablePane, BorderLayout.CENTER);
    }

    public JPanel createAdvancePane() {
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p, p, p, p, p, p, p};
        double[] columnSize = {p, f};
        int[][] rowCount = {{1, 3}, {1, 1}, {1, 1}, {1, 1}, {1, 1}};
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("FR-Designer-Estate_Widget_Value")), new FormWidgetValuePane()},
                new Component[]{new UICheckBox(Inter.getLocText("FR-Designer_StyleAlignment-Wrap_Text")), null},
                new Component[]{new UICheckBox(Inter.getLocText("FR-Designer_PageSetup-Vertically")), null},
                new Component[]{new UILabel(Inter.getLocText("FR-Designer_WidgetDisplyPosition")), new UITextField()},
                new Component[]{new UILabel(Inter.getLocText("FR-Designer_Font-Size")), new UITextField(16)},
        };
        JPanel panel = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, 20, 7);
        panel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        return panel;
    }

    @Override
    public String title4PopupWindow() {
        return "label";
    }

    @Override
    public void populateBean(Label ob) {

    }


    @Override
    public Label updateBean() {
        Label layout = (Label) creator.toData();
        return layout;
    }
}
