/**
 * 
 */
package com.fr.design.designer.creator;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ContainerEvent;

import com.fr.design.designer.beans.LayoutAdapter;
import com.fr.design.designer.beans.adapters.layout.FRScaleLayoutAdapter;
import com.fr.design.form.layout.FRScaleLayout;
import com.fr.design.fun.WidgetPropertyUIProvider;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WScaleLayout;
import com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget;

/**
 * 自适应布局中添加组件时，部分控件如数字文本下拉等需要保持控件默认高度21， 用此容器来实现
 * 
 * @author jim
 * @date 2014-8-5
 */
public class XWScaleLayout extends DedicateLayoutContainer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8870102816567181548L;
    public static final int INDEX=0;

	/**
	 * 构造方法
	 */
	public XWScaleLayout() {
		this(new WScaleLayout("scalePanel"), new Dimension());
	}

	/**
	 * 容器构造函数
	 * 
	 * @param widget 控件widget
	 * @param initSize 尺寸大小
	 */
	public XWScaleLayout(WScaleLayout widget, Dimension initSize) {
		super(widget, initSize);
	}
	
	/**
	 * 初始化容器对应的布局 由于是只装一个需要保持原样高度的控件，布局设为absolute
	 */
	@Override
	protected void initLayoutManager() {
		this.setLayout(new FRScaleLayout());
	}

	/**
	 * 容器的渲染器
	 */
	@Override
	public LayoutAdapter getLayoutAdapter() {
		return new FRScaleLayoutAdapter(this);
	}

	/**
	 * 返回容器对应的wlayout
	 * 
	 * @return 同上
	 */
	@Override
	public WScaleLayout toData() {
		return (WScaleLayout) data;
	}

	/**
	 * 返回默认组件name
	 * 
	 * @return 容器名
	 */
	@Override
	public String createDefaultName() {
		return "scalePanel";
	}

	/**
	 * 将WLayout转换为XLayoutContainer
	 */
	@Override
	public void convert() {
		isRefreshing = true;
		WScaleLayout layout = this.toData();
		this.removeAll();
		for (int i = 0, num = layout.getWidgetCount(); i < num; i++) {
			BoundsWidget bw = (BoundsWidget) layout.getWidget(i);
			if (bw != null) {
				Rectangle bounds = bw.getBounds();
				XWidgetCreator comp = (XWidgetCreator) XCreatorUtils
						.createXCreator(bw.getWidget());
				this.add(comp, bw.getWidget().getWidgetName());
				comp.setBounds(bounds);
			}
		}
		isRefreshing = false;
	}

	/**
	 * 组件增加
	 * 
	 * @param e
	 *            容器事件
	 */
	@Override
	public void componentAdded(ContainerEvent e) {
		if (isRefreshing) {
			return;
		}
		WScaleLayout layout = this.toData();
		layout.removeAll();
		for (int i = 0, num = this.getComponentCount(); i < num; i++) {
			XWidgetCreator creator = (XWidgetCreator) this.getComponent(i);
			layout.addWidget(new BoundsWidget(creator.toData(), creator
					.getBounds()));
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
		BoundsWidget bw = new BoundsWidget(wgt, xwc.getBounds());
		WScaleLayout wlayout = this.toData();
		wlayout.removeWidget(bw);
	}

	/**
	 * 返回此容器的对应组件
	 * @return 组件
	 */
	public XCreator getEditingChildCreator() {
		return getXCreator(INDEX);
	}
	
	/**
	 * 更新组件的backupBound,scale和title容器也要同步更新子组件的
	 * @param minHeight 最小高度
	 */
	public void updateChildBound(int minHeight) {
		XCreator child = getXCreator(INDEX);
		child.setSize(getWidth(), minHeight);
	}


	/**
	 * data属性改变触发其他操作
	 *
	 */
	public void firePropertyChange(){
		XCreator child = getXCreator(INDEX);
		child.firePropertyChange();
	}

	/**
	 * 获取被包装的XCreator扩展的属性tab
	 * @return
	 */
	@Override
	public WidgetPropertyUIProvider[] getWidgetPropertyUIProviders() {
		if (this.getXCreatorCount() > 0) {
			return this.getXCreator(0).getWidgetPropertyUIProviders();
		}
		return super.getWidgetPropertyUIProviders();
	}

	@Override
	public boolean supportMobileStyle() {
		return true;
	}
}