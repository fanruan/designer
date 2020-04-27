package com.fr.design.data;

import com.fr.design.dialog.FineJOptionPane;
import com.fr.design.i18n.Toolkit;
import com.fr.stable.StringUtils;

/**
 * @author hades
 * @version 10.0
 * Created by hades on 2020/4/27
 */
public abstract class BasicTableDataUtils {

    private static final int LEN = 2;


    public static boolean checkName(String name) {
        if (isInValidName(name)) {
            FineJOptionPane.showMessageDialog(null,
                                              Toolkit.i18nText("Fine-Design_Basic_DataSet_Rename_Warning", name),
                                              Toolkit.i18nText("Fine-Design_Basic_Alert"),
                                              FineJOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    public static boolean isInValidName(String name) {
        String[] values = name.split("\\.");
        if (values.length == LEN) {
            return (StringUtils.isNotEmpty(values[0]) && StringUtils.isNotEmpty(values[1]))
                    || (StringUtils.isEmpty(values[0]) && StringUtils.isNotEmpty(values[1]));
        }
        return false;
    }
}
