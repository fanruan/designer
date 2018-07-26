package com.fr.design.widget.ui.designer;

import com.fr.design.data.DataCreatorUI;
import com.fr.design.designer.IntervalConstants;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.widget.component.CheckBoxDictPane;
import com.fr.form.ui.ComboCheckBox;


import javax.swing.*;
import java.awt.*;

public class ComboCheckBoxDefinePane extends DictEditorDefinePane<ComboCheckBox> {
    private UICheckBox supportTagCheckBox;
	private CheckBoxDictPane checkBoxDictPane;
    private UITextField waterMarkDictPane;
    private UICheckBox removeRepeatCheckBox;

	public ComboCheckBoxDefinePane(XCreator xCreator) {
		super(xCreator);
	}

	public UICheckBox createRepeatCheckBox(){
		removeRepeatCheckBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Widget_No_Repeat"));
		removeRepeatCheckBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		return removeRepeatCheckBox;
	}

	public Component[] createWaterMarkPane() {
		waterMarkDictPane = new UITextField();
		return new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_WaterMark")), waterMarkDictPane};
	}


	public JPanel createOtherPane(){
		supportTagCheckBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Form-SupportTag"), true);
		supportTagCheckBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		checkBoxDictPane = new CheckBoxDictPane();

		double f = TableLayout.FILL;
		double p = TableLayout.PREFERRED;
		Component[][] components = new Component[][]{
				new Component[]{supportTagCheckBox,  null },
				new Component[]{checkBoxDictPane, null},
		};
		double[] rowSize = {p, p};
		double[] columnSize = {p, f};
		int[][] rowCount = {{1, 1},{1, 1}};
		JPanel panel =  TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, IntervalConstants.INTERVAL_L2, IntervalConstants.INTERVAL_L1);
		return panel;
	}

	protected  void populateSubDictionaryEditorBean(ComboCheckBox ob){
		this.checkBoxDictPane.populate(ob);
		waterMarkDictPane.setText(ob.getWaterMark());
		formWidgetValuePane.populate(ob);
		this.supportTagCheckBox.setSelected(ob.isSupportTag());
		this.removeRepeatCheckBox.setSelected(ob.isRemoveRepeat());
	}

	protected  ComboCheckBox updateSubDictionaryEditorBean(){
		ComboCheckBox combo = (ComboCheckBox) creator.toData();
		checkBoxDictPane.update(combo);
		formWidgetValuePane.update(combo);
		combo.setWaterMark(waterMarkDictPane.getText());
		combo.setSupportTag(this.supportTagCheckBox.isSelected());
		combo.setRemoveRepeat(removeRepeatCheckBox.isSelected());
		return combo;
	}

	@Override
	public DataCreatorUI dataUI() {
		return null;
	}
	
	@Override
	public String title4PopupWindow() {
		return "ComboCheckBox";
	}

}