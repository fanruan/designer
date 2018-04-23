/**
 *
 */
package com.fr.design.designer.creator;

import com.fr.design.designer.beans.LayoutAdapter;
import com.fr.design.designer.beans.adapters.layout.FRTitleLayoutAdapter;
import com.fr.design.form.layout.FRTitleLayout;
import com.fr.design.fun.WidgetPropertyUIProvider;
import com.fr.form.ui.Label;
import com.fr.form.ui.Widget;
import com.fr.form.ui.WidgetTitle;
import com.fr.form.ui.container.WTitleLayout;
import com.fr.general.ComparatorUtils;
import com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget;

import java.awt.*;
import java.awt.event.ContainerEvent;

/**
 * 一些控件 如图表、报表块，有标题设置，且标题的高度字体等不变
 * @author jim
 * @date 2014-9-25
 */
public class XWTitleLayout extends DedicateLayoutContainer {

	/**
	 *
	 */
	private static final long serialVersionUID = 5274572473978467325L;

	private static final int INDEX = 0;

	public XWTitleLayout() {
		super(new WTitleLayout("titlePane"), new Dimension());
	}

	/**
	 * 容器构造函数
	 *
	 * @param widget 控件widget
	 * @param initSize 尺寸大小
	 */
	public XWTitleLayout(WTitleLayout widget, Dimension initSize) {
		super(widget, initSize);
	}

	/**
	 * 初始化容器对应的布局 由于是只装一个需要保持原样高度的控件，布局设为absolute
	 */
	@Override
	protected void initLayoutManager() {
		this.setLayout(new FRTitleLayout());
	}

	/**
	 * 容器的渲染器
	 */
	@Override
	public LayoutAdapter getLayoutAdapter() {
		return new FRTitleLayoutAdapter(this);
	}

	public XCreator getEditingChildCreator(){
		return getXCreator(INDEX);
	}

	/**
	 * 返回容器对应的wlayout
	 *
	 * @return 同上
	 */
	@Override
	public WTitleLayout toData() {
		return (WTitleLayout) data;
	}

	/**
	 * 重置组件的名称
	 * @param name 名称
	 */
	public void resetCreatorName(String name) {
		super.resetCreatorName(name);
		// 有标题的话，标题的名字也要重置下
		if (getXCreatorCount() > 1) {
			getTitleCreator().toData().setWidgetName(WidgetTitle.TITLE_NAME_INDEX + name);
		}
	}

	/**
	 * 返回默认组件name
	 *
	 * @return 容器名
	 */
	@Override
	public String createDefaultName() {
		return "titlePanel";
	}

	/**
	 * 返回标题组件
	 * @return 标题组件
	 */
	public XCreator getTitleCreator() {
		for (int i=0; i < getXCreatorCount(); i++) {
			XCreator creator = getXCreator(i);
			if (!creator.hasTitleStyle()) {
				return creator;
			}
		}
		return null;
	}

	/**
	 * 将WLayout转换为XLayoutContainer
	 */
	@Override
	public void convert() {
		isRefreshing = true;
		WTitleLayout layout = this.toData();
		this.removeAll();
		for (int i = 0, num = layout.getWidgetCount(); i < num; i++) {
			BoundsWidget bw = (BoundsWidget) layout.getWidget(i);
			if (bw != null) {
				Rectangle bounds = bw.getBounds();
				XWidgetCreator comp = (XWidgetCreator) XCreatorUtils.createXCreator(bw.getWidget());
				String constraint = bw.getWidget().acceptType(Label.class) ? WTitleLayout.TITLE : WTitleLayout.BODY;
				this.add(comp, constraint);
				comp.setBounds(bounds);
			}
		}
		isRefreshing = false;
	}

	/**
	 * 组件增加
	 *
	 * @param e 容器事件
	 */
	@Override
	public void componentAdded(ContainerEvent e) {
		if (isRefreshing) {
			return;
		}
		WTitleLayout layout = this.toData();
		XWidgetCreator creator = (XWidgetCreator) e.getChild();
		FRTitleLayout lay = (FRTitleLayout) getLayout();
		Object constraints = lay.getConstraints(creator);
		if (ComparatorUtils.equals(WTitleLayout.TITLE,constraints)) {
			layout.addTitle(creator.toData(), creator.getBounds());
		} else if (ComparatorUtils.equals(WTitleLayout.BODY,constraints)) {
			layout.addBody(creator.toData(), creator.getBounds());
		}
	}

	/**
	 * 组件删除
	 *
	 * @param e
	 *            容器事件
	 */
	@Override
	public void componentRemoved(ContainerEvent e) {
		if (isRefreshing) {
			return;
		}
		XWidgetCreator xwc = ((XWidgetCreator) e.getChild());
		Widget wgt = xwc.toData();
		WTitleLayout wlayout = this.toData();
		wlayout.removeWidget(wgt);
	}

	@Override
	public WidgetPropertyUIProvider[] getWidgetPropertyUIProviders() {
		XCreator creator = getPropertyDescriptorCreator();
		return creator.getWidgetPropertyUIProviders();
	}
}