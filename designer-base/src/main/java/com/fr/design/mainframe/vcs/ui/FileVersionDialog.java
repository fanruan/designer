package com.fr.design.mainframe.vcs.ui;

import com.fr.design.dialog.UIDialog;
import com.fr.design.editor.editor.DateEditor;
import com.fr.design.gui.date.UIDatePicker;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.i18n.Toolkit;
import com.fr.design.mainframe.vcs.common.VcsHelper;
import com.fr.report.entity.VcsEntity;
import com.fr.stable.StringUtils;
import com.fr.workspace.WorkContext;
import com.fr.workspace.server.vcs.VcsOperator;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;



public class FileVersionDialog extends UIDialog {
    public static final long DELAY = 24 * 60 * 60 * 1000;
    private UIButton okBtn;
    private UIButton cancelBtn;
    private DateEditor dateEditor;
    private UITextField textField;


    public FileVersionDialog(Frame frame, final String fileName) {
        super(frame);
        setUndecorated(true);
        setModal(true);
        JPanel panel = new JPanel(new BorderLayout());
        Box upBox = Box.createHorizontalBox();
        upBox.setBorder(VcsHelper.EMPTY_BORDER);
        upBox.add(new UILabel(Toolkit.i18nText("Fine-Design_Vcs_buildTime") + "    "));
        upBox.add(Box.createHorizontalGlue());
        dateEditor = new DateEditor(new Date(), true, StringUtils.EMPTY, UIDatePicker.STYLE_CN_DATE1);
        upBox.add(dateEditor);
        Box downBox = Box.createHorizontalBox();
        downBox.setBorder(VcsHelper.EMPTY_BORDER);
        downBox.add(new UILabel(Toolkit.i18nText("Fine-Design_Vcs_CommitMsg") + " "));
        textField = new UITextField();
        downBox.add(textField);
        JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        okBtn = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_OK"));
        cancelBtn = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Utils_Design_Action_Cancel"));
        buttonPane.setBorder(VcsHelper.EMPTY_BORDER);
        buttonPane.add(okBtn);
        buttonPane.add(cancelBtn);
        okBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileVersionDialog.this.setVisible(false);
                Date date = dateEditor.getValue();
                List<VcsEntity> vcsEntities = WorkContext.getCurrent().get(VcsOperator.class).getFilterVersions(fileName, date, new Date(date.getTime() + DELAY), textField.getText());
                FileVersionTable.getInstance().updateModel(1, vcsEntities);

            }
        });
        cancelBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileVersionDialog.this.setVisible(false);
            }
        });
        panel.add(upBox, BorderLayout.NORTH);
        panel.add(downBox, BorderLayout.CENTER);
        panel.add(buttonPane, BorderLayout.SOUTH);
        add(panel);
        setSize(new Dimension(230, 105));
        centerWindow(this);
    }

    private void centerWindow(Window window) {
        window.setLocation(0, 95);

    }

    @Override
    public void checkValid() throws Exception {

    }

}
