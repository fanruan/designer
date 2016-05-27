package com.fr.design.style.background.impl;

import com.fr.design.style.color.ColorSelectBox;

import javax.swing.*;
import java.awt.*;

/**
 * Created by richie on 16/5/18.
 */
public class PatternBackgroundPaneNoFore extends PatternBackgroundPane {

    public PatternBackgroundPaneNoFore(int nColumn) {
        super(nColumn);
    }

    // 重载 不加载两个前后按钮
    protected void setChildrenOfContentPane(JPanel contentPane) {
        foregroundColorPane = new ColorSelectBox(80);
        backgroundColorPane = new ColorSelectBox(80);
        foregroundColorPane.setSelectObject(Color.lightGray);
        backgroundColorPane.setSelectObject(Color.black);
    }
}
