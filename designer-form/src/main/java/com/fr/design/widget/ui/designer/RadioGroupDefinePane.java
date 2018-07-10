package com.fr.design.widget.ui.designer;

import com.fr.design.data.DataCreatorUI;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.present.dict.DictionaryPane;
import com.fr.design.widget.ui.designer.btn.ButtonGroupDefinePane;
import com.fr.form.ui.RadioGroup;

public class RadioGroupDefinePane extends ButtonGroupDefinePane<RadioGroup> {


	public RadioGroupDefinePane(XCreator xCreator) {
		super(xCreator);
	}


	@Override
	protected void initComponents() {
		super.initComponents();
	}

	@Override
	protected RadioGroup updateSubButtonGroupBean() {
		RadioGroup ob = (RadioGroup)creator.toData();

		return ob;
	}

	@Override
	public String title4PopupWindow() {
		return "radiogroup";
	}

	@Override
	protected void populateSubButtonGroupBean(RadioGroup ob) {
	}

    @Override
    public DataCreatorUI dataUI() {
        return null;
    }
}