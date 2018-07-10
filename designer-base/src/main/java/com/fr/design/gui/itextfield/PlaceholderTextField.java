package com.fr.design.gui.itextfield;

import javax.swing.text.Document;

public class PlaceholderTextField extends UITextField {

    public PlaceholderTextField() {
    }

    public PlaceholderTextField(
            final Document pDoc,
            final String pText,
            final int pColumns) {
        super(pDoc, pText, pColumns);
    }

    public PlaceholderTextField(final int pColumns) {
        super(pColumns);
    }

    public PlaceholderTextField(final String pText) {
        super(pText);
    }

    public PlaceholderTextField(final String pText, final int pColumns) {
        super(pText, pColumns);
    }
}