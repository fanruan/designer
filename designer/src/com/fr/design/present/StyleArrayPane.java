package com.fr.design.present;

import com.fr.base.Style;
import com.fr.config.ServerPreferenceConfig;
import com.fr.design.gui.controlpane.AbstractNameableCreator;
import com.fr.design.gui.controlpane.JListControlPane;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.design.gui.controlpane.UnrepeatedNameHelper;
import com.fr.design.gui.ilist.ListModelElement;
import com.fr.design.gui.ilist.ModNameActionListener;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.style.StylePane;
import com.fr.general.Inter;
import com.fr.general.NameObject;
import com.fr.stable.Nameable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StyleArrayPane extends JListControlPane {
	
	/**
	 * Constructor.
	 */
	public StyleArrayPane() {
		super();
		this.addModNameActionListener(new ModNameActionListener() {
			public void nameModed(int index, String oldName, String newName) {
				populateSelectedValue();
			}
		});
	}

	/**
	 * 创建样式设置组件
	 *
	 * @return 样式设置组件
	 */
	public NameableCreator[] createNameableCreators() {
		return new NameableCreator[] { new AbstractNameableCreator(Inter.getLocText("FR-Engine_Style_Name"), Style.class, StylePane.class) {
			public NameObject createNameable(UnrepeatedNameHelper helper) {
				// 返回参数设置面板.
				return new NameObject(helper.createUnrepeatedName("H"), Style.getInstance());
			}

			@Override
			public void saveUpdatedBean(ListModelElement wrapper, Object bean) {
				((NameObject)wrapper.wrapper).setObject(bean);
			}

			@Override
			public String createTooltip() {
				return null;
			}
		} };
	}

	@Override
	protected String title4PopupWindow() {
		return Inter.getLocText("ServerM-Predefined_Styles");
	}

	/**
	 * Populate
	 */
	public void populate(ServerPreferenceConfig configManager) {
		if (configManager == null) {
			return;
		}

		List nameStyleList = new ArrayList();

		Iterator styleNameIterator = configManager.getStyleNameIterator();
		while (styleNameIterator.hasNext()) {
			String name = (String)styleNameIterator.next();
			Style tmpStyle = configManager.getStyle(name);

			if (tmpStyle != null) {
				nameStyleList.add(new NameObject(name, tmpStyle));
			}
		}

		NameObject[] nameObjects = new NameObject[nameStyleList.size()];
		nameStyleList.toArray(nameObjects);

		populate(nameObjects);
	}

	/**
	 * Update.
	 */
	public void update(ServerPreferenceConfig configManager) {
		configManager.clearAllStyle();

		// Nameable[]居然不能强转成Parameter[],一定要这么写...
		Nameable[] nameables = this.update();

		for (int i = 0; i < nameables.length; i++) {
			configManager.putStyle(((NameObject)nameables[i]).getName(), (Style)((NameObject)nameables[i]).getObject());
		}
		DesignerContext.getDesignerBean("predefinedStyle").refreshBeanElement();
	}
	

}