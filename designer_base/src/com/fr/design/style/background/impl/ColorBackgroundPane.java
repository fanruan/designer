package com.fr.design.style.background.impl;

import com.fr.base.background.ColorBackground;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.style.background.BackgroundDetailPane;
import com.fr.design.style.color.DetailColorSelectPane;
import com.fr.general.Background;

import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * Color background pane.
 */
public class ColorBackgroundPane extends BackgroundDetailPane {

    private DetailColorSelectPane detailColorSelectPane;

    public ColorBackgroundPane() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());

        detailColorSelectPane = new DetailColorSelectPane();
        this.add(detailColorSelectPane, BorderLayout.CENTER);
    }

    public void populate(Background background) {
        if (background instanceof ColorBackground) {
            ColorBackground colorBackgroud = (ColorBackground) background;
            this.detailColorSelectPane.populate(colorBackgroud.getColor());
        } else {
            this.detailColorSelectPane.populate(Color.WHITE);
        }
    }

    public Background update() throws Exception {
        return ColorBackground.getInstance(this.detailColorSelectPane.update());
    }

    public void addChangeListener(ChangeListener changeListener) {
        detailColorSelectPane.addChangeListener(changeListener);
    }
}
