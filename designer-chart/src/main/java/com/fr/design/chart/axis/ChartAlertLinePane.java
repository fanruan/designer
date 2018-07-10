package com.fr.design.chart.axis;

import com.fr.general.Inter;
import com.fr.general.NameObject;
import com.fr.stable.Nameable;
import com.fr.chart.chartattr.ChartAlertValue;
import com.fr.design.gui.controlpane.JListControlPane;
import com.fr.design.gui.controlpane.NameObjectCreator;
import com.fr.design.gui.controlpane.NameableCreator;

import java.util.ArrayList;
import java.util.List;

public class ChartAlertLinePane extends JListControlPane {

    public NameableCreator[] createNameableCreators() {
        return new NameableCreator[]{
                new NameObjectCreator(Inter.getLocText("ChartF-Alert-Line"),
                        ChartAlertValue.class, ChartAlertValuePane.class)
        };
    }

    @Override
    protected String title4PopupWindow() {
        return Inter.getLocText(new String[]{"Edit", "ChartF-Alert-Line"});
    }

    public void populate(ChartAlertValue[] alertList) {
        if (alertList == null) {
            alertList = new ChartAlertValue[0];
        }
        List<NameObject> nameObjectList = new ArrayList<NameObject>();

        for (int i = 0; i < alertList.length; i++) {
            ChartAlertValue value = alertList[i];
            nameObjectList.add(new NameObject(value.getAlertPaneSelectName(), value));
        }

        if (!nameObjectList.isEmpty()) {
            populate(nameObjectList.toArray(new NameObject[nameObjectList.size()]));
        }
    }

    public ChartAlertValue[] updateAlertValues() {
        Nameable[] res = update();

        NameObject[] res_array = new NameObject[res.length];
        java.util.Arrays.asList(res).toArray(res_array);

        if (res_array.length < 1) {
            return new ChartAlertValue[0];
        }

        List alertValueList = new ArrayList();
        for (int i = 0; i < res_array.length; i++) {
            NameObject nameObject = res_array[i];
            ChartAlertValue chartAlertValue = (ChartAlertValue) nameObject.getObject();
            chartAlertValue.setAlertPaneSelectName(nameObject.getName());
            alertValueList.add(chartAlertValue);
        }
        return (ChartAlertValue[]) alertValueList.toArray(new ChartAlertValue[alertValueList.size()]);
    }
}