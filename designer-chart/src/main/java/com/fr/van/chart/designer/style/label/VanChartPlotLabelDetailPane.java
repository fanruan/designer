package com.fr.van.chart.designer.style.label;

import com.fr.chart.chartattr.Plot;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ibutton.UIToggleButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.chart.gui.style.ChartTextAttrPane;
import com.fr.design.style.color.ColorSelectBox;

import com.fr.plugin.chart.attr.plot.VanChartLabelPositionPlot;
import com.fr.plugin.chart.base.AttrLabelDetail;
import com.fr.plugin.chart.base.AttrTooltipContent;
import com.fr.stable.Constants;
import com.fr.van.chart.designer.PlotFactory;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;
import com.fr.van.chart.designer.style.VanChartStylePane;

import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Mitisky on 15/12/7.
 */
public class VanChartPlotLabelDetailPane extends BasicPane {
    private static final long serialVersionUID = -22438250307946275L;

    protected BasicBeanPane<AttrTooltipContent> dataLabelContentPane;

    protected UIButtonGroup<Integer> position;
    protected UIButtonGroup<Boolean> autoAdjust;
    protected UIToggleButton tractionLine;

    protected UIButtonGroup<Integer> style;
    protected ChartTextAttrPane textFontPane;

    protected ColorSelectBox backgroundColor;

    private JPanel tractionLinePane;

    protected VanChartStylePane parent;

    public VanChartPlotLabelDetailPane(Plot plot, VanChartStylePane parent) {
        this.parent = parent;
        this.setLayout(new BorderLayout());
        initToolTipContentPane(plot);
        JPanel contentPane = createLabelPane(plot);
        this.add(contentPane,BorderLayout.CENTER);
    }

    //默认从factory中取
    protected void initToolTipContentPane(Plot plot) {
        dataLabelContentPane = PlotFactory.createPlotLabelContentPane(plot, parent, VanChartPlotLabelDetailPane.this);
    }

    private JPanel createLabelPane(Plot plot) {
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double e = TableLayout4VanChartHelper.EDIT_AREA_WIDTH;
        double[] columnSize = {f, e};
        double[] rowSize = getLabelPaneRowSize(plot, p);

        Component[][] components = getLabelPaneComponents(plot, p, columnSize);
        return TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
    }

    protected Component[][] getLabelPaneComponents(Plot plot, double p, double[] columnSize) {
        if(hasLabelPosition(plot)){
            return new Component[][]{
                    new Component[]{dataLabelContentPane,null},
                    new Component[]{createLabelPositionPane(new double[]{p,p,p}, columnSize, plot),null},
                    new Component[]{createLabelStylePane(getLabelStyleRowSize(p), columnSize, plot),null},
            };
        } else {
            return  new Component[][]{
                    new Component[]{dataLabelContentPane,null},
                    new Component[]{createLabelStylePane(getLabelStyleRowSize(p), columnSize, plot),null},
            };
        }
    }

    protected double[] getLabelStyleRowSize(double p) {
        return new double[]{p, p, p};
    }

    protected double[] getLabelPaneRowSize(Plot plot, double p) {
        return hasLabelPosition(plot) ? new double[]{p,p,p,p,p} : new double[]{p,p,p};
    }

    protected boolean hasLabelPosition(Plot plot) {
        return plot instanceof VanChartLabelPositionPlot;
    }

    protected JPanel createTableLayoutPaneWithTitle(String title, JPanel panel) {
        return TableLayout4VanChartHelper.createExpandablePaneWithTitle(title, panel);
    }

    protected JPanel createLabelPositionPane(double[] row, double[] col, Plot plot) {
        if(plot instanceof VanChartLabelPositionPlot){
            String[] names = ((VanChartLabelPositionPlot) plot).getLabelLocationNameArray();
            Integer[] values =  ((VanChartLabelPositionPlot) plot).getLabelLocationValueArray();

            if(names == null || names.length == 0){
                return new JPanel();
            }
            if(values == null || values.length == 0){
                return new JPanel();
            }

            position = new UIButtonGroup<Integer>(names, values);
            autoAdjust = new UIButtonGroup<Boolean>(new String[]{com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_On"), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Off")}, new Boolean[]{true, false});


            Component[][] comps = new Component[2][2];

            comps[0] = new Component[]{null,null};
            comps[1] = new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Chart-Layout_Position"), SwingConstants.LEFT), position};

            JPanel panel =new JPanel(new BorderLayout());
            panel.add(getLabelPositionPane(comps,row,col),BorderLayout.CENTER);
            if(plot.isSupportLeadLine()){
                tractionLine = new UIToggleButton(com.fr.design.i18n.Toolkit.i18nText("ChartF-Show_GuidLine"));
                tractionLinePane = TableLayout4VanChartHelper.createGapTableLayoutPane("",tractionLine);
                panel.add(tractionLinePane, BorderLayout.SOUTH);
                initPositionListener();
            } else if(PlotFactory.plotAutoAdjustLabelPosition(plot)){
                panel.add(TableLayout4VanChartHelper.createGapTableLayoutPane(com.fr.design.i18n.Toolkit.i18nText("Plugin-ChartF_Auto_Adjust"),autoAdjust), BorderLayout.SOUTH);
            }
            return panel;
        }
        return new JPanel();
    }

    protected JPanel getLabelPositionPane (Component[][] comps, double[] row, double[] col){
        JPanel panel = TableLayoutHelper.createTableLayoutPane(comps,row,col);
        return createTableLayoutPaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("FR-Chart_Layout"), panel);
    }


    protected void initPositionListener() {
        position.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                checkPosition();
            }
        });
    }

    protected JPanel createLabelStylePane(double[] row, double[] col, Plot plot) {
        style = new UIButtonGroup<Integer>(new String[]{com.fr.design.i18n.Toolkit.i18nText("Plugin-ChartF_Automatic"),
                com.fr.design.i18n.Toolkit.i18nText("Plugin-ChartF_Custom")});
        textFontPane =initTextFontPane();

        initStyleListener();

        JPanel panel = TableLayout4VanChartHelper.createGapTableLayoutPane(getLabelStyleComponents(plot),row,col);
        return createTableLayoutPaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("FR-Designer-Widget_Style"), panel);
    }

    protected ChartTextAttrPane initTextFontPane () {
        return new ChartTextAttrPane(){
            protected Component[][] getComponents(JPanel buttonPane) {
                return new Component[][]{
                        new Component[]{null, null},
                        new Component[]{null, fontNameComboBox},
                        new Component[]{null, buttonPane}
                };
            }
        };
    }

    protected Component[][] getLabelStyleComponents(Plot plot) {
        UILabel text = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Plugin-Chart_Character"), SwingConstants.LEFT);
        return new Component[][]{
                new Component[]{null,null},
                new Component[]{text,style},
                new Component[]{textFontPane,null},
        };
    }

    protected void initStyleListener() {
        style.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkStyleUse();
            }
        });
    }

    protected JPanel createBackgroundColorPane() {
        backgroundColor = new ColorSelectBox(100);
        return createTableLayoutPaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Plugin-ChartF_Background"), backgroundColor);
    }

    protected String title4PopupWindow() {
        return null;
    }

    private void checkAllUse() {
        checkStyleUse();
        if(tractionLine == null){
            return;
        }
        checkPositionEnabled();
    }

    private void checkStyleUse() {
        textFontPane.setVisible(style.getSelectedIndex() == 1);
        textFontPane.setPreferredSize(style.getSelectedIndex() == 1 ? new Dimension(0, 60) : new Dimension(0, 0));
    }

    private void checkPosition() {
        tractionLine.setSelected(position.getSelectedItem() == Constants.OUTSIDE);
        checkPositionEnabled();
    }
    private void checkPositionEnabled() {
        tractionLinePane.setVisible(position.getSelectedItem() == Constants.OUTSIDE);
    }

    public void populate(AttrLabelDetail detail) {
        dataLabelContentPane.populateBean(detail.getContent());
        if(position != null){
            position.setSelectedItem(detail.getPosition());
        }
        if(tractionLine != null){
            tractionLine.setSelected(detail.isShowGuidLine());
        }
        if(autoAdjust != null){
            autoAdjust.setSelectedIndex(detail.isAutoAdjust() == true ? 0 : 1);
        }
        style.setSelectedIndex(detail.isCustom() ? 1 : 0);
        textFontPane.populate(detail.getTextAttr());

        if(backgroundColor != null){
            backgroundColor.setSelectObject(detail.getBackgroundColor());
        }

        checkAllUse();
    }


    public void update(AttrLabelDetail detail) {
        detail.setContent(dataLabelContentPane.updateBean());

        if(position != null && position.getSelectedItem() != null){
            detail.setPosition(position.getSelectedItem());
            
        } else if(position != null){
            position.setSelectedItem(detail.getPosition());
        }

        detail.setAutoAdjust(autoAdjust != null && autoAdjust.getSelectedItem());

        if(tractionLine != null){
            detail.setShowGuidLine(tractionLine.isSelected() && detail.getPosition() == Constants.OUTSIDE);
        }
        detail.setCustom(style.getSelectedIndex() == 1);
        if(textFontPane != null){
            detail.setTextAttr(textFontPane.update());
        }
        if(backgroundColor != null){
            detail.setBackgroundColor(backgroundColor.getSelectObject());
        }
    }

}