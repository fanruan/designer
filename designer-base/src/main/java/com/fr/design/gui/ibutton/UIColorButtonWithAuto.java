package com.fr.design.gui.ibutton;

import com.fr.chart.base.ChartConstants;
import com.fr.design.constants.UIConstants;
import com.fr.design.style.color.ColorControlWindow;
import com.fr.design.style.color.ColorControlWindowWithAuto;
import com.fr.general.ComparatorUtils;

import javax.swing.Icon;
import java.awt.Color;

public class UIColorButtonWithAuto extends UIColorButton {

    public UIColorButtonWithAuto() {
        super();
    }

    public UIColorButtonWithAuto(Icon autoFontIcon) {
        super(autoFontIcon);
    }

    protected void checkColorChange(Color oldColor, Color newColor) {
        if (ComparatorUtils.equals(oldColor, ChartConstants.AUTO_FONT_COLOR) && !ComparatorUtils.equals(newColor, ChartConstants.AUTO_FONT_COLOR)) {
            setIcon(UIConstants.FONT_ICON);
        }

        if (!ComparatorUtils.equals(oldColor, ChartConstants.AUTO_FONT_COLOR) && ComparatorUtils.equals(newColor, ChartConstants.AUTO_FONT_COLOR)) {
            setIcon(UIConstants.AUTO_FONT_ICON);
        }

        super.checkColorChange(oldColor, newColor);
    }

    protected ColorControlWindow getColorControlWindow() {
        if (getPopupWin() == null) {
            ColorControlWindowWithAuto colorControlWindowWithAuto = new ColorControlWindowWithAuto(UIColorButtonWithAuto.this) {
                protected void colorChanged() {
                    UIColorButtonWithAuto.this.setColor(this.getColor());
                }
            };

            setPopupWin(colorControlWindowWithAuto);
        }

        return getPopupWin();
    }
}
