package com.fr.design.present;

import com.fr.base.Style;
import com.fr.config.ServerPreferenceConfig;
import com.fr.config.StyleMap;
import com.fr.design.dialog.FineJOptionPane;
import com.fr.design.gui.controlpane.AbstractNameableCreator;
import com.fr.design.gui.controlpane.JListControlPane;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.design.gui.controlpane.UnrepeatedNameHelper;
import com.fr.design.gui.ilist.ListModelElement;
import com.fr.design.gui.ilist.ModNameActionListener;
import com.fr.design.i18n.Toolkit;
import com.fr.design.style.StylePane;

import com.fr.general.ComparatorUtils;
import com.fr.general.NameObject;
import com.fr.stable.Nameable;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class StyleArrayPane extends JListControlPane {

	private boolean namePermitted = true;

	/**
	 * Constructor.
	 */
	public StyleArrayPane() {
		super();
		this.addModNameActionListener(new ModNameActionListener() {
			public void nameModed(int index, String oldName, String newName) {
				if (ComparatorUtils.equals(oldName, newName)) {
					return;
				}
				namePermitted = true;
				String[] allNames = nameableList.getAllNames();
				allNames[index] = StringUtils.EMPTY;
				if (StringUtils.isEmpty(newName)) {
					showTipDialogAndReset(Toolkit.i18nText("Fine-Design_Basic_Predefined_Style_Empty_Name"), index);
					return;
				}
				if (isNameRepeated(new List[] {Arrays.asList(allNames)}, newName)) {
					showTipDialogAndReset(Toolkit.i18nText("Fine-Design_Basic_Predefined_Style_Duplicate_Name", newName), index);
					return;
				}
				populateSelectedValue();
			}
		});
	}


	private void showTipDialogAndReset(String content, int index) {
		nameableList.stopEditing();

		FineJOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(StyleArrayPane.this),
										  content,
										  Toolkit.i18nText("Fine-Design_Basic_Alert"),
										  JOptionPane.WARNING_MESSAGE);
		setIllegalIndex(index);
		namePermitted = false;
	}

	/**
	 * 创建样式设置组件
	 *
	 * @return 样式设置组件
	 */
	public NameableCreator[] createNameableCreators() {
		return new NameableCreator[] { new AbstractNameableCreator(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Engine_Style_Name"), Style.class, StylePane.class) {
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
		return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_ServerM_Predefined_Styles");
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
	public void update(final ServerPreferenceConfig configManager) {
		//configManager.clearAllStyle();

		// Nameable[]居然不能强转成Parameter[],一定要这么写...
		Nameable[] nameables = this.update();
		final StyleMap styleMap = new StyleMap();
		for (int i = 0; i < nameables.length; i++) {
			styleMap.put(((NameObject)nameables[i]).getName(), (Style)((NameObject)nameables[i]).getObject());
		}

	    configManager.setStyleMap(styleMap);
	}

	public boolean isNamePermitted() {
		return namePermitted;
	}
}
