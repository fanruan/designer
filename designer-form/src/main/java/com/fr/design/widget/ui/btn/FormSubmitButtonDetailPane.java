package com.fr.design.widget.ui.btn;

import com.fr.form.parameter.FormSubmitButton;
import com.fr.design.widget.btn.ButtonWithHotkeysDetailPane;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-11-15
 * Time   : 下午6:25
 */
public class FormSubmitButtonDetailPane extends ButtonWithHotkeysDetailPane<FormSubmitButton> {

    @Override
    protected Component createCenterPane() {
        return null;
    }

    @Override
    public FormSubmitButton update() {
        FormSubmitButton fb = super.update();
        return fb;
    }


    @Override
    public FormSubmitButton createButton() {
        FormSubmitButton button = new FormSubmitButton();
        button.setCustomStyle(true);
        return button;
    }

    @Override
    public Class classType() {
        return FormSubmitButton.class;
    }
}