package com.fr.design.mainframe.widget.renderer;

import com.fr.design.mainframe.widget.wrappers.DateWrapper;

public class DateCellRenderer extends EncoderCellRenderer {
	public DateCellRenderer() {
		super(new DateWrapper());
	}
}