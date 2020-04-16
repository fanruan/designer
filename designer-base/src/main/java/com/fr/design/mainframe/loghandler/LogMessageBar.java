package com.fr.design.mainframe.loghandler;

import com.fr.design.constants.UIConstants;
import com.fr.design.gui.ilable.UILabel;


import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LogMessageBar extends JPanel {

	private static final String LOG_MARK = com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Log");

	private UILabel messageLabel;
    private int width = 600;
	private static volatile LogMessageBar THIS;
	private JFrame dlg = new LogDetailPane().showDialog();

	public static LogMessageBar getInstance() {
		if (THIS == null) {
			synchronized (LogMessageBar.class) {
				if (THIS == null) {
					THIS = new LogMessageBar();
				}
			}
		}
		return THIS;
	}

    public static LogMessageBar getInstance(int width) {
        LogMessageBar bar = LogMessageBar.getInstance();
        bar.setLoggerBarWidth(width);
        return bar;
    }

	private LogMessageBar() {
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
		messageLabel.setText(LOG_MARK + " | " + message);
		repaint();
	}

    public void setLoggerBarWidth(int width) {
        this.width = width;
    }

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(width, 24);
	}

	/**
	 * 销毁内置的日志面板,外部插件会用到
	 */
	public void disposeLogDialog() {
		if (dlg != null && dlg.isShowing()) {
			dlg.dispose();
		}
		THIS = null;
	}
}