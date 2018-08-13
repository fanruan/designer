package com.fr.design.widget.ui;

import javax.swing.*;

import com.fr.data.Dictionary;
import com.fr.design.data.DataCreatorUI;
import com.fr.design.designer.IntervalConstants;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.widget.accessibles.AccessibleDictionaryEditor;
import com.fr.form.ui.ComboBox;


import java.awt.*;

public class ComboBoxDefinePane extends CustomWritableRepeatEditorPane<ComboBox> {
	protected AccessibleDictionaryEditor dictPane;

	public ComboBoxDefinePane() {
		this.initComponents();
	}

	@Override
	protected void initComponents() {
		super.initComponents();
	}

	protected JPanel setForthContentPane () {
		dictPane = new AccessibleDictionaryEditor();
		JPanel jPanel =  TableLayoutHelper.createGapTableLayoutPane(
				new Component[][]{new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_DS_Dictionary")), dictPane}}, TableLayoutHelper.FILL_LASTCOLUMN, IntervalConstants.INTERVAL_W1, IntervalConstants.INTERVAL_L1);
		jPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
		return jPanel;
	}

	protected void populateSubCustomWritableRepeatEditorBean(ComboBox e) {
		this.dictPane.setValue(e.getDictionary());
	}

	protected ComboBox updateSubCustomWritableRepeatEditorBean() {
		ComboBox combo = new ComboBox();
		combo.setDictionary((Dictionary) this.dictPane.getValue());

		return combo;
	}

	@Override
	protected String title4PopupWindow() {
		return "ComboBox";
	}

    @Override
    public DataCreatorUI dataUI() {
        return null;
    }
}