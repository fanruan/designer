package com.fr.design.gui;

import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.mainframe.BaseJForm;
import com.fr.design.mainframe.JTemplate;
import com.fr.general.ComparatorUtils;


/**
 * Created by mengao on 2017/9/22.
 * 单元格超链、图表超链在不同类型模版中，下拉选项不同
 */
public class HyperlinkFilterHelper {

    public static boolean whetherAddHyperlink4cell(String itemName) {
        JTemplate jTemplate = getCurrentEditingTemplate();
        if (jTemplate == null) {
            return false;
        }
        //先屏蔽掉这个，之后还有别的
        String[] names = {com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_ Hyperlink_Chart_Float")};
        for (String name : names) {
            if (!jTemplate.isJWorkBook() && ComparatorUtils.equals(itemName, name)) {
                return false;
            }
        }
        return whetherAddFormLink(jTemplate, itemName);
    }

    public static boolean whetherAddHyperlink4Chart(String itemName) {
        JTemplate jTemplate = getCurrentEditingTemplate();
        if (jTemplate == null) {
            return false;
        }
        //先屏蔽掉这个，之后还有别的
        String[] names = {com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_ Hyperlink_Chart_Float"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Hyperlink_Chart_Cell")};
        for (String name : names) {
            if (!jTemplate.isJWorkBook() && ComparatorUtils.equals(itemName, name)) {
                if (jTemplate.getEditingReportIndex() == BaseJForm.ELEMENTCASE_TAB && ComparatorUtils.equals(itemName, names[0])) {
                    //表单报表块中图表悬浮元素超链，只屏蔽联动悬浮元素
                    return false;
                } else if (jTemplate.getEditingReportIndex() == BaseJForm.FORM_TAB) {
                    //表单图表超链屏蔽掉联动悬浮元素和联动单元格
                    return false;
                }
            }
        }
        return whetherAddFormLink(jTemplate, itemName);
    }

    private static JTemplate getCurrentEditingTemplate() {
        return HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
    }

    private static boolean whetherAddFormLink(JTemplate jTemplate, String itemName) {
        String formName = com.fr.design.i18n.Toolkit.i18nText("Hyperlink-Form_link");
        return !(jTemplate.isJWorkBook() && ComparatorUtils.equals(itemName, formName));
    }
}
