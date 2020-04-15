package com.fr.design.unit;

import com.fr.stable.fun.mark.Immutable;

/**
 * Created by kerry on 2020-04-09
 */
public interface ReportLengthUnitProcessor extends Immutable {
    String MARK_STRING = "ReportLengthUnitProcessor";
    int CURRENT_LEVEL = 1;

    ReportLengthUNIT getReportLengthUNIT(int unitType);

}
