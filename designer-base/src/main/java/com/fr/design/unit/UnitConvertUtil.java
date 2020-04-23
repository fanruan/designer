package com.fr.design.unit;

import com.fr.design.ExtraDesignClassManager;
import com.fr.design.fun.ReportLengthUNITProvider;
import com.fr.design.unit.impl.CMReportLengthUNIT;
import com.fr.design.unit.impl.INCHReportLengthUNIT;
import com.fr.design.unit.impl.MMReportLengthUNIT;
import com.fr.design.unit.impl.PTReportLengthUNIT;
import com.fr.general.GeneralContext;
import com.fr.plugin.context.PluginContext;
import com.fr.plugin.manage.PluginFilter;
import com.fr.plugin.observer.PluginEvent;
import com.fr.plugin.observer.PluginEventListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by kerry on 2020-04-09
 */
public class UnitConvertUtil {
    private static List<ReportLengthUNITProvider> lengthUNITList = new ArrayList<ReportLengthUNITProvider>();

    static {
        GeneralContext.listenPluginRunningChanged(new PluginEventListener() {
            @Override
            public void on(PluginEvent pluginEvent) {
                initSupportedReportLengthUNIT();
            }
        }, new PluginFilter() {
            @Override
            public boolean accept(PluginContext pluginContext) {
                return pluginContext.contain(ReportLengthUNITProvider.MARK_STRING);
            }
        });

        initSupportedReportLengthUNIT();
    }

    private static void initSupportedReportLengthUNIT(){
        lengthUNITList.clear();
        lengthUNITList.add(new MMReportLengthUNIT());
        lengthUNITList.add(new CMReportLengthUNIT());
        lengthUNITList.add(new INCHReportLengthUNIT());
        lengthUNITList.add(new PTReportLengthUNIT());
        Set<ReportLengthUNITProvider> providers = ExtraDesignClassManager.getInstance().getArray(ReportLengthUNITProvider.MARK_STRING);
        for (ReportLengthUNITProvider provider : providers) {
            lengthUNITList.add(provider);
        }
    }

    private UnitConvertUtil() {

    }


    public static ReportLengthUNITProvider parseLengthUNIT(int unitType) {
        for (ReportLengthUNITProvider lengthUNIT : lengthUNITList) {
            if (unitType == lengthUNIT.unitType()) {
                return lengthUNIT;
            }
        }
        return new MMReportLengthUNIT();
    }

    public static String[] getUnitItems() {
        String[] unitItems = new String[lengthUNITList.size()];
        for (int i = 0; i < lengthUNITList.size(); i++) {
            unitItems[i] = lengthUNITList.get(i).unitText();
        }
        return unitItems;
    }
}
