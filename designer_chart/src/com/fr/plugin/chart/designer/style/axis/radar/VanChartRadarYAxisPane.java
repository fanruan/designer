package com.fr.plugin.chart.designer.style.axis.radar;

import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.icombobox.LineComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.general.Inter;
import com.fr.plugin.chart.attr.axis.VanChartAxis;
import com.fr.plugin.chart.attr.axis.VanChartValueAxis;
import com.fr.plugin.chart.designer.TableLayout4VanChartHelper;
import com.fr.plugin.chart.designer.style.axis.VanChartValueAxisPane;
import com.fr.plugin.chart.designer.style.axis.component.MinMaxValuePaneWithOutSecTick;
import com.fr.stable.CoreConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 雷达图的y轴，即值轴。
 */
public class VanChartRadarYAxisPane extends VanChartValueAxisPane {
    private static final long serialVersionUID = -3244137561391931009L;

    private UIButtonGroup valueStyle;
    private RadarTableDataPane tableDataPane;
    private JPanel centerPane;

    protected JPanel createContentPane(boolean isXAxis) {

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double e = TableLayout4VanChartHelper.EDIT_AREA_WIDTH;
        double s = TableLayout4VanChartHelper.SECOND_EDIT_AREA_WIDTH;
        double[] columnSize = {f, e};
        double[] column = {f, s};
        double[] rowSize = {p, p, p, p, p, p, p, p};
        Component[][] components = new Component[][]{
                new Component[]{createLabelPane(new double[]{p, p}, column), null},
                new Component[]{createMinMaxValuePane(new double[]{p, p, p}, columnSize), null},
                new Component[]{createLineStylePane(new double[]{p, p, p, p}, columnSize), null},
                new Component[]{createValueStylePane(), null},
        };

        return TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
    }

    protected LineComboBox createLineComboBox() {
        return new LineComboBox(CoreConstants.STRIKE_LINE_STYLE_ARRAY_4_CHART);
    }

    protected JPanel createMinMaxValuePane(double[] row, double[] col) {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double e = TableLayout4VanChartHelper.EDIT_AREA_WIDTH;
        double[] columnSize = {f, e};

        valueStyle = new UIButtonGroup<Integer>(new String[]{Inter.getLocText("Plugin-ChartF_UnifiedComputing"),
                Inter.getLocText("Plugin-ChartF_RespectivelySpecified")});

        JPanel commenPane = createCommenValuePane(new double[]{p, p, p}, columnSize);
        tableDataPane = new RadarTableDataPane();

        centerPane = new JPanel(new CardLayout());
        centerPane.add(commenPane, Inter.getLocText("Plugin-ChartF_UnifiedComputing"));
        centerPane.add(tableDataPane, Inter.getLocText("Plugin-ChartF_RespectivelySpecified"));

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.add(valueStyle, BorderLayout.NORTH);
        contentPane.add(centerPane, BorderLayout.CENTER);
        valueStyle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkRadarCardPane();
            }
        });

        JPanel panel = TableLayout4VanChartHelper.createExpandablePaneWithTitle(Inter.getLocText("Plugin-ChartF_ValueDefinition"), contentPane);
        contentPane.setBorder(BorderFactory.createEmptyBorder(10,5,0,0));
        return panel;
    }

    protected Component[][] getLineStylePaneComponents() {
        return new Component[][]{
                new Component[]{null,null} ,
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_type")), axisLineStyle},
                new Component[]{new UILabel(Inter.getLocText("FR-Chart-Color_Color")), axisLineColor},
        };
    }

    protected void initMinMaxValuePane() {
        minMaxValuePane = new MinMaxValuePaneWithOutSecTick();
    }

    private void checkRadarCardPane() {
        if (centerPane != null && valueStyle != null) {
            CardLayout cardLayout = (CardLayout) centerPane.getLayout();
            if (valueStyle.getSelectedIndex() == 0) {
                cardLayout.show(centerPane, Inter.getLocText("Plugin-ChartF_UnifiedComputing"));
            } else {
                cardLayout.show(centerPane, Inter.getLocText("Plugin-ChartF_RespectivelySpecified"));
            }
        }
    }

    public void populateBean(VanChartAxis axis) {
        super.populateBean(axis);
        if (axis instanceof VanChartValueAxis) {
            VanChartValueAxis vanChartValueAxis = (VanChartValueAxis) axis;
            valueStyle.setSelectedIndex(vanChartValueAxis.isValueStyle() ? 1 : 0);
            tableDataPane.populateBean(vanChartValueAxis.getRadarYAxisTableDefinition());
        }
        checkRadarCardPane();
    }

    @Override
    public void updateBean(VanChartAxis axis) {
        super.updateBean(axis);
        if (axis instanceof VanChartValueAxis) {
            VanChartValueAxis vanChartValueAxis = (VanChartValueAxis) axis;
            vanChartValueAxis.setValueStyle(valueStyle.getSelectedIndex() == 1);
            tableDataPane.updateBean(vanChartValueAxis.getRadarYAxisTableDefinition());
        }
    }
}