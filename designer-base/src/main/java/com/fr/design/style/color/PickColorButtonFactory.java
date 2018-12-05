package com.fr.design.style.color;

import com.fr.base.BaseUtils;
import com.fr.design.gui.ibutton.SpecialUIButton;
import com.fr.design.gui.ibutton.UIBasicButtonUI;

import javax.swing.*;
import javax.swing.plaf.ButtonUI;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by plough on 2016/12/22.
 */
class PickColorButtonFactory {
    private static int iconSize;
    private static final int SIZE_16 = 16;
    private static final int SIZE_18 = 18;
    private static IconType iconType;
    private static Image iconImage;

    /**
     * 生成取色按钮
     * @param colorSelectable 接收取到的颜色值的对象
     * @param iconType IconType 枚举，可选择 16px 和 18px 两个尺寸
     * @param setColorRealTime 是否在取色过程中，实时更新颜色值
     * @return SpecialUIButton 屏幕取色按钮
     */
    static JButton getPickColorButton(final ColorSelectable colorSelectable, IconType iconType, final boolean setColorRealTime) {
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

        pickColorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ColorPicker(colorSelectable, setColorRealTime);
            }
        });

        return pickColorButton;
    }

    /**
     * 取色按钮可使用的图标尺寸
     */
    public enum IconType {
        ICON16, ICON18
    }

    private static class WhiteButtonUI extends UIBasicButtonUI {
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
