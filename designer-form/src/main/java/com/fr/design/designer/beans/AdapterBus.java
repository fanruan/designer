package com.fr.design.designer.beans;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.RootPaneContainer;

import com.fr.design.mainframe.FormDesigner;
import com.fr.design.designer.beans.adapters.component.CompositeComponentAdapter;
import com.fr.design.designer.beans.painters.NullLayoutPainter;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XCreatorUtils;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.utils.ComponentUtils;

/**
 * 适配器中枢，为组件和组件适配器、布局和布局适配器。
 */
public class AdapterBus {

	public static final String CLIENT_PROPERTIES = "component.adapter";

	public static JComponent getJComponent(Component component) {
		JComponent jcomponent;
		if (component instanceof JComponent) {
			jcomponent = (JComponent) component;
		} else if (component instanceof RootPaneContainer) {
			jcomponent = (JComponent) ((RootPaneContainer) component).getContentPane();
		} else {
			return null;
		}
		return jcomponent;
	}

	/**
	 * 获取组件类型是componentClass对应的组件适配器，如果初始映射表中没有该适配器，
	 * 则继续查找其父类对应的适配器，直至查找到Component类为止，如果还是没有查找到，
	 * 则使用缺省的组件适配器：DefaultComponentAdapter
	 * 
	 * @return 该组件类所对应的组件适配器对象
	 */
	public static ComponentAdapter getComponentAdapter(FormDesigner designer, JComponent creator) {
		JComponent jcomponent = getJComponent(creator);
		if (null == jcomponent) {
			return null;
		}
		ComponentAdapter adapter = (ComponentAdapter) jcomponent.getClientProperty("component.adapter");
		if (adapter == null) {
			adapter = new CompositeComponentAdapter(designer, creator);
			jcomponent.putClientProperty(CLIENT_PROPERTIES, adapter);
		}
		return adapter;
	}

	public static XCreator getFirstInvisibleParent(XCreator comp) {
		XCreator parent = comp;
	
		while ((parent != null) && parent.isVisible()) {
			parent = XCreatorUtils.getParentXLayoutContainer(parent);
		}
	
		return parent;
	}

	public static LayoutAdapter searchLayoutAdapter(FormDesigner designer, XCreator comp) {
		if (ComponentUtils.isRootComponent(comp)) {
			return null;
		}
		return XCreatorUtils.getParentXLayoutContainer(comp).getLayoutAdapter();
	}

	public static HoverPainter getContainerPainter(FormDesigner designer, XLayoutContainer container) {
		// 容器组件的适配器
		LayoutAdapter containerAdapter = container.getLayoutAdapter();
		HoverPainter painter = containerAdapter.getPainter();
		if (painter != null) {
			return painter;
		}
		return new NullLayoutPainter(container);
	}
}