package com.fr.design.dialog;

import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.i18n.Toolkit;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.FRFont;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author hades
 * @version 10.0
 * Created by hades on 2020/1/8
 */
public abstract class ErrorDialog extends JDialog implements ActionListener {

    private UIButton okButton;
    private UIButton restartButton;


    public ErrorDialog(Frame parent, String message, String title, String detail) {
        super(parent, true);
        JPanel northPane = FRGUIPaneFactory.createBorderLayout_L_Pane();
        JPanel messagePane = FRGUIPaneFactory.createVerticalFlowLayout_S_Pane(true);
        UILabel boldFontLabel = new UILabel(message);
        UILabel label = new UILabel(Toolkit.i18nText("Fine-Design_Send_Report_To_Us"));
        Font font = FRFont.getInstance("Dialog", Font.BOLD, 20);
        boldFontLabel.setFont(font);
        messagePane.add(boldFontLabel);
        messagePane.add(label);
        northPane.add(messagePane);

        JTextArea area = new JTextArea(detail);
        area.setPreferredSize(new Dimension(400, 100));
        area.setEnabled(true);
        area.setEditable(false);
        JPanel centerPane = FRGUIPaneFactory.createBorderLayout_L_Pane();
        UILabel detailLabel = new UILabel(Toolkit.i18nText("Fine-Design_Problem_Detail_Message"));
        centerPane.add(detailLabel, BorderLayout.NORTH);
        centerPane.add(area, BorderLayout.CENTER);

        JPanel southPane = FRGUIPaneFactory.createBorderLayout_L_Pane();
        JPanel controlPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        okButton = new UIButton(Toolkit.i18nText("Fine-Design_Basic_Ok"));
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                okEvent();
            }
        });
        buttonPane.add(okButton);
        restartButton = new UIButton(Toolkit.i18nText("Fine-Design_Basic_Restart"));
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restartEvent();
            }
        });
        buttonPane.add(restartButton);
        controlPane.add(buttonPane, BorderLayout.EAST);
        southPane.add(controlPane);

        this.setTitle(title);
        this.setResizable(false);
        this.add(northPane, BorderLayout.NORTH);
        this.add(centerPane, BorderLayout.CENTER);
        this.add(southPane, BorderLayout.SOUTH);
        this.setSize(new Dimension(600, 500));
        GUICoreUtils.centerWindow(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        dispose();
    }

    protected abstract void okEvent();

    protected abstract void restartEvent();

}
