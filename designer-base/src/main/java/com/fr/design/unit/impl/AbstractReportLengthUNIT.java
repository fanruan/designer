package com.fr.design.unit.impl;


import com.fr.design.unit.ReportLengthUNIT;

/**
 * Created by kerry on 2020-04-09
 */
public abstract class AbstractReportLengthUNIT implements ReportLengthUNIT {


    @Override
    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

    @Override
    public String mark4Provider() {
        return getClass().getName();
    }

}
