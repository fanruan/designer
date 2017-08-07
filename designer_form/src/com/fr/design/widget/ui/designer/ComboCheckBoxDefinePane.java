package com.fr.design.widget.ui.designer;

import com.fr.design.data.DataCreatorUI;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.gui.ibutton.UIHeadGroup;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.form.ui.ComboCheckBox;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;

public class ComboCheckBoxDefinePane extends DictEditorDefinePane<ComboCheckBox> {
    private UICheckBox supportTagCheckBox;
    private UIHeadGroup returnType;
    private UITextField waterMarkDictPane;
    private UICheckBox removeRepeatCheckBox;

	public ComboCheckBoxDefinePane(XCreator xCreator) {
		super(xCreator);
	}

	public UICheckBox createRepeatCheckBox(){
		removeRepeatCheckBox = new UICheckBox(Inter.getLocText("FR-Designer_Widget_No_Repeat"));
		return removeRepeatCheckBox;
	}

	public Component[] createWaterMarkPane() {
		waterMarkDictPane = new UITextField();
		return new Component[]{new UILabel(Inter.getLocText("FR-Designer_WaterMark")), waterMarkDictPane};
	}


	public JPanel createOtherPane(){
		supportTagCheckBox = new UICheckBox(Inter.getLocText("Form-SupportTag"), true);

		final String[] tabTitles = new String[]{Inter.getLocText("Widget-Array"), Inter.getLocText("String")};
		returnType = new UIHeadGroup(tabTitles) {
			@Override
			public void tabChanged(int index) {
				ComboCheckBox combo = (ComboCheckBox) creator.toData();
				//todo
				if (index == 1) {
					combo.setReturnString(true);
				} else {
					combo.setReturnString(false);
				}
			}
		};

		double f = TableLayout.FILL;
		double p = TableLayout.PREFERRED;
		Component[][] components = new Component[][]{
				new Component[]{supportTagCheckBox,  null },
				new Component[]{new UILabel(Inter.getLocText("Widget-Date_Selector_Return_Type")), returnType},
		};
		double[] rowSize = {p, p};
		double[] columnSize = {p, f};
		int[][] rowCount = {{1, 1},{1, 1}};
		JPanel panel =  TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, 10, 7);
		panel.setBorder(BorderFactory.createEmptyBorder(0,5,5,5));
		return panel;
	}

	protected  void populateSubDictionaryEditorBean(ComboCheckBox ob){
		if (ob.isReturnString()) {
			returnType.setSelectedIndex(1);
		} else {
			returnType.setSelectedIndex(0);
		}
		waterMarkDictPane.setText(ob.getWaterMark());
		formWidgetValuePane.populate(ob);
		this.supportTagCheckBox.setSelected(ob.isSupportTag());
		this.removeRepeatCheckBox.setSelected(ob.isRemoveRepeat());
	}

	protected  ComboCheckBox updateSubDictionaryEditorBean(){
		ComboCheckBox combo = (ComboCheckBox) creator.toData();
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