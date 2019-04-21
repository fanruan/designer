package com.fr.design.mainframe.vcs.ui;

import com.fr.design.dialog.UIDialog;
import com.fr.design.editor.editor.DateEditor;
import com.fr.design.gui.date.UIDatePicker;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import static com.fr.workspace.server.vcs.common.Constants.EMPTY_BORDER;
import static com.fr.workspace.server.vcs.common.Constants.EMPTY_BORDER_BOTTOM;


public class FileVersionDialog extends UIDialog {
    private UIButton okBtn = new UIButton("确定");
    private UIButton cancelBtn = new UIButton("取消");


    public FileVersionDialog(Frame frame) {
        super(frame);
        setUndecorated(true);
        JPanel panel = new JPanel(new BorderLayout());
        Box box0 = Box.createHorizontalBox();
        box0.setBorder(EMPTY_BORDER_BOTTOM);
        box0.add(new UILabel("生成日期"));
        box0.add(Box.createHorizontalGlue());
        box0.add(new DateEditor(new Date(), true, "生成日期", UIDatePicker.STYLE_CN_DATE1));
        Box box1 = Box.createHorizontalBox();
        box1.setBorder(EMPTY_BORDER_BOTTOM);
        box1.add(new UILabel("备注关键词 "));
        box1.add(new UITextField());
        Box box2 = Box.createHorizontalBox();
        box2.add(Box.createHorizontalGlue());
        box2.setBorder(EMPTY_BORDER);
        box2.add(okBtn);
        box2.add(cancelBtn);
        okBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileVersionDialog.this.setVisible(false);
            }
        });
        cancelBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileVersionDialog.this.setVisible(false);
            }
        });
        panel.add(box0, BorderLayout.NORTH);
        panel.add(box1, BorderLayout.CENTER);
        panel.add(box2, BorderLayout.SOUTH);
        add(panel);
        setSize(new Dimension(220, 100));
        centerWindow(this);
    }

    private void centerWindow(Window window) {
        window.setLocation(0, 95);

    }

    @Override
    public void checkValid() throws Exception {

    }
}
