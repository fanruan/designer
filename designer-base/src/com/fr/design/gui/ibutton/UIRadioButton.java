package com.fr.design.gui.ibutton;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date: 13-3-27
 * Time: 下午5:04
 */
public class UIRadioButton extends JRadioButton {

    public UIRadioButton() {
        super();
    }

    public UIRadioButton(Icon icon) {
        super(icon);
    }

    public UIRadioButton(Action a) {
        super(a);
    }

    public UIRadioButton(Icon icon, boolean selected) {
        super(icon, selected);
    }

    public UIRadioButton(String text) {
        super(text);
    }

    public UIRadioButton(String text, boolean selected) {
        super(text, selected);
    }

    public UIRadioButton(String text, Icon icon) {
        super(text, icon);
    }


    public UIRadioButton(String text, Icon icon, boolean selected) {
        super(text, icon, selected);
    }
}