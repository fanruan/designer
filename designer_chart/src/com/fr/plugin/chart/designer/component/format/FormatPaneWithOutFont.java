package com.fr.plugin.chart.designer.component.format;

import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.style.FormatPane;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;

/**
 * Created by mengao on 2017/8/28.
 * 只有文本格式设置，没有字体设置
 */
public class FormatPaneWithOutFont extends FormatPane {
    private static final int HEIGHT = 30;


    protected Component[][] getComponent (JPanel fontPane, JPanel centerPane, JPanel typePane) {
        return new Component[][]{
                new Component[]{null, null},
                new Component[]{new UILabel(Inter.getLocText("FR-Base_Format"), SwingConstants.LEFT), typePane},
                new Component[]{centerPane, null},
        };
    }

    public Dimension getPreferredSize() {
        //todo @mango
        if (getTypeComboBox().getSelectedIndex() == 0) {
            return new Dimension((int)getTypeComboBox().getPreferredSize().getWidth(), HEIGHT);
        }
        return super.getPreferredSize();
    }
}
