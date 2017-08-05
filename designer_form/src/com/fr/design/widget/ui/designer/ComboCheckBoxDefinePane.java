package com.fr.design.widget.ui.designer;

import com.fr.design.data.DataCreatorUI;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.present.dict.DictionaryPane;
import com.fr.form.ui.ComboCheckBox;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;

public class ComboCheckBoxDefinePane extends CustomWritableRepeatEditorPane<ComboCheckBox> {
	private CheckBoxDictPane checkBoxDictPane;
	private DictionaryPane dictPane;
    private UICheckBox supportTagCheckBox;

	public ComboCheckBoxDefinePane(XCreator xCreator) {
		super(xCreator);
		dictPane = new DictionaryPane();
		checkBoxDictPane = new CheckBoxDictPane();
		supportTagCheckBox = new UICheckBox(Inter.getLocText("Form-SupportTag"), true);

	}

	@Override
	protected JPanel setForthContentPane() {
		JPanel attrPane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_S_Pane();
		attrPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		JPanel contenter = FRGUIPaneFactory.createBorderLayout_L_Pane();
        contenter.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		attrPane.add(contenter);
        //是否以标签形式显示
        JPanel tagPane = FRGUIPaneFactory.createMediumHGapFlowInnerContainer_M_Pane();
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

	public JPanel createOtherPane(){
		double f = TableLayout.FILL;
		double p = TableLayout.PREFERRED;
		Component[][] components = new Component[][]{
				new Component[]{new UICheckBox(Inter.getLocText("Form-SupportTag")),  null },
				new Component[]{new UILabel(Inter.getLocText("Widget-Date_Selector_Return_Type")), new UITextField()},
		};
		double[] rowSize = {p, p};
		double[] columnSize = {p, f};
		int[][] rowCount = {{1, 1},{1, 1}};
		JPanel panel =  TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, 10, 7);
		panel.setBorder(BorderFactory.createEmptyBorder(0,5,5,5));
		return panel;
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
	public String title4PopupWindow() {
		return "ComboCheckBox";
	}

}