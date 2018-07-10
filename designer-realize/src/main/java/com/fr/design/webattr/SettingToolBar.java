package com.fr.design.webattr;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import com.fr.design.gui.ilable.UILabel;
import javax.swing.JPanel;

import com.fr.base.BaseUtils;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.general.Inter;
import com.fr.design.utils.gui.GUICoreUtils;

public  class SettingToolBar extends JPanel {
	private Icon setIcon = BaseUtils.readIcon("com/fr/design/images/toolbarbtn/toolbarbtnsetting.png");
	private Icon delIcon = BaseUtils.readIcon("com/fr/design/images/toolbarbtn/toolbarbtnclear.png");
	private UIButton setButton;
	private UIButton delButton;
	private ToolBarPane toolBarPane;

	public SettingToolBar(String name,ToolBarPane toolBarPane) {
		super();
		this.setBackground(Color.lightGray);
		this.add(new UILabel(name));
		this.toolBarPane = toolBarPane;
		setButton = GUICoreUtils.createTransparentButton(setIcon, setIcon, setIcon);
		setButton.setToolTipText(Inter.getLocText("Edit_Button_ToolBar"));
		setButton.setAction(new SetAction());
		delButton = GUICoreUtils.createTransparentButton(delIcon, delIcon, delIcon);
		delButton.setToolTipText(Inter.getLocText("Remove_Button_ToolBar"));
		delButton.setAction(new DelAction());
		this.add(setButton);
		this.add(delButton);
	}

	public void setEnabled(boolean b) {
		setButton.setEnabled(b);
		delButton.setEnabled(b);
	}
	
	public void addActionListener(ActionListener l){
		setButton.addActionListener(l);
		delButton.addActionListener(l);
	}

	private class SetAction extends AbstractAction {

		public SetAction() {
			this.putValue(Action.SMALL_ICON, setIcon);
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			final EditToolBar tb = new EditToolBar();
			tb.populate(toolBarPane.getFToolBar());
			BasicDialog dialog = tb.showWindow(DesignerContext.getDesignerFrame());
			dialog.addDialogActionListener(new DialogActionAdapter() {
				public void doOk() {
					toolBarPane.setFToolBar(tb.update());
				}
			});
			dialog.setVisible(true);
		}
	}

	private class DelAction extends AbstractAction {

		public DelAction() {
			this.putValue(Action.SMALL_ICON, delIcon);
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			toolBarPane.removeAll();
			toolBarPane.removeButtonList();
			toolBarPane.repaint();
		}
	}
}