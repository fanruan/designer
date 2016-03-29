package com.fr.design.mainframe.widget.wrappers;

import com.fr.stable.StringUtils;
import com.fr.design.Exception.ValidationException;
import com.fr.design.designer.properties.Decoder;
import com.fr.design.designer.properties.Encoder;

public class IconWrapper implements Encoder, Decoder {

    @Override
    public Object decode(String txt) {
        return txt;
    }

    @Override
    public String encode(Object v) {
        if (v == null) {
            return StringUtils.EMPTY;
        }
        return v.toString();
    }

    @Override
    public void validate(String txt) throws ValidationException {
    }
}