package com.fr.design.write.submit;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.fun.impl.AbstractSubmitProvider;
import com.fr.general.Inter;

public class DefaultSubmit extends AbstractSubmitProvider {

    @Override
    public BasicBeanPane appearanceForSubmit() {
        return new CustomSubmitJobPane();
    }

    @Override
    public String dataForSubmit() {
        return Inter.getLocText("FR-Designer_Submmit_WClass");
    }

    @Override
    public String keyForSubmit() {
        return "submitnormal";
    }
}