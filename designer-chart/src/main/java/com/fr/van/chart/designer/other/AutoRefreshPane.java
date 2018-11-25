package com.fr.van.chart.designer.other;

import com.fr.base.BaseUtils;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.dialog.DialogActionListener;
import com.fr.design.dialog.UIDialog;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.layout.TableLayout;

import com.fr.plugin.chart.attr.plot.VanChartPlot;
import com.fr.plugin.chart.base.RefreshMoreLabel;
import com.fr.plugin.chart.vanchart.VanChart;
import com.fr.van.chart.designer.PlotFactory;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;
import com.fr.van.chart.designer.style.tooltip.VanChartPlotTooltipPane;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by hufan on 2016/12/30.
 */
public class AutoRefreshPane extends BasicBeanPane<RefreshMoreLabel> {


    private VanChart chart;
    private UIButtonGroup moreLabel;
    private UISpinner autoRefreshTime;
    private UICheckBox autoTooltip;
    private UIButton tooltipSet;
    private JPanel contentPane;

    private boolean isLargeModel;

    public UISpinner getAutoRefreshTime() {
        return autoRefreshTime;
    }

    public AutoRefreshPane(VanChart chart, boolean isLargeModel) {
        this.chart = chart;
        this.isLargeModel = isLargeModel;
        this.setLayout(new BorderLayout());
        this.add(createContentPane());
    }


    protected JPanel createContentPane() {
        JPanel content = new JPanel(new BorderLayout(0, 6));
        moreLabel = new UIButtonGroup(new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Open"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Close")});
        moreLabel.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                checkRefreshEnable();
            }
        });

        autoRefreshTime = new UISpinner(0, Integer.MAX_VALUE, 1, 0);

        autoTooltip = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Auto_Tooltip"));
        autoTooltip.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                checkTooltipEnable();
            }
        });

        initTooltipSet();

        JPanel jPanel = new JPanel();
        jPanel.add(autoTooltip, BorderLayout.CENTER);
        jPanel.add(tooltipSet, BorderLayout.EAST);

        JPanel moreLabelPane = TableLayout4VanChartHelper.createGapTableLayoutPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_More_Label"), moreLabel);

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {p, f, 20};
        double[] column = {f, 20};
        double[] rowSize = {p};

        Component[][] components = initComponent(jPanel);
        JPanel panel1 = TableLayout4VanChartHelper.createGapTableLayoutPane(components, rowSize, columnSize);
        contentPane = new JPanel(new BorderLayout());
        contentPane.add(panel1, BorderLayout.CENTER);

        Component[][] AutoTooltipComponent = initAutoTooltipComponent();
        JPanel panel2 = TableLayout4VanChartHelper.createGapTableLayoutPane(AutoTooltipComponent, rowSize, column);
        panel2.setBorder(BorderFactory.createEmptyBorder(10, 0,0,0));
        contentPane.add(panel2, BorderLayout.SOUTH);
        contentPane.setBorder(BorderFactory.createEmptyBorder(0, 72,0,0));
        content.add(moreLabelPane, BorderLayout.NORTH);
        content.add(contentPane, BorderLayout.CENTER);
        return content;
    }

    protected Component[][] initComponent(JPanel autoTooltipPane){

        return new Component[][]{
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Time_Interval")), autoRefreshTime, new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Time_Seconds"))},
                new Component[]{autoTooltip,null, tooltipSet},
        };

    }

    protected Component[][] initAutoTooltipComponent () {

        return new Component[][]{
                new Component[]{autoTooltip, tooltipSet},
        };
    }

    private void initTooltipSet() {
        tooltipSet = new UIButton(BaseUtils.readIcon("/com/fr/design/images/buttonicon/config.png"));
        tooltipSet.setPreferredSize(new Dimension(20, 20));
        tooltipSet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                final VanChartPlotTooltipPane pane = PlotFactory.createPlotRefreshTooltipPane(chart.getPlot());
                pane.populate(chart.getRefreshMoreLabel().getAttrTooltip());
                UIDialog dialog = pane.showUnsizedWindow(SwingUtilities.getWindowAncestor(contentPane), new DialogActionListener() {
                    @Override
                    public void doOk() {

                        chart.getRefreshMoreLabel().setAttrTooltip(pane.update());
                    }

                    @Override
                    public void doCancel() {

                    }
                });
                dialog.pack();
                dialog.setModal(true);
                dialog.setVisible(true);
            }
        });
    }

    public void checkRefreshEnable() {
        Boolean enable = moreLabel.getSelectedIndex() == 0;
        contentPane.setVisible(enable);
        autoTooltip.setEnabled(enable && !isLargeModel);
        checkTooltipEnable();
    }

    public void checkTooltipEnable() {
        Boolean enable = moreLabel.getSelectedIndex() == 0 && autoTooltip.isSelected();
        tooltipSet.setEnabled(enable && !isLargeModel);
    }


    @Override
    public void populateBean(RefreshMoreLabel refreshMoreLabel) {
        moreLabel.setSelectedIndex(refreshMoreLabel.isMoreLabel() ? 0 : 1);
        populateAutoRefreshTime();
        autoTooltip.setSelected(refreshMoreLabel.isAutoTooltip());

        checkRefreshEnable();

    }

    @Override
    public RefreshMoreLabel updateBean() {
        return null;
    }

    protected void populateAutoRefreshTime() {
        VanChartPlot plot = (VanChartPlot)chart.getPlot();
        if(plot.isSupportAutoRefresh()) {
            autoRefreshTime.setValue(plot.getAutoRefreshPerSecond());
        }
    }

    @Override
    public void updateBean(RefreshMoreLabel refreshMoreLabel) {
        refreshMoreLabel.setMoreLabel(moreLabel.getSelectedIndex() == 0);
        updateAutoRefreshTime();
        updateAutoTooltip(refreshMoreLabel);
    }

    protected void updateAutoTooltip(RefreshMoreLabel refreshMoreLabel) {
        refreshMoreLabel.setAutoTooltip(autoTooltip.isSelected());
    }


    private void updateAutoRefreshTime() {
        VanChartPlot plot = (VanChartPlot)chart.getPlot();
        if(plot.isSupportAutoRefresh()) {
            plot.setAutoRefreshPerSecond((int) autoRefreshTime.getValue());
        }
    }
    @Override
    protected String title4PopupWindow() {
        return null;
    }
}
