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
import com.fr.base.background.ColorBackground;
import com.fr.design.actions.core.ActionFactory;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.style.color.UIToolbarColorButton;

/**
 * Background.
 */
public class StyleBackgroundAction extends AbstractStyleAction implements ChangeListener {
	public StyleBackgroundAction(ElementCasePane t) {
		super(t);
		
        this.setName(Inter.getLocText("Background"));
    }

    public void stateChanged(ChangeEvent evt) {
		this.actionPerformedUndoable();
	}

    @Override
	public Style executeStyle(Style style, Style defStyle) {
        Object object = this.getValue(UIToolbarColorButton.class.getName());
        if (object != null && object instanceof UIToolbarColorButton) {
            Color selectedColor = ((UIToolbarColorButton) object).getColor();
            if (style.getBackground() != null &&
                    style.getBackground() instanceof ColorBackground &&
                    ComparatorUtils.equals(selectedColor,
                            ((ColorBackground) style.getBackground()).getColor())) {
                return style;
            }

            style = style.deriveBackground(ColorBackground.getInstance(selectedColor));
        }

        return style;
    }
    
	@Override
        public boolean isFontStye() {
            return false;
        }

        @Override
        public JComponent createToolBarComponent() {
            Object object = this.getValue(UIToolbarColorButton.class.getName());
            if (object == null || !(object instanceof UIToolbarColorButton)) {
                UIToolbarColorButton tbButton = new UIToolbarColorButton(BaseUtils.readIcon("/com/fr/design/images/gui/color/background.png"));
                tbButton.set4Toolbar();
                this.putValue(UIToolbarColorButton.class.getName(), tbButton);

                tbButton.setEnabled(this.isEnabled());
                tbButton.setColor(Color.WHITE);
                tbButton.addColorChangeListener(this);

            //peter:产生tooltip
            tbButton.setToolTipText(ActionFactory.createButtonToolTipText(this));
            return tbButton;
        }

        return (JComponent) object;
    }
}