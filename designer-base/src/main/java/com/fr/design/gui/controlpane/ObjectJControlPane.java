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
	public BasicBeanPane createPaneByCreators(NameableCreator creator) {
		try {
			Class target = creator.getUpdatePane();
			Reflect reflect = Reflect.on(target);
			// 判断是否存在对应构造函数
			if (reflect.matchConstructor(object)) {
				return reflect.create(object).get();
			}

			return reflect.create().get();
		} catch (Exception e) {
			return super.createPaneByCreators(creator);
		}
	}
}