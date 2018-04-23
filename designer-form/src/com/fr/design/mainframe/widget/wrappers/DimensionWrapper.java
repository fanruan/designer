package com.fr.design.mainframe.widget.wrappers;

import java.awt.Dimension;
import java.util.StringTokenizer;

import com.fr.stable.StringUtils;
import com.fr.design.Exception.ValidationException;
import com.fr.design.designer.properties.Decoder;
import com.fr.design.designer.properties.Encoder;

public class DimensionWrapper implements Encoder, Decoder {
    public DimensionWrapper() {
    }

    @Override
    public Object decode(String txt) {
        if (StringUtils.isEmpty(txt)) {
            return null;
        }

        txt = txt.trim();
        txt = txt.substring(1, txt.length() - 1).trim();

        StringTokenizer tokenizer = new StringTokenizer(txt, ",");

        return new Dimension(Integer.parseInt(tokenizer.nextToken().trim()), Integer.parseInt(tokenizer.nextToken().trim()));
    }

    @Override
    public String encode(Object v) {
        if (v == null) {
            return null;
        }

        Dimension dim = (Dimension) v;

        return "[" + dim.width + ", " + dim.height + "]";
    }
    
    @Override
    public void validate(String txt) throws ValidationException {
        WrapperUtils.validateIntegerTxtFomat(txt, 2, newValidateException());
    }

    private ValidationException newValidateException(){
        return new ValidationException("Dimension string takes form like: [width, height]!");
    }
}