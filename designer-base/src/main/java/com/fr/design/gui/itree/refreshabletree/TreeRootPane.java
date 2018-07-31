package com.fr.design.gui.itree.refreshabletree;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import com.fr.design.gui.ilable.UILabel;
import javax.swing.JPanel;

import com.fr.data.impl.TreeAttr;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.dialog.BasicPane;


public class TreeRootPane extends BasicPane {
	
	// 是否支持多选(checkBoxTree)
	//private JCheckBox multipleSelection;
	private UICheckBox checkTypeCheckBox;
	
	// richer:加载的方式，支持异步加载和完全加载
	private UICheckBox loadTypeCheckBox;
	
	private UICheckBox layerTypeCheckBox;

    private UICheckBox returnFullPathCheckBox ;
	
	public TreeRootPane() {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		JPanel checkTypePane = FRGUIPaneFactory.createBoxFlowInnerContainer_S_Pane_First0();
		checkTypePane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		checkTypeCheckBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Tree-Mutiple_Selection_Or_Not"));
		checkTypeCheckBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		checkTypePane.add(checkTypeCheckBox);
		this.add(checkTypePane);

		JPanel loadTypePane = FRGUIPaneFactory.createBoxFlowInnerContainer_S_Pane_First0();
		checkTypePane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		loadTypeCheckBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Widget-Load_By_Async"));
		loadTypeCheckBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		loadTypePane.add(loadTypeCheckBox);
		this.add(loadTypePane);

		JPanel leafSelectPane = FRGUIPaneFactory.createBoxFlowInnerContainer_S_Pane_First0();
		checkTypePane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		leafSelectPane.add(layerTypeCheckBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Tree-Select_Leaf_Only")));
		layerTypeCheckBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		this.add(leafSelectPane);

        JPanel returnFullPathPane = FRGUIPaneFactory.createBoxFlowInnerContainer_S_Pane_First0();
		checkTypePane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		returnFullPathPane.add(returnFullPathCheckBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Tree-Return_Full_Path")));
		returnFullPathCheckBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		this.add(returnFullPathPane);

	}
	
	@Override
	protected String title4PopupWindow() {
		return "tree";
	}
	
	public void populate(TreeAttr treeAttr) {
		checkTypeCheckBox.setSelected(treeAttr.isMultipleSelection());
		loadTypeCheckBox.setSelected(treeAttr.isAjax());
		layerTypeCheckBox.setSelected(treeAttr.isSelectLeafOnly());
        returnFullPathCheckBox.setSelected(treeAttr.isReturnFullPath());
	}

	public TreeAttr update() {
		TreeAttr treeAttr = new TreeAttr();
		treeAttr.setMultipleSelection(checkTypeCheckBox.isSelected());
		treeAttr.setAjax(loadTypeCheckBox.isSelected());
		treeAttr.setSelectLeafOnly(layerTypeCheckBox.isSelected());
        treeAttr.setReturnFullPath(returnFullPathCheckBox.isSelected());

		return treeAttr;
	}
}