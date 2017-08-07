package com.fr.design.widget.ui.designer;

import com.fr.design.data.DataCreatorUI;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.gui.frpane.TreeSettingPane;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.gui.itree.refreshabletree.TreeRootPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.widget.ui.designer.component.FormWidgetValuePane;
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
	private FormWidgetValuePane formWidgetValuePane;

	private UICheckBox removeRepeatCheckBox;

	public TreeEditorDefinePane(XCreator xCreator){
		super(xCreator);
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
		JPanel jPanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
		treeRootPane = new TreeRootPane();
		treeSettingPane = new TreeSettingPane(true);
		formWidgetValuePane = new FormWidgetValuePane();
		double f = TableLayout.FILL;
		double p = TableLayout.PREFERRED;
		Component[][] components = new Component[][]{
				new Component[]{new UILabel(Inter.getLocText("FR-Designer-Estate_Widget_Value")),  formWidgetValuePane },
				new Component[]{new UILabel(Inter.getLocText("FR-Designer_DS-Dictionary")), new UITextField()},
				new Component[]{new UILabel(Inter.getLocText("FR-Designer_Font-Size")), fontSizePane}
		};
		double[] rowSize = {p, p,p};
		double[] columnSize = {p,f};
		int[][] rowCount = {{1, 1},{1, 1},{1, 1}};
		JPanel panel =  TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, 10, 7);
		panel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		jPanel.add(panel, BorderLayout.NORTH);

		JPanel otherContentPane = this.createOtherPane();
		jPanel.add(otherContentPane,BorderLayout.CENTER);
		return jPanel;
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
		return "tree";
	}

    @Override
    public DataCreatorUI dataUI() {
        return treeSettingPane;
    }
}