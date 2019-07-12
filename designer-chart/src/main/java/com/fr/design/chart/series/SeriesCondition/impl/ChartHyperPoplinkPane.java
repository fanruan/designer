package com.fr.design.chart.series.SeriesCondition.impl;

import com.fr.chart.chartattr.Bar2DPlot;
import com.fr.chart.chartattr.Chart;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.charttypes.ChartTypeManager;
import com.fr.chart.web.ChartHyperPoplink;
import com.fr.chartx.attr.ChartProvider;
import com.fr.design.chart.gui.ChartComponent;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.hyperlink.AbstractHyperLinkPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.chart.ChartHyperEditPane;
import com.fr.design.module.DesignModuleFactory;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.log.FineLoggerFactory;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.HashMap;

/**
 * 类说明: 图表超链 -- 弹出 悬浮窗.
 *
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2011-12-28 上午10:41:39
 */
public class ChartHyperPoplinkPane extends AbstractHyperLinkPane<ChartHyperPoplink> {
    private static final long serialVersionUID = 2469115951510144738L;
    private static final int EDIT_PANE_WIDTH = 248;
    private UITextField itemNameTextField;
    private ChartHyperEditPane hyperEditPane;
    private ChartComponent chartComponent;


    public ChartHyperPoplinkPane() {
        this(null, false);
    }

    public ChartHyperPoplinkPane(HashMap hyperLinkEditorMap, boolean needRenamePane) {
        super(hyperLinkEditorMap, needRenamePane);
        this.setLayout(FRGUIPaneFactory.createM_BorderLayout());

        if (this.needRenamePane()) {
            itemNameTextField = new UITextField();
            this.add(GUICoreUtils.createNamedPane(itemNameTextField, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Use_Name") + ":"), BorderLayout.NORTH);
        }

        hyperEditPane = new ChartHyperEditPane(getChartParaType(), getValueEditorPane(), getValueEditorPane());
        hyperEditPane.setPreferredSize(new Dimension(EDIT_PANE_WIDTH, (int) hyperEditPane.getPreferredSize().getHeight()));// 固定属性配置面板大小,灵活调整图表显示面板.
        this.add(hyperEditPane, BorderLayout.WEST);
        ChartCollection cc = createChartCollection();

        chartComponent = new ChartComponent();
        chartComponent.setPreferredSize(new Dimension((int) this.getPreferredSize().getWidth() - EDIT_PANE_WIDTH, 170));// 在单元格弹出时 需要调整保证属性表的大小.
        chartComponent.setSupportEdit(false);
        chartComponent.populate(cc);

        this.add(chartComponent, BorderLayout.CENTER);

        hyperEditPane.populate(cc);

        hyperEditPane.useChartComponent(chartComponent);
    }

    private ChartCollection createChartCollection() {
        ChartCollection cc = new ChartCollection();

        ChartProvider chart = ChartTypeManager.getInstanceWithCheck().getFirstChart();
        if (chart != null) {
            try {
                cc.addChart((ChartProvider) chart.clone());
            } catch (CloneNotSupportedException e) {
                FineLoggerFactory.getLogger().error(e.getMessage(), e);
            }

        } else {
            cc.addChart(new Chart(new Bar2DPlot()));
        }
        return cc;
    }

    @Override
    public String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Pop_Chart");
    }

    @Override
    public void populateBean(ChartHyperPoplink chartHyperlink) {
        if (itemNameTextField != null) {
            this.itemNameTextField.setText(chartHyperlink.getItemName());
        }

        ChartCollection cc = (ChartCollection) chartHyperlink.getChartCollection();
        if (cc == null || cc.getChartCount() < 1) {
            cc = createChartCollection();
            chartHyperlink.setChartCollection(cc);
        }

        hyperEditPane.populateHyperLink(chartHyperlink);
        chartComponent.populate(cc);
    }

    /**
     * 超链数组HyperlinkGoup切换时 updateBean.
     *
     * @return 返回的弹出超链.
     */
    public ChartHyperPoplink updateBean() {
        ChartHyperPoplink chartLink = new ChartHyperPoplink();
        updateBean(chartLink);
        if (itemNameTextField != null) {
            chartLink.setItemName(this.itemNameTextField.getText());
        }
        return chartLink;
    }

    /**
     * 属性表 对应update
     */
    public void updateBean(ChartHyperPoplink chartHyperlink) {
        hyperEditPane.updateHyperLink(chartHyperlink);
        chartHyperlink.setChartCollection(chartComponent.update());

        DesignModuleFactory.getChartPropertyPane().getChartEditPane().fire();// 响应整个图表保存事件等.
        if (itemNameTextField != null) {
            chartHyperlink.setItemName(this.itemNameTextField.getText());
        }
    }

    public static class ChartNoRename extends ChartHyperPoplinkPane {
        protected boolean needRenamePane() {
            return false;
        }
    }
}