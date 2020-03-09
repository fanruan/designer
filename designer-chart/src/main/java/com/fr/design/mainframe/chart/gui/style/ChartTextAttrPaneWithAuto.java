package com.fr.design.mainframe.chart.gui.style;

import com.fr.chart.base.ChartConstants;
import com.fr.design.gui.ibutton.UIColorButton;
import com.fr.design.gui.ibutton.UIColorButtonWithAuto;
import com.fr.design.i18n.Toolkit;
import com.fr.plugin.chart.type.FontAutoType;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRFont;
import com.fr.general.GeneralUtils;

public class ChartTextAttrPaneWithAuto extends ChartTextAttrPane {

    private static final String AUTO = Toolkit.i18nText("Fine-Design_Basic_ChartF_Auto");
    private boolean isFontSizeAuto = false;
    private boolean isColorAuto = false;
    public static String[] FONT_SIZES_WITH_AUTO = new String[FONT_END - FONT_START + 2];
    static {
        FONT_SIZES_WITH_AUTO[0] = AUTO;

        for (int i = 1; i < FONT_SIZES_WITH_AUTO.length; i++) {
            FONT_SIZES_WITH_AUTO[i] = FONT_START + i - 1 + "";
        }
    }

    public ChartTextAttrPaneWithAuto(FontAutoType type) {
        initAutoType(type);
        initState();
        initComponents();
    }

    private void initAutoType(FontAutoType type) {
        switch (type) {
            case NONE:
                this.isFontSizeAuto = false;
                this.isColorAuto = false;
                break;
            case SIZE:
                this.isFontSizeAuto = true;
                this.isColorAuto = false;
                break;
            case COLOR:
                this.isFontSizeAuto = false;
                this.isColorAuto = true;
                break;
            case SIZE_AND_COLOR:
                this.isFontSizeAuto = true;
                this.isColorAuto = true;
                break;
        }
    }

    protected void initFontColorState() {
        setFontColor(isColorAuto ? new UIColorButtonWithAuto() : new UIColorButton());
    }

    protected Object[] getFontSizeComboBoxModel() {
        return isFontSizeAuto ? FONT_SIZES_WITH_AUTO : FONT_SIZES;
    }

    protected float updateFontSize() {
        if (isFontSizeAuto && ComparatorUtils.equals(getFontSizeComboBox().getSelectedItem(), AUTO)) {
            return ChartConstants.AUTO_FONT_SIZE;
        }

        return Float.parseFloat(GeneralUtils.objectToString(getFontSizeComboBox().getSelectedItem()));
    }

    protected void populateFontSize(FRFont frFont) {
        if (getFontSizeComboBox() != null && isFontSizeAuto) {
            if (frFont.getSize() == ChartConstants.AUTO_FONT_SIZE) {
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