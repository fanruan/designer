package com.fr.design.style.color;

import com.fr.base.BaseUtils;
import com.fr.design.gui.ibutton.UIButton;

import java.awt.*;

/**
 * Created by plough on 2016/12/22.
 */
public class PickColorButton extends UIButton {

    public PickColorButton(IconType iconType) {
        super();

        if (iconType == IconType.ICON16) {
            this.setIcon(BaseUtils.readIcon("/com/fr/design/images/gui/colorSelectPane/colorPicker16.png"));
            this.setPreferredSize(new Dimension(16, 16));
        } else {
            this.setIcon(BaseUtils.readIcon("/com/fr/design/images/gui/colorSelectPane/colorPicker18.png"));
            this.setPreferredSize(new Dimension(18, 18));
        }
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    //  取色器按钮使用的图标
    public enum IconType {
        ICON16, ICON18
    }
}
