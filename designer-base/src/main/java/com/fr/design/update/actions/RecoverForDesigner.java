package com.fr.design.update.actions;

import com.fr.decision.update.backup.Recover;
import com.fr.decision.update.data.LibPathManager;
import com.fr.decision.update.data.UpdateConstants;
import com.fr.decision.update.exception.UpdateException;
import com.fr.design.mainframe.DesignerContext;
import com.fr.general.CommonIOUtils;
import com.fr.general.GeneralUtils;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.CommonUtils;
import com.fr.stable.StableUtils;
import com.fr.stable.project.ProjectConstants;

import java.io.File;
import java.io.IOException;

/**
 * @author Bryant
 * @version 10.0
 * Created by Bryant on 2019-10-09
 */
public class RecoverForDesigner implements Recover {

    private final String installHome = StableUtils.getInstallHome();

    @Override
    public boolean recover() {
        try{
            CommonIOUtils.copyFilesInDirByPath(StableUtils.pathJoin(installHome, ProjectConstants.LOGS_NAME,
                    UpdateConstants.INSTALL_LIB, UpdateConstants.DESIGNERBACKUPPATH),
                    StableUtils.pathJoin(StableUtils.getInstallHome(), ProjectConstants.LIB_NAME));
            return true;
        } catch (IOException ignore) {
            FineLoggerFactory.getLogger().error("Recover error for designer");
            return false;
        }
    }

    @Override
    public boolean backup() {
        //jar包备份文件的目录为"backup/"+jar包当前版本号
        String todayBackupDir = StableUtils.pathJoin(installHome, UpdateConstants.DESIGNER_BACKUP_DIR, (GeneralUtils.readBuildNO()));
        String envHome = LibPathManager.getInstance().getEnvHome();
        backupFilesFromInstallEnv(envHome, todayBackupDir);
        backupFilesFromInstallLib(installHome, todayBackupDir);
        try {
            String installBackup = StableUtils.pathJoin(installHome, ProjectConstants.LOGS_NAME,
                    UpdateConstants.INSTALL_LIB);
            File installLib = new File(installBackup);
            CommonUtils.mkdirs(installLib);
            File download = new File(StableUtils.pathJoin(installBackup,UpdateConstants.DOWNLOADPATH));
            CommonUtils.mkdirs(download);
            CommonIOUtils.copyFilesInDirByPath(StableUtils.pathJoin(installHome,ProjectConstants.LIB_NAME),
                    StableUtils.pathJoin(installBackup,UpdateConstants.DESIGNERBACKUPPATH));
            DesignerContext.getDesignerFrame().prepareForExit();
            return true;
        } catch (IOException e) {
            UpdateException exception = new UpdateException("Backup Exception for designer" + e.getMessage());
            FineLoggerFactory.getLogger().error(exception.getMessage(),exception);
            return false;
        }
    }

    private void backupFilesFromInstallEnv(String envHome, String todayBackupDir) {
        try {
            File file = new File(StableUtils.pathJoin(todayBackupDir,UpdateConstants.BACKUPPATH));
            CommonUtils.mkdirs(file);
            file = new File(StableUtils.pathJoin(envHome,ProjectConstants.LIB_NAME));
            File[] files = file.listFiles();
            File dir = new File(StableUtils.pathJoin(todayBackupDir,UpdateConstants.BACKUPPATH));
            if (files != null) {
                for (File file1 : files) {
                    if (file1.getName().startsWith(UpdateConstants.FINE) && file1.getName().endsWith(UpdateConstants.JAR_FILE_SUFFIX)) {
                        CommonIOUtils.copy(file1, dir);
                    }
                }
            }
        } catch (IOException e) {
            UpdateException exception = new UpdateException(e.getMessage());
            FineLoggerFactory.getLogger().error(exception.getMessage() + "backup for Designer recover in env failed");
        }
    }

    private void backupFilesFromInstallLib(String installHome, String todayBackupDir) {
        try {
            CommonUtils.mkdirs(new File(StableUtils.pathJoin(todayBackupDir,UpdateConstants.DESIGNERBACKUPPATH)));
            File file = new File(StableUtils.pathJoin(installHome,ProjectConstants.LIB_NAME));
            File[] files = file.listFiles();
            File dir = new File(StableUtils.pathJoin(todayBackupDir,UpdateConstants.DESIGNERBACKUPPATH));
            if (files != null) {
                for (File file1 : files) {
                    if (file1.getName().startsWith(UpdateConstants.FINE) || file1.getName().contains(UpdateConstants.ASPECTJRT)) {
                        CommonIOUtils.copy(file, dir);
                    }
                }
            }
        } catch (IOException e) {
            UpdateException exception = new UpdateException(e.getMessage());
            FineLoggerFactory.getLogger().error(exception.getMessage() + "backup for Designer recover in install failed");
        }
    }
}
