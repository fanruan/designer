package com.fr.design.fun.impl;


import com.fr.design.fun.ReportLengthUNITProvider;
import com.fr.stable.StringUtils;
import com.fr.stable.fun.impl.AbstractProvider;
import com.fr.stable.fun.mark.API;
import com.fr.stable.unit.UNIT;

/**
 * Created by kerry on 2020-04-09
 */
@API(level = ReportLengthUNITProvider.CURRENT_LEVEL)
public abstract class AbstractReportLengthUNITProvider extends AbstractProvider implements ReportLengthUNITProvider {


    @Override
    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

    @Override
    public String unitText() {
        return StringUtils.EMPTY;
    }

    @Override
    public int unitType() {
        return 0;
    }

    @Override
    public float unit2Value4Scale(UNIT value) {
        return 0;
    }

    @Override
    public UNIT float2UNIT(float value) {
        return UNIT.ZERO;
    }
}

