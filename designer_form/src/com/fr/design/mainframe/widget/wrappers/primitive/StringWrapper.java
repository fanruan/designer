package com.fr.design.mainframe.widget.wrappers.primitive;



import com.fr.design.Exception.ValidationException;
import com.fr.design.designer.properties.Decoder;
import com.fr.design.designer.properties.Encoder;

public class StringWrapper implements Encoder, Decoder {

    @Override
    public String encode(Object v) {
        return (String)v;
    }

    @Override
    public Object decode(String txt) {
        return txt;
    }

    @Override
    public void validate(String txt) throws ValidationException {
    }
}