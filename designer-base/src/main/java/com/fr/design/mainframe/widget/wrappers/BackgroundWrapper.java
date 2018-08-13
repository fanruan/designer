/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.mainframe.widget.wrappers;


import com.fr.base.background.ColorBackground;
import com.fr.base.background.GradientBackground;
import com.fr.base.background.ImageFileBackground;
import com.fr.base.background.PatternBackground;
import com.fr.base.background.TextureBackground;
import com.fr.design.Exception.ValidationException;
import com.fr.design.designer.properties.Decoder;
import com.fr.design.designer.properties.Encoder;

/**
 * @author richer
 * @since 6.5.3
 */
public class BackgroundWrapper implements Encoder, Decoder {

    public BackgroundWrapper() {
    }

    @Override
    public String encode(Object v) {
        if (v instanceof ColorBackground) {
            return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Color");
        } else if (v instanceof TextureBackground) {
            return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Background_Texture");
        } else if (v instanceof PatternBackground) {
            return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Background_Pattern");
        } else if (v instanceof ImageFileBackground) {
            return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Image");
        } else if (v instanceof GradientBackground) {
            return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Gradient_Color");
        } else {
            return com.fr.design.i18n.Toolkit.i18nText("None");
        }
    }

    @Override
    public Object decode(String txt) {
        return null;
    }

    @Override
    public void validate(String txt) throws ValidationException {
    }
}
