package com.fr.design.mainframe.widget.wrappers;

import com.fr.general.Inter;
import com.fr.stable.ParameterProvider;
import com.fr.stable.StringUtils;
import com.fr.design.Exception.ValidationException;
import com.fr.design.designer.properties.Decoder;
import com.fr.design.designer.properties.Encoder;

public class ParameterWrapper implements Encoder, Decoder {

    @Override
    public String encode(Object v) {
        if (v == null) {
            return StringUtils.EMPTY;
        }
        return Inter.getLocText("Total") + ((ParameterProvider[]) v).length + Inter.getLocText("Parameters");
    }

    @Override
    public Object decode(String txt) {
        return null;
    }

    @Override
    public void validate(String txt) throws ValidationException {
       
    }
}