package com.fr.design.gui.controlpane;

import com.fr.design.beans.BasicBeanPane;
import com.fr.invoke.Reflect;

/**
 * 生成带参数的BasicBeanPane
 * 
 * @author zhou
 * @since 2012-4-5上午9:29:20
 */
public abstract class ObjectJControlPane extends JListControlPane {
	private Object object;

	public ObjectJControlPane() {
		this(null);
	}

	public ObjectJControlPane(Object object) {
		super();
		this.object = object;
	}

	@Override
	protected BasicBeanPane createPaneByCreators(NameableCreator creator) {
		try {
			return Reflect.on(creator.getUpdatePane()).create(object).get();
		} catch (Exception e) {
			return super.createPaneByCreators(creator);
		}
	}
}