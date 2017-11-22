package com.fr.plugin.chart.range.component;

import com.fr.design.gui.frpane.UINumberDragPane;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.mainframe.chart.gui.ColorSelectBoxWithOutTransparent;
import com.fr.general.Inter;
import com.fr.plugin.chart.designer.TableLayout4VanChartHelper;
import com.fr.plugin.chart.designer.style.axis.component.MinMaxValuePaneWithOutTick;
import com.fr.plugin.chart.range.GradualIntervalConfig;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class GradualIntervalConfigPane extends JPanel{
    private static final long serialVersionUID = 1614283200308877353L;

    //最大最小值面板
    private MinMaxValuePaneWithOutTick minMaxValuePane;
    //主题颜色
    private ColorSelectBoxWithOutTransparent colorSelectBox;
    //划分阶段
    UINumberDragPane numberDragPane;
    //渐变色编辑器
    private LegendGradientBar legendGradientBar;

    public GradualIntervalConfigPane(){
        initComponents();
    }

    private void initComponents() {
        minMaxValuePane = new MinMaxValuePaneWithOutTick();

        colorSelectBox = new ColorSelectBoxWithOutTransparent(100);

        colorSelectBox.addSelectChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                legendGradientBar.refreshSubColor(colorSelectBox.getSelectObject());
            }
        });

        numberDragPane = new UINumberDragPane(1,6) {
            @Override
            public void userEvent(double value) {
                legendGradientBar.refreshColorSelectionBtnNum((int)value);
            }
        };

        legendGradientBar = new LegendGradientBar();

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double e = TableLayout4VanChartHelper.EDIT_AREA_WIDTH;
        double[] col = new double[]{f, e};
        double[] row = new double[]{p, p, p, p};

        Component[][] components = getPaneComponents();

        //控件承载面板
        JPanel contentPane = TableLayout4VanChartHelper.createGapTableLayoutPane(components,row,col);
        this.setLayout(new BorderLayout());
        this.add(contentPane,BorderLayout.CENTER);
    }

    protected Component[][] getPaneComponents(){
        return new Component[][]{
                new Component[]{minMaxValuePane, null},
                new Component[]{new BoldFontTextLabel(Inter.getLocText(new String[]{"FR-Chart-Color_Subject", "FR-Chart-Color_Color"})), colorSelectBox},
                new Component[]{new BoldFontTextLabel(Inter.getLocText("FR-Chart-Value_Divided_stage")), numberDragPane},
                new Component[]{null, legendGradientBar},
        };
    }

    protected Component[][] getPaneComponentsWithOutTheme(){
        return new Component[][]{
                new Component[]{minMaxValuePane, null},
                new Component[]{new BoldFontTextLabel(Inter.getLocText("FR-Chart-Value_Divided_stage")), numberDragPane},
                new Component[]{null, legendGradientBar},
        };
    }

    public void populate(GradualIntervalConfig intervalConfig){
        minMaxValuePane.populate(intervalConfig.getMinAndMaxValue());

        colorSelectBox.setSelectObject(intervalConfig.getSubColor());

        numberDragPane.populateBean(intervalConfig.getDivStage());

        //由于选择主题色和选择划分阶段会重置颜色选择器，故而这个需要放到后面更新
        legendGradientBar.populate(intervalConfig);
    }

    public void update(GradualIntervalConfig intervalConfig){
        minMaxValuePane.update(intervalConfig.getMinAndMaxValue());

        intervalConfig.setSubColor(colorSelectBox.getSelectObject());

        intervalConfig.setDivStage(numberDragPane.updateBean());

        legendGradientBar.update(intervalConfig);
    }
}