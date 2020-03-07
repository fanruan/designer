package com.fr.design.mainframe.chart.gui.style;

import com.fr.design.gui.ibutton.UIColorButton;
import com.fr.design.gui.ibutton.UIColorButtonWithAuto;
import com.fr.design.i18n.Toolkit;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRFont;
import com.fr.general.GeneralUtils;

public class ChartTextAttrPaneWithAuto extends ChartTextAttrPane {

    private static final String AUTO = Toolkit.i18nText("Fine-Design_Basic_ChartF_Auto");
    private static final int AUTO_SIZE_INT = 0;
    private boolean isFontSizeAuto = false;
    private boolean isColorAuto = false;

    public ChartTextAttrPaneWithAuto() {
        super();
    }

    public ChartTextAttrPaneWithAuto(boolean isFontSizeAuto, boolean isColorAuto) {
        this.isFontSizeAuto = isFontSizeAuto;
        this.isColorAuto = isColorAuto;

        initState();
        initComponents();
    }

    protected void initFontColorState() {
        setFontColor(isColorAuto ? new UIColorButtonWithAuto() : new UIColorButton());
    }

    protected Object[] getFontSizeComboBoxModel() {
        if (isFontSizeAuto) {
            String[] fontSizes = new String[FONT_END - FONT_START + 2];

            fontSizes[0] = AUTO;

            for (int i = 1; i < fontSizes.length; i++) {
                fontSizes[i] = FONT_START + i + "";
            }

            return fontSizes;
        }

        return super.getFontSizeComboBoxModel();
    }

    protected float getFontSize() {
        if (isFontSizeAuto && ComparatorUtils.equals(getFontSizeComboBox().getSelectedItem(), AUTO)) {
            return AUTO_SIZE_INT;
        }

        return Float.parseFloat(GeneralUtils.objectToString(getFontSizeComboBox().getSelectedItem()));
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
