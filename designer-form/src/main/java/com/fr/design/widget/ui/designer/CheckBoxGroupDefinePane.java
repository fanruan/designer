package com.fr.design.widget.ui.designer;

import com.fr.design.data.DataCreatorUI;
import com.fr.design.designer.IntervalConstants;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.present.dict.DictionaryPane;
import com.fr.design.widget.component.CheckBoxDictPane;
import com.fr.design.widget.ui.designer.btn.ButtonGroupDefinePane;
import com.fr.form.ui.CheckBoxGroup;


import javax.swing.*;
import java.awt.*;

public class CheckBoxGroupDefinePane extends ButtonGroupDefinePane<CheckBoxGroup> {
	private DictionaryPane dictPane;
	private CheckBoxDictPane checkBoxDictPane;
	private UICheckBox checkbox;

	public CheckBoxGroupDefinePane(XCreator xCreator) {
		super(xCreator);
	}


	@Override
	protected void initComponents() {
		super.initComponents();

		dictPane = new DictionaryPane();
	}
	
	@Override
	public String title4PopupWindow() {
		return "CheckBoxGroup";
	}


	public JPanel createOtherPane(){
		checkbox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Widget_Choose_Type_All"));
		checkbox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		checkBoxDictPane = new CheckBoxDictPane();
		double f = TableLayout.FILL;
		double p = TableLayout.PREFERRED;
		Component[][] components = new Component[][]{
				new Component[]{checkbox,  null },
				new Component[]{checkBoxDictPane, null},
		};
		double[] rowSize = {p, p};
		double[] columnSize = {p, f};
		int[][] rowCount = {{1, 1},{1, 1}};
		JPanel panel =  TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, IntervalConstants.INTERVAL_L2, IntervalConstants.INTERVAL_L1);
		return panel;
	}

	@Override
	protected void populateSubButtonGroupBean(CheckBoxGroup ob) {
		this.checkBoxDictPane.populate(ob);
		this.dictPane.populateBean(ob.getDictionary());
		checkbox.setSelected(ob.isChooseAll());
	}



	@Override
	protected CheckBoxGroup updateSubButtonGroupBean() {
		CheckBoxGroup ob = (CheckBoxGroup) creator.toData();
		checkBoxDictPane.update(ob);
		ob.setDictionary(this.dictPane.updateBean());
		ob.setChooseAll(checkbox.isSelected());
		return ob;
	}

    @Override
    public DataCreatorUI dataUI() {
        return dictPane;
    }
}
