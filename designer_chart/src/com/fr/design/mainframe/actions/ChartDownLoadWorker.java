package com.fr.design.mainframe.actions;

import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.file.SaveSomeTemplatePane;
import com.fr.design.mainframe.DesignerContext;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogger;
import com.fr.json.JSONObject;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 7.1.1
 */
public class ChartDownLoadWorker extends SwingWorker<Void, Double>{
    private static final String FILE_PATH = "http://chart.finedevelop.com/update/";
    private static final String VERSION = "version";
    private static final String TEMP = "_temp";
    private static final int BYTE = 153600;
    private static final int FILE_BYTE = 1024;
    private HashMap<String,String> files = new HashMap<String, String>();

    public ChartDownLoadWorker() {
    }

    private void loadFilesPaths() throws Exception {
        files.clear();
        final String installHome = StableUtils.getInstallHome();

        JSONObject serverVersion = UpdateVersion.getJsonContent();
        if(serverVersion == null){
            return;
        }
        Iterator<String> keys = serverVersion.keys();
        while (keys.hasNext()){
            String jarName = keys.next();
            if(!ComparatorUtils.equals(jarName, VERSION)){
                String filePath = (String) serverVersion.get(jarName);
                String path =installHome + filePath.substring(2);
                files.put(jarName,path);
            }
        }
        files.isEmpty();
    }

    @Override
    protected Void doInBackground() throws Exception {
        try {
            loadFilesPaths();
            Set key = files.keySet();
            Iterator iterator = key.iterator();
            int totalSize = 0;
            //先得到所有的长度，方便计算百分比
            while (iterator.hasNext()) {
                String jarName = (String) iterator.next();
                String jarUrl = FILE_PATH + jarName;
                URL url = new URL(jarUrl);
                URLConnection connection = url.openConnection();
                totalSize += connection.getContentLength();
            }

            int totalBytesRead = 0;
            iterator = key.iterator();
            while (iterator.hasNext()) {
                String jarName = (String) iterator.next();
                String jarUrl = FILE_PATH + jarName;
                URL url = new URL(jarUrl);
                InputStream reader = url.openStream();
                String filePath = files.get(jarName);
                int point = filePath.lastIndexOf(".");
                //先写临时文件，防止更新一半意外中止
                String tmpFilePath = filePath.substring(0,point)+TEMP+filePath.substring(point);
                FileOutputStream writer = new FileOutputStream(tmpFilePath);
                byte[] buffer = new byte[BYTE];
                int bytesRead = 0;
                while ((bytesRead = reader.read(buffer)) > 0) {
                    writer.write(buffer, 0, bytesRead);
                    buffer = new byte[BYTE];
                    totalBytesRead += bytesRead;
                    publish(totalBytesRead/(double)totalSize);
                }
            }


        } catch (Exception e) {
            throw new Exception("Update Failed !" + e.getMessage());
        }

        return null;
    }

    //替换更新下来的临时文件
    protected void replaceFiles(){
        try {
            Set key = files.keySet();
            Iterator iterator = key.iterator();
            while (iterator.hasNext()) {
                String jarName = (String) iterator.next();
                String filePath = files.get(jarName);
                int point = filePath.lastIndexOf(".");
                //先写临时文件，防止更新一半意外中止
                String tmpFilePath = filePath.substring(0,point)+TEMP+filePath.substring(point);
                FileInputStream inputStream = new FileInputStream(tmpFilePath);
                FileOutputStream writer = new FileOutputStream(filePath);
                byte[] buffer = new byte[FILE_BYTE];
                int bytesRead = 0;
                while ((bytesRead = inputStream.read(buffer))>0){
                   writer.write(buffer,0,bytesRead);
                    buffer = new byte[FILE_BYTE];
                }
                writer.flush();
                writer.close();
                inputStream.close();
            }
        } catch (Exception e) {
            FRLogger.getLogger().error(e.getMessage());
        }
    }

    /**
     * 完成时的动作
     */
    public void done() {
        //检测是否没有保存的模版
        SaveSomeTemplatePane saveSomeTempaltePane = new SaveSomeTemplatePane(true);
        // 只有一个文件未保存时
        if (HistoryTemplateListPane.getInstance().getHistoryCount() == 1) {
            int choose = saveSomeTempaltePane.saveLastOneTemplate();
            if (choose != JOptionPane.CANCEL_OPTION) {
                restartChartDesigner();
            }
        } else {
            if (saveSomeTempaltePane.showSavePane()) {
                restartChartDesigner();
            }
        }
    }

    private void restartChartDesigner(){
        String installHome = StableUtils.getInstallHome();
        if(StringUtils.isEmpty(installHome) || ComparatorUtils.equals(".",installHome)){
            DesignerContext.getDesignerFrame().exit();
            return;
        }

        try {
            String path = installHome + File.separator + "bin" + File.separator + "restart.bat";
            ProcessBuilder builder = new ProcessBuilder(path,installHome);
            builder.start();
            DesignerContext.getDesignerFrame().exit();
        }catch (Exception e){
            FRLogger.getLogger().error(e.getMessage());
        }
    }



}