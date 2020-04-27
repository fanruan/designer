package com.fr.van.chart.designer.style.background;

import com.fr.chart.chartattr.Plot;
import com.fr.chartx.config.info.constant.ConfigType;
import com.fr.design.gui.frpane.AbstractAttrNoScrollPane;
import com.fr.design.mainframe.chart.PaneTitleConstants;
import com.fr.design.mainframe.chart.gui.style.ThirdTabPane;
import com.fr.design.mainframe.chart.gui.style.legend.AutoSelectedPane;
import com.fr.design.mainframe.chart.info.ChartInfoCollector;
import com.fr.general.ComparatorUtils;
import com.fr.plugin.chart.vanchart.VanChart;
import com.fr.van.chart.designer.style.VanChartStylePane;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.List;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

/**
 * 属性表, 图表样式-背景界面.
 */
public class VanChartAreaPane extends ThirdTabPane<VanChart> implements AutoSelectedPane {
    private static final long serialVersionUID = 3961996287868450144L;

    private static final int PRE_WIDTH = 220;

    private VanChartAreaBackgroundPane areaPane;
    protected VanChartAreaBackgroundPane plotPane;

    public VanChartAreaPane(Plot plot, VanChartStylePane parent) {
        super(plot, parent);
    }


    protected void initLayout() {
        this.setLayout(new BorderLayout());
        if (!paneList.isEmpty()) {
            JPanel pane = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 0));
            if (nameArray.length > 1) {
                pane.add(tabPane);
                tabPane.setBorder(BorderFactory.createEmptyBorder(0,5,0,0));
                this.add(pane, BorderLayout.NORTH);
            }
        }
        centerPane.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));
        this.add(centerPane, BorderLayout.CENTER);
    }

    /**
     * 界面 使用标题
     * @return     标题
     */
    public String title4PopupWindow() {
        return PaneTitleConstants.CHART_STYLE_AREA_TITLE;
    }

    @Override
    protected List<NamePane> initPaneList(Plot plot, AbstractAttrNoScrollPane parent) {
        List<NamePane> paneList = new ArrayList<NamePane>();

        areaPane = new VanChartAreaBackgroundPane(false, parent);

        initPlotPane(true, parent);

        paneList.add(new NamePane(areaPane.title4PopupWindow(), areaPane));

        if(plot.isSupportPlotBackground()) {
            paneList.add(new NamePane(plotPane.title4PopupWindow(), plotPane));
        }
        return paneList;
    }

    protected void initPlotPane(boolean b, AbstractAttrNoScrollPane parent) {
        plotPane = new VanChartAreaBackgroundPane(true, parent);
    }

    @Override
    protected int getContentPaneWidth() {
        return PRE_WIDTH;
    }

    /**
     * 更新界面
     */
    public void populateBean(VanChart chart) {
        areaPane.populateBean(chart);
        plotPane.populateBean(chart);
    }

    /**
     * 保存界面属性.
     */
    @Override
    public void updateBean(VanChart chart) {
        areaPane.updateBean(chart);
        plotPane.updateBean(chart);
        //背景埋点
        ChartInfoCollector.getInstance().updateChartConfig(chart, ConfigType.BACKGROUND, chart.getPlot().getBuryingPointBackGroundConfig());
    }

    /**
     * 设置选中的界面id
     */
    public void setSelectedIndex(String id) {
        for (int i = 0; i < paneList.size(); i++) {
            if (ComparatorUtils.equals(id, nameArray[i])) {
                tabPane.setSelectedIndex(i);
                break;
            }
        }
    }
}