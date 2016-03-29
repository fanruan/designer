package com.fr.design.parameter;

import com.fr.base.FRContext;
import com.fr.base.Parameter;
import com.fr.io.TemplateWorkBookIO;
import com.fr.main.TemplateWorkBook;

/**
 * @author richie
 * @date 14/11/10
 * @since 8.0
 * 从报表模板中读取参数
 */
public class WorkBookParameterReader extends AbstractParameterReader {

    @Override
    public Parameter[] readParameterFromPath(String tplPath) {
        if (accept(tplPath, ".cpt")) {
            try {
                TemplateWorkBook template = TemplateWorkBookIO.readTemplateWorkBook(FRContext.getCurrentEnv(), tplPath);
                return template.getParameters();
            } catch (Exception e1) {
                FRContext.getLogger().error(e1.getMessage(), e1);
            }
            return new Parameter[0];
        }
        return null;
    }
}