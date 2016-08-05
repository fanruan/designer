package com.fr.design.widget.ui;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.form.ui.WriteUnableRepeatEditor;
import com.fr.general.Inter;

public abstract class WriteUnableRepeatEditorPane<E extends WriteUnableRepeatEditor> extends FieldEditorDefinePane<WriteUnableRepeatEditor> {
	// richer:是否去除重复的值
	protected UICheckBox removeRepeatCheckBox;
	
	public WriteUnableRepeatEditorPane(){
		this.initComponents();
	}
	
	@Override
	protected JPanel setFirstContentPane() {
		JPanel advancedPane = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("Advanced"));
		JPanel contentPane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_L_Pane();
		advancedPane.add(contentPane);
		contentPane.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0));
		JPanel contenter=FRGUIPaneFactory.createMediumHGapFlowInnerContainer_M_Pane();
		removeRepeatCheckBox = new UICheckBox(Inter.getLocText("Form-Remove_Repeat_Data"), false);
		contentPane.add(contenter);
		contenter.add(removeRepeatCheckBox);
		JPanel otherContentPane = this.setThirdContentPane();
		if (otherContentPane != null)
			contentPane.add(otherContentPane,BorderLayout.CENTER);
		return advancedPane;
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