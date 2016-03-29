package com.fr.design.mainframe.widget.wrappers.primitive;

import com.fr.design.Exception.ValidationException;
import com.fr.design.designer.properties.Decoder;
import com.fr.design.designer.properties.Encoder;

public class CharWrapper implements Encoder, Decoder {

    @Override
    public String encode(Object v) {
        if (v == null) {
            return "\\0";
        }
        return v.toString();
    }

    @Override
    public Object decode(String txt) {
        if (txt == null || txt.length() == 0) {
            return '\0';
        }
        if (txt.equals("\\0")) {
            return '\0';
        } else {
            return txt.charAt(0);
        }
    }

    @Override
    public void validate(String txt) throws ValidationException {
        if (txt == null || txt.length() != 1) {
            throw new ValidationException("Character should be 1 character long!");
        }
    }
}