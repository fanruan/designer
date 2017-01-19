package com.fr.design.style.background.impl;

import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.stable.ArrayUtils;

import javax.swing.*;

/**
 * Created by richie on 16/5/18.
 */
public class ImageBackgroundPane4Browser extends ImageBackgroundPane {


    public ImageBackgroundPane4Browser() {
        super();
    }

    @Override
    protected UIRadioButton[] imageLayoutButtons() {

        return (UIRadioButton[]) ArrayUtils.addAll(super.imageLayoutButtons(), new UIRadioButton[] {
                defaultRadioButton,
                tiledRadioButton,
        });
    }
}
