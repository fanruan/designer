package com.fr.design.mainframe.loghandler;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;

import com.fr.design.constants.UIConstants;
import com.fr.design.gui.ilable.UILabel;
import com.fr.general.Inter;

import javax.swing.JPanel;

public class LogMessageBar extends JPanel {
	private UILabel messageLabel;
    private int width = 600;
	public static LogMessageBar THIS;
	private JFrame dlg = new LogDetailPane().showDialog();

	public static LogMessageBar getInstance() {
		if (THIS == null) {
			THIS = new LogMessageBar();
		}
		return THIS;
	}

    public static LogMessageBar getInstance(int width) {
        LogMessageBar bar = LogMessageBar.getInstance();
        bar.setLoggerBarWidth(width);
        return bar;
    }

	public LogMessageBar() {
		messageLabel = new UILabel();
		setLayout(new BorderLayout());
		add(messageLabel, BorderLayout.CENTER);
		setBackground(UIConstants.LOG_MESSAGE_BAR_BACKGROUND);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (dlg != null && dlg.isVisible()) {
					dlg.setExtendedState(JFrame.NORMAL);
					return;
				}
				dlg = new LogDetailPane().showDialog();
				dlg.setVisible(true);
			}
		});
	}

	public void setMessage(String message) {
		if (message == null) {
			return;
		}
		messageLabel.setText(Inter.getLocText("Log") + " | " + message);
		repaint();
	}

    public void setLoggerBarWidth(int width) {
        this.width = width;
    }

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(width, 24);
	}
}