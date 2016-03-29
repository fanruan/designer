package com.fr.design.widget.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.fr.design.data.DataCreatorUI;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.present.dict.DictionaryPane;
import com.fr.form.ui.RadioGroup;

public class RadioGroupDefinePane extends FieldEditorDefinePane<RadioGroup> {
	private DictionaryPane dictPane;

	private ButtonGroupDictPane buttonGroupDictPane;

	public RadioGroupDefinePane() {
		this.initComponents();
	}

	@Override
	protected void initComponents() {
		super.initComponents();

		dictPane = new DictionaryPane();
	}
	
	@Override
	protected JPanel setFirstContentPane() {
		JPanel attrPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
		attrPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		JPanel centerPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
		buttonGroupDictPane = new ButtonGroupDictPane();
		buttonGroupDictPane.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
		centerPane.add(buttonGroupDictPane);
		attrPane.add(centerPane, BorderLayout.CENTER);
		return attrPane;
	}

	@Override
	protected RadioGroup updateSubFieldEditorBean() {
		RadioGroup ob = new RadioGroup();

		ob.setDictionary(this.dictPane.updateBean());
		this.buttonGroupDictPane.update(ob);

		return ob;
	}

	@Override
	protected String title4PopupWindow() {
		return "radiogroup";
	}

	@Override
	protected void populateSubFieldEditorBean(RadioGroup ob) {
		this.dictPane.populateBean(ob.getDictionary());
		this.buttonGroupDictPane.populate(ob);
	}

    @Override
    public DataCreatorUI dataUI() {
        return dictPane;
    }
}