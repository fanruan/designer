package com.fr.design.widget.ui;

import javax.swing.JPanel;

import com.fr.design.data.DataCreatorUI;
import com.fr.design.present.dict.DictionaryPane;
import com.fr.form.ui.ComboBox;

public class ComboBoxDefinePane extends CustomWritableRepeatEditorPane<ComboBox> {
	protected DictionaryPane dictPane;

	public ComboBoxDefinePane() {
		this.initComponents();
	}

	@Override
	protected void initComponents() {
		super.initComponents();
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
	protected String title4PopupWindow() {
		return "ComboBox";
	}

    @Override
    public DataCreatorUI dataUI() {
        return dictPane;
    }
}