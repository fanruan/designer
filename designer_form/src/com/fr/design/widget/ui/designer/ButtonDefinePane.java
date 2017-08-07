package com.fr.design.widget.ui.designer;

import com.fr.design.designer.creator.XCreator;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.widget.btn.ButtonDetailPane;
import com.fr.design.widget.ui.designer.btn.ButtonDetailPaneFactory;
import com.fr.form.ui.Button;
import com.fr.form.ui.FreeButton;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class ButtonDefinePane extends AbstractDataModify<Button> {
    private ButtonDetailPane detailPane;

    public ButtonDefinePane(XCreator creator){
        super(creator);
        this.initComponent();
    }

    private void initComponent() {
        setLayout(FRGUIPaneFactory.createBorderLayout());
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    }

    @Override
    public String title4PopupWindow() {
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

//        resetDetailPane(btn, btn instanceof FreeButton && !((FreeButton) btn).isCustomStyle() ? Button.class : null);
        return new Button();
    }

}