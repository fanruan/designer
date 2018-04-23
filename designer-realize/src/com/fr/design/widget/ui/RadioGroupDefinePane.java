package com.fr.design.widget.ui;

import javax.swing.JPanel;
import com.fr.design.data.DataCreatorUI;
import com.fr.form.ui.RadioGroup;

public class RadioGroupDefinePane extends FieldEditorDefinePane<RadioGroup> {

	private ButtonGroupDictPane buttonGroupDictPane;

	public RadioGroupDefinePane() {
		this.initComponents();
	}

	@Override
	protected void initComponents() {
		super.initComponents();

	}
	
	@Override
	protected JPanel setFirstContentPane() {
		buttonGroupDictPane = new ButtonGroupDictPane();
		return buttonGroupDictPane;
	}

	@Override
	protected RadioGroup updateSubFieldEditorBean() {
		RadioGroup ob = new RadioGroup();

		this.buttonGroupDictPane.update(ob);

		return ob;
	}

	@Override
	protected String title4PopupWindow() {
		return "radiogroup";
	}

	@Override
	protected void populateSubFieldEditorBean(RadioGroup ob) {
		this.buttonGroupDictPane.populate(ob);
	}

    @Override
    public DataCreatorUI dataUI() {
        return null;
    }
}