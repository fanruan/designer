package com.fr.design.unit.impl;

import com.fr.design.fun.impl.AbstractReportLengthUNITProvider;
import com.fr.stable.Constants;
import com.fr.stable.unit.INCH;
import com.fr.stable.unit.UNIT;

/**
 * Created by kerry on 2020-04-09
 */
public class INCHReportLengthUNIT extends AbstractReportLengthUNITProvider {
    @Override
    public String unitText() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Unit_INCH");
    }

    @Override
    public int unitType() {
        return Constants.UNIT_INCH;
    }

    @Override
    public float unit2Value4Scale(UNIT value) {
        return value.toINCHValue4Scale3();
    }

    @Override
    public UNIT float2UNIT(float value) {
        return new INCH(value);
    }
}
