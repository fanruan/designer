/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.gui.icombobox;

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
			return com.fr.design.i18n.Toolkit.i18nText("Center");
		} else if (alignment == Constants.TOP) {//1
			return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_StyleAlignment_Top");
		} else if (alignment == Constants.LEFT) {//2
			return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_StyleAlignment_Left");
		} else if (alignment == Constants.BOTTOM) {//3
			return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_StyleAlignment_Bottom");
		} else if (alignment == Constants.RIGHT) { //4
			return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_StyleAlignment_Right");
		} else if (alignment == Constants.DISTRIBUTED) { //added by Kevin Wang 6
			return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Style_Alignment_Distibuted");
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
