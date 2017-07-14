package com.fr.design.fun.impl;

import com.fr.design.fun.DesignerFrameUpButtonProvider;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.stable.fun.impl.AbstractProvider;
import com.fr.stable.fun.mark.API;

/**
 * Coder: zack
 * Date: 2016/9/22
 * Time: 15:50
 */
@API(level = DesignerFrameUpButtonProvider.CURRENT_LEVEL)
public abstract class AbstractDsinFrameUpButtonProvider extends AbstractProvider implements DesignerFrameUpButtonProvider {
    @Override
    public UIButton[] getUpButtons(int menuState) {
        return new UIButton[0];
    }

    @Override
    public String mark4Provider() {
        return getClass().getName();
    }
}
