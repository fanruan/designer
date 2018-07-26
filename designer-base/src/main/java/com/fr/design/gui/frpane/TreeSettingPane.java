package com.fr.design.gui.frpane;

import com.fr.data.impl.TableDataDictionary;
import com.fr.data.impl.TreeAttr;
import com.fr.data.impl.TreeNodeAttr;
import com.fr.data.impl.TreeNodeWrapper;
import com.fr.design.data.DataCreatorUI;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.controlpane.NameObjectCreator;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.design.gui.frpane.tree.layer.config.LayerDataControlPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itree.refreshabletree.TreeDataCardPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.form.ui.TreeComboBoxEditor;
import com.fr.form.ui.TreeEditor;
import com.fr.form.ui.tree.LayerConfig;

import com.fr.general.NameObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;

public class TreeSettingPane extends BasicPane implements DataCreatorUI {
	private JTreeControlPane controlPane;

	private JTreeAutoBuildPane autoBuildPane;

	/**
	 * 新的分层构建方式
	 */
	private LayerDataControlPane layerDataControlPane;

	private UIComboBox buildBox;

	/**
	 *
	 */
	private static final long serialVersionUID = 1762889323082827111L;

	private String[] buildWay = new String[]{com.fr.design.i18n.Toolkit.i18nText("FR-Designer_DataTable-Build"),
		com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Auto-Build"), com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Layer-Build")};

	public TreeSettingPane(boolean isEditor) {
		this.initComponents(isEditor);
	}

	private void initComponents(boolean isEditor) {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		JPanel buildWayPanel= FRGUIPaneFactory.createMediumHGapFlowInnerContainer_M_Pane();
		buildWayPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		UILabel buildWayLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Build-Way") + " ：");
		buildWayPanel.add(buildWayLabel);
		buildBox = new UIComboBox(buildWay);
		buildBox.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				cardChanged(buildBox.getSelectedIndex());
			}
		});
		buildWayPanel.add(buildBox);

		controlPane = new JTreeControlPane(new NameableCreator[] { treeNode },
			new TreeDataCardPane(), isEditor);
		autoBuildPane = new JTreeAutoBuildPane();
		layerDataControlPane = new LayerDataControlPane();
		this.add(buildWayPanel, BorderLayout.NORTH);
		cardChanged(0);
	}

	private void cardChanged(int index) {

		this.remove(controlPane);
		this.remove(autoBuildPane);
		this.remove(layerDataControlPane);
		switch (index) {
			case 0:
				this.add(layerDataControlPane);
				break;
			case 1:
				this.add(autoBuildPane);
				break;
			case 2:
				this.add(controlPane);

				break;
			default:
				break;
		}
		validate();
		repaint();
		revalidate();
	}

	@Override
	protected String title4PopupWindow() {
		return com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Create_Tree");
	}

	@Override
	public JComponent toSwingComponent() {
		return this;
	}

	NameableCreator treeNode = new NameObjectCreator(
		com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Gradation"),
		"/com/fr/design/images/data/source/jdbcTableData.png",
		TreeNodeAttr.class);

	/**
	 *
	 * @param treeEditor
	 */
	public void populate(TreeEditor treeEditor) {
		boolean isAutoBuild = treeEditor.isAutoBuild();
		TreeAttr treeAttr = treeEditor.getTreeAttr();
		if (treeAttr != null) {
			NameObject no = new NameObject("name", treeEditor);
			controlPane.populate(no);
		}
		if (isAutoBuild)  {
			buildBox.setSelectedIndex(1);
			TableDataDictionary dictionary = treeEditor.getDictionary();
			autoBuildPane.populate(dictionary);
		} else if (treeEditor.isLayerBuild()) {
			buildBox.setSelectedIndex(0);
			java.util.List<LayerConfig> layerConfigList = treeEditor.getLayerConfigs();
			LayerConfig[] layerConfigs = new LayerConfig[layerConfigList.size()];
			int i = 0;
			for (LayerConfig layerConfig : layerConfigList) {
				layerConfigs[i++] = layerConfig;
			}
			this.layerDataControlPane.populate(new NameObject("Tree Layer Data", layerConfigs));
		} else {
			buildBox.setSelectedIndex(2);
		}
	}

	/**
	 * 视图树的update
	 * @return
	 */
	public TreeEditor updateTreeEditor() {
//		NameObject no = this.controlPane.update();
//		if (no != null) {
//			return ((TreeEditor) no.getObject());
//		}
//
//		return null;
		TreeEditor te = new TreeEditor();
		if (buildBox.getSelectedIndex() == 1) {
			TableDataDictionary dictionary = this.autoBuildPane.update();
			te.setAutoBuild(true);
			te.setLayerBuild(false);
			te.setDictionary(dictionary);
			te.setNodeOrDict(dictionary);
		} else if (buildBox.getSelectedIndex() == 2) {
			te.setAutoBuild(false);
			te.setLayerBuild(false);
			NameObject no = this.controlPane.update();
			if (no != null) {
				TreeEditor editor = (TreeEditor) no.getObject();
				te.setAllowBlank(editor.isAllowBlank());
				te.setEnabled(editor.isEnabled());
				te.setDirectEdit(editor.isDirectEdit());
				te.setErrorMessage(editor.getErrorMessage());
				te.setWidgetName(editor.getWidgetName());
				te.setVisible(editor.isVisible());
				te.setWaterMark(editor.getWaterMark());
				te.setRemoveRepeat(editor.isRemoveRepeat());
				te.setTreeAttr(editor.getTreeAttr());
				te.setTreeNodeAttr(editor.getTreeNodeAttr());
				te.setNodeOrDict(editor.getTreeNodeAttr());
				te.setPerformanceFirst(editor.isPerformanceFirst());
			}
		} else {
			LayerConfig[] configs = (LayerConfig[]) layerDataControlPane.update().getObject();
			te.setAutoBuild(false);
			te.setLayerBuild(true);
			te.setLayerConfigs(Arrays.asList(configs));
		}
		return te;
	}

	/**
	 * 树节点属性的update
	 * @return
	 */
	public Object updateTreeNodeAttrs() {

		if (buildBox.getSelectedIndex() == 2) {
			NameObject no = controlPane.update();
			if (no != null) {
				return no.getObject();
			}
		} else if (buildBox.getSelectedIndex() == 0) {
			return layerDataControlPane.update();
		} else {
			return autoBuildPane.update();
		}
		return null;
	}

	/**
	 * 下拉树的update
	 * @return
	 */
	public TreeComboBoxEditor updateTreeComboBox() {
		TreeComboBoxEditor tcb = new TreeComboBoxEditor();
		if (buildBox.getSelectedIndex() == 1) {
			TableDataDictionary dictionary = this.autoBuildPane.update();
			tcb.setAutoBuild(true);
            tcb.setLayerBuild(false);
            tcb.setDictionary(dictionary);
            tcb.setNodeOrDict(dictionary);
        } else if (buildBox.getSelectedIndex() == 2) {
            tcb.setAutoBuild(false);
            tcb.setLayerBuild(false);
            NameObject no = this.controlPane.update();
            if (no != null) {
                if (no.getObject() instanceof TreeComboBoxEditor) {
                    return (TreeComboBoxEditor) no.getObject();
                }

				TreeEditor editor = (TreeEditor) no.getObject();
				tcb.setAllowBlank(editor.isAllowBlank());
				tcb.setEnabled(editor.isEnabled());
				tcb.setDirectEdit(editor.isDirectEdit());
				tcb.setErrorMessage(editor.getErrorMessage());
				tcb.setWidgetName(editor.getWidgetName());
				tcb.setVisible(editor.isVisible());
				tcb.setWaterMark(editor.getWaterMark());
				tcb.setRemoveRepeat(editor.isRemoveRepeat());
				tcb.setTreeAttr(editor.getTreeAttr());
				tcb.setTreeNodeAttr(editor.getTreeNodeAttr());
				tcb.setNodeOrDict(editor.getTreeNodeAttr());
				tcb.setPerformanceFirst(editor.isPerformanceFirst());
			}
		}else {
            LayerConfig[] configs = (LayerConfig[]) layerDataControlPane.update().getObject();
            tcb.setAutoBuild(false);
            tcb.setLayerBuild(true);
            tcb.setLayerConfigs(Arrays.asList(configs));
        }
		return tcb;
	}

	/**
	 *
	 * @param nodeOrDict
	 */
	public void populate(Object nodeOrDict) {
		if(nodeOrDict instanceof TreeNodeAttr[] || nodeOrDict instanceof TreeNodeWrapper) {
			buildBox.setSelectedIndex(2);
			NameObject no = new NameObject("name", nodeOrDict);
			controlPane.populate(no);
		} else if(nodeOrDict instanceof TableDataDictionary) {
			buildBox.setSelectedIndex(1);
			autoBuildPane.populate((TableDataDictionary)nodeOrDict);
		} else if (nodeOrDict instanceof NameObject) {
			buildBox.setSelectedIndex(0);
			layerDataControlPane.populate((NameObject) nodeOrDict);
		}
	}
}