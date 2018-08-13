package com.fr.design.widget.ui.designer;

import com.fr.design.designer.creator.XCreator;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.form.ui.WriteUnableRepeatEditor;


import javax.swing.*;
import java.awt.*;

public abstract class WriteUnableRepeatEditorPane<E extends WriteUnableRepeatEditor> extends FieldEditorDefinePane<WriteUnableRepeatEditor> {
	// richer:是否去除重复的值
	protected UICheckBox removeRepeatCheckBox;

	public WriteUnableRepeatEditorPane(XCreator xCreator){
		super(xCreator);
	}

	@Override
	protected JPanel setFirstContentPane() {
		JPanel contentPane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_L_Pane();
		contentPane.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0));
		JPanel contenter=FRGUIPaneFactory.createMediumHGapFlowInnerContainer_M_Pane();
		removeRepeatCheckBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Remove_Repeat_Data"), false);
		contentPane.add(contenter);
		contenter.add(removeRepeatCheckBox);
		JPanel otherContentPane = this.setThirdContentPane();
		if (otherContentPane != null)
			contentPane.add(otherContentPane,BorderLayout.CENTER);
		return contentPane;
	}
	protected abstract JPanel setThirdContentPane();
	@Override
	protected void populateSubFieldEditorBean(WriteUnableRepeatEditor e) {
		removeRepeatCheckBox.setSelected(e.isRemoveRepeat());
		
		populateSubWriteUnableRepeatBean((E)e);
	}
	
	protected abstract void populateSubWriteUnableRepeatBean(E e);
	
	@Override
	protected WriteUnableRepeatEditor updateSubFieldEditorBean() {
		WriteUnableRepeatEditor ob = updateSubWriteUnableRepeatBean();
		ob.setRemoveRepeat(removeRepeatCheckBox.isSelected());
		
		return ob;
	}
	
	protected abstract E updateSubWriteUnableRepeatBean();
}