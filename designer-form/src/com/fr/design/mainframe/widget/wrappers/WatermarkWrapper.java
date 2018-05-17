package com.fr.design.mainframe.widget.wrappers;

import com.fr.base.Watermark;
import com.fr.design.Exception.ValidationException;
import com.fr.design.designer.properties.Decoder;
import com.fr.design.designer.properties.Encoder;

/**
 * Created by plough on 2018/5/15.
 */
public class WatermarkWrapper implements Encoder, Decoder {
    @Override
    public Object decode(String txt) {
        return new Watermark();
    }

    @Override
    public void validate(String txt) throws ValidationException {
        // do nothing
    }

    @Override
    public String encode(Object v) {
        return ((Watermark)v).getText();
    }
}
