package com.fr.van.chart.range.component;

import com.fr.design.gui.frpane.UINumberDragPane;
import com.fr.design.gui.ilable.BoldFontTextLabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.mainframe.chart.gui.ColorSelectBoxWithOutTransparent;

import com.fr.plugin.chart.range.GradualIntervalConfig;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;
import com.fr.van.chart.designer.style.axis.component.MinMaxValuePaneWithOutTick;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.Component;

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

        legendGradientBar = createLegendGradientBar();

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

    protected LegendGradientBar createLegendGradientBar() {
        return new LegendGradientBar();
    }

    protected Component[][] getPaneComponents(){
        return new Component[][]{
                new Component[]{minMaxValuePane, null},
                new Component[]{new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Subject_Color")), colorSelectBox},
                new Component[]{new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("FR-Chart-Value_Divided_stage")), numberDragPane},
                new Component[]{null, legendGradientBar},
        };
    }

    protected Component[][] getPaneComponentsWithOutTheme(){
        return new Component[][]{
                new Component[]{minMaxValuePane, null},
                new Component[]{new BoldFontTextLabel(com.fr.design.i18n.Toolkit.i18nText("FR-Chart-Value_Divided_stage")), numberDragPane},
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