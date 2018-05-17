package com.fr.design.mainframe.widget.wrappers;

import com.fr.base.iofileattr.WatermarkAttr;
import com.fr.design.Exception.ValidationException;
import com.fr.design.designer.properties.Decoder;
import com.fr.design.designer.properties.Encoder;
import com.fr.stable.StringUtils;

/**
 * Created by plough on 2018/5/15.
 */
public class WatermarkWrapper implements Encoder, Decoder {
    @Override
    public Object decode(String txt) {
        return new WatermarkAttr();
    }

    @Override
    public void validate(String txt) throws ValidationException {
        // do nothing
    }

    @Override
    public String encode(Object v) {
        if (v == null) {
            return StringUtils.EMPTY;
        }
        return ((WatermarkAttr)v).getText();
    }
}
