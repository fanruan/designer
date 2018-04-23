package com.fr.design.gui.itextfield;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;


/**
 * Number Field.
 */
public class EditTextField extends UIGridTextField {
	private int maxLength = 24;

	public EditTextField() {
		this(10000);
	}

	public EditTextField(int maxLength) {
		this.maxLength = maxLength;
		setDocument(new TextDocument());
	}

	public int getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	@Override
	public void paint(Graphics g) {
		FRGraphics2D FRg2d = new FRGraphics2D((Graphics2D)g);
		super.paint(FRg2d);
	}

	class TextDocument extends PlainDocument {
		public TextDocument() {
		}

		@Override
		public void insertString(int offset, String s, AttributeSet a) throws BadLocationException {
			String str = getText(0, getLength());
			if (str != null && str.length() > getMaxLength()) {
				Toolkit.getDefaultToolkit().beep();
				return;
			}
			super.insertString(offset, s, a);
		}
	}
}