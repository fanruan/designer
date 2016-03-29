/*
 * Copyright (c) 2001-2014,FineReport Inc, All Rights Reserved.
 */

package com.fr.design.mainframe.actions;

import com.fr.design.actions.JTemplateAction;
import com.fr.design.mainframe.JChart;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 14-10-15
 * Time: 上午9:47
 */
public abstract class JChartAction extends JTemplateAction<JChart> {
    public JChartAction(JChart jChart) {
        super(jChart);
    }
}