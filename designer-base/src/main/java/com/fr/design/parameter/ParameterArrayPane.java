package com.fr.design.parameter;

import com.fr.base.Parameter;
import com.fr.base.ParameterConfig;
import com.fr.design.dialog.FineJOptionPane;
import com.fr.design.gui.controlpane.JListControlPane;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.design.gui.controlpane.NameableSelfCreator;
import com.fr.design.gui.controlpane.UnrepeatedNameHelper;
import com.fr.design.gui.ilist.ModNameActionListener;
import com.fr.general.ComparatorUtils;
import com.fr.stable.Nameable;
import com.fr.stable.StringUtils;
import com.fr.stable.core.PropertyChangeAdapter;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;

public class ParameterArrayPane extends JListControlPane {
	/**
	 * Constructor.
	 */
	public ParameterArrayPane() {
		super();
		this.addModNameActionListener(new ModNameActionListener() {
			public void nameModed(int index, String oldName, String newName) {
				populateSelectedValue();
			}

		});
		this.addEditingListener(new PropertyChangeAdapter() {
			public void propertyChange() {
				Parameter[] parameters = ParameterConfig.getInstance().getGlobalParameters();
				String[] allListNames = nameableList.getAllNames();
				allListNames[nameableList.getSelectedIndex()] = StringUtils.EMPTY;
				String tempName = getEditingName();
				if (StringUtils.isEmpty(tempName)) {
					nameableList.stopEditing();
					FineJOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(ParameterArrayPane.this), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Empty_Parameter_Name"));
					setIllegalIndex(editingIndex);
					return;
				}
				if (!ComparatorUtils.equals(tempName, selectedName)
						&& isNameRepeated(new List[]{Arrays.asList(parameters), Arrays.asList(allListNames)}, tempName)) {
					nameableList.stopEditing();
					FineJOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(ParameterArrayPane.this),
							com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Duplicate_Parameter_Name"));
					setIllegalIndex(editingIndex);
				}
			}
		});
	}


	@Override
	protected String title4PopupWindow() {
		return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Engine_Schedule_Template_Parameter");
	}

	/**
	 * 创建模板参数设置组件
	 *
	 * @return 模板参数设置组件
	 */
	public NameableCreator[] createNameableCreators() {
		return new NameableCreator[]{
				new NameableSelfCreator(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Engine_Parameter_Name"), Parameter.class, ParameterPane.class) {
					public Parameter createNameable(UnrepeatedNameHelper helper) {
						// 返回参数设置面板.
						return new Parameter(helper.createUnrepeatedName("p"));
					}

					@Override
					public String createTooltip() {
						return null;
					}
				}
		};
	}


	/**
	 * 更新参数
     *
     * @return 更新后的参数
	 */
	public Parameter[] updateParameters() {
		// Nameable[]居然不能强转成Parameter[],一定要这么写...
		Nameable[] res = this.update();
		Parameter[] res_array = new Parameter[res.length];
		java.util.Arrays.asList(res).toArray(res_array);

		return res_array;
	}
}
