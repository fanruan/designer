package com.fr.design.extra.plugindependence;

import com.fr.base.FRContext;
import com.fr.design.extra.PluginConstants;
import com.fr.design.extra.PluginHelper;
import com.fr.design.mainframe.DesignerContext;
import com.fr.general.IOUtils;
import com.fr.general.Inter;
import com.fr.general.SiteCenter;
import com.fr.general.http.HttpClient;
import com.fr.plugin.dependence.PluginDependenceException;
import com.fr.plugin.dependence.PluginDependenceUnit;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.util.Enumeration;
import java.util.List;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

/**
 * Created by hufan on 2016/9/5.
 */
public class DownLoadDependenceUI implements ActionListener {
    //进度显示界面
    private JDialog frame = null;
    //进度条
    private JProgressBar progressbar;
    //进度信息
    private JLabel label;
    //进度条更新时钟
    private Timer timer;
    //是否继续下载
    private boolean flag = true;

    // 定义加载窗口大小
    private final int LOAD_WIDTH = 455;
    private final int LOAD_HEIGHT = 295;

    //安装环境相关信息
    private String currentID;
    private List<PluginDependenceUnit> list = null;
    //安装结果
    private boolean result = false;
    //链接服务器的客户端
    private HttpClient httpClient;
    //已读文件字节数
    private int totalBytesRead = 0;
    //文件总长度
    private int totalSize = 0;

    public DownLoadDependenceUI() {
    }
    public DownLoadDependenceUI(String currentID, List<PluginDependenceUnit> list) {
        this.currentID = currentID;
        this.list = list;
        this.totalSize = getFileLength();
        init();
    }

    private void init() {
        // 创建标签,并在标签上放置一张图片
        BufferedImage image = IOUtils.readImage("/com/fr/design/extra/plugindependence/image/background.png");
        ImageIcon imageIcon = new ImageIcon(image);
        label = new JLabel(imageIcon);
        label.setBounds(0, 0, LOAD_WIDTH, LOAD_HEIGHT - 15);

        progressbar = new JProgressBar();
        // 显示当前进度值信息
        progressbar.setStringPainted(true);
        // 设置进度条边框不显示
        progressbar.setBorderPainted(false);
        // 设置进度条的前景色
        progressbar.setForeground(new Color(0x38aef5));
        // 设置进度条的背景色
        progressbar.setBackground(new Color(188, 190, 194));
        progressbar.setBounds(0, LOAD_HEIGHT - 15, LOAD_WIDTH, 15);
        progressbar.setMinimum(0);
        progressbar.setMaximum(totalSize);
        progressbar.setValue(0);

        timer = new Timer(100, this);

        frame = new JDialog(DesignerContext.getDesignerFrame(), true);
        frame.setTitle(Inter.getLocText("FR-Designer-Dependence_Install_Online"));
        frame.setSize(LOAD_WIDTH, LOAD_HEIGHT);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(screenSize.width / 2 - LOAD_WIDTH / 2, screenSize.height / 2 - LOAD_HEIGHT / 2);
        frame.setResizable(false);
        // 设置布局为空
        frame.setLayout(new BorderLayout(0, 0));
        frame.getContentPane().add(label, BorderLayout.CENTER);
        frame.getContentPane().add(progressbar, BorderLayout.SOUTH);

        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                //取消下载
                flag = false;
                frame.dispose();
            }
        });
    }


    //是否可以连接服务器
    private boolean connectToServer() {
        for (int i = 0; i < list.size(); i++) {
            PluginDependenceUnit dependenceUnit = list.get(i);
            httpClient = new HttpClient(SiteCenter.getInstance().acquireUrlByKind(dependenceUnit.getDependenceID()));
            if (httpClient.getResponseCode() != HttpURLConnection.HTTP_OK){
                return false;
            }
        }
        return true;
    }

    //获取依赖文件大小
    private int getFileLength(){
        int size = 0;
        for (int i = 0; i < list.size(); i++) {
            PluginDependenceUnit dependenceUnit = list.get(i);
            HttpClient httpClient = new HttpClient(SiteCenter.getInstance().acquireUrlByKind(dependenceUnit.getDependenceID()));
            if (httpClient.getResponseCode() == HttpURLConnection.HTTP_OK) {
                size += httpClient.getContentLength();
            }else {
                return -1;
            }
        }
        return size;
    }

    //安装
    private boolean install() {
        //开始时钟
        timer.start();
        //开始下载
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                installDependenceOnline();
            }
        });
        thread.start();

        frame.setVisible(true);
        //等待下载线程处理结束
        try {
            thread.join();
        } catch (InterruptedException e) {
            FRContext.getLogger().error(e.getMessage());
            return false;
        }
        //停止时钟
        timer.stop();
        return result;
    }

    /**
     * 下载和安装不分开是因为，本地如果只安装好了一个依赖，下次就不需要重复下载了
     * 如果下载依赖后不安装，则后面的插件会把前面的插件覆盖，故而下载好了一个安装一个
     * @return
     * @throws Exception
     */
    private void downloadAndInstallPluginDependenceFile() throws Exception {
        totalBytesRead = 0;
        for (int i = 0; i < list.size(); i++) {
            PluginDependenceUnit dependenceUnit = list.get(i);
            httpClient = new HttpClient(SiteCenter.getInstance().acquireUrlByKind(dependenceUnit.getDependenceID()));
            if (httpClient.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream reader = httpClient.getResponseStream();
                String temp = StableUtils.pathJoin(PluginHelper.DEPENDENCE_DOWNLOAD_PATH, PluginHelper.TEMP_FILE);
                StableUtils.makesureFileExist(new File(temp));
                FileOutputStream writer = new FileOutputStream(temp);
                byte[] buffer = new byte[PluginConstants.BYTES_NUM];
                int bytesRead = 0;
                while ((bytesRead = reader.read(buffer)) > 0 && flag) {
                    writer.write(buffer, 0, bytesRead);
                    buffer = new byte[PluginConstants.BYTES_NUM];
                    totalBytesRead += bytesRead;
                }
                reader.close();
                writer.flush();
                writer.close();

                //下载被取消
                if (flag == false) {
                    result = false;
                    throw new PluginDependenceException(Inter.getLocText("FR-Designer-Dependence_Install_Failed"));
                }

                //安装文件
                IOUtils.unZipFilesGBK(temp, FRContext.getCurrentEnv().getPath() + dependenceUnit.getDependenceDir());

            } else {
                result = false;
                throw new PluginDependenceException(Inter.getLocText("FR-Designer-Dependence_Install_Failed"));
            }
        }
        //所有依赖都正常安装下载完毕，则结果为true
        result = true;
    }

    public void installDependenceOnline() {
        try {
            //下载并安装文件
            downloadAndInstallPluginDependenceFile();
        } catch (Exception e) {
            result = false;
            FRContext.getLogger().error(e.getMessage());
        }
    }

    //安装已经下载好的文件,如果是服务文件，则需要复制一份到安装目录下，
    //以便切换远程时，使用本地的服务
    //如果是服务器环境，则只会安装一份
    private void installPluginDependenceFile(List<String> filePathList){
        if (filePathList.isEmpty()){
            result = false;
            return;
        }
        for(int i = 0; i < filePathList.size(); i++) {
            if (StringUtils.EMPTY.equals(filePathList.get(i))){
                result = false;
                return;
            }
            PluginDependenceUnit dependenceUnit = list.get(i);
            IOUtils.unzip(new File(filePathList.get(i)), FRContext.getCurrentEnv().getPath() + dependenceUnit.getDependenceDir());
            result = true;
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == timer) {
            int value = progressbar.getValue();
            if (value < totalSize) {
                progressbar.setValue(totalBytesRead);
            } else {
                timer.stop();
                frame.dispose();
            }
        }
    }

    public void installOnline()throws PluginDependenceException {
        int choose = JOptionPane.showConfirmDialog(null, Inter.getLocText("FR-Designer-Plugin_Plugin") + Inter.getLocText("FR-Designer-Need") + Inter.getLocText("FR-Designer-Dependence") + Inter.getLocText("FR-Designer-Support") + "," + Inter.getLocText("FR-Designer-Dependence_Need_Install")  + "(" + showFileLength() + " m)?", "install tooltip", JOptionPane.YES_NO_OPTION);
        if (choose == 0) {//下载安装
            if (!connectToServer()) {
                throw new PluginDependenceException(Inter.getLocText("FR-Designer-Dependence_Connect_Server_Error"));
            }
            //安装依赖环境
            if (install()) {
                JOptionPane.showMessageDialog(null, Inter.getLocText("FR-Designer-Dependence_Install_Succeed") + "!!");
            } else {
                throw new PluginDependenceException(Inter.getLocText("FR-Designer-Dependence_Install_Failed"));
            }
        }else {//不选择下载，则不安装图标插件
            throw new PluginDependenceException(Inter.getLocText("FR-Designer-Dependence_Install_Failed"));
        }
    }

    private String showFileLength() {
        return totalSize == -1 ? "NAN" : totalSize / Math.pow(10, 6) + "";
    }
}
