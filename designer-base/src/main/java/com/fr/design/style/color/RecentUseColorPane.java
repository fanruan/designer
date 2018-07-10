package com.fr.design.style.color;

import com.fr.design.dialog.BasicPane;
import com.fr.design.layout.FRGUIPaneFactory;

import javax.swing.*;
import java.awt.*;

public class RecentUseColorPane extends BasicPane implements ColorSelectable {

    private JColorChooser chooser;

    @Override
    protected String title4PopupWindow() {
        return null;
    }


    public RecentUseColorPane(JColorChooser chooser) {
        this.chooser = chooser;

        // center
        JPanel centerPane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_S_Pane();
        this.add(centerPane, BorderLayout.CENTER);
        // 最近使用
        UsedColorPane pane = new UsedColorPane(2, 10, this);
        centerPane.add(pane.getPane());
    }

    /**
     * 选中颜色
     */
    @Override
    public void colorSetted(ColorCell color) {

    }

    @Override
    public Color getColor() {
        return null;
    }

    @Override
    public void setColor(Color color) {
        chooser.getSelectionModel().setSelectedColor(color);
    }

}