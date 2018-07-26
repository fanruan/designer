package com.fr.design.widget.ui.btn;

import com.fr.design.designer.IntervalConstants;
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.form.ui.Button;

import com.fr.report.web.button.form.TreeNodeToggleButton;
import com.fr.design.widget.btn.ButtonDetailPane;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-11-15
 * Time   : 下午7:45
 */
public class TreeNodeToogleButtonDefinePane<T extends TreeNodeToggleButton> extends ButtonDetailPane<Button> {

    public TreeNodeToogleButtonDefinePane() {
        initComponents();
    }

    protected void initComponents() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        double rowSize[] = {p};
        double columnSize[] = {p, f};
        Component[][] n_components = {
                {new UILabel(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Button-Type")), createButtonTypeComboBox()},
        };
        JPanel panel = TableLayoutHelper.createGapTableLayoutPane(n_components, rowSize, columnSize, IntervalConstants.INTERVAL_L2, 8);
        JPanel borderPanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        borderPanel.add(panel, BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createEmptyBorder(IntervalConstants.INTERVAL_L1, 0, 0, 0));
        UIExpandablePane advancedPane = new UIExpandablePane(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Advanced"), 280, 20, borderPanel);
        this.add(advancedPane);

    }

    @Override
    public TreeNodeToggleButton update() {
        return createButton();
    }

    @Override
    public TreeNodeToggleButton createButton() {
        return new TreeNodeToggleButton();
    }

    @Override
    public Class classType() {
        return TreeNodeToggleButton.class;
    }
}