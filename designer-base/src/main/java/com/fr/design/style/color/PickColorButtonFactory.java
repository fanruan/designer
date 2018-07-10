package com.fr.design.style.color;

import com.fr.base.BaseUtils;
import com.fr.design.gui.ibutton.SpecialUIButton;

import javax.swing.*;
import javax.swing.plaf.ButtonUI;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by plough on 2016/12/22.
 */
public class PickColorButtonFactory {
    private static int iconSize;
    private static final int SIZE_16 = 16;
    private static final int SIZE_18 = 18;
    private static IconType iconType;
    private static Image iconImage;

    public static SpecialUIButton getPickColorButton(ColorSelectable colorSelectable, IconType iconType) {
        return getPickColorButton(colorSelectable, iconType, false);
    }

    public static SpecialUIButton getPickColorButton(final ColorSelectable colorSelectable, IconType iconType, final boolean setColorRealTime) {
        SpecialUIButton pickColorButton = new SpecialUIButton(new WhiteButtonUI());
        PickColorButtonFactory.iconType = iconType;

        if (iconType == IconType.ICON16) {
            iconImage = BaseUtils.readImage("/com/fr/design/images/gui/colorPicker/colorPicker16.png");
            iconSize = SIZE_16;
        } else {
            iconImage = BaseUtils.readImage("/com/fr/design/images/gui/colorPicker/colorPicker18.png");
            iconSize = SIZE_18;
            pickColorButton.setBorderPainted(false);
        }
        pickColorButton.setPreferredSize(new Dimension(iconSize, iconSize));
        pickColorButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        pickColorButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                new ColorPicker(colorSelectable, setColorRealTime);
            }
        });

        return pickColorButton;
    }

    //  取色器按钮使用的图标
    public enum IconType {
        ICON16, ICON18
    }

    private static class WhiteButtonUI extends ButtonUI {
        @Override
        public void paint(Graphics g, JComponent c) {
            super.paint(g, c);
            Dimension size = c.getSize();
            g.setColor(Color.WHITE);
            g.fillRoundRect(0, 0, size.width - 1, size.height - 1, 1, 1);
            g.setColor(new Color(153, 153, 153));  // #999999
            g.drawRoundRect(0, 0, size.width - 1, size.height - 1, 1, 1);
            if (iconType == IconType.ICON16) {
                g.drawImage(
                        iconImage,
                        (size.width - iconImage.getWidth(null)) / 2,
                        (size.height - iconImage.getHeight(null)) / 2,
                        iconImage.getWidth(null),
                        iconImage.getHeight(null),
                        null
                );
            } else {
                g.drawImage(iconImage, 0, 0, iconSize, iconSize, null);
            }
        }
    }
}
