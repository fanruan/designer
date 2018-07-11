package com.fr.design.designer.properties;

import com.fr.design.mainframe.widget.renderer.EncoderCellRenderer;
import com.fr.general.IOUtils;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by zhouping on 2016/10/14.
 */
public class BodyLayoutAttrRenderer extends EncoderCellRenderer {
    public BodyLayoutAttrRenderer(Encoder encoder) {
        super(encoder);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        BufferedImage image = IOUtils.readImage("com/fr/design/images/control/combobox.png");
        g.drawImage(image, getWidth() - image.getWidth(), 0, image.getWidth(), image.getHeight(), null, this);
    }
}
