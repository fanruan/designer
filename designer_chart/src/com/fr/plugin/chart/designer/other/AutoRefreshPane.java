package com.fr.plugin.chart.designer.other;

import com.fr.base.BaseUtils;
import com.fr.design.dialog.BasicScrollPane;
import com.fr.design.dialog.DialogActionListener;
import com.fr.design.dialog.UIDialog;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;
import com.fr.plugin.chart.attr.plot.VanChartPlot;
import com.fr.plugin.chart.base.RefreshMoreLabel;
import com.fr.plugin.chart.designer.PlotFactory;
import com.fr.plugin.chart.designer.TableLayout4VanChartHelper;
import com.fr.plugin.chart.designer.style.tooltip.VanChartPlotTooltipPane;
import com.fr.plugin.chart.vanchart.VanChart;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by hufan on 2016/12/30.
 */
public class AutoRefreshPane extends BasicScrollPane<RefreshMoreLabel> {

    private static final int P_W = 320;
    private static final int P_H = 460;

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
    }

    @Override
    protected JPanel createContentPane() {
        JPanel content = new JPanel(new BorderLayout(0, 6));
        moreLabel = new UIButtonGroup(new String[]{Inter.getLocText("Plugin-ChartF_Open"), Inter.getLocText("Plugin-ChartF_Close")});
        moreLabel.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                checkRefreshEnable();
            }
        });

        autoRefreshTime = new UISpinner(0, Integer.MAX_VALUE, 1, 0);

        autoTooltip = new UICheckBox(Inter.getLocText("Plugin-ChartF_Auto_Tooltip"));
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

        JPanel moreLabelPane = TableLayout4VanChartHelper.createGapTableLayoutPane(Inter.getLocText("Plugin-ChartF_More_Label"), moreLabel);

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {p, f};
        double[] rowSize = {p, p};

        Component[][] components = initComponent(jPanel);
        contentPane = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
        contentPane.setBorder(BorderFactory.createEmptyBorder(0,15,0,0));
        content.add(moreLabelPane, BorderLayout.NORTH);
        content.add(contentPane, BorderLayout.CENTER);
        return content;
    }

    protected Component[][] initComponent(JPanel autoTooltipPane){

        return new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_Time_Interval")), autoRefreshTime},
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
                UIDialog dialog = pane.showUnsizedWindow(SwingUtilities.getWindowAncestor(new JPanel()), new DialogActionListener() {
                    @Override
                    public void doOk() {

                        chart.getRefreshMoreLabel().setAttrTooltip(pane.update());
                    }

                    @Override
                    public void doCancel() {

                    }
                });
                dialog.setSize(P_W, P_H);
                dialog.setVisible(true);
            }
        });
    }

    protected void layoutContentPane() {
        leftcontentPane = createContentPane();
        leftcontentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 0));
        this.add(leftcontentPane);
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
