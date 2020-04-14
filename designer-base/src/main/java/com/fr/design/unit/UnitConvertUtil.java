package com.fr.design.unit;

import com.fr.design.ExtraDesignClassManager;
import com.fr.design.unit.impl.CMReportLengthUNIT;
import com.fr.design.unit.impl.INCHReportLengthUNIT;
import com.fr.design.unit.impl.MMReportLengthUNIT;
import com.fr.design.unit.impl.PTReportLengthUNIT;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kerry on 2020-04-09
 */
public class UnitConvertUtil {
    private static List<ReportLengthUNIT> lengthUNITList = new ArrayList<ReportLengthUNIT>();

    static {
        lengthUNITList.add(new CMReportLengthUNIT());
        lengthUNITList.add(new INCHReportLengthUNIT());
        lengthUNITList.add(new PTReportLengthUNIT());
        lengthUNITList.add(new MMReportLengthUNIT());
    }

    private UnitConvertUtil() {

    }


    public static ReportLengthUNIT parseLengthUNIT(int unitType) {
        ReportLengthUnitProcessor lengthUnitProcessor = ExtraDesignClassManager.getInstance().getSingle(ReportLengthUnitProcessor.MARK_STRING);
        if (lengthUnitProcessor != null) {
            return lengthUnitProcessor.getReportLengthUNIT();
        }
        for (ReportLengthUNIT lengthUNIT : lengthUNITList) {
            if (unitType == lengthUNIT.unitType()) {
                return lengthUNIT;
            }
        }
        return new MMReportLengthUNIT();
    }
}
