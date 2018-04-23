package com.fr.design.mainframe.widget.editors;

import java.text.NumberFormat;

public class PercentageEditor extends FormattedEditor {

    public PercentageEditor() {
        super(NumberFormat.getPercentInstance());
    }
}