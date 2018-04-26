package com.fr.van.chart.column;

import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.data.condition.AbstractCondition;
import com.fr.data.condition.ListCondition;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.condition.LiteConditionPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.plugin.chart.base.AttrSeriesStackAndAxis;
import com.fr.van.chart.designer.style.series.VanChartSeriesConditionPane;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 堆积和坐标轴设置
 */
public class VanChartCustomStackAndAxisConditionPane extends BasicBeanPane<ConditionAttr> {
    private static final long serialVersionUID = 2713124322060048526L;

    protected UIButtonGroup<Integer> XAxis;
    protected UIButtonGroup<Integer> YAxis;
    protected UIButtonGroup<Integer> isStacked;
    protected UIButtonGroup<Integer> isPercentStacked;

    private LiteConditionPane liteConditionPane;

    public VanChartCustomStackAndAxisConditionPane() {

    }

    private void doLayoutPane() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.removeAll();

        //配置界面
        JPanel deployPane = FRGUIPaneFactory.createBorderLayout_L_Pane();
        this.add(deployPane);

        deployPane.setBorder(GUICoreUtils.createTitledBorder(Inter.getLocText("Plugin-ChartF_Deploy") + ":", null));
        deployPane.add(createDeployPane());

        //条件界面
        JPanel conditionPane = FRGUIPaneFactory.createBorderLayout_L_Pane();
        this.add(conditionPane);
        conditionPane.setBorder(BorderFactory.createEmptyBorder());

        conditionPane.add(liteConditionPane = new VanChartSeriesConditionPane());
        liteConditionPane.setPreferredSize(new Dimension(300, 300));
    }

    private JPanel createDeployPane() {
        isStacked = new UIButtonGroup<Integer>(new String[]{Inter.getLocText("Plugin-ChartF_YES"), Inter.getLocText("Plugin-ChartF_NO")});
        isPercentStacked = new UIButtonGroup<Integer>(new String[]{Inter.getLocText("Plugin-ChartF_YES"), Inter.getLocText("Plugin-ChartF_NO")});
        double p = TableLayout.PREFERRED;
        double[] columnSize = {p, p};
        double[] rowSize = {p, p, p, p};

        return TableLayoutHelper.createTableLayoutPane(getDeployComponents(), rowSize, columnSize);
    }

    protected Component[][] getDeployComponents() {
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("ChartF-X_Axis")), XAxis},
                new Component[]{new UILabel(Inter.getLocText("ChartF-Y_Axis")), YAxis},
                new Component[]{new UILabel(Inter.getLocText("FR-Chart-Type_Stacked")), isStacked},
                new Component[]{new UILabel(Inter.getLocText("Plugin-ChartF_PercentStacked")), isPercentStacked},
        };

        isStacked.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkBox();
            }
        });
        return components;
    }

    @Override
    protected String title4PopupWindow() {
        return Inter.getLocText("Plugin-ChartF_StackAndSeries");
    }

    private void checkBox() {
        isPercentStacked.setEnabled(isStacked.getSelectedIndex() == 0);
    }

    public void populateBean(ConditionAttr conditionAttr) {
        AttrSeriesStackAndAxis seriesStackAndAxis = (AttrSeriesStackAndAxis) conditionAttr.getExisted(AttrSeriesStackAndAxis.class);
        XAxis = new UIButtonGroup<Integer>(seriesStackAndAxis.getXAxisNamesArray());
        YAxis = new UIButtonGroup<Integer>(seriesStackAndAxis.getYAxisNameArray());

        doLayoutPane();
        XAxis.setSelectedIndex(seriesStackAndAxis.getXAxisIndex());
        YAxis.setSelectedIndex(seriesStackAndAxis.getYAxisIndex());
        isStacked.setSelectedIndex(seriesStackAndAxis.isStacked() ? 0 : 1);
        isPercentStacked.setSelectedIndex(seriesStackAndAxis.isPercentStacked() ? 0 : 1);

        if (conditionAttr.getCondition() == null) {
            this.liteConditionPane.populateBean(new ListCondition());
        } else {
            this.liteConditionPane.populateBean(conditionAttr.getCondition());
        }

        checkBox();
    }

    protected void updateStackAndPercent(AttrSeriesStackAndAxis seriesStackAndAxis) {
        seriesStackAndAxis.setStacked(isStacked.getSelectedIndex() == 0);
        if (seriesStackAndAxis.isStacked()) {
            seriesStackAndAxis.setPercentStacked(isPercentStacked.getSelectedIndex() == 0);
        } else {
            seriesStackAndAxis.setPercentStacked(false);
        }
    }

    public ConditionAttr updateBean() {
        ConditionAttr conditionAttr = new ConditionAttr();
        AttrSeriesStackAndAxis seriesStackAndAxis = new AttrSeriesStackAndAxis();
        seriesStackAndAxis.setXAxisIndex(XAxis.getSelectedIndex());
        seriesStackAndAxis.setYAxisIndex(YAxis.getSelectedIndex());

        updateStackAndPercent(seriesStackAndAxis);
        conditionAttr.addDataSeriesCondition(seriesStackAndAxis);

        AbstractCondition con = (AbstractCondition) this.liteConditionPane.updateBean();
        conditionAttr.setCondition(con);
        return conditionAttr;
    }

}