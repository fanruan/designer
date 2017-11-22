/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.scrollruler;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import com.fr.base.BaseUtils;
import com.fr.base.GraphHelper;
import com.fr.base.ScreenResolution;
import com.fr.base.Style;
import com.fr.base.Utils;
import com.fr.design.file.HistoryTemplateListPane;

public class HorizontalRulerUI extends RulerUI {

    public HorizontalRulerUI(ScrollRulerComponent rulerComponent) {
        super(rulerComponent);
    }

    @Override
    protected void paintRuler(Graphics g, int showText, int extra, Dimension size, int ratio) {
        int k = pxToLength(extra) * ratio;
        for (int i = k; i < (pxToLength(size.getWidth() + extra) + 1) * ratio; i++) {
            g.setColor(BaseRuler.UNIT_SIGN_COLOR);
            if (i %  BaseRuler.SCALE_10 == 0) {
                double times = (double)HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().getJTemplateResolution() / ScreenResolution.getScreenResolution();
                String text = Utils.convertNumberStringToString(Math.round((i / times) / showText));
                Graphics2D gg = (Graphics2D) g.create((int) (toPX(i) / ratio) - extra + 1, 0, BaseRuler.NUMBER_100, size.height);
                BaseUtils.drawStringStyleInRotation(gg, BaseRuler.NUMBER_100, BaseRuler.NUMBER_14, text, Style.getInstance().deriveHorizontalAlignment(
                        Style.LEFT_TO_RIGHT).deriveFRFont(BaseRuler.TEXT_FONT), ScreenResolution.getScreenResolution());
                GraphHelper.drawLine(g, toPX(i) / ratio - extra, size.height, toPX(i) / ratio - extra, 0);
                gg.dispose();
            } else if (i % BaseRuler.SCALE_5 == 0) {
                GraphHelper.drawLine(g, toPX(i) / ratio - extra, size.height, toPX(i) / ratio - extra, BaseRuler.NUMBER_11);
            } else {
                GraphHelper.drawLine(g, toPX(i) / ratio - extra, size.height, toPX(i) / ratio - extra, BaseRuler.NUMBER_13);
            }
        }
        GraphHelper.drawLine(g, 0, size.height - 1, size.width, size.height - 1);
        g.setColor(BaseRuler.STRAR_BG);
        GraphHelper.drawLine(g, 0, 0, 0, size.height);
    }

    @Override
    protected void paintPTRuler(Graphics g, int extra, Dimension size, int unit) {
        int k = pxToLength(extra);
        for (int i = unit * (k/unit); i < pxToLength(size.getWidth() + extra); i+=unit) {
            g.setColor(BaseRuler.UNIT_SIGN_COLOR);
            if (i %  BaseRuler.SCALE_100 == 0) {
                String text = Utils.convertNumberStringToString(i );
                Graphics2D gg = (Graphics2D) g.create((int) (toPX(i) ) - extra + 1, 0,  BaseRuler.NUMBER_100, size.height);
                BaseUtils.drawStringStyleInRotation(gg,  BaseRuler.NUMBER_100,  BaseRuler.NUMBER_14, text, Style.getInstance().deriveHorizontalAlignment(
                        Style.LEFT_TO_RIGHT).deriveFRFont(BaseRuler.TEXT_FONT), ScreenResolution.getScreenResolution());
                GraphHelper.drawLine(g, toPX(i) - extra, size.height, toPX(i)  - extra, 0);
                gg.dispose();
            } else if (i %  BaseRuler.SCALE_50 == 0) {
                GraphHelper.drawLine(g, toPX(i) - extra, size.height, toPX(i)  - extra,  BaseRuler.NUMBER_5);
            } else {
                GraphHelper.drawLine(g, toPX(i) - extra, size.height, toPX(i)  - extra,  BaseRuler.NUMBER_11);
            }
        }
        GraphHelper.drawLine(g, 0, size.height - 1, size.width, size.height - 1);
        g.setColor(BaseRuler.STRAR_BG);
        GraphHelper.drawLine(g, 0, 0, 0, size.height);

    }
}