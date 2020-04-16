package com.fr.design.fun.impl;


import com.fr.design.beans.BasicBeanPane;
import com.fr.design.fun.FormAdaptiveConfigUIProcessor;
import com.fr.stable.fun.impl.AbstractProvider;
import com.fr.stable.fun.mark.API;

import javax.swing.JComponent;
import java.awt.Dimension;
import java.awt.image.BufferedImage;

/**
 * Created by kerry on 2020-04-09
 */
@API(level = FormAdaptiveConfigUIProcessor.CURRENT_LEVEL)
public abstract class AbstractFormAdaptiveConfigUIProcessor extends AbstractProvider implements FormAdaptiveConfigUIProcessor {

    @Override
    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

    @Override
    public int layerIndex() {
        return DEFAULT_LAYER_INDEX;
    }

    @Override
    public BasicBeanPane getConfigPane() {
        return null;
    }

    @Override
    public BufferedImage paintFormElementCaseImage(Dimension size, JComponent elementCasePane) {
        return null;
    }
}
