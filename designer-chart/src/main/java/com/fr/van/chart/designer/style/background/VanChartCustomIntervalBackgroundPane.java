package com.fr.van.chart.designer.style.background;

import com.fr.base.BaseFormula;
import com.fr.base.Utils;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.formula.TinyFormulaPane;
import com.fr.design.gui.frpane.UINumberDragPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.style.color.ColorSelectBox;
import com.fr.design.utils.gui.GUICoreUtils;

import com.fr.plugin.chart.VanChartAttrHelper;
import com.fr.plugin.chart.attr.axis.VanChartCustomIntervalBackground;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import java.awt.Component;
import java.awt.Dimension;

/**
 * 自定义间隔背景设置
 */
public class VanChartCustomIntervalBackgroundPane extends BasicBeanPane<VanChartCustomIntervalBackground>{
    private static final long serialVersionUID = 2700739847414325705L;

    private UIButtonGroup backgroundAxis;
    private TinyFormulaPane bottomValue;
    private TinyFormulaPane topValue;
    private ColorSelectBox color;
    private UINumberDragPane transparent;

    private VanChartCustomIntervalBackground customIntervalBackground;

    private void doLayoutPane(){
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        //
        JPanel top = FRGUIPaneFactory.createBorderLayout_L_Pane();
        this.removeAll();
        this.add(top);
        top.setBorder(GUICoreUtils.createTitledBorder(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Custom_Interval_Background") + ":", null));
        top.add(createContentPane());
    }

    private JPanel createContentPane() {
        bottomValue = new TinyFormulaPane();
        topValue = new TinyFormulaPane();
        bottomValue.setPreferredSize(new Dimension(124,20));
        topValue.setPreferredSize(new Dimension(124,20));
        color = new ColorSelectBox(100);
        transparent = new UINumberDragPane(0,100);
        double p = TableLayout.PREFERRED;
        double[] columnSize = {p,p};
        double[] rowSize = {p,p,p};

        JPanel axisPane = TableLayout4VanChartHelper.createTableLayoutPaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Axis"), backgroundAxis);

        Component[][] rangeComponents = new Component[][]{
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Bottom_Value")),bottomValue},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Plugin-ChartF_TopValue")),topValue},
        };
        JPanel temp = TableLayoutHelper.createTableLayoutPane(rangeComponents, rowSize, columnSize);
        JPanel rangePane = TableLayout4VanChartHelper.createTableLayoutPaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Range"), temp);

        Component[][] styleComponents = new Component[][]{
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("FR-Chart-Color_Color")),color},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Alpha")),transparent},
        };
        temp = TableLayoutHelper.createTableLayoutPane(styleComponents, rowSize, columnSize);
        JPanel stylePane = TableLayout4VanChartHelper.createTableLayoutPaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Chart-Style_Name"), temp);

        Component[][] components = getPaneComponents(axisPane, rangePane, stylePane);

        return TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
    }

    protected Component[][] getPaneComponents(JPanel axisPane, JPanel rangePane, JPanel stylePane) {
        return new Component[][]{
                new Component[]{axisPane,null},
                new Component[]{rangePane,null},
                new Component[]{stylePane,null},
        };
    }

    protected String title4PopupWindow(){
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Custom_Interval_Background");
    }

    public void populateBean(VanChartCustomIntervalBackground customIntervalBackground){
        this.customIntervalBackground = customIntervalBackground;
        backgroundAxis = new UIButtonGroup(customIntervalBackground.getAxisNamesArray(), customIntervalBackground.getAxisNamesArray());
        backgroundAxis.setSelectedItem(customIntervalBackground.getAxisName());

        doLayoutPane();

        bottomValue.populateBean(Utils.objectToString(customIntervalBackground.getFromFormula()));
        topValue.populateBean(Utils.objectToString(customIntervalBackground.getToFormula()));
        color.setSelectObject(customIntervalBackground.getBackgroundColor());
        transparent.populateBean(customIntervalBackground.getAlpha() * VanChartAttrHelper.PERCENT);
    }

    public VanChartCustomIntervalBackground updateBean(){

        customIntervalBackground.setAxisName(backgroundAxis.getSelectedItem().toString());

        customIntervalBackground.setFromFormula(BaseFormula.createFormulaBuilder().build(bottomValue.updateBean()));
        customIntervalBackground.setToFormula(BaseFormula.createFormulaBuilder().build(topValue.updateBean()));
        customIntervalBackground.setBackgroundColor(color.getSelectObject());
        customIntervalBackground.setAlpha(transparent.updateBean() / VanChartAttrHelper.PERCENT);
        return customIntervalBackground;
    }
}