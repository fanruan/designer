package com.fr.design.update.ui.dialog;

import com.fr.decision.update.data.UpdateConstants;
import com.fr.design.RestartHelper;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.ComparatorUtils;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;
import com.fr.stable.project.ProjectConstants;
import com.fr.workspace.WorkContext;

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

    RestoreResultDialog(Frame parent, boolean modal, String jarDir) {
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

    static boolean deletePreviousPropertyFile() {
        File moveFile = new File(RestartHelper.MOVE_FILE);
        File delFile = new File(RestartHelper.RECORD_FILE);
        if (StableUtils.mkdirs(moveFile) && StableUtils.mkdirs(delFile)) {
            return StableUtils.deleteFile(moveFile) && StableUtils.deleteFile(delFile);
        }
        return false;
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
        List<String> list = new ArrayList<>();
        String installHome = StableUtils.getInstallHome();

        filesToMove(installHome, map);
        filesToDelete(installHome, list);
        RestartHelper.saveFilesWhichToMove(map);
        RestartHelper.saveFilesWhichToDelete(list.toArray(new String[list.size()]));
    }

    private void filesToMove(String installHome, Map<String, String> map) {
        String backupDir = UpdateConstants.DESIGNER_BACKUP_DIR;
        String envHome = WorkContext.getCurrent().getPath();
        File installLib = new File(StableUtils.pathJoin(installHome, backupDir, jarRestoreDir, UpdateConstants.DESIGNERBACKUPPATH));
        File envLib = new File(StableUtils.pathJoin(installHome, backupDir, jarRestoreDir, UpdateConstants.BACKUPPATH));
        File[] files;
        if (installLib.exists() && envLib.exists()) {
            files = installLib.listFiles();
            if (files != null) {
                for (File file : files) {
                    map.put(file.getAbsolutePath(),
                            StableUtils.pathJoin(installHome, ProjectConstants.LIB_NAME, file.getName()));
                }
            }
            files = envLib.listFiles();
            if (files != null) {
                for (File file : files) {
                    map.put(file.getAbsolutePath(),
                            StableUtils.pathJoin(envHome, ProjectConstants.LIB_NAME, file.getName()));
                }
            }
        } else {
            installLib = new File(StableUtils.pathJoin(installHome, backupDir, jarRestoreDir));
            files = installLib.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.getName().contains(UpdateConstants.DESIGNER) || file.getName().equals(UpdateConstants.ASPECTJRT)) {
                        map.put(file.getAbsolutePath(),
                                StableUtils.pathJoin(installHome, ProjectConstants.LIB_NAME, file.getName()));
                    } else {
                        map.put(file.getAbsolutePath(),
                                StableUtils.pathJoin(envHome, ProjectConstants.LIB_NAME, file.getName()));
                    }
                }
            }
        }
    }

    private void filesToDelete(String installHome, List<String> list) {
        String envHome = WorkContext.getCurrent().getPath();
        File installEnv = new File(StableUtils.pathJoin(envHome,ProjectConstants.LIB_NAME));
        File[] files = installEnv.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().startsWith(UpdateConstants.FINE)) {
                    list.add(StableUtils.pathJoin(envHome, ProjectConstants.LIB_NAME, file.getName()));
                }
            }
        }
        installEnv = new File(StableUtils.pathJoin(installHome,ProjectConstants.LIB_NAME));
        files = installEnv.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().startsWith(UpdateConstants.FINE) || file.getName().equals(UpdateConstants.ASPECTJRT)) {
                    list.add(StableUtils.pathJoin(installHome, ProjectConstants.LIB_NAME, file.getName()));
                }
            }
        }
    }
}