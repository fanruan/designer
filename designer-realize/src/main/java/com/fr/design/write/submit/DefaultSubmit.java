package com.fr.design.write.submit;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.fun.impl.AbstractSubmitProvider;


public class DefaultSubmit extends AbstractSubmitProvider {

    @Override
    public BasicBeanPane appearanceForSubmit() {
        return new CustomSubmitJobPane();
    }

    @Override
    public String dataForSubmit() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Submmit_WClass");
    }

    @Override
    public String keyForSubmit() {
        return "submitnormal";
    }
}
