package com.fr.design.mainframe.chart.gui.style;

import com.fr.base.Utils;
import com.fr.design.i18n.Toolkit;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRFont;

public class ChartTextAttrPaneWithAuto extends ChartTextAttrPane {

    private static final String AUTO = Toolkit.i18nText("Fine-Design_Basic_ChartF_Auto");
    private static final int AUTO_SIZE_INT = 0;
    private boolean isFontSizeAuto;
    private boolean isColorAuto;

    public ChartTextAttrPaneWithAuto() {
        this.isFontSizeAuto = false;
        this.isColorAuto = false;

        initState();
        initComponents();
    }

    public ChartTextAttrPaneWithAuto(boolean isFontSizeAuto, boolean isColorAuto) {
        this.isFontSizeAuto = isFontSizeAuto;
        this.isColorAuto = isColorAuto;

        initState();
        initComponents();
    }

    protected Object[] getFontSizeComboBoxModel() {
        if (isFontSizeAuto) {
            String[] fontSizes = new String[Font_Sizes.length + 1];

            fontSizes[0] = AUTO;

            for (int i = 1; i <= Font_Sizes.length; i++) {
                fontSizes[i] = i + "";
            }

            return fontSizes;
        } else {
            return super.getFontSizeComboBoxModel();
        }
    }

    protected float getFontSize() {
        if (isFontSizeAuto && ComparatorUtils.equals(getFontSizeComboBox().getSelectedItem(), AUTO)) {
            return AUTO_SIZE_INT;
        }

        return Float.parseFloat(Utils.objectToString(getFontSizeComboBox().getSelectedItem()));
    }

    protected void setFontSize(FRFont frFont) {
        if (getFontSizeComboBox() != null && isFontSizeAuto) {
            if (frFont.getSize() == AUTO_SIZE_INT) {
                getFontSizeComboBox().setSelectedItem(AUTO);
            } else {
                getFontSizeComboBox().setSelectedItem(frFont.getSize() + "");
            }
        }

        if (getFontSizeComboBox() != null && !isFontSizeAuto) {
            getFontSizeComboBox().setSelectedItem(frFont.getSize());
        }
    }
}
