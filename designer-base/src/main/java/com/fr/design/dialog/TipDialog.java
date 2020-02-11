package com.fr.design.dialog;

import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.i18n.Toolkit;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.IOUtils;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author hades
 * @version 10.0
 * Created by hades on 2020/1/8
 */
public abstract class TipDialog extends JDialog implements ActionListener {

    private UIButton endButton;
    private UIButton cancelButton;

    public TipDialog(Frame parent, String type) {
        super(parent, true);
        JPanel northPane = FRGUIPaneFactory.createBorderLayout_L_Pane();
        JPanel iconPane = new JPanel();
        UILabel iconLabel = new UILabel();
        iconLabel.setIcon(IOUtils.readIcon("com/fr/design/images/error/error.png"));
        iconPane.add(iconLabel);
        iconPane.setPreferredSize(new Dimension(100, 100));
        JPanel tipPane = FRGUIPaneFactory.createBorderLayout_L_Pane();
        UILabel tipLabel = new UILabel(Toolkit.i18nText("Fine-Design_Last_Designer_Process_Not_Exist"));
        tipPane.add(tipLabel);
        northPane.add(iconPane, BorderLayout.WEST);
        northPane.add(tipPane, BorderLayout.CENTER);

        JTextArea area = new JTextArea(type);
        area.setPreferredSize(new Dimension(400, 100));
        area.setEnabled(true);
        area.setEditable(false);
        JPanel centerPane = FRGUIPaneFactory.createBorderLayout_L_Pane();
        centerPane.add(area);

        JPanel southPane = FRGUIPaneFactory.createBorderLayout_L_Pane();
        JPanel controlPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        endButton = new UIButton(Toolkit.i18nText("Fine-Design_End_Occupied_Process"));
        endButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                endEvent();
            }
        });
        buttonPane.add(endButton);
        cancelButton = new UIButton(Toolkit.i18nText("Fine-Design_Basic_Cancel"));
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelEvent();
            }
        });
        buttonPane.add(cancelButton);
        controlPane.add(buttonPane, BorderLayout.EAST);
        southPane.add(controlPane);

        this.setTitle(Toolkit.i18nText("Fine-Design_Basic_Error_Tittle"));
        this.setResizable(false);
        this.add(northPane, BorderLayout.NORTH);
        this.add(centerPane, BorderLayout.CENTER);
        this.add(southPane, BorderLayout.SOUTH);
        this.setSize(new Dimension(600, 500));
        GUICoreUtils.centerWindow(this);

    }

    protected abstract void endEvent();

    protected abstract void cancelEvent();

    @Override
    public void actionPerformed(ActionEvent e) {
        dispose();
    }

}
