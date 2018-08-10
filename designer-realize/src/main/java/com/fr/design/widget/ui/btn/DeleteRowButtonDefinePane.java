package com.fr.design.widget.ui.btn;

import com.fr.base.IconManager;

import com.fr.report.web.button.write.DeleteRowButton;
import com.fr.design.widget.btn.ButtonWithHotkeysDetailPane;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-11-15
 * Time   : 下午8:04
 */
public class DeleteRowButtonDefinePane<T extends DeleteRowButton> extends ButtonWithHotkeysDetailPane<DeleteRowButton> {
    private DefineDeleteColumnRowPane ddcp;

    @Override
    protected Component createCenterPane() {
        return ddcp = new DefineDeleteColumnRowPane();
    }

    @Override
    public DeleteRowButton createButton() {
        DeleteRowButton button = new DeleteRowButton();
        button.setText(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Utils_Delete_Row"));
        button.setIconName(IconManager.DELETE.getName());
        return button;
    }

    @Override
    public Class classType() {
        return DeleteRowButton.class;
    }

    @Override
    public void populate(DeleteRowButton btn) {
        super.populate(btn);
        ddcp.populate(btn);

    }

    @Override
    public DeleteRowButton update() {
        DeleteRowButton btn = super.update();
        ddcp.update(btn);
        return btn;
    }
}
