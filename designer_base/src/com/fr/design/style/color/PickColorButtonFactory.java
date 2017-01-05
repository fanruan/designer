package com.fr.design.style.color;

import com.fr.base.BaseUtils;
import com.fr.design.gui.ibutton.UIButton;

import java.awt.*;
import java.awt.event.*;

/**
 * Created by plough on 2016/12/22.
 */
public class PickColorButtonFactory {

    public static UIButton getPickColorButton(ColorSelectable colorSelectable, IconType iconType) {
        return getPickColorButton(colorSelectable, iconType, false);
    }

    public static UIButton getPickColorButton(final ColorSelectable colorSelectable, IconType iconType, final Boolean setColorRealTime) {
        UIButton pickColorButton = new UIButton();

        if (iconType == IconType.ICON16) {
            pickColorButton.setIcon(BaseUtils.readIcon("/com/fr/design/images/gui/colorPicker/colorPicker16.png"));
            pickColorButton.setPreferredSize(new Dimension(16, 16));
        } else {
            pickColorButton.setIcon(BaseUtils.readIcon("/com/fr/design/images/gui/colorPicker/colorPicker18.png"));
            pickColorButton.setPreferredSize(new Dimension(18, 18));
        }
        pickColorButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        pickColorButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                new ColorPicker(colorSelectable, setColorRealTime);
            }
        });

        return pickColorButton;
    }

    //  取色器按钮使用的图标
    public enum IconType {
        ICON16, ICON18
    }
}
