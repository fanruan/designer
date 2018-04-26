package com.fr.design.mainframe.widget.renderer;

import com.fr.design.mainframe.widget.wrappers.RegexWrapper;

public class RegexCellRencerer extends EncoderCellRenderer {
	public RegexCellRencerer() {
		super(new RegexWrapper());
	}
}