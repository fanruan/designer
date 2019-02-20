package com.fr.van.chart.designer.component.border;

import com.fr.design.gui.frpane.UINumberDragPane;
import com.fr.design.gui.ilable.UILabel;

import com.fr.design.utils.gui.UIComponentUtils;
import com.fr.design.widget.FRWidgetFactory;
import com.fr.plugin.chart.VanChartAttrHelper;
import com.fr.plugin.chart.base.AttrBorderWithAlpha;

import javax.swing.JSeparator;
import java.awt.BorderLayout;
import java.awt.Component;

/**
 * Created by Mitisky on 16/5/19.
 * 边框,线型/颜色/不透明度
 */
public class VanChartBorderWithAlphaPane extends VanChartBorderPane{
    private UINumberDragPane transparent;

    @Override
    protected void initComponents() {
        transparent = new UINumberDragPane(0,100);
        this.add(new JSeparator(), BorderLayout.SOUTH);

        super.initComponents();
    }

    @Override
    protected Component[][] getUseComponent() {
        return new Component[][]{
                new Component[]{null,null},
                new Component[]{
                        FRWidgetFactory.createLineWrapLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Line_Style")),
                        UIComponentUtils.wrapWithBorderLayoutPane(currentLineCombo)
                },
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Color")),currentLineColorPane},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Alpha")), transparent}
        };
    }

    public void populate(AttrBorderWithAlpha attr) {
        if(attr == null){
            return;
        }
        super.populate(attr);
        transparent.populateBean(attr.getAlpha() * VanChartAttrHelper.PERCENT);
    }

    @Override
    public AttrBorderWithAlpha update() {
        AttrBorderWithAlpha attrBorderWithAlpha = new AttrBorderWithAlpha();
        super.update(attrBorderWithAlpha);
        attrBorderWithAlpha.setAlpha(transparent.updateBean()/ VanChartAttrHelper.PERCENT);
        return attrBorderWithAlpha;
    }
}
