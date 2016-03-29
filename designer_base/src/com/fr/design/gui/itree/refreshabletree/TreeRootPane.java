package com.fr.design.gui.itree.refreshabletree;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import com.fr.design.gui.ilable.UILabel;
import javax.swing.JPanel;

import com.fr.data.impl.TreeAttr;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.dialog.BasicPane;
import com.fr.general.Inter;

public class TreeRootPane extends BasicPane {
	
	// 是否支持多选(checkBoxTree)
	//private JCheckBox multipleSelection;
	private UIComboBox checkTypeComboBox;
	
	// richer:加载的方式，支持异步加载和完全加载
	private UIComboBox loadTypeComboBox;
	
	private UICheckBox layerTypeCheckBox;

    private UICheckBox returnFullPathCheckBox ;
	
	public TreeRootPane() {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		JPanel checkTypePane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
		checkTypePane.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
		checkTypePane.add(new UILabel(Inter.getLocText("Tree-Mutiple_Selection_Or_Not") + ":"));
		checkTypeComboBox = new UIComboBox(new String[] {Inter.getLocText("Yes"), Inter.getLocText("No")});
		checkTypePane.add(checkTypeComboBox);
		this.add(checkTypePane);

		JPanel loadTypePane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
		loadTypePane.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
		loadTypePane.add(new UILabel(Inter.getLocText("Widget-Load_Type") + ":"));
		loadTypeComboBox = new UIComboBox(new String[]{Inter.getLocText("Widget-Load_By_Async"), Inter.getLocText("Widget-Load_By_Complete")});
		loadTypePane.add(loadTypeComboBox);
		this.add(loadTypePane);

		JPanel leafSelectPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
		leafSelectPane.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
		leafSelectPane.add(layerTypeCheckBox = new UICheckBox(Inter.getLocText("Tree-Select_Leaf_Only")));
		UILabel tips = new UILabel(Inter.getLocText("Tree-Select_Leaf_Only_Tips"));
		tips.setForeground(new Color(147, 178, 233));
		leafSelectPane.add(tips);
		this.add(leafSelectPane);

        JPanel returnFullPathPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        returnFullPathPane.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        returnFullPathPane.add(returnFullPathCheckBox = new UICheckBox(Inter.getLocText("Tree-Return_Full_Path")));
        this.add(returnFullPathPane);

	}
	
	@Override
	protected String title4PopupWindow() {
		return "tree";
	}
	
	public void populate(TreeAttr treeAttr) {
		checkTypeComboBox.setSelectedIndex(treeAttr.isMultipleSelection() ? 0 : 1);
		loadTypeComboBox.setSelectedIndex(treeAttr.isAjax() ? 0 : 1);
		layerTypeCheckBox.setSelected(treeAttr.isSelectLeafOnly());
        returnFullPathCheckBox.setSelected(treeAttr.isReturnFullPath());
	}

	public TreeAttr update() {
		TreeAttr treeAttr = new TreeAttr();
		treeAttr.setMultipleSelection(checkTypeComboBox.getSelectedIndex() == 0);
		treeAttr.setAjax(loadTypeComboBox.getSelectedIndex() == 0);
		treeAttr.setSelectLeafOnly(layerTypeCheckBox.isSelected());
        treeAttr.setReturnFullPath(returnFullPathCheckBox.isSelected());

		return treeAttr;
	}
}