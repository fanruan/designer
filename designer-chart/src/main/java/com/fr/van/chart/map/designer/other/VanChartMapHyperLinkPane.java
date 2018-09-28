package com.fr.van.chart.map.designer.other;

import com.fr.base.BaseFormula;
import com.fr.base.FormulaBuilder;
import com.fr.chart.chartattr.Plot;
import com.fr.design.dialog.BasicScrollPane;
import com.fr.extended.chart.HyperLinkPara;
import com.fr.extended.chart.HyperLinkParaHelper;
import com.fr.js.NameJavaScriptGroup;
import com.fr.plugin.chart.map.VanChartMapPlot;
import com.fr.plugin.chart.type.MapType;
import com.fr.van.chart.custom.component.VanChartHyperLinkPane;
import com.fr.van.chart.map.designer.VanMapAreaPointAndLineGroupPane;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by hufan on 2016/12/20.
 */
public class VanChartMapHyperLinkPane extends BasicScrollPane<Plot> {
    private Plot plot;
    private VanChartHyperLinkPane areaHyperLinkPane;
    private VanChartHyperLinkPane pointHyperLinkPane;
    private VanChartHyperLinkPane lineHyperLinkPane;

    @Override
    public void populateBean(Plot plot) {
        this.plot = plot;
        if (plot instanceof VanChartMapPlot){
            MapType type = ((VanChartMapPlot) plot).getMapType();
            switch (type){
                case AREA: {
                    populateAreaHyperLinkPane(plot);
                    break;
                }
                case POINT: {
                    populatePointHyperLinkPane(plot);
                    break;
                }
                case LINE: {
                    populateLineHyperLinkPane(plot);
                    break;
                }
                case CUSTOM:{
                    populateCustomHyperLingPane(plot);
                    break;
                }
            }
        }

    }

    private void populateCustomHyperLingPane(Plot plot) {
        if (areaHyperLinkPane == null || pointHyperLinkPane == null || lineHyperLinkPane == null){
            this.remove(leftcontentPane);
            layoutContentPane();
        }

        areaHyperLinkPane.populate(plot);
        lineHyperLinkPane.populate(plot);
        pointHyperLinkPane.populate(plot);
    }

    private void populatePointHyperLinkPane(Plot plot) {
        if (pointHyperLinkPane == null){
            this.remove(leftcontentPane);
            layoutContentPane();
        }
        pointHyperLinkPane.populate(plot);
    }

    private void populateLineHyperLinkPane(Plot plot) {
        if (lineHyperLinkPane == null){
            this.remove(leftcontentPane);
            layoutContentPane();
        }
        lineHyperLinkPane.populate(plot);
    }

    private void populateAreaHyperLinkPane(Plot plot) {
        if (areaHyperLinkPane == null){
            this.remove(leftcontentPane);
            layoutContentPane();
        }
        areaHyperLinkPane.populate(plot);
    }

    public void updateBean(Plot plot) {
        if (plot instanceof VanChartMapPlot){
            MapType type = ((VanChartMapPlot) plot).getMapType();
            switch (type){
                case AREA: {
                    areaHyperLinkPane.update(plot);
                    break;
                }
                case POINT: {
                    pointHyperLinkPane.update(plot);
                    break;
                }
                case LINE: {
                    lineHyperLinkPane.update(plot);
                    break;
                }
                case CUSTOM:{
                    updateCustomHyperLinkPane(plot);
                    break;
                }
                default:areaHyperLinkPane.update(plot);
            }
        }
    }

    private void updateCustomHyperLinkPane(Plot plot) {
        areaHyperLinkPane.update(plot);
        pointHyperLinkPane.update(plot);
        lineHyperLinkPane.update(plot);

    }

    @Override
    protected JPanel createContentPane() {
        JPanel contentPane = new JPanel(new BorderLayout());
        if(plot == null) {
            return contentPane;
        }

        JPanel pane = getContentPane(plot);

        if(pane != null) {
            contentPane.add(pane, BorderLayout.CENTER);
        }
        return contentPane;
    }

    protected void layoutContentPane() {
        leftcontentPane = createContentPane();
        leftcontentPane.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
        this.setLayout(new BorderLayout());
        this.add(leftcontentPane, BorderLayout.CENTER);
    }

    private JPanel getContentPane(Plot plot) {
        if (plot instanceof VanChartMapPlot){
            createHyperLinkPane();
            MapType type = ((VanChartMapPlot) plot).getMapType();
            switch (type){
                case AREA: {
                    return areaHyperLinkPane;
                }
                case POINT: {
                    return pointHyperLinkPane;
                }
                case LINE: {
                    return lineHyperLinkPane;
                }
                case CUSTOM: {
                    return new VanMapAreaPointAndLineGroupPane(areaHyperLinkPane, pointHyperLinkPane, lineHyperLinkPane);
                }
                default:{
                    return areaHyperLinkPane;
                }
            }
        }
        return null;
    }

    private void createHyperLinkPane() {
        areaHyperLinkPane = new VanChartHyperLinkPane(){
            @Override
            protected void updateHotHyperLink(Plot plot, NameJavaScriptGroup nameGroup) {
                ((VanChartMapPlot)plot).setAreaHotHyperLink(nameGroup);
            }
            @Override
            protected NameJavaScriptGroup populateHotHyperLink(Plot plot) {
                return ((VanChartMapPlot)plot).getAreaHotHyperLink();
            }

        };
        pointHyperLinkPane = new VanChartHyperLinkPane(){
            @Override
            protected void updateHotHyperLink(Plot plot, NameJavaScriptGroup nameGroup) {
                ((VanChartMapPlot)plot).setPointHotHyperLink(nameGroup);
            }
            @Override
            protected NameJavaScriptGroup populateHotHyperLink(Plot plot) {
                return ((VanChartMapPlot)plot).getPointHotHyperLink();
            }
        };
        lineHyperLinkPane = new VanChartHyperLinkPane(){
            @Override
            protected void updateHotHyperLink(Plot plot, NameJavaScriptGroup nameGroup) {
                ((VanChartMapPlot)plot).setLineHotHyperLink(nameGroup);
            }
            @Override
            protected NameJavaScriptGroup populateHotHyperLink(Plot plot) {
                return ((VanChartMapPlot)plot).getLineHotHyperLink();
            }

            @Override
            protected Map<String, BaseFormula> getHyperLinkEditorMap() {
                FormulaBuilder builder = BaseFormula.createFormulaBuilder();
                Map<String, BaseFormula> map = new LinkedHashMap<>();
                for (HyperLinkPara para : HyperLinkParaHelper.LINE_MAP) {
                    map.put(para.getName(), builder.build(para.getFormulaContent()));
                }
                return map;
            }
        };
    }

    @Override
    protected String title4PopupWindow() {
        return null;
    }
}
