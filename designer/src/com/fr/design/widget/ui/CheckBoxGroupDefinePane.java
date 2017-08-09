package com.fr.design.widget.ui;

import java.awt.*;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.fr.data.Dictionary;
import com.fr.design.data.DataCreatorUI;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.widget.accessibles.AccessibleDictionaryEditor;
import com.fr.design.present.dict.DictionaryPane;
import com.fr.design.widget.ui.designer.component.FormWidgetValuePane;
import com.fr.form.ui.CheckBoxGroup;
import com.fr.general.Inter;

public class CheckBoxGroupDefinePane extends FieldEditorDefinePane<CheckBoxGroup> {
	private AccessibleDictionaryEditor dictPane;

	CheckBoxDictPane checkBoxDictPane;

	private UICheckBox checkbox;
	private ButtonGroupDictPane buttonGroupDictPane;

	public CheckBoxGroupDefinePane() {
		this.initComponents();
	}

	@Override
	protected void initComponents() {
		super.initComponents();

	}
	
	@Override
	protected String title4PopupWindow() {
		return "CheckBoxGroup";
	}
	
	@Override
	protected JPanel setFirstContentPane() {
		JPanel advancePane = FRGUIPaneFactory.createBorderLayout_S_Pane();
		dictPane = new AccessibleDictionaryEditor();
		checkbox = new UICheckBox(Inter.getLocText(new String[]{"Provide", "Choose_All"}));
		buttonGroupDictPane = new ButtonGroupDictPane();
		checkBoxDictPane = new CheckBoxDictPane();
		double f = TableLayout.FILL;
		double p = TableLayout.PREFERRED;
		Component[][] components = new Component[][]{
				new Component[]{buttonGroupDictPane,  null },
				new Component[]{checkbox,  null },
				new Component[]{new UILabel(Inter.getLocText("FR-Designer_DS-Dictionary")),  dictPane },
				new Component[]{checkBoxDictPane,  null },

		};
		double[] rowSize = {p, p, p, p};
		double[] columnSize = {p, f};
		int[][] rowCount = {{1, 1},{1, 1},{1,1},{1,1}};
		JPanel panel =  TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, 10, 7);
		panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
		advancePane.add(panel);
		return advancePane;

	}
	
	@Override
	protected void populateSubFieldEditorBean(CheckBoxGroup ob) {
		this.dictPane.setValue(ob.getDictionary());
		checkBoxDictPane.populate(ob);
		checkbox.setSelected(ob.isChooseAll());
		this.buttonGroupDictPane.populate(ob);
	}

	@Override
	protected CheckBoxGroup updateSubFieldEditorBean() {
		CheckBoxGroup ob = new CheckBoxGroup();

		ob.setDictionary((Dictionary) this.dictPane.getValue());
		checkBoxDictPane.update(ob);
		ob.setChooseAll(checkbox.isSelected());
		this.buttonGroupDictPane.update(ob);
		return ob;
	}

    @Override
    public DataCreatorUI dataUI() {
        return null;
    }
}