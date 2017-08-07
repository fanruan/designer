package com.fr.design.widget.ui.designer;

import com.fr.design.data.DataCreatorUI;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.present.dict.DictionaryPane;
import com.fr.design.widget.ui.designer.btn.ButtonGroupDefinePane;
import com.fr.form.ui.RadioGroup;

public class RadioGroupDefinePane extends ButtonGroupDefinePane<RadioGroup> {
	private DictionaryPane dictPane;


	public RadioGroupDefinePane(XCreator xCreator) {
		super(xCreator);
	}


	@Override
	protected void initComponents() {
		super.initComponents();

		dictPane = new DictionaryPane();
	}

	@Override
	protected RadioGroup updateSubButtonGroupBean() {
		RadioGroup ob = (RadioGroup)creator.toData();

		ob.setDictionary(this.dictPane.updateBean());

		return ob;
	}

	@Override
	public String title4PopupWindow() {
		return "radiogroup";
	}

	@Override
	protected void populateSubButtonGroupBean(RadioGroup ob) {
		this.dictPane.populateBean(ob.getDictionary());
	}

    @Override
    public DataCreatorUI dataUI() {
        return dictPane;
    }
}