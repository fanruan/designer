package com.fr.design.mainframe.widget.wrappers;

import com.fr.design.Exception.ValidationException;
import com.fr.design.designer.properties.Decoder;
import com.fr.design.designer.properties.Encoder;

/**
 * @author hades
 * @version 10.0
 * Created by hades on 2019/12/24
 */
public class MobileBookMarkStyleWrapper implements Encoder, Decoder {
    @Override
    public Object decode(String txt) {
        return null;
    }

    @Override
    public void validate(String txt) throws ValidationException {

    }

    @Override
    public String encode(Object v) {
        if (v == null) {
            return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Mobile_None_BookMark_Style");
        }
        return v.toString();
    }
}
