package com.fr.design.widget.ui;

import com.fr.data.Dictionary;
import com.fr.design.data.DataCreatorUI;
import com.fr.design.designer.IntervalConstants;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.widget.accessibles.AccessibleDictionaryEditor;
import com.fr.design.widget.component.CheckBoxDictPane;
import com.fr.form.ui.ComboCheckBox;


import javax.swing.*;
import java.awt.*;

public class ComboCheckBoxDefinePane extends CustomWritableRepeatEditorPane<ComboCheckBox> {
	private CheckBoxDictPane checkBoxDictPane;
	private AccessibleDictionaryEditor dictPane;
    private UICheckBox supportTagCheckBox;

	public ComboCheckBoxDefinePane() {
		super.initComponents();
	}

	@Override
	protected JPanel setForthContentPane() {
		dictPane = new AccessibleDictionaryEditor();
		checkBoxDictPane = new CheckBoxDictPane();
		supportTagCheckBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Form-SupportTag"), true);
		supportTagCheckBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		JPanel advancePane = FRGUIPaneFactory.createBorderLayout_S_Pane();
		double f = TableLayout.FILL;
		double p = TableLayout.PREFERRED;
		Component[][] components = new Component[][]{
				new Component[]{supportTagCheckBox,  null },
				new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_DS-Dictionary")),  dictPane },
				new Component[]{checkBoxDictPane,  null },

		};
		double[] rowSize = {p, p, p, p};
		double[] columnSize = {p, f};
		int[][] rowCount = {{1, 1},{1, 1},{1,1},{1,1}};
		JPanel panel =  TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, IntervalConstants.INTERVAL_W1, IntervalConstants.INTERVAL_L1);
		panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
		advancePane.add(panel);
		return advancePane;
	}

	@Override
	protected void populateSubCustomWritableRepeatEditorBean(ComboCheckBox e) {
		this.dictPane.setValue(e.getDictionary());
		this.checkBoxDictPane.populate(e);
        this.supportTagCheckBox.setSelected(e.isSupportTag());
	}

	@Override
	protected ComboCheckBox updateSubCustomWritableRepeatEditorBean() {
		ComboCheckBox combo = new ComboCheckBox();
        combo.setSupportTag(this.supportTagCheckBox.isSelected());
		combo.setDictionary((Dictionary) this.dictPane.getValue());
		checkBoxDictPane.update(combo);
		return combo;
	}

	@Override
	public DataCreatorUI dataUI() {
		return null;
	}
	
	@Override
	protected String title4PopupWindow() {
		return "ComboCheckBox";
	}

}