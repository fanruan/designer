package com.fr.design.widget.ui.designer;

import com.fr.design.designer.creator.XCreator;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.design.widget.ui.designer.component.FormWidgetValuePane;
import com.fr.form.ui.DirectWriteEditor;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;

//richer:需要提供能否直接编辑的控件设置面板——下拉框、复选框、时间、日期、下拉树
public abstract class DirectWriteEditorDefinePane<T extends DirectWriteEditor> extends FieldEditorDefinePane<T> {
	public UICheckBox directWriteCheckBox;
	protected WaterMarkDictPane waterMarkDictPane;
	private UICheckBox removeRepeatCheckBox;

	public DirectWriteEditorDefinePane(XCreator xCreator) {
		super(xCreator);
	}


	@Override
	protected JPanel setFirstContentPane() {
		JPanel advancePane = FRGUIPaneFactory.createBorderLayout_S_Pane();

		waterMarkDictPane = new WaterMarkDictPane();
		removeRepeatCheckBox = new UICheckBox(Inter.getLocText("FR-Designer_Widget_No_Repeat"));
		FormWidgetValuePane formWidgetValuePane = new FormWidgetValuePane();
		double f = TableLayout.FILL;
		double p = TableLayout.PREFERRED;
		Component[][] components = new Component[][]{
				new Component[]{new UILabel(Inter.getLocText("FR-Designer-Estate_Widget_Value")),  formWidgetValuePane },
				new Component[]{new UILabel(Inter.getLocText("FR-Designer_DS-Dictionary")), new UITextField()},
				new Component[]{removeRepeatCheckBox, null},
				new Component[]{new UILabel(Inter.getLocText("FR-Designer_WaterMark")), waterMarkDictPane},
				new Component[]{new UILabel(Inter.getLocText("FR-Designer_Font-Size")), fontSizePane}
		};
		double[] rowSize = {p, p, p, p, p, p,p};
		double[] columnSize = {p, f};
		int[][] rowCount = {{1, 3},{1, 1},{1, 1},{1,1},{1,1}};
		JPanel panel =  TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, 10, 7);
		panel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		advancePane.add(panel, BorderLayout.NORTH);
		JPanel otherPane = createOtherPane();
		if(otherPane != null){
			advancePane.add(otherPane, BorderLayout.CENTER);
		}

		return advancePane;
	}

	public JPanel createOtherPane(){
		return null;
	}

	public  JPanel setValidatePane(){
        directWriteCheckBox = new UICheckBox(Inter.getLocText("Form-Allow_Edit"), false);
        JPanel otherContentPane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_S_Pane();
        otherContentPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        JPanel jPanel = GUICoreUtils.createFlowPane(new JComponent[]{directWriteCheckBox}, FlowLayout.LEFT, 5);
        jPanel.setPreferredSize(new Dimension(220, 30));
		otherContentPane.add(jPanel);
		return otherContentPane;
	}

	@Override
	protected void populateSubFieldEditorBean(T e) {
		this.directWriteCheckBox.setSelected(e.isDirectEdit());
		this.waterMarkDictPane.populate(e);

		populateSubDirectWriteEditorBean(e);
	}

	protected abstract void populateSubDirectWriteEditorBean(T e);

	@Override
	protected T updateSubFieldEditorBean() {
		T e = updateSubDirectWriteEditorBean();

		e.setDirectEdit(directWriteCheckBox.isSelected());
		this.waterMarkDictPane.update(e);

		return e;
	}

	protected abstract T updateSubDirectWriteEditorBean();
}