package com.fr.design.widget.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.fr.design.data.DataCreatorUI;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.present.dict.DictionaryPane;
import com.fr.form.ui.CheckBoxGroup;
import com.fr.general.Inter;

public class CheckBoxGroupDefinePane extends FieldEditorDefinePane<CheckBoxGroup> {
	private DictionaryPane dictPane;

	CheckBoxDictPane checkBoxDictPane;

	private UICheckBox checkbox;
	private ButtonGroupDictPane buttonGroupDictPane;

	public CheckBoxGroupDefinePane() {
		this.initComponents();
	}

	@Override
	protected void initComponents() {
		super.initComponents();

		dictPane = new DictionaryPane();
	}
	
	@Override
	protected String title4PopupWindow() {
		return "CheckBoxGroup";
	}
	
	@Override
	protected JPanel setFirstContentPane() {
		JPanel attrPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
		attrPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		JPanel advancedPane = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("FR-Designer_Advanced"));
		advancedPane.add(attrPane);
		JPanel northPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
		northPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		checkBoxDictPane = new CheckBoxDictPane();
		checkBoxDictPane.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
		northPane.add(checkBoxDictPane, BorderLayout.NORTH);
		JPanel chooseAllPane = new JPanel();
		checkbox = new UICheckBox(Inter.getLocText("FR-Designer_Provide_Choose_All"));
		chooseAllPane.add(checkbox);
		chooseAllPane.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 0));
		northPane.add(chooseAllPane, BorderLayout.CENTER);
		attrPane.add(northPane, BorderLayout.NORTH);

		JPanel centerPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
		buttonGroupDictPane = new ButtonGroupDictPane();
		buttonGroupDictPane.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 0));
		centerPane.add(buttonGroupDictPane);
		attrPane.add(centerPane, BorderLayout.CENTER);

		return advancedPane;
	}
	
	@Override
	protected void populateSubFieldEditorBean(CheckBoxGroup ob) {
		this.dictPane.populateBean(ob.getDictionary());
		checkBoxDictPane.populate(ob);
		checkbox.setSelected(ob.isChooseAll());
		this.buttonGroupDictPane.populate(ob);
	}

	@Override
	protected CheckBoxGroup updateSubFieldEditorBean() {
		CheckBoxGroup ob = new CheckBoxGroup();

		ob.setDictionary(this.dictPane.updateBean());
		checkBoxDictPane.update(ob);
		ob.setChooseAll(checkbox.isSelected());
		this.buttonGroupDictPane.update(ob);
		return ob;
	}

    @Override
    public DataCreatorUI dataUI() {
        return dictPane;
    }
}