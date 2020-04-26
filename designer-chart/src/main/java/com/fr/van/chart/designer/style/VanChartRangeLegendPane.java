package com.fr.van.chart.designer.style;


import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.plugin.chart.attr.VanChartLegend;
import com.fr.plugin.chart.range.VanChartRangeLegend;
import com.fr.plugin.chart.type.LegendType;
import com.fr.van.chart.designer.TableLayout4VanChartHelper;
import com.fr.van.chart.range.component.GradualLegendPane;
import com.fr.van.chart.range.component.SectionLegendPane;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * 属性表, 图表样式-图例 界面.
 */
public class VanChartRangeLegendPane extends VanChartPlotLegendPane {
    private static final long serialVersionUID = 1614283200308877353L;

    //散点图不同类型面板容器,容器布局管理
    private JPanel rangeLegendPane;

    //图例切换按钮
    private UIButtonGroup<LegendType> legendTypeButton;
    //普通图例面板(因为普通图例没有新内容，故而为空)
    private JPanel ordinaryLegendPane;
    //渐变色图例面板
    private GradualLegendPane gradualLegendPane;
    //区域段图例面板
    private SectionLegendPane sectionLegendPane;

    public VanChartRangeLegendPane() {
        super();
    }
    public VanChartRangeLegendPane(VanChartStylePane parent){
        super(parent);
    }
    private JPanel createRangeLegendPane() {
        //普通图例面板
        ordinaryLegendPane = new JPanel();
        //渐变色图例面板
        gradualLegendPane = createGradualLegendPane();
        gradualLegendPane.setParentPane(parent);
        //区域段图例面板
        sectionLegendPane = createSectionLegendPane();
        sectionLegendPane.setParentPane(parent);

        JPanel panel = new JPanel(new CardLayout()){
            @Override
            public Dimension getPreferredSize() {
                if(legendTypeButton.getSelectedItem() == LegendType.ORDINARY){
                    return new Dimension(ordinaryLegendPane.getWidth(), 0);
                } else if (legendTypeButton.getSelectedItem() == LegendType.GRADUAL){
                    return gradualLegendPane.getPreferredSize();
                }else{
                    return sectionLegendPane.getPreferredSize();
                }
            }
        };

        panel.add(ordinaryLegendPane, LegendType.ORDINARY.getStringType());
        panel.add(gradualLegendPane, LegendType.GRADUAL.getStringType());
        panel.add(sectionLegendPane, LegendType.SECTION.getStringType());

        return panel;
    }

    protected GradualLegendPane createGradualLegendPane() {
        return new GradualLegendPane();
    }

    protected SectionLegendPane createSectionLegendPane() {
        return new SectionLegendPane(this.parent);
    }

    private JPanel createTableLayoutPaneWithTitle(String title, Component component) {
        return TableLayout4VanChartHelper.createGapTableLayoutPane(title, component);
    }

    protected UIButtonGroup<LegendType> createLegendTypeButton(){
        return new UIButtonGroup<LegendType>(new String[]{
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Legend_Ordinary"),
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Legend_Gradual"),
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Legend_Section")
        }, new LegendType[]{LegendType.ORDINARY, LegendType.GRADUAL, LegendType.SECTION});
    }

    protected JPanel createCommonLegendPane(){
        return super.createLegendPane();
    }

    @Override
    protected JPanel createLegendPane(){
        legendTypeButton = createLegendTypeButton();

        initLegendTypeButtonListener();

        JPanel legendTypeButtonWithTilePane = createTableLayoutPaneWithTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Legend_Form"),legendTypeButton);
        legendTypeButtonWithTilePane.setBorder(BorderFactory.createEmptyBorder(0,5,0,0));

        rangeLegendPane = createRangeLegendPane();

        //不包含新内容的普通面板内容
        JPanel commonLegendPane = this.createCommonLegendPane();

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] col = {f};
        double[] row = {p, p, p, p};
        Component[][] components = new Component[][]{
                new Component[]{legendTypeButtonWithTilePane},
                new Component[]{rangeLegendPane},
                new Component[]{commonLegendPane}
        };
        return TableLayoutHelper.createTableLayoutPane(components,row,col);
    }

    private void initLegendTypeButtonListener() {
        legendTypeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkCardPane();
            }
        });
    }

    private void checkCardPane() {
        CardLayout cardLayout = (CardLayout) rangeLegendPane.getLayout();
        cardLayout.show(rangeLegendPane, legendTypeButton.getSelectedItem().getStringType());
    }

    @Override
    protected void addLegendListener(){
        isLegendVisible.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                checkBoxUse();
            }
        });
    }

    @Override
    protected void checkAllUse() {
        checkBoxUse();
        checkDisplayStrategyUse();
        checkCardPane();
        this.repaint();
    }

    @Override
    protected void checkBoxUse() {
        isLegendVisible.setEnabled(true);
        legendPane.setVisible(isLegendVisible.isSelected());
    }

    private void checkHighlightVisible(){
        if(this.highlightPane != null){
            LegendType legendType = legendTypeButton.getSelectedItem();
            this.highlightPane.setVisible(legendType != LegendType.GRADUAL);
        }
    }

    @Override
    public void updateBean(VanChartLegend legend) {
        if(legend == null) {
            legend = new VanChartRangeLegend();
        }
        super.updateBean(legend);

        VanChartRangeLegend scatterLegend = (VanChartRangeLegend)legend;
        //范围图例部分
        LegendType legendType = legendTypeButton.getSelectedItem();
        scatterLegend.setLegendType(legendType);
        if (legendType == LegendType.GRADUAL) {
            gradualLegendPane.update(scatterLegend.getGradualLegend());
        }else if (legendType == LegendType.SECTION) {
            sectionLegendPane.update(scatterLegend.getSectionLegend());
        }

        this.checkHighlightVisible();
    }

    @Override
    public void populateBean(VanChartLegend legend) {
        VanChartRangeLegend scatterLegend = (VanChartRangeLegend)legend;
        if (scatterLegend != null) {

            //范围图例部分
            legendTypeButton.setSelectedItem(scatterLegend.getLegendType());
            gradualLegendPane.populate(scatterLegend.getGradualLegend());
            sectionLegendPane.populate(scatterLegend.getSectionLegend());
            super.populateBean(scatterLegend);
        }
        checkAllUse();
        this.checkHighlightVisible();
    }
}