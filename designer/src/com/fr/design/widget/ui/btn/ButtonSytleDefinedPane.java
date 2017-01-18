package com.fr.design.widget.ui.btn;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;

import com.fr.base.background.ColorBackground;
import com.fr.design.gui.ilable.UILabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import com.fr.base.background.ImageBackground;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.BasicPane;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.style.background.BackgroundButtonPane;
import com.fr.form.ui.FreeButton;
import com.fr.general.Background;
import com.fr.general.Inter;

public class ButtonSytleDefinedPane extends BasicPane {

//	private UIComboBox buttonStyleComboBox;
//	private JPanel card;
//	private CardLayout cardLayout;
	private BackgroundPane initBackgroundPane;
	private BackgroundPane overBackgroundPane;
	private BackgroundPane clickBackgroundPane;
	private Background initBackground;
	private Background overBackground;
	private Background clickBackground;

	public ButtonSytleDefinedPane() {
		this.initComponents();
	}

	protected void initComponents() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());

		JPanel buttonStylePane = new JPanel();
		buttonStylePane.setLayout(new BorderLayout());
		initBackgroundPane = new BackgroundPane(Inter.getLocText("Background-Initial") + ":", Inter.getLocText("The_initial_background_of_the_button"));
		overBackgroundPane = new BackgroundPane(Inter.getLocText("Background-Over") + ":", Inter.getLocText("Mouse_move-background"));
		clickBackgroundPane = new BackgroundPane(Inter.getLocText("Background-Click") + ":",  Inter.getLocText("Mouse_move-background"));

		JPanel table = FRGUIPaneFactory.createYBoxEmptyBorderPane();
		table.setBorder(new TitledBorder(Inter.getLocText(new String[]{"Custom", "Form-Button", "Style"})));
		table.add(initBackgroundPane);
		table.add(overBackgroundPane);
		table.add(clickBackgroundPane);
		buttonStylePane.add(table, BorderLayout.WEST);

		this.add(buttonStylePane, BorderLayout.CENTER);

	}

	public void populate(FreeButton button) {
		if (button == null) {
			return;
		}
		initBackgroundPane.populate(button.getInitialBackground());
		overBackgroundPane.populate(button.getOverBackground());
		clickBackgroundPane.populate(button.getClickBackground());
	}

	public FreeButton update(FreeButton button) {
		button.setCustomStyle(true);
		button.setInitialBackground(initBackgroundPane.update());
		button.setOverBackground(overBackgroundPane.update());
		button.setClickBackground(clickBackgroundPane.update());

		return button;
	}

	@Override
	protected String title4PopupWindow() {
		return null;
	}

	class BackgroundPane extends JPanel {
		private UIButton editButton;
		private BackgroundButtonPane choosePane;
		private Background background;
		private UILabel ImagePreviewPane;

		BackgroundPane(String text, String ToolTips) {
			this.setLayout(FRGUIPaneFactory.createLabelFlowLayout());
			this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 150));

			UILabel label = new UILabel(text);
			label.setToolTipText(ToolTips);
			label.setPreferredSize(new Dimension(100, 20));
			this.add(label);

			ImagePreviewPane = new UILabel();
			ImagePreviewPane.setPreferredSize(new Dimension(100, 20));
			this.add(ImagePreviewPane);

			editButton = new UIButton(Inter.getLocText("Edit"));
			editButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (choosePane == null) {
						choosePane = new BackgroundButtonPane();
					}
					BasicDialog dlg = choosePane.showWindow(SwingUtilities
							.getWindowAncestor(ButtonSytleDefinedPane.this));
					dlg.addDialogActionListener(new DialogActionAdapter() {
						@Override
						public void doOk() {
							populate(choosePane.update());
						}
					});
					if(BackgroundPane.this.background == null){
						BackgroundPane.this.background = new ColorBackground();
					}
					choosePane.populate((Background) BackgroundPane.this.background);
					dlg.setVisible(true);
				}
			});
			this.add(editButton);
		}
		
		public void populate(Background background) {
			this.background = background;

			if (background instanceof ImageBackground && ((ImageBackground) background).getImage() != null) {
				ImagePreviewPane.setIcon(new ImageIcon(((ImageBackground) background).getImage()));
			} else if(background instanceof ColorBackground && ((ColorBackground) background).getColor() != null){
				ImagePreviewPane.setIcon(null);
				ImagePreviewPane.setOpaque(true);
				ImagePreviewPane.setBackground(((ColorBackground) background).getColor());
			}else{
				ImagePreviewPane.setIcon(null);
				ImagePreviewPane.setOpaque(false);
				ImagePreviewPane.setBackground(null);
			}
		}

		public Background update() {
			return background;
		}
	}
}