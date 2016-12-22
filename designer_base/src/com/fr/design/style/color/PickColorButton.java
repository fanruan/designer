package com.fr.design.style.color;

import com.fr.base.BaseUtils;
import com.fr.design.gui.ibutton.UIButton;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by plough on 2016/12/22.
 */
public class PickColorButton extends UIButton {
    private ColorSelectable colorSelectable;

    public PickColorButton(ColorSelectable colorSelectable) {
        super(BaseUtils.readIcon("/com/fr/design/images/gui/colorSelectPane/colorPicker.png"));

        this.colorSelectable = colorSelectable;

        this.setPreferredSize(new Dimension(16, 16));
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doPickColor();
            }
        });
    }
    /**
     * 打开取色框，开始取色
     */
    public void doPickColor() {
        ColorPicker colorPicker = new ColorPicker(colorSelectable);
        colorPicker.start();
    }
}
