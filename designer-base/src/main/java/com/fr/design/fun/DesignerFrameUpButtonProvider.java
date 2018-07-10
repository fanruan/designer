package com.fr.design.fun;

import com.fr.design.gui.ibutton.UIButton;
import com.fr.stable.fun.mark.Mutable;

/**
 * 设计器面板最上方的按钮接口（保存，赋值，撤销同级）
 * Coder: zack
 * Date: 2016/9/22
 * Time: 15:40
 */
public interface DesignerFrameUpButtonProvider extends Mutable {

    int CURRENT_LEVEL = 1;

    String XML_TAG = "DesignerFrameUpButtonProvider";

    /**
     * 根据当前的设计状态返回最上层工具按钮
     * @param menuState 现在设计器的设计状态
     * @return 按钮
     */
    UIButton[] getUpButtons(int menuState);
}
