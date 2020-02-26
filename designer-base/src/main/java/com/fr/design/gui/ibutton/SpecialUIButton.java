/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.gui.ibutton;

import javax.swing.*;
import javax.swing.plaf.ButtonUI;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-3-5
 * Time: 下午4:52
 */
public class SpecialUIButton extends JButton {
    public SpecialUIButton(ButtonUI ui) {
        this.ui = ui;
        ui.installUI(this);
    }

    /**
     * refuses to change the UI delegate. It keeps the one set in the constructor.
     *
     * @see javax.swing.AbstractButton#setUI(ButtonUI)
     */
    @Override
    public void setUI(ButtonUI ui) {
        // do nothing
    }
}