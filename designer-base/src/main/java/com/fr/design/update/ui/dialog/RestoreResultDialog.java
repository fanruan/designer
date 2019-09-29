package com.fr.design.update.ui.dialog;

import com.fr.design.RestartHelper;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.update.domain.UpdateConstants;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.ComparatorUtils;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.ProductConstants;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;
import com.fr.stable.project.ProjectConstants;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by XINZAI on 2018/8/21.
 */
public class RestoreResultDialog extends JDialog {
    private static final Dimension RESTORE = new Dimension(340, 100);

    private static final Dimension RESTORE_OLD_VERSION = new Dimension(340, 135);

    private String jarRestoreDir;

    public RestoreResultDialog(Dialog parent, boolean modal) {
        super(parent, modal);
        initCommonComponents();
    }

    public RestoreResultDialog(Frame parent, boolean modal, String jarDir) {
        super(parent, modal);
        this.jarRestoreDir = jarDir;
        if (ComparatorUtils.equals(jarDir, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Updater_Restore_Old_Version"))) {
            initOldVersionRestoreComps();
        } else {
            initCommonComponents();
        }
    }

    private void initCommonComponents() {
        this.setResizable(false);
        JPanel pane = new JPanel();
        pane.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        pane.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.setContentPane(pane);

        UIButton restartButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Updater_Restart_Designer"));
        UIButton restartLaterButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Updater_Restart_Later"));

        restartButton.setFont(new Font("Default", Font.PLAIN, 12));
        restartButton.setEnabled(false);
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RestartHelper.restart();
            }
        });
        restartLaterButton.setFont(new Font("Default", Font.PLAIN, 12));
        restartLaterButton.setEnabled(false);
        restartLaterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        JPanel buttonPane = new JPanel();
        buttonPane.add(restartLaterButton);
        buttonPane.add(restartButton);
        pane.add(buttonPane, BorderLayout.SOUTH);

        JPanel progressLabelPane = new JPanel(new BorderLayout());
        UILabel jarProgressLabel = new UILabel((com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Updater_Restore_To")) + " " + jarRestoreDir + " " + (com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Updater_WorksAfterRestart")));
        jarProgressLabel.setFont(new Font("Default", Font.PLAIN, 12));
        jarProgressLabel.setVisible(true);
        progressLabelPane.add(jarProgressLabel);
        pane.add(progressLabelPane, BorderLayout.CENTER);
        deletePreviousPropertyFile();
        putJarBackupFiles();
        restartButton.setEnabled(true);
        restartLaterButton.setEnabled(true);
        this.setSize(RESTORE);
        this.setTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Updater_Jar_Restore"));
    }

    public static void deletePreviousPropertyFile() {
        //在进行更新升级之前确保move和delete.properties删除
        File moveFile = new File(RestartHelper.MOVE_FILE);
        File delFile = new File(RestartHelper.RECORD_FILE);
        if ((moveFile.exists()) && (!moveFile.delete())) {
            FineLoggerFactory.getLogger().error(RestartHelper.MOVE_FILE + "delete failed!");
        }
        if ((delFile.exists()) && (!delFile.delete())) {
            FineLoggerFactory.getLogger().error(RestartHelper.RECORD_FILE + "delete failed!");
        }
    }

    private void initOldVersionRestoreComps() {
        this.setResizable(false);
        JPanel pane = new JPanel();
        pane.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        pane.setLayout(FRGUIPaneFactory.createBorderLayout());
        this.setContentPane(pane);

        UIButton okButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Updater_Ok"));
        okButton.setFont(new Font("Default", Font.PLAIN, 12));
        okButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        JPanel buttonPane = new JPanel();
        buttonPane.add(okButton);
        pane.add(buttonPane, BorderLayout.SOUTH);

        JPanel infoPane = new JPanel(new BorderLayout());
        JTextArea jTextArea = new JTextArea(
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Updater_Already_Backup_Old_Project")
                        + StringUtils.BLANK
                        + StableUtils.pathJoin(StableUtils.getInstallHome(), UpdateConstants.DESIGNER_BACKUP_DIR)
                        + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Updater_Unzip_Replace_Restore")
        );
        jTextArea.setLineWrap(true);
        jTextArea.setEditable(false);
        jTextArea.setBackground(null);
        jTextArea.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
        jTextArea.setFont(new Font("Default", Font.PLAIN, 12));
        infoPane.add(jTextArea);
        pane.add(infoPane, BorderLayout.CENTER);

        this.setSize(RESTORE_OLD_VERSION);
        this.setTitle(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Updater_Restore_to_V8"));
    }


    /**
     * 显示窗口
     */
    public void showDialog() {
        GUICoreUtils.centerWindow(this);
        this.setVisible(true);
    }

    private void putJarBackupFiles() {
        Map<String, String> map = new HashMap<>();
        java.util.List<String> list = new ArrayList<>();
        String installHome = StableUtils.getInstallHome();

        putJarBackupFilesToInstallLib(installHome, map, list);
        putJarBackupFilesToInstallEnv(installHome, map, list);
        RestartHelper.saveFilesWhichToMove(map);
        RestartHelper.saveFilesWhichToDelete(list.toArray(new String[list.size()]));
    }

    private void putJarBackupFilesToInstallLib(String installHome, Map<String, String> map, java.util.List<String> list) {
        List<String> files = UpdateConstants.JARS_FOR_DESIGNER_X;
        String backupDir = UpdateConstants.DESIGNER_BACKUP_DIR;
        for (String file : files) {
            map.put(StableUtils.pathJoin(installHome, backupDir, jarRestoreDir, file),
                    StableUtils.pathJoin(installHome, ProjectConstants.LIB_NAME, file));
            list.add(StableUtils.pathJoin(installHome, ProjectConstants.LIB_NAME, file));
        }
    }

    private void putJarBackupFilesToInstallEnv(String installHome, Map<String, String> map, java.util.List<String> list) {
        List<String> files = UpdateConstants.JARS_FOR_SERVER_X;
        String backupDir = UpdateConstants.DESIGNER_BACKUP_DIR;
        for (String file : files) {
            map.put(StableUtils.pathJoin(installHome, backupDir, jarRestoreDir, file),
                    StableUtils.pathJoin(installHome, UpdateConstants.APPS_FOLDER_NAME, ProductConstants.getAppFolderName(), ProjectConstants.WEBINF_NAME, ProjectConstants.LIB_NAME, file));
            list.add(StableUtils.pathJoin(installHome, UpdateConstants.APPS_FOLDER_NAME, ProductConstants.getAppFolderName(), ProjectConstants.WEBINF_NAME, ProjectConstants.LIB_NAME, file));
        }
    }
}