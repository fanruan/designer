/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.mainframe.chart.gui.style;

import com.fr.design.constants.LayoutConstants;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.FRFont;
import com.fr.general.GeneralUtils;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date: 14-9-11
 * Time: 上午11:27
 */
public class ChartTextAttrNoColorPane extends ChartTextAttrPane {

    public ChartTextAttrNoColorPane() {
        super();
    }

    protected void initComponents() {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;

        Component[] components1 = new Component[]{
                getItalic(), getBold()
        };
        JPanel buttonPane = new JPanel(new BorderLayout());
        buttonPane.add(getFontSizeComboBox(), BorderLayout.CENTER);
        buttonPane.add(GUICoreUtils.createFlowPane(components1, FlowLayout.LEFT, LayoutConstants.HGAP_LARGE), BorderLayout.EAST);

        double[] columnSize = {f};
        double[] rowSize = {p, p};
        Component[][] components = new Component[][]{
                new Component[]{getFontNameComboBox()},
                new Component[]{buttonPane}
        };

        JPanel panel = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
        this.setLayout(new BorderLayout());
        this.add(panel, BorderLayout.CENTER);
        populate(FRFont.getInstance());
    }

    public FRFont updateFRFont() {
        String name = GeneralUtils.objectToString(getFontNameComboBox().getSelectedItem());

        return FRFont.getInstance(name, updateFontStyle(), updateFontSize());
    }
}