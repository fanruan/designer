package com.fr.design.mainframe.widget.wrappers;


import java.awt.Rectangle;
import java.util.StringTokenizer;

import com.fr.stable.StringUtils;
import com.fr.design.Exception.ValidationException;
import com.fr.design.designer.properties.Decoder;
import com.fr.design.designer.properties.Encoder;

public class RectangleWrapper implements Encoder, Decoder {

    public RectangleWrapper() {
    }

    @Override
    public Object decode(String txt) {
        if (StringUtils.isEmpty(txt)) {
            return null;
        }

        txt = txt.trim();
        txt = txt.substring(1, txt.length() - 1).trim();

        StringTokenizer tokenizer = new StringTokenizer(txt, ",");

        return new Rectangle(Integer.parseInt(tokenizer.nextToken().trim()), Integer.parseInt(tokenizer.nextToken().trim()), Integer.parseInt(tokenizer.nextToken().trim()), Integer.parseInt(tokenizer.nextToken().trim()));
    }

    @Override
    public String encode(Object v) {
        if (v == null) {
            return null;
        }

        Rectangle r = (Rectangle) v;

        return "[" + r.x + ", " + r.y + ", " + r.width + ", " + r.height + "]";
    }

    @Override
    public void validate(String txt) throws ValidationException {
        WrapperUtils.validateIntegerTxtFomat(txt, 4, newValidationException());
    }

    private ValidationException newValidationException() {
        return new ValidationException("Rectangle string takes form like: [X, Y, width, height]!");
    }
}