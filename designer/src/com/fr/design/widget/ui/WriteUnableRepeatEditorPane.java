package com.fr.design.widget.ui;

import java.awt.*;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
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
		JPanel contentPane = FRGUIPaneFactory.createYBoxEmptyBorderPane();
//		JPanel contenter=FRGUIPaneFactory.createMediumHGapFlowInnerContainer_M_Pane_First0();
		removeRepeatCheckBox = new UICheckBox(Inter.getLocText("Form-Remove_Repeat_Data"), false);
//		contentPane.add(contenter);
//		contenter.add(removeRepeatCheckBox);
//		JPanel otherContentPane = this.setThirdContentPane();
//		if (otherContentPane != null)
//			contentPane.add(otherContentPane,BorderLayout.CENTER);


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
		JPanel panel =  TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, 10, 7);
		panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
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