package com.fr.design.widget.ui;

import com.fr.design.data.DataCreatorUI;
import com.fr.design.gui.frpane.TreeSettingPane;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.itree.refreshabletree.TreeRootPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.form.ui.TreeEditor;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;


/*
 * richer:tree editor
 */
public class TreeEditorDefinePane extends FieldEditorDefinePane<TreeEditor> {
	protected TreeSettingPane treeSettingPane;
	protected TreeRootPane treeRootPane;

	private UICheckBox removeRepeatCheckBox;

	public TreeEditorDefinePane(){
		this.initComponents();			
	}

	@Override
	protected void populateSubFieldEditorBean(TreeEditor e) {
		this.treeSettingPane.populate(e);
		treeRootPane.populate(e.getTreeAttr());
		if (this.removeRepeatCheckBox != null) {
			this.removeRepeatCheckBox.setSelected(e.isRemoveRepeat());
		}
	}

	@Override
	protected TreeEditor updateSubFieldEditorBean() {
		TreeEditor editor = treeSettingPane.updateTreeEditor();
		editor.setTreeAttr(treeRootPane.update());
		if (this.removeRepeatCheckBox != null) {
			editor.setRemoveRepeat(this.removeRepeatCheckBox.isSelected());
		}
		return editor;
	}

	@Override
	protected JPanel setFirstContentPane() {
		return this.setSecondContentPane();
	}

	protected JPanel setSecondContentPane() {
		JPanel advancedPane = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("FR-Designer_Advanced"));
		JPanel contentPane = FRGUIPaneFactory.createBorderLayout_L_Pane();
		advancedPane.add(contentPane);
		contentPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		JPanel contenter = FRGUIPaneFactory.createMediumHGapFlowInnerContainer_M_Pane();
		contentPane.add(contenter,BorderLayout.NORTH);
		removeRepeatCheckBox = new UICheckBox(Inter.getLocText("Form-Remove_Repeat_Data"), false);
		contenter.add(removeRepeatCheckBox);
		JPanel otherContentPane = this.setThirdContentPane();
		if (otherContentPane != null) {
			contentPane.add(otherContentPane,BorderLayout.CENTER);
		}
		return advancedPane;
	}

	protected JPanel setThirdContentPane() {
		JPanel content = FRGUIPaneFactory.createBorderLayout_L_Pane();
		content.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		treeRootPane = new TreeRootPane();
		content.add(treeRootPane, BorderLayout.NORTH);
		treeSettingPane = new TreeSettingPane(true);
		return content;
	}
	
	@Override
	protected String title4PopupWindow() {
		return "tree";
	}

    @Override
    public DataCreatorUI dataUI() {
        return treeSettingPane;
    }
}