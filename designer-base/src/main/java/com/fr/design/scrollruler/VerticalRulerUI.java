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

public class VerticalRulerUI  extends RulerUI{

    public VerticalRulerUI(ScrollRulerComponent rulerComponent) {
        super(rulerComponent);
    }

    @Override
    protected void paintRuler(Graphics g, int showText, int extra, Dimension size, int ratio) {
        int k = pxToLength(extra) * ratio;
        for (int i = k; i < (pxToLength( (double)size.height + extra) + 1) * ratio; i++) {
            g.setColor(BaseRuler.UNIT_SIGN_COLOR);
            if (i % BaseRuler.SCALE_10 == 0) {
                double times = (double) HistoryTemplateListPane.getInstance().getCurrentEditingTemplate().getJTemplateResolution() / ScreenResolution.getScreenResolution();
                String text = Utils.convertNumberStringToString(Math.round((i / times) / showText));
                GraphHelper.drawLine(g, size.width, toPX(i) / ratio - extra, 0, toPX(i) / ratio - extra);
                Graphics2D gg = (Graphics2D) g.create(0, (int) (toPX(i) / ratio - extra + 1), size.width, BaseRuler.NUMBER_99);
                BaseUtils.drawStringStyleInRotation(gg, BaseRuler.NUMBER_11, BaseRuler.NUMBER_100, text, Style.getInstance().deriveVerticalAlignment(1).deriveRotation(
                        BaseRuler.NUMBER_90).deriveFRFont(BaseRuler.TEXT_FONT), ScreenResolution.getScreenResolution());
                gg.dispose();
            } else if (i % BaseRuler.SCALE_5 == 0) {
                GraphHelper.drawLine(g, size.width, toPX(i) / ratio - extra, BaseRuler.NUMBER_11, toPX(i) / ratio - extra);
            } else {
                GraphHelper.drawLine(g, size.width, toPX(i) / ratio - extra, BaseRuler.NUMBER_13, toPX(i) / ratio - extra);
            }
        }
        GraphHelper.drawLine(g, size.width - 1, 0, size.width - 1, size.height);
        g.setColor(BaseRuler.STRAR_BG);
        GraphHelper.drawLine(g, 0, 0, size.width, 0);
    }

    @Override
    protected void paintPTRuler(Graphics g, int extra, Dimension size, int unit) {
        int k = pxToLength(extra);
        for (int i = unit * (k/unit); i < pxToLength((double)size.height + extra); i += unit) {
            g.setColor(BaseRuler.UNIT_SIGN_COLOR);
            if (i % BaseRuler.SCALE_100 == 0) {
                GraphHelper.drawLine(g, size.width, toPX(i) - extra, 0, toPX(i) - extra);
                String text = Utils.convertNumberStringToString(i);
                Graphics2D gg = (Graphics2D) g.create(0, (int) (toPX(i) - extra + 1), size.width, BaseRuler.NUMBER_99);
                BaseUtils.drawStringStyleInRotation(gg, BaseRuler.NUMBER_11, BaseRuler.NUMBER_100, text, Style.getInstance().deriveVerticalAlignment(1).deriveRotation(
                        BaseRuler.NUMBER_90).deriveFRFont(BaseRuler.TEXT_FONT), ScreenResolution.getScreenResolution());
                gg.dispose();
            } else if (i % BaseRuler.SCALE_50 == 0) {
                GraphHelper.drawLine(g, size.width, toPX(i) - extra, BaseRuler.NUMBER_5, toPX(i) - extra);
            } else {
                GraphHelper.drawLine(g, size.width, toPX(i) - extra, BaseRuler.NUMBER_11, toPX(i) - extra);
            }
        }
        GraphHelper.drawLine(g, size.width - 1, 0, size.width - 1, size.height);
        g.setColor(BaseRuler.STRAR_BG);
        GraphHelper.drawLine(g, 0, 0, size.width, 0);

    }
}