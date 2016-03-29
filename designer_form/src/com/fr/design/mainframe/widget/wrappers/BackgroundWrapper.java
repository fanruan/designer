/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.mainframe.widget.wrappers;

import com.fr.general.Inter;
import com.fr.base.background.ColorBackground;
import com.fr.base.background.GradientBackground;
import com.fr.base.background.ImageBackground;
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
            return Inter.getLocText("Color");
        } else if (v instanceof TextureBackground) {
            return Inter.getLocText("Background-Texture");
        } else if (v instanceof PatternBackground) {
            return Inter.getLocText("Background-Pattern");
        } else if (v instanceof ImageBackground) {
            return Inter.getLocText("Image");
        } else if (v instanceof GradientBackground) {
            return Inter.getLocText("Gradient-Color");
        } else {
            return Inter.getLocText("None");
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