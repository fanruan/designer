package com.fr.design.mainframe.vcs.ui;

import com.fr.design.dialog.FineJOptionPane;
import com.fr.design.gui.frpane.UITextPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.i18n.Toolkit;
import com.fr.design.mainframe.DesignerFrameFileDealerPane;
import com.fr.design.mainframe.vcs.common.VcsHelper;
import com.fr.log.FineLoggerFactory;
import com.fr.report.entity.VcsEntity;
import com.fr.stable.StringUtils;
import com.fr.workspace.WorkContext;
import com.fr.workspace.server.vcs.VcsOperator;

import javax.swing.Box;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;


public class FileVersionRowPanel extends JPanel {

    private VcsEntity vcsEntity;
    private UILabel versionLabel = new UILabel();
    private UILabel usernameLabel = new UILabel(StringUtils.EMPTY, VcsHelper.VCS_USER_PNG, SwingConstants.LEFT);
    private UITextPane msgLabel = new UITextPane();
    private UILabel timeLabel = new UILabel();
    private EditFileVersionDialog editDialog;


    public FileVersionRowPanel() {
        setLayout(new BorderLayout());

        // version + username
        Box upPane = Box.createHorizontalBox();
        upPane.setBorder(VcsHelper.EMPTY_BORDER_MEDIUM);
        upPane.add(versionLabel);
        upPane.add(Box.createHorizontalGlue());


        // msg
        msgLabel.setBorder(VcsHelper.EMPTY_BORDER_MEDIUM);
        msgLabel.setOpaque(false);
        msgLabel.setBackground(new Color(0, 0, 0, 0));
        msgLabel.setEditable(false);

        // confirm + delete + edit
        UIButton confirmBtn = new UIButton(VcsHelper.VCS_REVERT);
        confirmBtn.set4ToolbarButton();
        confirmBtn.setToolTipText(Toolkit.i18nText("Fine-Design_Vcs_Revert"));
        confirmBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if (FineJOptionPane.showConfirmDialog(null, Toolkit.i18nText("Fine-Design_Vcs_Version_Revert_Confirm"), Toolkit.i18nText("Fine-Design_Vcs_Version_Revert_Title"),
                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    vcsEntity.setUsername(VcsHelper.getInstance().getCurrentUsername());
                    WorkContext.getCurrent().get(VcsOperator.class).rollbackTo(vcsEntity);
                    FileVersionsPanel.getInstance().exitVcs(vcsEntity.getFilename());
                }
            }
        });
        UIButton deleteBtn = new UIButton(VcsHelper.VCS_DELETE_PNG);
        deleteBtn.set4ToolbarButton();
        deleteBtn.setToolTipText(Toolkit.i18nText("Fine-Design_Vcs_Delete"));
        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if (FineJOptionPane.showConfirmDialog(null, Toolkit.i18nText("Fine-Design_Vcs_Delete-Confirm"), Toolkit.i18nText("Fine-Design_Vcs_Remove"),
                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    try {
                        WorkContext.getCurrent().get(VcsOperator.class).deleteVersion(vcsEntity.getFilename(), vcsEntity.getVersion());
                    } catch (Exception e) {
                        FineLoggerFactory.getLogger().error(e.getMessage());
                    }
                    FileVersionTable table = (FileVersionTable) (FileVersionRowPanel.this.getParent());
                    String path = DesignerFrameFileDealerPane.getInstance().getSelectedOperation().getFilePath();
                    try {
                        table.updateModel(table.getSelectedRow() - 1, WorkContext.getCurrent().get(VcsOperator.class).getVersions(path.replaceFirst("/", "")));
                    } catch (Exception e) {
                        FineLoggerFactory.getLogger().error(e.getMessage());
                    }
                }
            }
        });
        UIButton editBtn = new UIButton(VcsHelper.VCS_EDIT_PNG);
        editBtn.set4ToolbarButton();
        editBtn.setToolTipText(Toolkit.i18nText("Fine-Design_Vcs_Edit"));
        editBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showEditDialog();

            }
        });
        upPane.add(editBtn);
        upPane.add(confirmBtn);
        upPane.add(deleteBtn);
        Box downPane = Box.createHorizontalBox();
        downPane.add(usernameLabel);
        downPane.setBorder(VcsHelper.EMPTY_BORDER_BOTTOM);
        downPane.add(Box.createHorizontalGlue());
        timeLabel.setForeground(VcsHelper.COPY_VERSION_BTN_COLOR);
        downPane.add(timeLabel);
        add(upPane, BorderLayout.NORTH);
        add(msgLabel, BorderLayout.CENTER);
        add(downPane, BorderLayout.SOUTH);
    }

    private void showEditDialog() {
        this.editDialog = new EditFileVersionDialog(vcsEntity);

        editDialog.setVisible(true);
        update(vcsEntity);
    }


    public void update(final VcsEntity fileVersion) {
        this.vcsEntity = fileVersion;
        versionLabel.setText(String.format("V.%s", fileVersion.getVersion()));
        usernameLabel.setText(fileVersion.getUsername());
        msgLabel.setText(StringUtils.EMPTY);
        timeLabel.setText(timeStr(fileVersion.getTime()));
        try {
            StyledDocument doc = msgLabel.getStyledDocument();
            Style style = msgLabel.getLogicalStyle();
            StyleConstants.setForeground(style, Color.BLACK);
            doc.insertString(doc.getLength(), " " + fileVersion.getCommitMsg(), style);
        } catch (BadLocationException e) {
            FineLoggerFactory.getLogger().error(e.getMessage());
        }
    }

    private String timeStr(Date time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(time);
    }

    public VcsEntity getVcsEntity() {
        return vcsEntity;
    }
}
