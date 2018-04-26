/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.gui.icombobox;

import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.icombobox.UIComboBoxRenderer;
import com.fr.general.Inter;
import com.fr.stable.Constants;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Alignment ComboBox
 */
public class AlignmentComboBox extends UIComboBox {

	/**
	 * Constructor.
	 *
	 * @param alignmentArray the array of alignment.
	 */
	public AlignmentComboBox(int[] alignmentArray) {
		//copy lineStyle.
		Integer[] alignmentIntegerArray = new Integer[alignmentArray.length];

		for (int i = 0; i < alignmentArray.length; i++) {
			alignmentIntegerArray[i] = new Integer(alignmentArray[i]);
		}

		this.setModel(new DefaultComboBoxModel(alignmentIntegerArray));
		this.setRenderer(new AligmentCellRenderer());
	}

	/**
	 * Get selected alignment.
	 */
	public int getSelectedAlignment() {
		return ((Integer) getSelectedItem()).intValue();
	}

	/**
	 * Set the selected alignment.
	 */
	public void setSelectedAlignment(int alignment) {
		this.setSelectedItem(new Integer(alignment));
	}

	public static String getAlignDescription(int alignment) {
		if (alignment == Constants.CENTER) {//0
			return Inter.getLocText("Center");
		} else if (alignment == Constants.TOP) {//1
			return Inter.getLocText("StyleAlignment-Top");
		} else if (alignment == Constants.LEFT) {//2
			return Inter.getLocText("StyleAlignment-Left");
		} else if (alignment == Constants.BOTTOM) {//3
			return Inter.getLocText("StyleAlignment-Bottom");
		} else if (alignment == Constants.RIGHT) { //4
			return Inter.getLocText("StyleAlignment-Right");
		} else if (alignment == Constants.DISTRIBUTED) { //added by Kevin Wang 6
			return Inter.getLocText("StyleAlignment-Distibuted");
		} else {
			return StringUtils.EMPTY;
		}
	}

	/**
	 * CellRenderer.
	 */
	class AligmentCellRenderer extends UIComboBoxRenderer {

		public Component getListCellRendererComponent(JList list,
													  Object value, int index, boolean isSelected, boolean cellHasFocus) {
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

			int alignment = ((Integer) value).intValue();
			this.setText(getAlignDescription(alignment));
			return this;
		}
	}
}