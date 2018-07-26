package com.fr.design.widget.ui;

import java.awt.*;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.fr.design.designer.IntervalConstants;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.form.ui.WriteUnableRepeatEditor;


public abstract class WriteUnableRepeatEditorPane<E extends WriteUnableRepeatEditor> extends FieldEditorDefinePane<WriteUnableRepeatEditor> {
	// richer:是否去除重复的值
	protected UICheckBox removeRepeatCheckBox;
	
	public WriteUnableRepeatEditorPane(){
		this.initComponents();
	}
	
	@Override
	protected JPanel setFirstContentPane() {
		JPanel contentPane = FRGUIPaneFactory.createYBoxEmptyBorderPane();
		removeRepeatCheckBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Form-Remove_Repeat_Data"), false);
		removeRepeatCheckBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		Component[] dicPane = createDicPane();
		double f = TableLayout.FILL;
		double p = TableLayout.PREFERRED;
		Component[][] components = new Component[][]{
				dicPane,
				new Component[]{removeRepeatCheckBox, null},
		};
		double[] rowSize = {p, p};
		double[] columnSize = {p, f};
		int[][] rowCount = {{1,1},{1,1}};
		JPanel panel =  TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, IntervalConstants.INTERVAL_W1, IntervalConstants.INTERVAL_L1);
		contentPane.add(panel);
		JPanel otherContentPane = this.setThirdContentPane();
		if (otherContentPane != null)
			contentPane.add(otherContentPane,BorderLayout.CENTER);

		return contentPane;
	}

	protected Component[] createDicPane(){
		return new Component[]{null, null};
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