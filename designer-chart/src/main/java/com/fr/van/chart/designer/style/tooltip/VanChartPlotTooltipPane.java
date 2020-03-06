package com.fr.van.chart.designer.style.tooltip;

import com.fr.chart.chartattr.Plot;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.style.ChartTextAttrPane;

import com.fr.design.utils.gui.UIComponentUtils;
import com.fr.design.widget.FRWidgetFactory;
import com.fr.plugin.chart.base.AttrTooltip;
import com.fr.van.chart.designer.PlotFactory;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;
import com.fr.van.chart.designer.component.VanChartTooltipContentPane;
import com.fr.van.chart.designer.component.background.VanChartBackgroundWithOutImagePane;
import com.fr.van.chart.designer.component.border.VanChartBorderWithRadiusPane;
import com.fr.van.chart.designer.style.VanChartStylePane;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VanChartPlotTooltipPane extends BasicPane {
    private static final long serialVersionUID = 6087381131907589370L;

    protected UICheckBox isTooltipShow;

    protected VanChartTooltipContentPane tooltipContentPane;

    protected UIButtonGroup<Integer> style;
    protected ChartTextAttrPane textFontPane;

    protected VanChartBorderWithRadiusPane borderPane;

    protected VanChartBackgroundWithOutImagePane backgroundPane;

    protected UICheckBox showAllSeries;
    protected UIButtonGroup followMouse;

    protected VanChartStylePane parent;

    protected JPanel tooltipPane;

    public VanChartPlotTooltipPane(Plot plot, VanChartStylePane parent) {
        this.parent = parent;
        addComponents(plot);
    }

    protected  void addComponents(Plot plot) {
        isTooltipShow = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Use_Tooltip"));
        tooltipPane = createTooltipPane(plot);

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {f};
        double[] rowSize = {p,p};
        Component[][] components = new Component[][]{
                new Component[]{isTooltipShow},
                new Component[]{tooltipPane}
        };

        JPanel panel = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
        this.setLayout(new BorderLayout());
        this.add(panel,BorderLayout.CENTER);

        isTooltipShow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                checkBoxUse();
            }
        });
    }

    protected JPanel createTooltipPane(Plot plot) {
        borderPane = new VanChartBorderWithRadiusPane();
        backgroundPane = new VanChartBackgroundWithOutImagePane();

        initTooltipContentPane(plot);

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double e = TableLayout4VanChartHelper.EDIT_AREA_WIDTH;
        double[] columnSize = {f, e};
        double[] rowSize = {p,p,p,p,p,p,p,p,p};

        Component[][] components = createComponents(plot);

        return TableLayoutHelper.createTableLayoutPane(components,rowSize,columnSize);
    }

    protected Component[][] createComponents(Plot plot) {
        Component[][] components = new Component[][]{
                new Component[]{tooltipContentPane,null},
                new Component[]{createLabelStylePane(),null},
                new Component[]{TableLayout4VanChartHelper.createExpandablePaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Border"),borderPane),null},
                new Component[]{TableLayout4VanChartHelper.createExpandablePaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Background"), backgroundPane),null},
                new Component[]{createDisplayStrategy(plot),null},
        };
        return components;
    }


    protected void initTooltipContentPane(Plot plot){
        tooltipContentPane = PlotFactory.createPlotTooltipContentPane(plot, parent, VanChartPlotTooltipPane.this);
    }

    protected JPanel createLabelStylePane() {
        style = new UIButtonGroup<Integer>(new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Automatic"),com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Custom")});
        textFontPane = new ChartTextAttrPane() {
            protected Component[][] getComponents(JPanel buttonPane) {
                return new Component[][]{
                        new Component[]{null, null},
                        new Component[]{null, getFontNameComboBox()},
                        new Component[]{null, buttonPane}
                };
            }
        };

        JPanel panel = TableLayout4VanChartHelper.createGapTableLayoutPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Character"), style);
        JPanel panel1 = new JPanel(new BorderLayout());
        panel1.add(panel, BorderLayout.CENTER);
        panel1.add(textFontPane, BorderLayout.SOUTH);

        initStyleListener();

        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Widget_Style"), panel1);
    }


    private void initStyleListener() {
        style.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkStyleUse();
            }
        });
    }

    protected JPanel createDisplayStrategy(Plot plot) {
        showAllSeries = new UICheckBox(getShowAllSeriesLabelText());
        followMouse = new UIButtonGroup(new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Follow_Mouse"),
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Not_Follow_Mouse")});
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double e = TableLayout4VanChartHelper.EDIT_AREA_WIDTH;
        double[] columnSize = {f, e};
        double[] rowSize = { p,p,p};
        Component[][] components = new Component[3][2];
        components[0] = new Component[]{null,null};
        components[1] = new Component[]{FRWidgetFactory.createLineWrapLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Prompt_Box")), UIComponentUtils.wrapWithBorderLayoutPane(followMouse)};

        if(plot.isSupportTooltipSeriesType() && hasTooltipSeriesType()){
            components[2] = new Component[]{showAllSeries,null};
        }
        JPanel panel = TableLayout4VanChartHelper.createGapTableLayoutPane(components,rowSize,columnSize);
        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Display_Strategy"), panel);
    }

    protected String getShowAllSeriesLabelText() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Show_All_Series");
    };

    protected boolean hasTooltipSeriesType() {
        return true;
    }

    @Override
    protected String title4PopupWindow() {
        return null;
    }

    private void checkAllUse() {
        checkBoxUse();
        checkStyleUse();
    }
    /**
     * 检查box使用.
     */
    private void checkBoxUse() {
        tooltipPane.setVisible(isTooltipShow.isSelected());
    }

    private void checkStyleUse() {
        textFontPane.setVisible(style.getSelectedIndex() == 1);
    }

    protected AttrTooltip getAttrTooltip() {
        return new AttrTooltip();
    }

    public void populate(AttrTooltip attr) {
        if(attr == null) {
            attr = getAttrTooltip();
        }

        isTooltipShow.setSelected(attr.isEnable());
        if (tooltipContentPane != null) {
            tooltipContentPane.populateBean(attr.getContent());
        }

        style.setSelectedIndex(attr.isCustom() ? 1 : 0);
        textFontPane.populate(attr.getTextAttr());
        borderPane.populate(attr.getGeneralInfo());
        backgroundPane.populate(attr.getGeneralInfo());
        if(showAllSeries != null) {
            showAllSeries.setSelected(attr.isShowMutiSeries());
        }
        if(followMouse != null) {
            followMouse.setSelectedIndex(attr.isFollowMouse() ? 0 : 1);
        }

        checkAllUse();
    }

    public AttrTooltip update() {
        AttrTooltip attrTooltip = getAttrTooltip();

        attrTooltip.setEnable(isTooltipShow.isSelected());
        if (tooltipContentPane != null) {
            attrTooltip.setContent(tooltipContentPane.updateBean());
        }

        attrTooltip.setCustom(style.getSelectedIndex() == 1);
        if(textFontPane != null){
            attrTooltip.setTextAttr(textFontPane.update());
        }
        borderPane.update(attrTooltip.getGeneralInfo());
        backgroundPane.update(attrTooltip.getGeneralInfo());
        if(showAllSeries != null) {
            attrTooltip.setShowMutiSeries(showAllSeries.isSelected());
        }
        if(followMouse != null) {
            attrTooltip.setFollowMouse(followMouse.getSelectedIndex() == 0);
        }

        return attrTooltip;
    }
}
