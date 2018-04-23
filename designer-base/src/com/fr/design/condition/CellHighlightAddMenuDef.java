package com.fr.design.condition;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.fr.base.BaseUtils;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.imenu.UIPopupMenu;
import com.fr.design.menu.MenuDef;
import com.fr.design.utils.gui.GUICoreUtils;

/**
 * Define Menu.
 */
public class CellHighlightAddMenuDef extends MenuDef {
	public CellHighlightAddMenuDef() {
	}

	public CellHighlightAddMenuDef(String name) {
		this.setName(name);
	}

	public CellHighlightAddMenuDef(String name, char mnemonic) {
		this.setName(name);
		this.setMnemonic(mnemonic);
	}

	// 生成UIButton,iconpath不可为null
	public UIButton createUIButton() {
		if (createdButton == null) {
			if (this.iconPath != null && this.name != null) {
				createdButton = new UIButton(BaseUtils.readIcon(this.iconPath));
			} else if (this.iconPath != null) {
				createdButton = new UIButton(BaseUtils.readIcon(this.iconPath));
			} else {
				createdButton = new UIButton(this.name);
			}
			createdButton.addMouseListener(mouseListener);
		}

		return createdButton;
	}

	private MouseListener mouseListener = new MouseAdapter() {
		public void mouseReleased(MouseEvent evt) {
			Object source = evt.getSource();
			UIButton button = (UIButton) source;
			if (!button.isEnabled()) {
				return;
			}

			UIPopupMenu popupMenu = new UIPopupMenu();
			popupMenu.setInvoker(button);
			CellHighlightAddMenuDef.this.updatePopupMenu(popupMenu);

			GUICoreUtils.showPopupMenu(popupMenu, button, 0, button.getSize().height);
		}
		
		public void mouseExited(MouseEvent evt) {
			if (createdButton != null) {
				createdButton.setEnabled(true);
			}
		}
	};
}