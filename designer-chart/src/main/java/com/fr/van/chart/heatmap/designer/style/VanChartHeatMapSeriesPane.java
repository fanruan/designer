package com.fr.van.chart.heatmap.designer.style;

import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.design.gui.frpane.UINumberDragPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.ChartStylePane;
import com.fr.plugin.chart.heatmap.VanChartHeatMapPlot;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;
import com.fr.van.chart.designer.component.VanChartFillStylePane;
import com.fr.van.chart.map.VanChartMapSeriesPane;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;

/**
 * Created by Mitisky on 16/10/20.
 */
public class VanChartHeatMapSeriesPane extends VanChartMapSeriesPane {

    private UISpinner radius;
    private UISpinner blur;

    private UINumberDragPane minOpacity;
    private UINumberDragPane maxOpacity;

    public VanChartHeatMapSeriesPane(ChartStylePane parent, Plot plot) {
        super(parent, plot);
    }

    @Override
    /**
     * 返回 填充界面.
     */
    protected VanChartFillStylePane getVanChartFillStylePane() {
        return null;
    }

    //设置色彩面板内容
    protected void setColorPaneContent (JPanel panel) {
        panel.add(createNullValueColorPane(), BorderLayout.NORTH);
        panel.add(createAlphaPane(), BorderLayout.CENTER);
    }

    /**
     * 在每个不同类型Plot, 得到不同类型的属性. 比如: 柱形的风格, 折线的线型曲线.
     */
    @Override
    protected JPanel getContentInPlotType() {
        final UIButtonGroup group = new UIButtonGroup(new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Heat_Point"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Background_Area")});
        group.setSelectedIndex(0);

        JPanel backgroundAreaPane = createAreaPane();
        JPanel heatPointPane = createHeatPointPane();
        final CardLayout layout = new CardLayout();
        final JPanel detailPane = new JPanel(layout);

        detailPane.add(heatPointPane, "heatPointPane");
        detailPane.add(backgroundAreaPane, "backgroundAreaPane");

        JPanel contentPane = new JPanel(new BorderLayout(0,6));
        contentPane.add(group, BorderLayout.NORTH);
        contentPane.add(detailPane, BorderLayout.CENTER);

        group.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if(group.getSelectedIndex() == 0){
                    layout.show(detailPane, "heatPointPane");
                } else {
                    layout.show(detailPane, "backgroundAreaPane");
                }
            }
        });

        return contentPane;
    }

    private JPanel createHeatPointPane(){
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] row = {p, p, p, p, p};
        double[] col = {f};

        Component[][] components = new Component[][]{
                new Component[]{createHeatPointStylePane()},
                new Component[]{createOpacityPane()}
        };

        return TableLayoutHelper.createTableLayoutPane(components, row, col);
    }

    private JPanel createHeatPointStylePane(){
        radius = new UISpinner(0,Double.MAX_VALUE,1,30);
        blur = new UISpinner(0,100,1,30);

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double d = TableLayout4VanChartHelper.DESCRIPTION_AREA_WIDTH;
        double[] row = {p, p, p};
        double[] col = {d, f, p};

        Component[][] components = new Component[][]{
                new Component[]{null, null, null},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Heat_Map_Radius")), radius, null},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Heat_Map_Blur")), blur, new UILabel("%")}
        };

        JPanel panel = TableLayout4VanChartHelper.createGapTableLayoutPane(components, row, col);

        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Style_Name"), panel);

    }

    private JPanel createOpacityPane() {
        maxOpacity = new UINumberDragPane(0,100);
        minOpacity = new UINumberDragPane(0,100);

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double e = TableLayout4VanChartHelper.EDIT_AREA_WIDTH;

        double[] row = {p, p, p};
        double[] col = {f, e};

        Component[][] components = new Component[][]{
                new Component[]{null, null},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Min")), minOpacity},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Max")), maxOpacity}
        };

        JPanel panel = TableLayout4VanChartHelper.createGapTableLayoutPane(components, row, col);

        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Alpha"), panel);
    }


    public void populateBean(Plot plot) {
        super.populateBean(plot);

        if(plot instanceof VanChartHeatMapPlot){
            VanChartHeatMapPlot heatMapPlot = (VanChartHeatMapPlot)plot;
            radius.setValue(heatMapPlot.getRadius());
            blur.setValue(heatMapPlot.getBlur());
            maxOpacity.populateBean(heatMapPlot.getMaxOpacity());
            minOpacity.populateBean(heatMapPlot.getMinOpacity());
        }
    }

    @Override
    protected void populateCondition(ConditionAttr defaultAttr) {
        populateArea(defaultAttr);
    }

    public void updateBean(Plot plot) {
        super.updateBean(plot);

        if(plot instanceof VanChartHeatMapPlot){
            VanChartHeatMapPlot heatMapPlot = (VanChartHeatMapPlot)plot;
            heatMapPlot.setRadius(radius.getValue());
            heatMapPlot.setBlur(blur.getValue());
            heatMapPlot.setMaxOpacity(maxOpacity.updateBean());
            heatMapPlot.setMinOpacity(minOpacity.updateBean());
        }
    }

    @Override
    protected void updateCondition(ConditionAttr defaultAttr) {
        updateArea(defaultAttr);
    }

}
