package com.fr.van.chart.custom.component;

import com.fr.design.gui.controlpane.ShortCutListenerProvider;
import com.fr.design.gui.controlpane.shortcutfactory.ShortCutFactory;

/**
 * Created by plough on 2018/8/13.
 */
class VanChartShortCutFactory extends ShortCutFactory {
    private VanChartShortCutFactory(ShortCutListenerProvider listenerProvider) {
        super(listenerProvider);
    }

    public static VanChartShortCutFactory newInstance(ShortCutListenerProvider listenerProvider) {
        return new VanChartShortCutFactory(listenerProvider);
    }
}
