package com.fr.design.gui.itextfield;

import java.awt.Graphics;

import javax.swing.plaf.metal.MetalTextFieldUI;
import javax.swing.text.JTextComponent;

import de.muntjak.tinylookandfeel.Theme;

public class TextFieldUI extends MetalTextFieldUI {


	protected void paintBackground(Graphics g) {
		JTextComponent editor = getComponent();
		// We will only be here if editor is opaque, so we don't have to test

		if(editor.isEnabled()) {
			if(editor.isEditable()) {
				g.setColor(editor.getBackground());
			}
			else {
				// not editable
				if(editor.getBackground().equals(Theme.textBgColor[Theme.style].getColor())) {
					// set default panel background
					g.setColor(Theme.backColor[Theme.style].getColor());
				}
				else {
					// color changed by user - set textfield background
					g.setColor(editor.getBackground());
				}
			}

			g.fillRect(0, 0, editor.getWidth(), editor.getHeight());
		}
		else {
			if(editor.getBackground().equals(Theme.textBgColor[Theme.style].getColor())) {
				g.setColor(Theme.textDisabledBgColor[Theme.style].getColor());
			}
			else {
				// color changed by user - set textfield background
				g.setColor(editor.getBackground());
			}
			
			g.fillRect(0, 0, editor.getWidth(), editor.getHeight());
			
			if(Theme.style != Theme.YQ_STYLE) return;
			
			g.setColor(Theme.backColor[Theme.style].getColor());
			g.drawRect(1, 1, editor.getWidth() - 3, editor.getHeight() - 3);
			g.drawRect(2, 2, editor.getWidth() - 5, editor.getHeight() - 5);
		}
	}
}