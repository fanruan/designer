package com.fr.design.widget.ui;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.fr.design.data.DataCreatorUI;
import com.fr.design.gui.itree.refreshabletree.TreeRootPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.gui.frpane.TreeSettingPane;
import com.fr.form.ui.TreeComboBoxEditor;
import com.fr.form.ui.TreeEditor;

public class TreeComboBoxEditorDefinePane extends CustomWritableRepeatEditorPane<TreeEditor> {
	protected TreeSettingPane treeSettingPane;
	protected TreeRootPane treeRootPane;

	public TreeComboBoxEditorDefinePane() {
		this.initComponents();
	}


	@Override
	protected JPanel setForthContentPane() {
		JPanel content = FRGUIPaneFactory.createBorderLayout_L_Pane();
		content.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		treeRootPane = new TreeRootPane();
		content.add(treeRootPane, BorderLayout.NORTH);
		treeSettingPane = new TreeSettingPane(true);
		return content;
	}
	
	@Override
	protected String title4PopupWindow() {
		return "treecombobox";
	}

	@Override
	protected void populateSubCustomWritableRepeatEditorBean(TreeEditor e) {
		treeSettingPane.populate(e);
		treeRootPane.populate(e.getTreeAttr());
	}

	@Override
	protected TreeComboBoxEditor updateSubCustomWritableRepeatEditorBean() {
		TreeComboBoxEditor editor = treeSettingPane.updateTreeComboBox();
		editor.setTreeAttr(treeRootPane.update());
		return editor;
	}

    @Override
    public DataCreatorUI dataUI() {
        return treeSettingPane;
    }
}