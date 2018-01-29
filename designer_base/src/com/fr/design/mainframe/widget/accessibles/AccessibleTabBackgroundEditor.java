package com.fr.design.mainframe.widget.accessibles;

import com.fr.design.style.background.BackgroundButtonPane;
import com.fr.design.style.background.BackgroundCardSwitchButtonPane;
/**
 * @author kerry
 * @date 2018/1/29
 */
public class AccessibleTabBackgroundEditor extends AccessibleImgBackgroundEditor  {
    public AccessibleTabBackgroundEditor() {
        super();
    }
    @Override
    protected BackgroundButtonPane initBackgroundPane(){
        return new BackgroundCardSwitchButtonPane();
    }
}
