package com.fr.design.widget.ui.designer;

import com.fr.design.data.DataCreatorUI;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.present.dict.DictionaryPane;
import com.fr.form.ui.ComboBox;

import javax.swing.*;

public class ComboBoxDefinePane extends CustomWritableRepeatEditorPane<ComboBox> {
	protected DictionaryPane dictPane;


	public ComboBoxDefinePane(XCreator xCreator) {
		super(xCreator);
		dictPane = new DictionaryPane();
	}

	protected JPanel setForthContentPane () {
		return null;
	}

	protected void populateSubCustomWritableRepeatEditorBean(ComboBox e) {
		this.dictPane.populateBean(e.getDictionary());
	}

	protected ComboBox updateSubCustomWritableRepeatEditorBean() {
		ComboBox combo = new ComboBox();
		combo.setDictionary(this.dictPane.updateBean());

		return combo;
	}

	@Override
	public String title4PopupWindow() {
		return "ComboBox";
	}

    @Override
    public DataCreatorUI dataUI() {
        return dictPane;
    }
}