/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.cell.editor;

import com.fr.base.Style;
import com.fr.base.TextFormat;
import com.fr.report.ReportHelper;
import com.fr.report.cell.FloatElement;

/**
 * FloatEditor used to edit general object.
 */
public class GeneralFloatEditor extends TextFloatEditor {
    /**
     * Gets the value of the FloatEditor.
     */
    @Override
	public Object getFloatEditorValue()  throws Exception {
        Object textValue = super.getFloatEditorValue();

        //如果格式是TextFormat，就返回普通文本.
        //TODO, peter 这个地方需要重新设计,我感觉下面的 convertGeneralStringAccordingToExcel，可以用Foramt来实现。
        FloatElement floatElement = this.getFloatElement();
        //peter:只读方式获得Style.
        Style style = floatElement.getStyle();
        if(style != null &&
                style.getFormat() != null && style.getFormat() == TextFormat.getInstance()) {
            return textValue;
        }

        return ReportHelper.convertGeneralStringAccordingToExcel(textValue);
    }
}