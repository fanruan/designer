package com.fr.design.widget.ui;

import com.fr.design.data.DataCreatorUI;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.present.dict.DictionaryPane;
import com.fr.form.ui.ComboCheckBox;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;

public class ComboCheckBoxDefinePane extends CustomWritableRepeatEditorPane<ComboCheckBox> {
	private CheckBoxDictPane checkBoxDictPane;
	private DictionaryPane dictPane;
    private UICheckBox supportTagCheckBox;

	public ComboCheckBoxDefinePane() {
		super.initComponents();
		dictPane = new DictionaryPane();
	}

	@Override
	protected JPanel setForthContentPane() {
		JPanel attrPane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_S_Pane();
		attrPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		JPanel contenter = FRGUIPaneFactory.createBorderLayout_L_Pane();
        contenter.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        checkBoxDictPane = new CheckBoxDictPane();
		attrPane.add(contenter);
        //是否以标签形式显示
        JPanel tagPane = FRGUIPaneFactory.createMediumHGapFlowInnerContainer_M_Pane();
        supportTagCheckBox = new UICheckBox(Inter.getLocText("Form-SupportTag"), true);
        tagPane.add(supportTagCheckBox);
        contenter.add(tagPane, BorderLayout.NORTH);

        contenter.add(checkBoxDictPane, BorderLayout.WEST);
		return attrPane;
	}

	@Override
	protected void populateSubCustomWritableRepeatEditorBean(ComboCheckBox e) {
		this.dictPane.populateBean(e.getDictionary());
		this.checkBoxDictPane.populate(e);
        this.supportTagCheckBox.setSelected(e.isSupportTag());
	}

	@Override
	protected ComboCheckBox updateSubCustomWritableRepeatEditorBean() {
		ComboCheckBox combo = new ComboCheckBox();
        combo.setSupportTag(this.supportTagCheckBox.isSelected());
		combo.setDictionary(this.dictPane.updateBean());
		checkBoxDictPane.update(combo);
		return combo;
	}

	@Override
	public DataCreatorUI dataUI() {
		return dictPane;
	}
	
	@Override
	protected String title4PopupWindow() {
		return "ComboCheckBox";
	}

}