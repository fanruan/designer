package com.fr.design.widget.ui.designer;

import com.fr.design.data.DataCreatorUI;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.gui.frpane.TreeSettingPane;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.itree.refreshabletree.TreeRootPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.form.ui.TreeComboBoxEditor;
import com.fr.form.ui.TreeEditor;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;

public class TreeComboBoxEditorDefinePane extends CustomWritableRepeatEditorPane<TreeEditor> {
	protected TreeSettingPane treeSettingPane;
	protected TreeRootPane treeRootPane;

	public TreeComboBoxEditorDefinePane(XCreator xCreator) {
		super(xCreator);
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

	public JPanel createOtherPane(){

		double f = TableLayout.FILL;
		double p = TableLayout.PREFERRED;
		Component[][] components = new Component[][]{
				new Component[]{new UICheckBox(Inter.getLocText("Tree-Mutiple_Selection_Or_Not"))},
				new Component[]{new UICheckBox(Inter.getLocText("Widget-Load_By_Async"))},
				new Component[]{new UICheckBox(Inter.getLocText("FR-Designer_Widget_Return_Leaf"))},
				new Component[]{new UICheckBox(Inter.getLocText("FR-Designer_Widget_Return_Path"))}

		};
		double[] rowSize = {p, p,p,p};
		double[] columnSize = {p};
		JPanel panel =  TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, 10, 7);
		panel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		return panel;
	}


	@Override
	public String title4PopupWindow() {
		return "treecombobox";
	}

	@Override
	protected void populateSubCustomWritableRepeatEditorBean(TreeEditor e) {
//		treeSettingPane.populate(e);
//		treeRootPane.populate(e.getTreeAttr());
	}

	@Override
	protected TreeComboBoxEditor updateSubCustomWritableRepeatEditorBean() {
		TreeComboBoxEditor editor = (TreeComboBoxEditor)creator.toData();
		editor.setTreeAttr(treeRootPane.update());
		return editor;
	}

    @Override
    public DataCreatorUI dataUI() {
        return treeSettingPane;
    }
}