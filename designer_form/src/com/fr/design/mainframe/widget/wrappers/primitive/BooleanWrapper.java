package com.fr.design.mainframe.widget.wrappers.primitive;

import com.fr.design.Exception.ValidationException;
import com.fr.design.designer.properties.Decoder;
import com.fr.design.designer.properties.Encoder;

public class BooleanWrapper implements Encoder, Decoder {

    @Override
    public String encode(Object v) {
        if (v == null) {
            return "false";
        }
        Boolean bool = (Boolean) v;
        return bool.booleanValue() ? "true" : "false";
    }

    @Override
    public Object decode(String txt) {
        if (txt == null) {
            return false;
        }
        return txt.equals("true");
    }

    public void validate(String txt) throws ValidationException {
        if (txt == null) {
            throw new ValidationException("Boolean value should be either true or false!");
        }
        if (!(txt.equals("true") || txt.equals("false"))) {
            throw new ValidationException("Boolean value should be either true or false!");
        }
    }
}