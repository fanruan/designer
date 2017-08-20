package com.fr.design.widget.ui.designer;

import com.fr.design.data.DataCreatorUI;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UIPropertyTextField;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.form.ui.ComboCheckBox;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;

public class ComboCheckBoxDefinePane extends DictEditorDefinePane<ComboCheckBox> {
    private UICheckBox supportTagCheckBox;
    private UIButtonGroup returnType;
    private UIPropertyTextField waterMarkDictPane;
    private UICheckBox removeRepeatCheckBox;

	public ComboCheckBoxDefinePane(XCreator xCreator) {
		super(xCreator);
	}

	public UICheckBox createRepeatCheckBox(){
		removeRepeatCheckBox = new UICheckBox(Inter.getLocText("FR-Designer_Widget_No_Repeat"));
		removeRepeatCheckBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		return removeRepeatCheckBox;
	}

	public Component[] createWaterMarkPane() {
		waterMarkDictPane = new UIPropertyTextField();
		return new Component[]{new UILabel(Inter.getLocText("FR-Designer_WaterMark")), waterMarkDictPane};
	}


	public JPanel createOtherPane(){
		supportTagCheckBox = new UICheckBox(Inter.getLocText("Form-SupportTag"), true);

		final String[] tabTitles = new String[]{Inter.getLocText("Widget-Array"), Inter.getLocText("String")};
		returnType = new UIButtonGroup(tabTitles) ;

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
		panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
		return panel;
	}

	protected  void populateSubDictionaryEditorBean(ComboCheckBox ob){
		returnType.setSelectedIndex(ob.isReturnString() ? 1 : 0);
		waterMarkDictPane.setText(ob.getWaterMark());
		formWidgetValuePane.populate(ob);
		this.supportTagCheckBox.setSelected(ob.isSupportTag());
		this.removeRepeatCheckBox.setSelected(ob.isRemoveRepeat());
	}

	protected  ComboCheckBox updateSubDictionaryEditorBean(){
		ComboCheckBox combo = (ComboCheckBox) creator.toData();
		combo.setReturnString(returnType.getSelectedIndex() == 1);
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