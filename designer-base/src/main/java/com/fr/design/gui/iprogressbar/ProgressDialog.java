package com.fr.design.gui.iprogressbar;

import com.fr.design.constants.UIConstants;
import com.fr.design.dialog.UIDialog;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.os.OSBasedAction;
import com.fr.design.os.OSSupportCenter;
import com.fr.design.os.impl.SupportOSImpl;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.FRFont;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.plaf.ColorUIResource;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;

/**
 * 加载进度弹窗
 */
public class ProgressDialog extends UIDialog {
    private JProgressBar progressBar;
    private JDialog centerDialog;
    private JLabel text;

    public ProgressDialog(Frame parent) {
        super(parent);
        setUndecorated(true);
        setSize(parent.getSize());
        setLocationRelativeTo(null);
        OSSupportCenter.buildAction(new OSBasedAction() {
            @Override
            public void execute() {
                setOpacity(0.5f);
            }
        }, SupportOSImpl.OPACITY);
        initComponent();
    }

    private void initComponent() {

        centerDialog = new JDialog(this);
        centerDialog.setSize(new Dimension(482, 124));
        centerDialog.setUndecorated(true);
        GUICoreUtils.centerWindow(centerDialog);
        JPanel panel = new JPanel();
        panel.setBorder(new UIProgressBorder(3, UIConstants.DEFAULT_BG_RULER, 14, 46, 47, 37, 47));
        panel.setLayout(new BorderLayout(4, 15));
        progressBar = new JProgressBar();
        progressBar.setUI(new ModernUIProgressBarUI());
        progressBar.setBorderPainted(false);
        progressBar.setOpaque(false);
        progressBar.setBorder(null);
        progressBar.setMaximum(1000);
        panel.add(progressBar, BorderLayout.CENTER);
        text = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Loading_Project"), JLabel.CENTER);
        FRFont font = FRFont.getInstance().applySize(14).applyForeground(new ColorUIResource(333334));
        text.setFont(font);
        panel.add(text, BorderLayout.SOUTH);
        panel.setVisible(true);
        centerDialog.getContentPane().add(panel);
    }

    @Override
    public void checkValid() throws Exception {

    }

    @Override
    public void setVisible(boolean b) {
        centerDialog.setVisible(b);
        super.setVisible(b);
    }

    public void setProgressValue(int value) {
        progressBar.setValue(value);
    }

    public void setProgressMaximum(int value) {
        progressBar.setMaximum(value);
    }

    public int getProgressMaximum() {
        return progressBar.getMaximum();
    }

    @Override
    public void dispose() {
        centerDialog.dispose();
        super.dispose();
    }

    public void updateLoadingText(String text) {
        this.text.setText(text);
    }
}
