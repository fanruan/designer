/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.file.export;

import com.fr.base.Parameter;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.JWorkBook;
import com.fr.design.parameter.ParameterInputPane;
import com.fr.main.TemplateWorkBook;
import com.fr.main.impl.WorkBook;

import java.util.HashMap;
import java.util.Map;

import static com.fr.io.exporter.DesignExportScope.FINE_BOOK;

/**
 * Abstract export action.
 */
public abstract class AbstractWorkBookExportAction extends AbstractExportAction<JWorkBook> {


    protected AbstractWorkBookExportAction(JWorkBook jwb) {
        super(jwb);
    }


    protected WorkBook getTemplateWorkBook() {
        return this.getEditingComponent().getTarget();
    }

    public String exportScopeName() {
        return FINE_BOOK.toString();
    }

    @Override
    protected Map<String, Object> processParameter() {
        // 弹出参数
        final Map<String, Object> parameterMap = new HashMap<>();
        final TemplateWorkBook tpl = getTemplateWorkBook();
        Parameter[] parameters = tpl.getParameters();
        // 检查Parameter
        if (parameters != null && parameters.length > 0) {
            final ParameterInputPane pPane = new ParameterInputPane(
                    parameters);
            pPane.showSmallWindow(DesignerContext.getDesignerFrame(), new DialogActionAdapter() {
                @Override
                public void doOk() {
                    parameterMap.putAll(pPane.update());
                }
            }).setVisible(true);
        }
        return parameterMap;
    }
}
