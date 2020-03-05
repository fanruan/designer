/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.mainframe.chart.gui.style;

import com.fr.base.BaseUtils;
import com.fr.base.Utils;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.gui.ibutton.UIColorButton;
import com.fr.design.gui.ibutton.UIToggleButton;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.FRFont;
import com.fr.design.utils.gui.GUICoreUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date: 14-9-11
 * Time: 上午11:27
 */
public class ChartTextAttrNoColorPane extends ChartTextAttrPane{

   	public ChartTextAttrNoColorPane() {
   		super();
    }

    protected void initComponents() {
        initFontSizes();
        fontColor = new UIColorButton();
        fontNameComboBox = new UIComboBox(Utils.getAvailableFontFamilyNames4Report());
        fontSizeComboBox = new UIComboBox(getFontSizes());
        bold = new UIToggleButton(BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/bold.png"));
        italic = new UIToggleButton(BaseUtils.readIcon("/com/fr/design/images/m_format/cellstyle/italic.png"));

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;

        Component[] components1 = new Component[]{
                italic, bold
        };
        JPanel buttonPane = new JPanel(new BorderLayout());
        buttonPane.add(fontSizeComboBox, BorderLayout.CENTER);
        buttonPane.add(GUICoreUtils.createFlowPane(components1, FlowLayout.LEFT, LayoutConstants.HGAP_LARGE), BorderLayout.EAST);


        double[] columnSize = {f};
        double[] rowSize = {p, p};
        Component[][] components = new Component[][]{
                new Component[]{fontNameComboBox},
                new Component[]{buttonPane}
        };

        JPanel panel = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
        this.setLayout(new BorderLayout());
        this.add(panel, BorderLayout.CENTER);
        populate(FRFont.getInstance());
    }

    /**
     * 更新字体
     * @return 字体
     */
    public FRFont updateFRFont() {
        int style = Font.PLAIN;
        if (bold.isSelected() && !italic.isSelected()) {
            style = Font.BOLD;
        } else if (!bold.isSelected() && italic.isSelected()) {
            style = Font.ITALIC;
        } else if (bold.isSelected() && italic.isSelected()) {
            style = 3;
        }
        return FRFont.getInstance(Utils.objectToString(fontNameComboBox.getSelectedItem()), style,
                Float.valueOf(Utils.objectToString(fontSizeComboBox.getSelectedItem())));
    }
}