package com.fr.design.widget.ui;

import java.awt.*;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.fr.data.Dictionary;
import com.fr.design.data.DataCreatorUI;
import com.fr.design.designer.IntervalConstants;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itree.refreshabletree.TreeRootPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.widget.accessibles.AccessibleTreeModelEditor;
import com.fr.form.ui.TreeComboBoxEditor;
import com.fr.form.ui.TreeEditor;
import com.fr.general.Inter;

public class TreeComboBoxEditorDefinePane extends CustomWritableRepeatEditorPane<TreeEditor> {
	protected AccessibleTreeModelEditor treeSettingPane;
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
		return content;
	}

	@Override
	protected JPanel setFirstContentPane() {
		treeSettingPane = new AccessibleTreeModelEditor();
		JPanel jPanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
		JPanel north = TableLayoutHelper.createGapTableLayoutPane(new Component[][]{
				new Component[]{new UILabel(Inter.getLocText("FR-Designer_Create_Tree")), treeSettingPane}}, TableLayoutHelper.FILL_LASTCOLUMN, IntervalConstants.INTERVAL_W2, IntervalConstants.INTERVAL_L1);
		north.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
		JPanel center = super.setFirstContentPane();
		jPanel.add(north, BorderLayout.NORTH);
		jPanel.add(center, BorderLayout.CENTER);
		return jPanel;
	}


	
	@Override
	protected String title4PopupWindow() {
		return "treecombobox";
	}

	@Override
	protected void populateSubCustomWritableRepeatEditorBean(TreeEditor e) {
		treeSettingPane.setValue(e.getNodeOrDict());
		treeRootPane.populate(e.getTreeAttr());
	}

	@Override
	protected TreeComboBoxEditor updateSubCustomWritableRepeatEditorBean() {
		TreeComboBoxEditor editor = new TreeComboBoxEditor();
		editor.setNodeOrDict(treeSettingPane.getValue());
		editor.setTreeAttr(treeRootPane.update());
		return editor;
	}

    @Override
    public DataCreatorUI dataUI() {
        return null;
    }
}