package com.fr.design.mainframe.widget.renderer;

import com.fr.design.mainframe.widget.wrappers.TreeModelWrapper;

public class TreeModelRenderer extends EncoderCellRenderer {
	public TreeModelRenderer() {
		super(new TreeModelWrapper());
	}
}