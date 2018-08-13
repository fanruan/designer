/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.actions.cell.style;

import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.fr.base.BaseUtils;
import com.fr.base.Style;
import com.fr.base.core.StyleUtils;
import com.fr.design.actions.core.ActionFactory;
import com.fr.general.ComparatorUtils;

import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.style.color.UIToolbarColorButton;

/**
 * Foreground.
 */
public class ReportFontForegroundAction extends AbstractStyleAction implements ChangeListener {
	public ReportFontForegroundAction(ElementCasePane t) {
		super(t);

		this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Foreground"));
	}

	public void stateChanged(ChangeEvent evt) {
		this.actionPerformedUndoable();
	}

	@Override
	public Style executeStyle(Style style, Style defStyle) {
		Object object = this.getValue(UIToolbarColorButton.class.getName());
		if (object != null && object instanceof UIToolbarColorButton) {
			Color selectedColor = ((UIToolbarColorButton) object).getColor();
			if (style.getFRFont() != null &&
					ComparatorUtils.equals(selectedColor, style.getFRFont())) {
				return style;
			}

			style = StyleUtils.setReportFontForeground(style, selectedColor);
		}

		return style;
	}

	@Override
	public JComponent createToolBarComponent() {
		Object object = this.getValue(UIToolbarColorButton.class.getName());
		if (object == null || !(object instanceof UIToolbarColorButton)) {
			UIToolbarColorButton tbButton = new UIToolbarColorButton(BaseUtils.readIcon("/com/fr/design/images/gui/color/foreground.png"));
			this.putValue(UIToolbarColorButton.class.getName(), tbButton);
			tbButton.set4Toolbar();
			tbButton.setEnabled(this.isEnabled());
			tbButton.addColorChangeListener(this);

			//peter:产生tooltip
			tbButton.setToolTipText(ActionFactory.createButtonToolTipText(this));
			return tbButton;
		}

		return (JComponent) object;
	}
}