package com.fr.design.parameter;

import com.fr.base.FRContext;
import com.fr.base.Parameter;
import com.fr.form.main.Form;
import com.fr.form.main.FormIO;

/**
 * @author richie
 * @date 14/11/10
 * @since 8.0
 * 表单模板参数读取实现
 */
public class FormParameterReader extends AbstractParameterReader {

    @Override
    public Parameter[] readParameterFromPath(String tplPath) {
        if (accept(tplPath, ".frm", ".form")) {
            try {
                Form form = FormIO.readForm(FRContext.getCurrentEnv(), tplPath);
                return form.getParameters();
            } catch (Exception e1) {
                FRContext.getLogger().error(e1.getMessage(), e1);
            }
            return new Parameter[0];
        }
        return null;
    }
}