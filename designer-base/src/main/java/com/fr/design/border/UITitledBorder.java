package com.fr.design.border;

import com.fr.design.constants.UIConstants;

import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;
import java.awt.Color;

/**
 * @author yaohwu
 */
public class UITitledBorder extends TitledBorder {

    private static final long serialVersionUID = 1L;

    public static UITitledBorder createBorderWithTitle(String title) {
        return new UITitledBorder(title);
    }

    public static UITitledBorder createBorderWithTitle(String title, int roundedCorner) {
        return new UITitledBorder(title, roundedCorner);
    }

    private UITitledBorder(String title) {
        super(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createEmptyBorder(
                                0,
                                0,
                                5,
                                0),
                        new UIRoundedBorder(
                                UIConstants.TITLED_BORDER_COLOR,
                                1,
                                10)
                ),
                title,
                TitledBorder.LEADING,
                TitledBorder.TOP,
                null,
                new Color(1, 159, 222)
        );
    }

    /**
     * @param title title
     * @param roundedCorner corner width 圆弧宽度，即圆角直径
     */
    private UITitledBorder(String title, int roundedCorner) {
        super(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createEmptyBorder(
                                0,
                                0,
                                5,
                                0),
                        new UIRoundedBorder(
                                UIConstants.TITLED_BORDER_COLOR,
                                1,
                                roundedCorner)
                ),
                title,
                TitledBorder.LEADING,
                TitledBorder.TOP,
                null,
                new Color(1, 159, 222)
        );
    }
}