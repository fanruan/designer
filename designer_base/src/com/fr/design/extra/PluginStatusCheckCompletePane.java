package com.fr.design.extra;

import com.fr.design.gui.ibutton.UIButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author richie
 * @date 2015-03-11
 * @since 8.0
 */
public abstract class PluginStatusCheckCompletePane extends PluginAbstractViewPane {

    private UIButton installButton;
    private JProgressBar progressBar;


    public PluginStatusCheckCompletePane() {
        setLayout(new BorderLayout());
        JComponent controlPane = centerPane();
        add(controlPane, BorderLayout.CENTER);
        JPanel pane = createOperationPane();
        add(pane, BorderLayout.SOUTH);
        installButton = new UIButton(textForInstallButton());
        installButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pressInstallButton();
            }
        });
        installButton.setEnabled(false);
        UIButton installFromDiskButton = new UIButton(textForInstallFromDiskButton());
        installFromDiskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pressInstallFromDiskButton();
            }
        });
        pane.add(installButton);
        pane.add(installFromDiskButton);

        progressBar = new JProgressBar();
        progressBar.setVisible(false);
        progressBar.setIndeterminate(false);
        progressBar.setStringPainted(true);
        progressBar.setPreferredSize(new Dimension(600, 20));
        pane.add(progressBar);
    }


    public abstract void pressInstallButton();

    public abstract void pressInstallFromDiskButton();

    public abstract String textForInstallButton();

    public abstract String textForInstallFromDiskButton();

    public abstract JComponent centerPane();

    public void setInstallButtonEnable(boolean enable) {
        installButton.setEnabled(enable);
    }

    public void setProgress(double percent) {
        progressBar.setVisible(true);
        progressBar.setValue((int)percent);
        progressBar.setString((int)percent + "%");
        progressBar.repaint();
    }

    public void didTaskFinished() {
        progressBar.setVisible(false);
    }
}