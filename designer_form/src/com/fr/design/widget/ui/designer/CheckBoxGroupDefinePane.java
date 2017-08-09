package com.fr.design.widget.ui.designer;

import com.fr.design.data.DataCreatorUI;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ibutton.UIHeadGroup;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.present.dict.DictionaryPane;
import com.fr.design.widget.ui.designer.btn.ButtonGroupDefinePane;
import com.fr.form.ui.CheckBoxGroup;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;

public class CheckBoxGroupDefinePane extends ButtonGroupDefinePane<CheckBoxGroup> {
	private DictionaryPane dictPane;
	private UIButtonGroup returnType;
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
		checkbox = new UICheckBox(Inter.getLocText(new String[]{"Provide", "Choose_All"}));
		checkbox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		final String[] tabTitles = new String[]{Inter.getLocText("Widget-Array"), Inter.getLocText("String")};
		returnType = new UIButtonGroup(tabTitles);


		double f = TableLayout.FILL;
		double p = TableLayout.PREFERRED;
		Component[][] components = new Component[][]{
				new Component[]{checkbox,  null },
				new Component[]{new UILabel(Inter.getLocText("Widget-Date_Selector_Return_Type")), returnType},
		};
		double[] rowSize = {p, p};
		double[] columnSize = {p, f};
		int[][] rowCount = {{1, 1},{1, 1}};
		JPanel panel =  TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, 10, 7);
		panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
		return panel;
	}

	@Override
	protected void populateSubButtonGroupBean(CheckBoxGroup ob) {
		returnType.setSelectedIndex(ob.isReturnString() ? 1 : 0);
		this.dictPane.populateBean(ob.getDictionary());
		checkbox.setSelected(ob.isChooseAll());
	}



	@Override
	protected CheckBoxGroup updateSubButtonGroupBean() {
		CheckBoxGroup ob = (CheckBoxGroup) creator.toData();
		ob.setReturnString(returnType.getSelectedIndex() == 1);
		ob.setDictionary(this.dictPane.updateBean());
		ob.setChooseAll(checkbox.isSelected());
		return ob;
	}

    @Override
    public DataCreatorUI dataUI() {
        return dictPane;
    }
}