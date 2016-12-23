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
    public PickColorButton(final ColorSelectable colorSelectable, IconType iconType){
        this(colorSelectable, iconType, false);
    }

    public PickColorButton(final ColorSelectable colorSelectable, IconType iconType, final Boolean setColorRealTime) {
        super();

        if (iconType == IconType.ICON16) {
            this.setIcon(BaseUtils.readIcon("/com/fr/design/images/gui/colorPicker/colorPicker16.png"));
            this.setPreferredSize(new Dimension(16, 16));
        } else {
            this.setIcon(BaseUtils.readIcon("/com/fr/design/images/gui/colorPicker/colorPicker18.png"));
            this.setPreferredSize(new Dimension(18, 18));
        }
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));

        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ColorPicker colorPicker = new ColorPicker(colorSelectable, setColorRealTime);
                colorPicker.start();
            }
        });
    }

    //  取色器按钮使用的图标
    public enum IconType {
        ICON16, ICON18
    }
}
