package com.fr.van.chart.designer.style;

import com.fr.design.gui.frpane.AbstractAttrNoScrollPane;
import com.fr.design.gui.frpane.UINumberDragPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.i18n.Toolkit;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.mainframe.chart.gui.ColorSelectBoxWithOutTransparent;
import com.fr.design.mainframe.chart.gui.style.series.MapColorPickerPaneWithFormula;
import com.fr.plugin.chart.type.LegendType;
import com.fr.van.chart.designer.style.axis.component.MinMaxValuePaneWithOutTick;
import com.fr.van.chart.range.component.GradualIntervalConfigPane;
import com.fr.van.chart.range.component.GradualLegendPane;
import com.fr.van.chart.range.component.LegendGradientBar;
import com.fr.van.chart.range.component.SectionIntervalConfigPaneWithOutNum;
import com.fr.van.chart.range.component.SectionLegendPane;

import javax.swing.JPanel;
import java.awt.Component;

/**
 * Created by Mitisky on 16/10/20.
 * 只有渐变色图例和区域段图例.
 * 没有主题配色
 */
public class HeatMapRangeLegendPane extends VanChartRangeLegendPane {

    public HeatMapRangeLegendPane() {
    }

    public HeatMapRangeLegendPane(VanChartStylePane parent) {
        super(parent);
    }

    protected JPanel createCommonLegendPane(){
        return this.createLegendPaneWithoutHighlight();
    }

    @Override
    protected UIButtonGroup<LegendType> createLegendTypeButton(){
        return new UIButtonGroup<LegendType>(new String[]{
                Toolkit.i18nText("Fine-Design_Chart_Legend_Gradual"),
                Toolkit.i18nText("Fine-Design_Chart_Legend_Section")
        }, new LegendType[]{LegendType.GRADUAL, LegendType.SECTION});
    }

    @Override
    protected GradualLegendPane createGradualLegendPane() {
        return new GradualLegendPane(){
            @Override
            protected GradualIntervalConfigPane createGradualIntervalConfigPane() {
                return new GradualIntervalConfigPane(){
                    @Override
                    protected Component[][] getPaneComponents(MinMaxValuePaneWithOutTick minMaxValuePane, ColorSelectBoxWithOutTransparent colorSelectBox, UINumberDragPane numberDragPane, LegendGradientBar legendGradientBar) {
                        return new Component[][]{
                                new Component[]{minMaxValuePane, null},
                                new Component[]{new BoldFontTextLabel(Toolkit.i18nText("Fine-Design_Chart_Value_Divided_Stage")), numberDragPane},
                                new Component[]{null, legendGradientBar}
                        };
                    }
                };
            }
        };
    }

    @Override
    protected SectionLegendPane createSectionLegendPane() {
        return new SectionLegendPane(this.getLegendPaneParent()) {
            @Override
            protected MapColorPickerPaneWithFormula createSectionIntervalConfigPane(AbstractAttrNoScrollPane parent) {
                return new SectionIntervalConfigPaneWithOutNum(parent);
            }
        };
    }
}
