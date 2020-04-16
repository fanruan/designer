package com.fr.design.unit.impl;

import com.fr.design.fun.impl.AbstractReportLengthUNITProvider;
import com.fr.stable.Constants;
import com.fr.stable.unit.PT;
import com.fr.stable.unit.UNIT;

/**
 * Created by kerry on 2020-04-09
 */
public class PTReportLengthUNIT extends AbstractReportLengthUNITProvider {
    @Override
    public String unitText() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Unit_PT_Duplicate");
    }

    @Override
    public int unitType() {
        return Constants.UNIT_PT;
    }

    @Override
    public float unit2Value4Scale(UNIT value) {
        return value.toPTValue4Scale2();
    }

    @Override
    public UNIT float2UNIT(float value) {
        return new PT(value);
    }
}
