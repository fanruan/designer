package com.fr.design.mainframe.widget.accessibles;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;

import com.fr.design.mainframe.widget.editors.ITextComponent;
import com.fr.design.mainframe.widget.renderer.GenericCellRenderer;

public class RendererField  extends JComponent implements ITextComponent{
	private GenericCellRenderer renderer;

	public RendererField(GenericCellRenderer renderer) {
		this.setLayout(new BorderLayout());
		this.renderer = renderer;
		add(renderer, BorderLayout.CENTER);
	}

	@Override
	public void addActionListener(ActionListener l) {

	}

	@Override
	public String getText() {
		return null;
	}

	@Override
	public void selectAll() {

	}

	@Override
	public void setEditable(boolean editable) {
		this.setEnabled(editable);
	}

	@Override
	public void setText(String text) {

	}

	@Override
	public void setValue(Object v) {
		renderer.setValue(v);
		this.repaint();
	}
}