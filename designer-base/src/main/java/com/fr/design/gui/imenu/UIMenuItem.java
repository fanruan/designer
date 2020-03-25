package com.fr.design.gui.imenu;

import com.fr.design.constants.UIConstants;
import com.fr.stable.StringUtils;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JMenuItem;

public class UIMenuItem extends JMenuItem{
	public UIMenuItem() {
		this(StringUtils.BLANK);
	}

	public UIMenuItem(String string) {
		this(string, null);

	}

	public UIMenuItem(String string, Icon pageSmallIcon) {
		super(string, pageSmallIcon);
		setBackground(UIConstants.NORMAL_BACKGROUND);
		setUI(new UIMenuItemUI());
	}

    public UIMenuItem(String string, int key) {
        super(string, key);
        setBackground(UIConstants.NORMAL_BACKGROUND);
        setUI(new UIMenuItemUI());
    }

	public UIMenuItem(Action action) {
		this();
		setAction(action);
	}

	@Override
	public String getText() {
		return StringUtils.BLANK + super.getText();
	}
 
}