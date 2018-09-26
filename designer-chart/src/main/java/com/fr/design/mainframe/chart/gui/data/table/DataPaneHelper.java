package com.fr.design.mainframe.chart.gui.data.table;

import com.fr.data.util.function.DataFunction;
import com.fr.data.util.function.NoneFunction;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.mainframe.chart.gui.data.CalculateComboBox;
import com.fr.general.ComparatorUtils;
import com.fr.log.FineLoggerFactory;

import java.util.List;

/**
 * Created by shine on 2018/9/14.
 */
public class DataPaneHelper {
    /**
     * 刷新Box的选项.
     */
    public static void refreshBoxItems(UIComboBox box, List list) {
        if (box == null) {
            return;
        }

        Object ob = box.getSelectedItem();
        box.removeAllItems();

        int length = list.size();
        for (int i = 0; i < length; i++) {
            box.addItem(list.get(i));
        }

        box.getModel().setSelectedItem(ob);
    }

    /**
     * 清空box里所有东西
     *
     * @param box 容器
     */
    public static void clearBoxItems(UIComboBox box) {
        if (box != null) {
            box.removeAllItems();
        }
    }

    /**
     * 根据公式 获取 公式组件下拉框选项
     *
     * @param function
     * @return
     */
    public static String getFunctionString(DataFunction function) {
        for (int i = 0; i < CalculateComboBox.CLASS_ARRAY.length; i++) {
            Class tmp = function.getClass();
            if (ComparatorUtils.equals(tmp, CalculateComboBox.CLASS_ARRAY[i])) {
                return CalculateComboBox.CALCULATE_ARRAY[i];
            }
        }
        return CalculateComboBox.CALCULATE_ARRAY[0];
    }

    /**
     * 根据公式组件下拉选中项 获取对应公式
     *
     * @param name
     * @return
     */
    public static DataFunction getFunctionByName(String name) {
        int index = 0;
        for (int i = 0; i < CalculateComboBox.CALCULATE_ARRAY.length; i++) {
            if (ComparatorUtils.equals(name, CalculateComboBox.CALCULATE_ARRAY[i])) {
                index = i;
            }
        }
        try {
            return (DataFunction) CalculateComboBox.CLASS_ARRAY[index].newInstance();
        } catch (InstantiationException e) {
            FineLoggerFactory.getLogger().error("Function Error");
        } catch (IllegalAccessException e) {
            FineLoggerFactory.getLogger().error("Function Error");
        }
        return new NoneFunction();
    }
}
