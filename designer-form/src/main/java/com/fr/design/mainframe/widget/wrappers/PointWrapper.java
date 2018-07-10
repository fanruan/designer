package com.fr.design.mainframe.widget.wrappers;

import java.awt.Point;
import java.util.StringTokenizer;

import com.fr.stable.StringUtils;
import com.fr.design.Exception.ValidationException;
import com.fr.design.designer.properties.Decoder;
import com.fr.design.designer.properties.Encoder;


public class PointWrapper implements Encoder, Decoder {

    public PointWrapper() {
    }

    @Override
    public Object decode(String txt) {
        if (StringUtils.isEmpty(txt)) {
            return null;
        }

        txt = txt.trim();
        txt = txt.substring(1, txt.length() - 1).trim();

        StringTokenizer tokenizer = new StringTokenizer(txt, ",");

        return new Point(Integer.parseInt(tokenizer.nextToken().trim()), Integer.parseInt(tokenizer.nextToken().trim()));
    }

    @Override
    public String encode(Object v) {
        if (v == null) {
            return null;
        }

        Point p = (Point) v;

        return "[" + p.x + ", " + p.y + "]";
    }
    
    @Override
    public void validate(String txt) throws ValidationException {
        WrapperUtils.validateIntegerTxtFomat(txt, 2, newValidateException());
    }

    private ValidationException newValidateException(){
        return new ValidationException("Point string takes form like: [width, height]!");
    }
}