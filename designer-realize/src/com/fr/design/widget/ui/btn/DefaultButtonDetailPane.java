package com.fr.design.widget.ui.btn;

import com.fr.form.ui.Button;
import com.fr.form.ui.FreeButton;
import com.fr.design.widget.btn.ButtonWithHotkeysDetailPane;

import java.awt.*;

/**
 * Created by IntelliJ IDEA. Author : Richer Version: 6.5.6 Date : 11-11-15 Time
 * : 下午6:24
 */
public class DefaultButtonDetailPane extends ButtonWithHotkeysDetailPane<Button> {

    @Override
    protected Component createCenterPane() {
        return null;
    }

    @Override
    public FreeButton createButton() {
        return new FreeButton();
    }


    @Override
    public Class classType() {
        return Button.class;
    }
}