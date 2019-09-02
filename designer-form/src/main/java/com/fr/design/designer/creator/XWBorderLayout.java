/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.designer.creator;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ContainerEvent;
import java.beans.IntrospectionException;

import com.fr.design.designer.beans.LayoutAdapter;
import com.fr.design.designer.beans.adapters.layout.FRBorderLayoutAdapter;
import com.fr.design.designer.beans.location.Direction;
import com.fr.design.form.layout.FRBorderLayout;
import com.fr.design.i18n.Toolkit;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WBorderLayout;
import com.fr.general.ComparatorUtils;


/**
 * @author richer
 * @since 6.5.3
 */
public class XWBorderLayout extends XLayoutContainer {
    private int Num = 5;
    public XWBorderLayout(){
        super(new WBorderLayout(),new Dimension(WBorderLayout.DEFAULT_WIDTH, WBorderLayout.DEFAULT_HEIGHT));
    }
	
    public XWBorderLayout(WBorderLayout widget, Dimension initSize) {
        super(widget, initSize);
    }

    @Override
    protected String getIconName() {
        return "layout_border.png";
    }

    /**
     * 默认名称
     * @return      名称
     */
    @Override
	public String createDefaultName() {
    	return "border";
    }

    /**
     * 转化成相应 WBorderLayout
     * @return   相应 WBorderLayout
     */
    @Override
    public WBorderLayout toData() {
        return (WBorderLayout) data;
    }

    @Override
	protected void initLayoutManager() {
        this.setLayout(new FRBorderLayout(toData().getHgap(), toData().getVgap()));
    }
    /**
     *  初始大小
     * @return   初始大小
     */
    @Override
    public Dimension initEditorSize() {
        return new Dimension(WBorderLayout.DEFAULT_WIDTH, WBorderLayout.DEFAULT_HEIGHT);
    }

    /**
     *  得到属性名
     * @return 属性名
     * @throws java.beans.IntrospectionException    抛错
     */
    @Override
    public CRPropertyDescriptor[] supportedDescriptor() throws IntrospectionException {
        return  new CRPropertyDescriptor[] {
                new CRPropertyDescriptor("widgetName", this.data.getClass()).setI18NName(Toolkit.i18nText("Fine-Design_Form_Form_Widget_Name")),
                new CRPropertyDescriptor("customTitleName", this.data.getClass()).setI18NName(Toolkit.i18nText("Fine-Design_Form_Title"))
        };
    }

    /**
     * 将WLayout转换为XLayoutContainer
     */
    @Override
    public void convert() {
        isRefreshing = true;
        WBorderLayout wb = this.toData();
        this.removeAll();
        String[] arrs = {WBorderLayout.NORTH, WBorderLayout.SOUTH, WBorderLayout.EAST, WBorderLayout.WEST, WBorderLayout.CENTER};
        for (int i = 0; i < arrs.length; i++) {
            Widget wgt = wb.getLayoutWidget(arrs[i]);
            if (wgt != null) {
                XWidgetCreator comp = (XWidgetCreator) XCreatorUtils.createXCreator(wgt, calculatePreferredSize(wgt));
                this.add(comp, arrs[i]);
                comp.setBackupParent(this);
            }
        }
        isRefreshing = false;
    }
    

    /**
     * 设计界面中有组件添加时，要通知WLayout容器重新paint
     * @param e    事件
     */
    @Override
    public void componentAdded(ContainerEvent e) {
        if (isRefreshing) {
            return;
        }
        XWidgetCreator creator = (XWidgetCreator) e.getChild();
        BorderLayout b = (BorderLayout) getLayout();
        Object constraints = b.getConstraints(creator);
        WBorderLayout wb = this.toData();
        Widget w = creator.toData();
        add(wb, w, constraints);
        doResizePreferredSize(creator, calculatePreferredSize(w));
    }

    @Override
    protected Dimension calculatePreferredSize(Widget wgt) {
        WBorderLayout wlayout = this.toData();
        Object constraints = wlayout.getConstraints(wgt);
        Dimension d = new Dimension();
        if (ComparatorUtils.equals(WBorderLayout.NORTH,constraints)) {
            d.height = wlayout.getNorthSize();
        } else if (ComparatorUtils.equals(WBorderLayout.SOUTH,constraints)) {
            d.height = wlayout.getSouthSize();
        } else if (ComparatorUtils.equals(WBorderLayout.EAST,constraints)) {
            d.width = wlayout.getEastSize();
        } else if (ComparatorUtils.equals(WBorderLayout.WEST,constraints)) {
            d.width = wlayout.getWestSize();
        }
        return d;
    }

    private void doResizePreferredSize(XWidgetCreator comp, Dimension d) {
        comp.setPreferredSize(d);
    }

    /**
     * 加入控件
     * @param layout      布局
     * @param wgt   控件
     * @param constraints    方位
     */
    public static void add(WBorderLayout layout, Widget wgt, Object constraints) {
        if (ComparatorUtils.equals(WBorderLayout.NORTH,constraints)) {
            layout.addNorth(wgt);
        } else if (ComparatorUtils.equals(WBorderLayout.SOUTH,constraints)) {
            layout.addSouth(wgt);
        } else if (ComparatorUtils.equals(WBorderLayout.EAST,constraints)) {
            layout.addEast(wgt);
        } else if (ComparatorUtils.equals(WBorderLayout.WEST,constraints)) {
            layout.addWest(wgt);
        } else if (ComparatorUtils.equals(WBorderLayout.CENTER,constraints)) {
            layout.addCenter(wgt);
        }
    }

    /**
     * 重新计算大小
     */
    @Override
    public void recalculateChildrenSize() {
        Dimension d = getSize();
        WBorderLayout layout = toData();
        layout.setNorthSize(d.height / Num);
        layout.setSouthSize(d.height / Num);
        layout.setWestSize(d.width / Num);
        layout.setEastSize(d.width / Num);
    }


    /**
     * 在添加的时候需要把可拉伸的方向确定，所以重写了add方法
     * @param comp        组件
     * @param constraints         方位
     */
    @Override
    public void add(Component comp, Object constraints) {
        super.add(comp, constraints);
        if (comp == null) {
            return;
        }
        XCreator creator = (XCreator) comp;
        // 添加到北边时可拉伸底部
        if (ComparatorUtils.equals(BorderLayout.NORTH, constraints)) {
            creator.setDirections(new int[]{Direction.BOTTOM});
            // 添加到南部时可拉伸顶部
        } else if (ComparatorUtils.equals(BorderLayout.SOUTH, constraints)) {
            creator.setDirections(new int[]{Direction.TOP});
            // 添加到东部的时候可向左边拉伸
        } else if (ComparatorUtils.equals(BorderLayout.EAST, constraints)) {
            creator.setDirections(new int[]{Direction.LEFT});
            // 添加到西部的时候可向右边拉伸
        } else if (ComparatorUtils.equals(BorderLayout.WEST, constraints)) {
            creator.setDirections(new int[]{Direction.RIGHT});
        }
    }
    
	@Override
	public LayoutAdapter getLayoutAdapter() {
		return new FRBorderLayoutAdapter(this);
	}
}