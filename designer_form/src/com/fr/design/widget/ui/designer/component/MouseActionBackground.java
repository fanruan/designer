package com.fr.design.widget.ui.designer.component;

import com.fr.general.Background;

/**
 * Created by ibm on 2017/8/7.
 */
public class MouseActionBackground {

    private Background initialBackground;
    private Background overBackground;
    private Background clickBackground;

    public MouseActionBackground(Background initialBackground, Background overBackground, Background clickBackground){
        this.initialBackground = initialBackground;
        this.overBackground = overBackground;
        this.clickBackground = clickBackground;
    }

    public Background getInitialBackground() {
        return initialBackground;
    }

    public void setInitialBackground(Background initialBackground) {
        this.initialBackground = initialBackground;
    }

    public Background getOverBackground() {
        return overBackground;
    }

    public void setOverBackground(Background overBackground) {
        this.overBackground = overBackground;
    }

    public Background getClickBackground() {
        return clickBackground;
    }

    public void setClickBackground(Background clickBackground) {
        this.clickBackground = clickBackground;
    }
}
