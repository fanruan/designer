package com.fr.design.mainframe.widget.wrappers;

import com.fr.design.Exception.ValidationException;
import com.fr.design.designer.properties.Decoder;
import com.fr.design.designer.properties.Encoder;
import com.fr.locale.InterProviderFactory;

public class MobileStyleWrapper implements Encoder, Decoder {
    @Override
    public Object decode(String txt) {
        return null;
    }

    @Override
    public void validate(String txt) throws ValidationException {

    }

    @Override
    public String encode(Object v) {
        if (v == null) {
            return InterProviderFactory.getProvider().getLocText("Fine-Engine_Report_DEFAULT");
        }
        return v.toString();
    }
}
