package com.fr.plugin.chart;

import com.fr.base.FRContext;
import com.fr.design.RestartHelper;
import com.fr.design.extra.PluginConstants;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.IOUtils;
import com.fr.general.Inter;
import com.fr.general.SiteCenter;
import com.fr.general.http.HttpClient;
import com.fr.stable.StableUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shine on 2017/8/21.
 */
public class DownloadOnlineSourcesHelper implements DownloadSourcesEvent {
    // 定义加载窗口大小
    private static final int LOAD_WIDTH = 455;
    private static final int INCIDENT_HEIGHT = 15;
    private static final int LOAD_HEIGHT = 295;
    private static final int PERCENT = 100;

    //进度显示界面
    private JDialog dialog;
    //进度条
    private JProgressBar progressbar;

    private List<SiteInfo> list = new ArrayList<>();
    //安装结果
    private boolean result = true;
    //链接服务器的客户端
    private HttpClient httpClient;

    //总共字节数
    private double totalBytes = 0;


    private static final double PHANTOM_MB = 96.1 * 1024 * 1024;

    public void addPhantomSiteInfo() {
        this.addSiteInfo("plugin.phantomjs", "/assist/phantomjs", PHANTOM_MB);
    }

    private static final double MAP_JSON_MB = 3.8 * 1024 * 1024;

    public void addMapJSONSiteInfo() {
        this.addSiteInfo("map.json", "/assets/map", MAP_JSON_MB);
    }

    public void addSiteInfo(String siteKind, String localDir, double megaBits) {
        if (new File(FRContext.getCurrentEnv().getPath() + localDir).exists()) {
            //本地有这个资源，不下载
            return;
        }
        httpClient = new HttpClient(SiteCenter.getInstance().acquireUrlByKind(siteKind));
        if (httpClient.getResponseCode() != HttpURLConnection.HTTP_OK) {
            //服务器连不上，不下载
            return;
        }
        totalBytes += megaBits;
        list.add(new SiteInfo(siteKind, localDir));
    }

    public void installOnline() {

        int choose = JOptionPane.showConfirmDialog(null, Inter.getLocText("FR-Designer-Download_Online_Sources"), null, JOptionPane.YES_NO_OPTION);

        if (choose == JOptionPane.OK_OPTION) {
            initDialog();

            dialog.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    //取消下载
                    result = false;
                    exitDialog();
                }

                public void windowOpened(WindowEvent e) {
                    downloadAndInstallPluginDependenceFile();
                    exitDialog();
                }

            });

            dialog.setVisible(true);
        }
    }

    /**
     * 下载和安装不分开是因为，本地如果只安装好了一个依赖，下次就不需要重复下载了
     * 如果下载依赖后不安装，则后面的插件会把前面的插件覆盖，故而下载好了一个安装一个
     *
     * @return
     * @throws Exception
     */
    private void downloadAndInstallPluginDependenceFile() {
        try {
            double currentBytesRead = 0;

            for (int i = 0; i < list.size(); i++) {
                SiteInfo siteInfo = list.get(i);

                httpClient = new HttpClient(SiteCenter.getInstance().acquireUrlByKind(siteInfo.siteKind));
                if (httpClient.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream reader = httpClient.getResponseStream();
                    String temp = StableUtils.pathJoin(PluginConstants.DOWNLOAD_PATH, PluginConstants.TEMP_FILE);
                    File file = new File(temp);
                    StableUtils.makesureFileExist(file);
                    FileOutputStream writer = new FileOutputStream(temp);
                    byte[] buffer = new byte[PluginConstants.BYTES_NUM];
                    int bytesRead;
                    while ((bytesRead = reader.read(buffer)) > 0 && result) {
                        writer.write(buffer, 0, bytesRead);
                        buffer = new byte[PluginConstants.BYTES_NUM];

                        currentBytesRead += bytesRead;
                        setProgress(currentBytesRead);
                    }
                    reader.close();
                    writer.flush();
                    writer.close();


                    if (result) {
                        //安装文件
                        IOUtils.unZipFilesGBK(temp, FRContext.getCurrentEnv().getPath() + siteInfo.localDir);
                    }
                } else {
                    result = false;
                }
            }
        } catch (Exception e) {
            result = false;
        }
    }


    private void initDialog() {

        // 创建标签,并在标签上放置一张图片
        BufferedImage image = IOUtils.readImage("/com/fr/plugin/chart/background.png");
        ImageIcon imageIcon = new ImageIcon(image);
        UILabel label = new UILabel(imageIcon);
        label.setBounds(0, 0, LOAD_WIDTH, LOAD_HEIGHT);

        progressbar = new JProgressBar();
        // 显示当前进度值信息
        progressbar.setStringPainted(true);
        // 设置进度条边框不显示
        progressbar.setBorderPainted(false);
        // 设置进度条的前景色
        progressbar.setForeground(new Color(0x38aef5));
        // 设置进度条的背景色
        progressbar.setBackground(new Color(188, 190, 194));
        progressbar.setBounds(0, LOAD_HEIGHT, LOAD_WIDTH, INCIDENT_HEIGHT);
        progressbar.setMinimum(0);
        progressbar.setMaximum((int) totalBytes);
        setProgress(0);

        dialog = new JDialog();
        dialog.setTitle(Inter.getLocText("FR-Designer-Dependence_Install_Online"));

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.add(label, BorderLayout.CENTER);
        contentPane.add(progressbar, BorderLayout.SOUTH);
        dialog.getContentPane().add(contentPane);


        dialog.setModal(true);
        dialog.setResizable(true);
        dialog.setSize(LOAD_WIDTH, LOAD_HEIGHT + INCIDENT_HEIGHT);
        dialog.setResizable(false);
        GUICoreUtils.centerWindow(dialog);

        dialog.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    private void setProgress(double current) {
        progressbar.setValue((int) current);
        progressbar.setString((int) (current / totalBytes * PERCENT) + "%");
        progressbar.paintImmediately(new Rectangle(0, 0, LOAD_WIDTH, INCIDENT_HEIGHT * 2));
    }


    private void exitDialog() {
        dialog.dispose();

        if (result) {
            int choose = JOptionPane.showConfirmDialog(null, Inter.getLocText("FR-Designer_Work_After_Restart_Designer"), null, JOptionPane.YES_NO_OPTION);

            if (choose == JOptionPane.OK_OPTION) {
                RestartHelper.restart();
            }
        } else {
            JOptionPane.showMessageDialog(null, Inter.getLocText("FR-Designer-Dependence_Install_Failed"));
        }
    }

    @Override
    public void downloadSources() {
        this.addMapJSONSiteInfo();
        this.addPhantomSiteInfo();
        this.installOnline();
    }

    private class SiteInfo {
        String siteKind;
        String localDir;

        SiteInfo(String siteKind, String localDir) {
            this.siteKind = siteKind;
            this.localDir = localDir;
        }
    }

}


