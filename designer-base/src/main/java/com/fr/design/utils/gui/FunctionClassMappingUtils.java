package com.fr.design.utils.gui;

import com.fr.stable.StringUtils;

/**
 * @author： Harrison
 * @date： 2018/08/28
 * @description: 为 Function 类的名字做匹配, 从而方便国际化的类
 **/
public enum FunctionClassMappingUtils {
    DataFunction_Sum("Sum", "Fine-Design_DataFunction_Sum"),
    DataFunction_Average("Average","Fine-Design_DataFunction_Average"),
    DataFunction_Max("Max","Fine-Design_DataFunction_Max"),
    DataFunction_Min("Min","Fine-Design_DataFunction_Min"),
    DataFunction_Count("Count","Fine-Design_DataFunction_Count"),
    DataFunction_None("None","Fine-Design_DataFunction_None");

    private final String functionClassName;
    private final String localeKey;

    FunctionClassMappingUtils(String functionClassName, String localeKey) {
        this.functionClassName = functionClassName;
        this.localeKey = localeKey;
    }

    public String getFunctionClassName() {
        return functionClassName;
    }

    public String getLocaleKey() {
        return localeKey;
    }

    public static String getLocaleKey(String functionClassName) {
        for (FunctionClassMappingUtils value : FunctionClassMappingUtils.values()) {
            if (StringUtils.equals(value.getFunctionClassName(), functionClassName)) {
                return value.getLocaleKey();
            }
        }
        return functionClassName;
    }

}
