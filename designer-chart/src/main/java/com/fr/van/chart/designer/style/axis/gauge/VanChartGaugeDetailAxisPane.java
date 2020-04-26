package com.fr.van.chart.designer.style.axis.gauge;

import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.i18n.Toolkit;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.design.mainframe.chart.gui.style.ChartTextAttrPane;
import com.fr.design.mainframe.chart.gui.style.ChartTextAttrPaneWithAuto;
import com.fr.design.style.color.ColorSelectBox;

import com.fr.plugin.chart.attr.axis.VanChartAxis;
import com.fr.plugin.chart.attr.axis.VanChartGaugeAxis;
import com.fr.plugin.chart.gauge.VanChartGaugePlot;
import com.fr.plugin.chart.type.FontAutoType;
import com.fr.plugin.chart.type.GaugeStyle;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;
import com.fr.van.chart.designer.style.VanChartStylePane;
import com.fr.van.chart.designer.style.axis.VanChartValueAxisPane;
import com.fr.van.chart.designer.style.axis.component.MinMaxValuePaneWithOutTick;
import com.fr.van.chart.designer.style.axis.component.VanChartMinMaxValuePane;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 仪表盘的坐标轴界面
 */
public class VanChartGaugeDetailAxisPane extends VanChartValueAxisPane {

    private static final long serialVersionUID = -9213466625457882224L;

    private ColorSelectBox mainTickColor;
    private ColorSelectBox secTickColor;

    private GaugeStyle gaugeStyle;

    public VanChartGaugeDetailAxisPane() {
    }

    protected JPanel createContentPane(boolean isXAxis){
        if(gaugeStyle == null){
            gaugeStyle = GaugeStyle.POINTER;
        }
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {f, p};
        double[] rowSize = {p,p,p,p,p,p,p,p};
        return TableLayoutHelper.createTableLayoutPane(getPanelComponents(p, f, columnSize), rowSize, columnSize);
    }

    private Component[][] getPanelComponents(double p, double f, double[] columnSize) {
        switch (gaugeStyle){
            case THERMOMETER:
                return new Component[][]{
                        new Component[]{createLabelPane(new double[]{p, p}, columnSize), null},
                        new Component[]{createValueDefinition(), null},
                        new Component[]{createTickColorPane(new double[]{p, p, p}, new double[]{p, f}), null},
                        new Component[]{createValueStylePane()},
                };
            case RING:
                return new Component[][]{
                        new Component[]{createValueDefinition(), null},
                };
            case SLOT:
                return new Component[][]{
                        new Component[]{createValueDefinition(), null}
                };
            default:
                return new Component[][]{
                        new Component[]{createLabelPane(new double[]{p, p, p}, columnSize), null},
                        new Component[]{createValueDefinition(), null},
                        new Component[]{createValueStylePane()},
                };
        }
    }

    protected JPanel createLabelPane(double[] row, double[] col){
        showLabel = new UIButtonGroup(new String[]{Toolkit.i18nText("Fine-Design_Chart_Use_Show"), Toolkit.i18nText("Fine-Design_Chart_Hidden")});
        labelTextAttrPane = getChartTextAttrPane();
        labelPanel = new JPanel(new BorderLayout());
        labelPanel.add(labelTextAttrPane);
        labelPanel.setBorder(BorderFactory.createEmptyBorder(0,15,0,0));
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.add(TableLayout4VanChartHelper.createGapTableLayoutPane(Toolkit.i18nText("Fine-Design_Chart_Axis_Label"), showLabel), BorderLayout.NORTH);
        panel.add(labelPanel, BorderLayout.CENTER);
        showLabel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkLabelPane();
            }
        });
        JPanel jPanel = TableLayout4VanChartHelper.createExpandablePaneWithTitle(PaneTitleConstants.CHART_STYLE_LABEL_TITLE, panel);
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,0,15));
        return jPanel;
    }

    protected ChartTextAttrPane getChartTextAttrPane() {
        if (isMulti(gaugeStyle)) {
            return new ChartTextAttrPaneWithAuto(FontAutoType.SIZE_AND_COLOR);
        } else {
            return new ChartTextAttrPane();
        }
    }

    private JPanel createValueDefinition(){
        switch (gaugeStyle){
            case RING:
            case SLOT:
                minMaxValuePane = new MinMaxValuePaneWithOutTick();
                break;
            default:
                minMaxValuePane = new VanChartMinMaxValuePane();
                break;
        }
        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(Toolkit.i18nText("Fine-Design_Chart_Value_Definition"), minMaxValuePane);
    }

    private JPanel createTickColorPane(double[] row, double[] col){
        mainTickColor = new ColorSelectBox(100);
        secTickColor = new ColorSelectBox(100);
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Toolkit.i18nText("Fine-Design_Chart_Main_Graduation_Line")), mainTickColor},
                new Component[]{new UILabel(Toolkit.i18nText("Fine-Design_Chart_Second_Graduation_Line")), secTickColor},
        };
        JPanel panel = TableLayoutHelper.createTableLayoutPane(components, row, col);
        JPanel jPanel = TableLayout4VanChartHelper.createExpandablePaneWithTitle(Toolkit.i18nText("Fine-Design_Chart_TickColor"), panel);
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,0,15));
        return jPanel;
    }

    private boolean isMulti(GaugeStyle style){
        return style == GaugeStyle.POINTER || style == GaugeStyle.POINTER_SEMI;
    }

    private boolean samePane(GaugeStyle style1, GaugeStyle style2){
        return style1 == style2 || (isMulti(style1) && isMulti(style2));
    }

    public void populateBean(VanChartGaugePlot gaugePlot, VanChartStylePane parent){
        if(!samePane(gaugePlot.getGaugeStyle(), gaugeStyle)){
            gaugeStyle = gaugePlot.getGaugeStyle();
            reLayoutPane(false);
            parent.initAllListeners();
        }
        populateBean(gaugePlot.getGaugeAxis());
    }

    public void populateBean(VanChartAxis axis){
        VanChartGaugeAxis gaugeAxis = (VanChartGaugeAxis)axis;
        if(mainTickColor != null){
            mainTickColor.setSelectObject(gaugeAxis.getMainTickColor());
        }
        if(secTickColor != null){
            secTickColor.setSelectObject(gaugeAxis.getSecTickColor());
        }
        super.populateBean(gaugeAxis);
    }

    public void updateBean(VanChartAxis axis) {
        VanChartGaugeAxis gaugeAxis = (VanChartGaugeAxis)axis;
        if(mainTickColor != null){
            gaugeAxis.setMainTickColor(mainTickColor.getSelectObject());
        }
        if(secTickColor != null){
            gaugeAxis.setSecTickColor(secTickColor.getSelectObject());
        }
        super.updateBean(gaugeAxis);
    }
}