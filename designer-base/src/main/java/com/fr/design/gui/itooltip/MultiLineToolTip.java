package com.fr.design.gui.itooltip;

import javax.swing.JToolTip;

public class MultiLineToolTip extends JToolTip {

    public MultiLineToolTip() {
        setUI(new MultiLineToolTipUI());
    }
}