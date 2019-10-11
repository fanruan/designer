package com.fr.design.update.actions;

import com.fr.decision.update.backup.Recover;
import com.fr.decision.update.data.UpdateConstants;
import com.fr.decision.update.exception.UpdateException;
import com.fr.general.CommonIOUtils;
import com.fr.general.GeneralUtils;
import com.fr.general.IOUtils;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.CommonUtils;
import com.fr.stable.StableUtils;
import com.fr.stable.project.ProjectConstants;
import com.fr.workspace.WorkContext;

import java.io.File;
import java.io.IOException;

/**
 * @author Bryant
 * @version 10.0
 * Created by Bryant on 2019-10-09
 */
public class RecoverForDesigner implements Recover {

    @Override
    public boolean recover() {
        try{
            CommonIOUtils.copyFilesInDirByPath(StableUtils.pathJoin(StableUtils.getInstallHome(), UpdateConstants.DESIGNERBACKUPPATH),
                    StableUtils.pathJoin(StableUtils.getInstallHome(), ProjectConstants.LIB_NAME));
            return true;
        } catch (IOException ignore) {
            FineLoggerFactory.getLogger().error("Recover error for designer");
            return false;
        }
    }

    @Override
    public boolean backup() {
        String installHome = StableUtils.getInstallHome();
        //jar包备份文件的目录为"backup/"+jar包当前版本号
        String todayBackupDir = StableUtils.pathJoin(installHome, UpdateConstants.DESIGNER_BACKUP_DIR, (GeneralUtils.readBuildNO()));
        backupFilesFromInstallEnv(todayBackupDir);
        backupFilesFromInstallLib(installHome, todayBackupDir);
        try {
            File file = new File(StableUtils.pathJoin(installHome, UpdateConstants.DOWNLOADPATH));
            CommonUtils.mkdirs(file);
            IOUtils.copyFilesInDirByPath(StableUtils.pathJoin(installHome,ProjectConstants.LIB_NAME),
                    StableUtils.pathJoin(installHome, UpdateConstants.DESIGNERBACKUPPATH));
            return true;
        }catch (IOException e) {
            UpdateException exception = new UpdateException("Backup Exception for designer" + e.getMessage());
            FineLoggerFactory.getLogger().error(exception.getMessage(),exception);
            return false;
        }
    }

    private void backupFilesFromInstallEnv(String todayBackupDir) {
        try {
            CommonUtils.mkdirs(new File(StableUtils.pathJoin(todayBackupDir,UpdateConstants.BACKUPPATH)));
            IOUtils.copyFilesInDirByPath(
                    StableUtils.pathJoin(WorkContext.getCurrent().getPath(),ProjectConstants.LIB_NAME),
                    StableUtils.pathJoin(todayBackupDir,UpdateConstants.BACKUPPATH));
        } catch (IOException e) {
            UpdateException exception = new UpdateException(e.getMessage());
            FineLoggerFactory.getLogger().error(exception.getMessage() + "backup for Designer recover in env failed");
        }
    }

    private void backupFilesFromInstallLib(String installHome, String todayBackupDir) {
        try {
            CommonUtils.mkdirs(new File(StableUtils.pathJoin(todayBackupDir,UpdateConstants.DESIGNERBACKUPPATH)));
            IOUtils.copyFilesInDirByPath(
                    StableUtils.pathJoin(installHome,ProjectConstants.LIB_NAME),
                    StableUtils.pathJoin(todayBackupDir,UpdateConstants.DESIGNERBACKUPPATH));
        } catch (IOException e) {
            UpdateException exception = new UpdateException(e.getMessage());
            FineLoggerFactory.getLogger().error(exception.getMessage() + "backup for Designer recover in install failed");
        }
    }
}
