package com.fr.design.onlineupdate.actions;

import com.fr.design.onlineupdate.domain.UpdateConstants;
import com.fr.locale.InterProviderFactory;
import com.fr.log.FineLoggerFactory;
import com.fr.design.onlineupdate.domain.DownloadItem;
import com.fr.stable.ArrayUtils;
import com.fr.stable.StableUtils;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;

/**
 * Created by XINZAI on 2018/8/21.
 */
public abstract class FileDownloader extends SwingWorker<Boolean, DownloadItem> {
    private static final int REPEAT_DOWNLOAD_TIMES = 3;
    private DownloadItem[] files;
    private String saveDir;
    //已经完成的大小
    private long completeSize;

    public FileDownloader(DownloadItem[] files, String saveDir) {
        this.files = files;
        this.saveDir = saveDir;
    }

    @Override
    protected Boolean doInBackground() throws Exception {
        if (ArrayUtils.isNotEmpty(files)) {
            setCompleteSize(0L);
            for (DownloadItem item : files) {
                for (int i = 0; i < REPEAT_DOWNLOAD_TIMES; i++) {
                    item.setTotalLength(0);
                    item.setDownloadLength(0);
                    download(item);
                    if (item.getTotalLength() == item.getDownloadLength()) {
                        break;
                    }
                }
                if (item.getTotalLength() != item.getDownloadLength()) {
                    JOptionPane.showMessageDialog(null,
                            com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Updater_Download_Failed"),
                            InterProviderFactory.getProvider().getLocText("Fine-Design_Updater_Alert"), JOptionPane.ERROR_MESSAGE);
                    return false;
                } else {
                    item.setDownloadLength(0);
                    completeSize += item.getTotalLength();
                }
            }
        }
        return true;
    }

    @Override
    protected void done() {
        boolean success = false;
        try {
            success = get();
        } catch (InterruptedException e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        } catch (ExecutionException e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
        if (success) {
            onDownloadSuccess();
        } else {
            onDownloadFailed();
        }
    }

    private void download(DownloadItem item) throws Exception {
        URL url = new URL(item.getUrl());
        URLConnection connection = url.openConnection();
        int total = connection.getContentLength();
        item.setTotalLength(total);
        InputStream reader = connection.getInputStream();
        File tempFile = new File(StableUtils.pathJoin(saveDir, item.getName()));
        StableUtils.makesureFileExist(tempFile);
        FileOutputStream writer = new FileOutputStream(tempFile);
        byte[] buffer = new byte[UpdateConstants.BYTE];
        int bytesRead = 0;
        int totalBytesRead = 0;
        while ((bytesRead = reader.read(buffer)) != -1) {
            writer.write(buffer, 0, bytesRead);
            buffer = new byte[UpdateConstants.BYTE];
            totalBytesRead += bytesRead;
            item.setDownloadLength(totalBytesRead);
            publish(item);
        }
        reader.close();
        writer.close();
    }

    /**
     * 下载成功
     */
    public abstract void onDownloadSuccess();

    /**
     * 下载失败
     */
    public abstract void onDownloadFailed();

    public long getCompleteSize() {
        return completeSize;
    }

    public void setCompleteSize(long completeSize) {
        this.completeSize = completeSize;
    }
}