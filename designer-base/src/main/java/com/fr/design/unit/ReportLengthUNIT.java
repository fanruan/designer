package com.fr.design.unit;

import com.fr.stable.fun.mark.Mutable;
import com.fr.stable.unit.UNIT;

/**
 * Created by kerry on 2020-04-09
 */
public interface ReportLengthUNIT extends Mutable {
    String MARK_STRING = "ReportLengthUNIT";

    int CURRENT_LEVEL = 1;

    String unitText();

    int unitType();

    float unit2Value4Scale(UNIT value);

    UNIT float2UNIT(float value);
}
