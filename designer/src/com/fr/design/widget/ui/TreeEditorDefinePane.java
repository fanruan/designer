package com.fr.design.widget.ui;

import com.fr.data.Dictionary;
import com.fr.design.data.DataCreatorUI;
import com.fr.design.designer.IntervalConstants;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itree.refreshabletree.TreeRootPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.widget.accessibles.AccessibleTreeModelEditor;
import com.fr.form.ui.TreeEditor;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;


/*
 * richer:tree editor
 */
public class TreeEditorDefinePane extends FieldEditorDefinePane<TreeEditor> {
	protected TreeRootPane treeRootPane;
	private AccessibleTreeModelEditor accessibleTreeModelEditor;

	private UICheckBox removeRepeatCheckBox;

	public TreeEditorDefinePane(){
		this.initComponents();			
	}

	@Override
	protected void populateSubFieldEditorBean(TreeEditor e) {
		this.accessibleTreeModelEditor.setValue(e.getNodeOrDict());
		treeRootPane.populate(e.getTreeAttr());
		if (this.removeRepeatCheckBox != null) {
			this.removeRepeatCheckBox.setSelected(e.isRemoveRepeat());
		}
	}

	@Override
	protected TreeEditor updateSubFieldEditorBean() {
		TreeEditor editor = new TreeEditor();
		editor.setNodeOrDict(accessibleTreeModelEditor.getValue());
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
		accessibleTreeModelEditor = new AccessibleTreeModelEditor();
		JPanel createTree = TableLayoutHelper.createGapTableLayoutPane(new Component[][]{
				new Component[]{new UILabel(Inter.getLocText("FR-Designer_Create_Tree")), accessibleTreeModelEditor}}, TableLayoutHelper.FILL_LASTCOLUMN, IntervalConstants.INTERVAL_W2, IntervalConstants.INTERVAL_L1);
		createTree.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
		JPanel contentPane = FRGUIPaneFactory.createBorderLayout_L_Pane();
		JPanel contenter = FRGUIPaneFactory.createBorderLayout_S_Pane();

		contentPane.add(contenter,BorderLayout.NORTH);
		removeRepeatCheckBox = new UICheckBox(Inter.getLocText("Form-Remove_Repeat_Data"), false);
		removeRepeatCheckBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		contenter.add(createTree, BorderLayout.NORTH);
		contenter.add(removeRepeatCheckBox, BorderLayout.CENTER);
		JPanel otherContentPane = this.setThirdContentPane();
		if (otherContentPane != null) {
			contentPane.add(otherContentPane,BorderLayout.CENTER);
		}
		return contentPane;
	}

	protected JPanel setThirdContentPane() {
		JPanel content = FRGUIPaneFactory.createBorderLayout_L_Pane();
		content.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		treeRootPane = new TreeRootPane();
		content.add(treeRootPane, BorderLayout.NORTH);
		return content;
	}
	
	@Override
	protected String title4PopupWindow() {
		return "tree";
	}

    @Override
    public DataCreatorUI dataUI() {
        return null;
    }
}