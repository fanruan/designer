package com.fr.design.report.fit.menupane;

import com.fr.design.beans.BasicBeanPane;
import com.fr.stable.fun.mark.Immutable;

import javax.swing.JComponent;
import java.awt.Dimension;
import java.awt.image.BufferedImage;


/**
 * Created by kerry on 2020-04-09
 */
public interface FormAdaptiveConfigUI extends Immutable {

    String MARK_STRING = "FormAdaptiveConfigUI";
    int CURRENT_LEVEL = 1;


    BasicBeanPane getConfigPane();

    BufferedImage getElementCaseImage(Dimension size, JComponent elementCasePane);


}

