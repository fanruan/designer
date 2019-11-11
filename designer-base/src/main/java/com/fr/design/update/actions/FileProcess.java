package com.fr.design.update.actions;

import com.fr.decision.update.info.UpdateCallBack;
import com.fr.decision.update.UpdateExecutor;
import com.fr.log.FineLoggerFactory;

import javax.swing.*;
import java.util.concurrent.ExecutionException;

/**
 * @author Bryant
 * @version 10.0
 * Created by Bryant on 2019-09-12
 */
public abstract class FileProcess extends SwingWorker<Boolean, Void> {

    private UpdateCallBack callBack;

    public FileProcess(UpdateCallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    protected Boolean doInBackground() throws Exception {
        return UpdateExecutor.getInstance().execute(callBack);
    }

    @Override
    protected void done() {
        boolean success = false;
        try {
            success = get();
        } catch (InterruptedException e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
        if (success) {
            onDownloadSuccess();
        } else {
            onDownloadFailed();
        }
    }

    /**
     * 下载成功
     */
    public abstract void onDownloadSuccess();

    /**
     * 下载失败
     */
    public abstract void onDownloadFailed();
}
