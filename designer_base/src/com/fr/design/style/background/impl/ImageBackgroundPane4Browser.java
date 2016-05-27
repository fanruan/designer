package com.fr.design.style.background.impl;

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
    protected JRadioButton[] imageLayoutButtons() {

        return (JRadioButton[]) ArrayUtils.addAll(super.imageLayoutButtons(), new JRadioButton[] {
                defaultRadioButton,
                tiledRadioButton,
        });
    }
}
