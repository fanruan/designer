package com.fr.design.chart.series.SeriesCondition;

import com.fr.base.FRContext;
import com.fr.chart.base.AttrAlpha;
import com.fr.chart.base.AttrBackground;
import com.fr.chart.base.AttrContents;
import com.fr.chart.base.DataSeriesCondition;
import com.fr.chart.chartattr.ChartCollection;
import com.fr.chart.chartattr.Plot;
import com.fr.chart.chartglyph.ConditionAttr;
import com.fr.data.condition.AbstractCondition;
import com.fr.data.condition.ListCondition;
import com.fr.design.condition.ConditionAttrSingleConditionPane;
import com.fr.design.condition.ConditionAttributesPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-11-30
 * Time   : 上午9:16
 */
public class DataSeriesConditionPane extends ConditionAttributesPane<ConditionAttr> {

	
    public DataSeriesConditionPane() {
        initAvailableActionList();
        initComponents();
    }

    private void initAvailableActionList() {
        addBasicAction();
        addAxisPositionAction();
        addStyleAction();
        addBorderAction();
        addTrendLineAction();
        addAction2UseAbleActionList();
    }

    protected void initComponents() {
        super.initComponents();
        JPanel pane = FRGUIPaneFactory.createBorderLayout_L_Pane();
        this.add(pane);
        pane.setBorder(BorderFactory.createEmptyBorder());

        // 条件界面
        pane.add(liteConditionPane = createListConditionPane(), BorderLayout.CENTER);
        // kunsnat_size  加载属性之后 被遮挡, 最少的高度为300, 5个按钮
        liteConditionPane.setPreferredSize(new Dimension(300, 300));
    }

    protected ChartConditionPane createListConditionPane() {
        return ChartConditionPaneFactory.createChartConditionPane(getClass());
    }

    protected void addBasicAction() {
        classPaneMap.put(AttrAlpha.class, new LabelAlphaPane(this));
        classPaneMap.put(AttrContents.class, new LabelContentsPane(this, class4Correspond()));
    }
    
    protected void addStyleAction() {
        classPaneMap.put(AttrBackground.class, new LabelBackgroundPane(this));
    }

    protected void addAxisPositionAction() {

    }

    protected void addBorderAction() {

    }

    protected void addTrendLineAction() {

    }

    /**
     * 返回class对象
     * @return class对象
     */
    public Class<? extends Plot> class4Correspond() {
        return Plot.class;
    }

    protected void addAction2UseAbleActionList() {
        useAbleActionList.clear();
        Iterator<ConditionAttrSingleConditionPane> iterator = classPaneMap.values().iterator();

        while (iterator.hasNext()) {
            useAbleActionList.add(iterator.next().getHighLightConditionAction());
        }
    }

    /**
     * 创建条件单独的属性.
     * @param clazz 单独条件属性面板
     * @return 单独条件属性面板
     */
    public ConditionAttrSingleConditionPane createConditionAttrSingleConditionPane(Class<? extends ConditionAttrSingleConditionPane> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            FRContext.getLogger().error(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            FRContext.getLogger().error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public void populateBean(ConditionAttr ob) {
        this.selectedItemPane.removeAll();
        this.initAvailableActionList();

        if (ob.getCondition() == null) {
            this.liteConditionPane.populateBean(new ListCondition());
        } else {
            this.liteConditionPane.populateBean(ob.getCondition());
        }

        for (int i = 0; i < ob.getDataSeriesConditionCount(); i++) {
            DataSeriesCondition condition = ob.getDataSeriesCondition(i);

            ConditionAttrSingleConditionPane pane = classPaneMap.get(condition.getClass());

            if (pane != null && useAbleActionList.contains(pane.getHighLightConditionAction())) {
                pane.populate(condition);
                this.selectedItemPane.add(pane);
                useAbleActionList.remove(pane.getHighLightConditionAction());
            }
        }

        updateMenuDef();
        validate();
        repaint(10);
    }

    @Override
    public ConditionAttr updateBean() {
        ConditionAttr ca = new ConditionAttr();

        updateBeanInvoked(ca);

        return ca;
    }
    
    public void updateBean(ConditionAttr condition) {
    	condition.removeAll();
    	updateBeanInvoked(condition);
    }

    @Override
    protected String title4PopupWindow() {
        return Inter.getLocText(new String[]{"Condition", "Display"});
    }

    /**
      * 为了给DataSeriesCustomConditionPane调用,不得不加了这么个方法
     * @param ca 条件属性
      */
    public void updateBeanInvoked(ConditionAttr ca) {
    	ca.removeAll();
        Iterator<ConditionAttrSingleConditionPane> iterator = this.classPaneMap.values().iterator();

        while (iterator.hasNext()) {
            ConditionAttrSingleConditionPane<DataSeriesCondition> pane = iterator.next();
            if (pane.getParent() == this.selectedItemPane) {
                ca.addDataSeriesCondition(pane.update());
            }
        }

        AbstractCondition con = (AbstractCondition) this.liteConditionPane.updateBean();
        ca.setCondition(con);
    }

}