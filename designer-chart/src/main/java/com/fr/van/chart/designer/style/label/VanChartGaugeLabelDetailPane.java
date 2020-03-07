package com.fr.van.chart.designer.style.label;

import com.fr.chart.base.ChartConstants;
import com.fr.chart.chartattr.Plot;
import com.fr.chartx.TwoTuple;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.i18n.Toolkit;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.style.ChartTextAttrPane;
import com.fr.design.mainframe.chart.gui.style.ChartTextAttrPaneWithAuto;
import com.fr.general.ComparatorUtils;
import com.fr.plugin.chart.base.AttrLabelDetail;
import com.fr.plugin.chart.gauge.VanChartGaugePlot;
import com.fr.plugin.chart.type.GaugeStyle;
import com.fr.stable.Constants;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;
import com.fr.van.chart.designer.style.VanChartStylePane;

import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

/**
 * Created by mengao on 2017/8/13.
 */
public class VanChartGaugeLabelDetailPane extends VanChartPlotLabelDetailPane {

    private GaugeStyle gaugeStyle;
    private UIButtonGroup<Integer> align;
    private JPanel alignPane;
    private Integer[] oldAlignValues;

    public VanChartGaugeLabelDetailPane(Plot plot, VanChartStylePane parent) {
        super(plot, parent);
    }

    protected void initLabelDetailPane(Plot plot) {
        setGaugeStyle(((VanChartGaugePlot) plot).getGaugeStyle());
        super.initLabelDetailPane(plot);
    }

    public GaugeStyle getGaugeStyle() {
        return gaugeStyle;
    }

    public void setGaugeStyle(GaugeStyle gaugeStyle) {
        this.gaugeStyle = gaugeStyle;
    }

    protected JPanel createLabelStylePane(double[] row, double[] col, Plot plot) {
        style = new UIButtonGroup<Integer>(new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Automatic"),
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Custom")});
        textFontPane = initTextFontPane();

        initStyleListener();

        return TableLayoutHelper.createTableLayoutPane(getLabelStyleComponents(plot), row, col);
    }

    protected boolean isFontSizeAuto() {
        return false;
    }

    protected boolean isFontColorAuto() {
        return false;
    }

    protected ChartTextAttrPane initTextFontPane() {

        return new ChartTextAttrPaneWithAuto(isFontSizeAuto(), isFontColorAuto()) {
            protected double[] getRowSize() {
                double p = TableLayout.PREFERRED;
                return new double[]{p, p};
            }

            protected Component[][] getComponents(JPanel buttonPane) {
                UILabel text = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Character"), SwingConstants.LEFT);
                return new Component[][]{
                        new Component[]{text, getFontNameComboBox()},
                        new Component[]{null, buttonPane}
                };
            }
        };
    }

    protected JPanel getLabelPositionPane(Component[][] comps, double[] row, double[] col) {
        return TableLayoutHelper.createTableLayoutPane(comps, row, col);
    }

    protected JPanel createTableLayoutPaneWithTitle(String title, JPanel panel) {
        return TableLayout4VanChartHelper.createGapTableLayoutPane(title, panel);
    }

    protected Component[][] getLabelPaneComponents(Plot plot, double p, double[] columnSize) {
        if (hasLabelAlign(plot)) {

            return new Component[][]{
                    new Component[]{dataLabelContentPane, null},
                    new Component[]{createLabelPositionPane(Toolkit.i18nText("Fine-Design_Chart_Layout_Vertical"), plot), null},
                    new Component[]{createLabelAlignPane(Toolkit.i18nText("Fine-Design_Chart_Layout_Horizontal")), null},
                    new Component[]{createLabelStylePane(getLabelStyleRowSize(p), columnSize, plot), null},
            };
        } else {

            return super.getLabelPaneComponents(plot, p, columnSize);
        }
    }

    private JPanel createLabelAlignPane(String title) {
        JPanel panel = new JPanel(new BorderLayout());

        alignPane = new JPanel();
        checkAlignPane(title);
        panel.add(alignPane, BorderLayout.CENTER);

        return panel;
    }

    protected void checkAlignPane(String title) {
        if (alignPane == null && !hasLabelAlign(getPlot())) {
            return;
        }
        if (alignPane != null && !hasLabelAlign(getPlot())) {
            oldAlignValues = null;
            alignPane.removeAll();
            return;
        }
        if (alignPane == null && hasLabelAlign(getPlot())) {
            alignPane = new JPanel();
        }
        TwoTuple<String[], Integer[]> result = getAlignNamesAndValues();

        String[] names = result.getFirst();
        Integer[] values = result.getSecond();
        if (ComparatorUtils.equals(values, oldAlignValues)) {
            return;
        }

        oldAlignValues = values;

        align = new UIButtonGroup<Integer>(names, values);

        Component[][] comps = new Component[2][2];

        comps[0] = new Component[]{null, null};
        comps[1] = new Component[]{new UILabel(title, SwingConstants.LEFT), align};

        double[] row = new double[]{TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED};
        double[] col = new double[]{TableLayout.FILL, TableLayout4VanChartHelper.EDIT_AREA_WIDTH};

        alignPane.removeAll();
        alignPane.setLayout(new BorderLayout());
        alignPane.add(getLabelPositionPane(comps, row, col), BorderLayout.CENTER);

        if (parent != null) {
            parent.initListener(alignPane);
        }
    }

    private TwoTuple<String[], Integer[]> getAlignNamesAndValues() {
        String[] names = new String[]{Toolkit.i18nText("Fine-Design_Chart_Follow"), Toolkit.i18nText("Fine-Design_Chart_Align_Left"), Toolkit.i18nText("Fine-Design_Chart_Align_Right")};
        Integer[] values = new Integer[]{ChartConstants.AUTO_LABEL_POSITION, Constants.LEFT, Constants.RIGHT};

        return new TwoTuple<>(names, values);
    }

    protected Component[][] getLabelStyleComponents(Plot plot) {
        return new Component[][]{
                new Component[]{textFontPane, null},
        };
    }

    protected void checkPane() {
        String verticalTitle = hasLabelAlign(getPlot())
                ? Toolkit.i18nText("Fine-Design_Chart_Layout_Vertical")
                : Toolkit.i18nText("Fine-Design_Chart_Layout_Position");

        checkPositionPane(verticalTitle);
        checkAlignPane(Toolkit.i18nText("Fine-Design_Chart_Layout_Horizontal"));
    }

    protected void checkStyleUse() {
        textFontPane.setVisible(true);
        textFontPane.setPreferredSize(new Dimension(0, 60));
    }

    protected boolean hasLabelAlign(Plot plot) {
        return getGaugeStyle() == GaugeStyle.THERMOMETER && !((VanChartGaugePlot) plot).getGaugeDetailStyle().isHorizontalLayout();
    }

    public void populate(AttrLabelDetail detail) {
        super.populate(detail);

        style.setSelectedIndex(1);
        if (hasLabelAlign(this.getPlot()) && align != null) {
            align.setSelectedItem(detail.getAlign());
        }
    }

    public void update(AttrLabelDetail detail) {
        super.update(detail);

        detail.setCustom(true);
        if (align != null) {
            if (align.getSelectedItem() != null) {
                detail.setAlign(align.getSelectedItem());
            } else {
                align.setSelectedItem(detail.getAlign());
            }
        }
    }
}
