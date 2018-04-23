package com.fr.design.gui.iprogressbar;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class FRProgressBar {
	private ProgressMonitor monitor;
	private SwingWorker worker;

	public FRProgressBar(SwingWorker worker, Component parentComponent, Object message, String note, int min, int max) {
		monitor = new ProgressMonitor(parentComponent, message, note, min, max);
		monitor.setProgress(0);
		monitor.setMillisToDecideToPopup(0);
		this.worker = worker;

		this.worker.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				int progress = FRProgressBar.this.worker.getProgress();
				monitor.setProgress(progress);

				if (ismonitorCanceled()) {
					FRProgressBar.this.worker.cancel(true);
				}
			}
		});
	}

	public void start() {
		this.worker.execute();
	}

	public void close() {
		monitor.close();
	}

    public boolean ismonitorCanceled() {
        return monitor.isCanceled();
    }
}