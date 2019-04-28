package com.fr.design.mainframe.vcs.ui;

import com.fr.design.dialog.UIDialog;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.icontainer.UIScrollPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextarea.UITextArea;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.report.entity.VcsEntity;
import com.fr.workspace.WorkContext;
import com.fr.workspace.server.vcs.VcsOperator;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * 编辑版本信息面板
 */
public class EditFileVersionDialog extends UIDialog {

    private final UITextArea msgTestArea = new UITextArea();
    private final UILabel versionLabel = new UILabel();
    private VcsEntity entity;

    public EditFileVersionDialog(VcsEntity entity) {
        this(DesignerContext.getDesignerFrame());
        this.entity = entity;
        msgTestArea.setText(entity.getCommitMsg());
        versionLabel.setText(String.valueOf(entity.getVersion()));
    }

    private EditFileVersionDialog(Frame parent) {
        super(parent);

        initComponents();
        setModal(true);
        setTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Vcs_Save_Version"));
        setSize(300, 220);
        setResizable(false);
        GUICoreUtils.centerWindow(this);

    }

    private void initComponents() {

        JPanel fontPane = new JPanel(new BorderLayout());
        fontPane.add(new UILabel("   " + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Vcs_Version_Message") + "："), BorderLayout.NORTH);

        msgTestArea.setBorder(null);
        UIScrollPane scrollPane = new UIScrollPane(msgTestArea);

        Component[][] components = new Component[][]{
                new Component[]{new UILabel("   " + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Vcs_Version_Number") + "："), versionLabel},
                new Component[]{fontPane, scrollPane}
        };
        double[] rowSizes = new double[]{25, 100};
        double[] columnSizes = new double[]{70, 200};

        add(TableLayoutHelper.createTableLayoutPane(components, rowSizes, columnSizes), BorderLayout.CENTER);

        JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        add(buttonPane, BorderLayout.SOUTH);

        UIButton ok = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_OK"));
        UIButton cancel = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Utils_Design_Action_Cancel"));

        buttonPane.add(ok);
        buttonPane.add(cancel);

        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                entity.setCommitMsg(msgTestArea.getText());
                WorkContext.getCurrent().get(VcsOperator.class).updateVersion(entity);
                setVisible(false);
            }
        });

        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doCancel();
            }
        });
    }

    @Override
    public void checkValid() throws Exception {

    }
}
