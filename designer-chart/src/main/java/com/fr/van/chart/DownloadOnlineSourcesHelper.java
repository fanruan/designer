package com.fr.van.chart;

import com.fr.chart.base.ChartConstants;
import com.fr.design.RestartHelper;
import com.fr.design.dialog.FineJOptionPane;
import com.fr.design.extra.PluginConstants;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.CloudCenter;
import com.fr.general.IOUtils;
import com.fr.general.http.HttpClient;
import com.fr.plugin.chart.DownloadSourcesEvent;
import com.fr.stable.CommonUtils;
import com.fr.stable.StableUtils;
import com.fr.workspace.WorkContext;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

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

    private static final double MAP_JSON_MB = 4.5 * 1024 * 1024;

    public void addMapJSONSiteInfo() {
        this.addSiteInfo("map10.json", ChartConstants.MAP_JSON_URL, MAP_JSON_MB);
    }

    public void addSiteInfo(String siteKind, String localDir, double megaBits) {

        if (new File(StableUtils.pathJoin(WorkContext.getCurrent().getPath(), localDir)).exists()) {
            //本地有这个资源，不下载
            return;
        }
        httpClient = new HttpClient(CloudCenter.getInstance().acquireUrlByKind(siteKind));
        if (httpClient.getResponseCode() != HttpURLConnection.HTTP_OK) {
            //服务器连不上，不下载
            return;
        }
        totalBytes += megaBits;
        list.add(new SiteInfo(siteKind, localDir));
    }

    public void installOnline() {

        int choose = JOptionPane.showConfirmDialog(null, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Download_Online_Sources"),
                com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Confirm"), JOptionPane.YES_NO_OPTION);

        if (choose == JOptionPane.OK_OPTION) {
            initDialog();

            dialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    //取消下载
                    result = false;
                    exitDialog();
                }

                @Override
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

                httpClient = new HttpClient(CloudCenter.getInstance().acquireUrlByKind(siteInfo.siteKind));
                if (httpClient.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String temp = StableUtils.pathJoin(PluginConstants.DOWNLOAD_PATH, PluginConstants.TEMP_FILE);
                    File file = new File(temp);
                    StableUtils.makesureFileExist(file);
                    try (InputStream reader = httpClient.getResponseStream();
                         FileOutputStream writer = new FileOutputStream(temp)) {
                        byte[] buffer = new byte[PluginConstants.BYTES_NUM];
                        int bytesRead;
                        while ((bytesRead = reader.read(buffer)) > 0 && result) {
                            writer.write(buffer, 0, bytesRead);
                            buffer = new byte[PluginConstants.BYTES_NUM];

                            currentBytesRead += bytesRead;
                            setProgress(currentBytesRead);
                        }
                        writer.flush();
                    }
                    if (result) {
                        //安装文件
                        IOUtils.unZipFilesGBK(temp, StableUtils.pathJoin(WorkContext.getCurrent().getPath(), siteInfo.localDir));
                        CommonUtils.deleteFile(file);
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
        BufferedImage image = IOUtils.readImage("/com/fr/van/chart/background.png");
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
        dialog.setTitle(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Dependence_Install_Online"));

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
            int choose = FineJOptionPane.showConfirmDialog(null, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Work_After_Restart_Designer"),
                    com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Confirm"), JOptionPane.YES_NO_OPTION);

            if (choose == JOptionPane.OK_OPTION) {
                RestartHelper.restart();
            }
        } else {
            FineJOptionPane.showMessageDialog(null, com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Chart_Dependence_Install_Failed"));
        }
    }

    @Override
    public void downloadSources() {
        this.addMapJSONSiteInfo();
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


