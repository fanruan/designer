package com.fr.start;

import com.fr.design.RestartHelper;
import com.fr.design.dialog.ErrorDialog;
import com.fr.design.dialog.FineJOptionPane;
import com.fr.design.i18n.Toolkit;
import com.fr.design.mainframe.messagecollect.StartErrorMessageCollector;
import com.fr.design.mainframe.messagecollect.entity.DesignerErrorMessage;
import com.fr.exit.DesignerExiter;
import com.fr.general.IOUtils;
import com.fr.io.utils.ResourceIOUtils;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.StableUtils;
import com.fr.stable.lifecycle.ErrorType;
import com.fr.stable.lifecycle.FineLifecycleFatalError;
import com.fr.stable.project.ProjectConstants;

import javax.swing.*;

/**
 * @author hades
 * @version 10.0
 * Created by hades on 2020/3/16
 */
public class LifecycleFatalErrorHandler {

    private static final LifecycleFatalErrorHandler INSTANCE = new LifecycleFatalErrorHandler();

    public static LifecycleFatalErrorHandler getInstance() {
        return INSTANCE;
    }


    public void handle(FineLifecycleFatalError fatal) {
        SplashContext.getInstance().hide();
        if (ErrorType.FINEDB.equals(fatal.getErrorType())) {
            StartErrorMessageCollector.getInstance().record(DesignerErrorMessage.FINEDB_PROBLEM.getId(),
                                                            DesignerErrorMessage.FINEDB_PROBLEM.getMessage(),
                                                            fatal.getMessage());
            FineLoggerFactory.getLogger().error(DesignerErrorMessage.FINEDB_PROBLEM.getId() + ": " + DesignerErrorMessage.FINEDB_PROBLEM.getMessage());
            int result = FineJOptionPane.showOptionDialog(null,
                                                          Toolkit.i18nText("Fine-Design_Error_Finedb_Backup_Reset"),
                                                          Toolkit.i18nText("Fine-Design_Basic_Error_Tittle"),
                                                          JOptionPane.YES_NO_OPTION,
                                                          JOptionPane.ERROR_MESSAGE,
                                                          IOUtils.readIcon("com/fr/design/images/error/error2.png"),
                                                          new Object[] {Toolkit.i18nText("Fine-Design_Basic_Reset"), Toolkit.i18nText("Fine-Design_Basic_Cancel")},
                                                          null);
            if (result == JOptionPane.YES_OPTION) {
                boolean success = false;
                try {
                    ResourceIOUtils.copy(StableUtils.pathJoin(ProjectConstants.EMBED_DB_DIRECTORY, ProjectConstants.FINE_DB_NAME),
                                         StableUtils.pathJoin(ProjectConstants.EMBED_DB_DIRECTORY, ProjectConstants.FINE_DB_BAK_NAME));
                    success = ResourceIOUtils.delete(StableUtils.pathJoin(ProjectConstants.EMBED_DB_DIRECTORY, ProjectConstants.FINE_DB_NAME));
                } catch (Exception e) {
                    FineLoggerFactory.getLogger().error(e.getMessage(), e);
                    afterBackupFailed();
                }
                if (!success) {
                    afterBackupFailed();
                } else {
                    RestartHelper.restart();
                }
            } else {
                DesignerExiter.getInstance().execute();
            }
        }
        FineLoggerFactory.getLogger().error(fatal.getMessage(), fatal);
        StartErrorMessageCollector.getInstance().record(DesignerErrorMessage.UNEXCEPTED_START_FAILED.getId(),
                                                        DesignerErrorMessage.UNEXCEPTED_START_FAILED.getMessage(),
                                                        fatal.getMessage());
        ErrorDialog dialog = new ErrorDialog(null, Toolkit.i18nText("Fine-Design_Error_Start_Apology_Message"),
                                             Toolkit.i18nText("Fine-Design_Error_Start_Report"),
                                             fatal.getMessage()) {
            @Override
            protected void okEvent() {
                dispose();
                DesignerExiter.getInstance().execute();
            }

            @Override
            protected void restartEvent() {
                dispose();
                RestartHelper.restart();
            }
        };
        dialog.setVisible(true);
    }

    private void afterBackupFailed() {
        FineJOptionPane.showMessageDialog(null,
                                          Toolkit.i18nText("Fine-Design_Error_Finedb_Backup_Reset_Result",
                                                           ResourceIOUtils.getRealPath(StableUtils.pathJoin(ProjectConstants.EMBED_DB_DIRECTORY, ProjectConstants.FINE_DB_NAME))),
                                          Toolkit.i18nText("Fine-Design_Basic_Error"),
                                          JOptionPane.ERROR_MESSAGE);
        DesignerExiter.getInstance().execute();
    }


}
