package com.fr.design.widget.ui.designer;

import com.fr.design.designer.IntervalConstants;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.design.widget.ui.designer.component.FormWidgetValuePane;
import com.fr.form.ui.DirectWriteEditor;


import javax.swing.*;
import java.awt.*;

//richer:需要提供能否直接编辑的控件设置面板——下拉框、复选框、时间、日期、下拉树
public abstract class DirectWriteEditorDefinePane<T extends DirectWriteEditor> extends FieldEditorDefinePane<T> {
	public UICheckBox directWriteCheckBox;
	protected FormWidgetValuePane formWidgetValuePane;

	public DirectWriteEditorDefinePane(XCreator xCreator) {
		super(xCreator);
	}


	@Override
	protected JPanel setFirstContentPane() {
		JPanel advancePane = FRGUIPaneFactory.createBorderLayout_S_Pane();
		UILabel widgetValueLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Estate_Widget_Value"));
		widgetValueLabel.setVerticalAlignment(SwingConstants.TOP);
		formWidgetValuePane = new FormWidgetValuePane(creator.toData(), false);
		Component[] removeRepeatPane = new Component[]{createRepeatCheckBox(), null};
		Component[] dicPane = createDictPane();
		Component[] waterMarkComponent = createWaterMarkPane();
		double f = TableLayout.FILL;
		double p = TableLayout.PREFERRED;
		Component[][] components = new Component[][]{
				new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Label_Name")), labelNameTextField},
				new Component[]{widgetValueLabel,  formWidgetValuePane },
				dicPane,
				removeRepeatPane,
				waterMarkComponent,
				new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Font-Size")), fontSizePane}
		};
		double[] rowSize = {p, p, p, p, p, p, p, p};
		double[] columnSize = {p, f};
		int[][] rowCount = {{1, 1},{1, 3},{1, 1},{1, 1},{1,1},{1,1}};
		JPanel panel =  TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, IntervalConstants.INTERVAL_W1, IntervalConstants.INTERVAL_L1);
//		panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
		advancePane.add(panel, BorderLayout.NORTH);
		JPanel otherPane = createOtherPane();
		if(otherPane != null){
			advancePane.add(otherPane, BorderLayout.CENTER);
		}

		return advancePane;
	}

	public UICheckBox createRepeatCheckBox(){
		return null;
	}

	public Component[] createWaterMarkPane() {
		return new Component[]{null, null};
	}

	protected Component[] createDictPane(){
		return new Component[]{null, null};
	}

	public JPanel createOtherPane(){
		return null;
	}

	public  JPanel setValidatePane(){
        directWriteCheckBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Form-Allow_Edit"), false);
		directWriteCheckBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        JPanel otherContentPane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_S_Pane();
        otherContentPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        JPanel jPanel = GUICoreUtils.createFlowPane(new JComponent[]{directWriteCheckBox}, FlowLayout.LEFT, 0);
		otherContentPane.add(jPanel);
		return otherContentPane;
	}

	@Override
	protected void populateSubFieldEditorBean(T e) {
		this.directWriteCheckBox.setSelected(e.isDirectEdit());
		populateSubDirectWriteEditorBean(e);
	}

	protected abstract void populateSubDirectWriteEditorBean(T e);

	@Override
	protected T updateSubFieldEditorBean() {
		T e = updateSubDirectWriteEditorBean();
		e.setDirectEdit(directWriteCheckBox.isSelected());

		return e;
	}

	protected abstract T updateSubDirectWriteEditorBean();
}
