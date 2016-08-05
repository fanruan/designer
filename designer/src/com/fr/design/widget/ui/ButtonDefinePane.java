package com.fr.design.widget.ui;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.frpane.TreeSettingPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.present.dict.DictionaryPane;
import com.fr.form.ui.Button;
import com.fr.form.ui.FreeButton;
import com.fr.design.widget.DataModify;
import com.fr.design.widget.btn.ButtonDetailPane;
import com.fr.design.widget.ui.btn.ButtonDetailPaneFactory;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class ButtonDefinePane extends AbstractDataModify<Button> {
    private ButtonDetailPane detailPane;

    public ButtonDefinePane() {
        this.initComponent();
    }

    private void initComponent() {
        setLayout(FRGUIPaneFactory.createBorderLayout());
        setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
    }

    @Override
    protected String title4PopupWindow() {
        return "Button";
    }

    private void resetDetailPane(Button btn, Class cls) {
        if (detailPane != null) {
            remove(detailPane);
        }
        detailPane = ButtonDetailPaneFactory.createButtonDetailPane(cls, btn);
        add(detailPane, BorderLayout.CENTER);
        detailPane.addTypeChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                resetDetailPane(null, (Class) e.getSource());
            }
        });
        this.updateUI();
    }

    @Override
    public void populateBean(Button btn) {
        resetDetailPane(btn, btn instanceof FreeButton && !((FreeButton) btn).isCustomStyle() ? Button.class : null);
    }

    @Override
    public Button updateBean() {
        return detailPane.update();
    }
}