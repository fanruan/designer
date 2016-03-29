package com.fr.design.extra;

import com.fr.base.Env;
import com.fr.base.FRContext;
import com.fr.design.DesignerEnvManager;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRLogger;
import com.fr.general.GeneralUtils;
import com.fr.general.IOUtils;
import com.fr.general.Inter;
import com.fr.general.http.HttpClient;
import com.fr.plugin.Plugin;
import com.fr.plugin.PluginLoader;
import com.fr.plugin.PluginManagerHelper;
import com.fr.stable.ArrayUtils;
import com.fr.stable.EncodeConstants;
import com.fr.stable.StableUtils;
import com.fr.stable.project.ProjectConstants;
import com.fr.stable.xml.XMLTools;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * @author richie
 * @date 2015-03-10
 * @since 8.0
 */
public class PluginHelper {
    private static final String TEMP_PATH = System.getProperty("user.dir") + "/tmp";
    private static final String DOWNLOAD_PATH = System.getProperty("user.dir") + "/download";
    private static final String TEMP_FILE = "temp.zip";

    /**
     * 下载插件
     *
     * @param id 插件id
     * @param p  下载百分比处理
     */
    public static void downloadPluginFile(String id, String username, String password, Process<Double> p) throws Exception {
        HttpClient httpClient = new HttpClient(getDownloadPath(id, username, password));
        if (httpClient.getResponseCode() == HttpURLConnection.HTTP_OK) {
            int totalSize = httpClient.getContentLength();
            InputStream reader = httpClient.getResponseStream();
            String temp = StableUtils.pathJoin(DOWNLOAD_PATH, TEMP_FILE);
            StableUtils.makesureFileExist(new File(temp));
            FileOutputStream writer = new FileOutputStream(temp);
            byte[] buffer = new byte[PluginConstants.BYTES_NUM];
            int bytesRead = 0;
            int totalBytesRead = 0;

            while ((bytesRead = reader.read(buffer)) > 0) {
                writer.write(buffer, 0, bytesRead);
                buffer = new byte[PluginConstants.BYTES_NUM];
                totalBytesRead += bytesRead;
                p.process(totalBytesRead / (double) totalSize);
            }
            reader.close();
            writer.flush();
            writer.close();
        }
    }

    private static String getDownloadPath(String id, String username, String password) throws Exception {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        map.put("username", username);
        map.put("password", password);
        HttpClient httpClient = new HttpClient(PluginConstants.PLUGIN_DOWNLOAD_URL, map);
        String resText = httpClient.getResponseText();
        String charSet = EncodeConstants.ENCODING_UTF_8;
        resText = URLDecoder.decode(URLDecoder.decode(resText, charSet), charSet);

        return resText;
    }

    public static File getDownloadTempFile() {
        return new File(StableUtils.pathJoin(DOWNLOAD_PATH, TEMP_FILE));
    }

    /**
     * 从压缩文件中读取插件信息
     *
     * @param chosenFile 选择的压缩文件
     * @return 返回插件对象
     * @throws Exception 读取插件信息失败则抛出异常
     */
    public static Plugin readPlugin(File chosenFile) throws Exception {
        // 需要先删除临时目录保证加压出来的文件不会和安装失败的文件混合到一起
        StableUtils.deleteFile(new File(TEMP_PATH));

        IOUtils.unzip(chosenFile, TEMP_PATH);
        File pluginFileDir = getTempPluginFileDirectory();
        if (pluginFileDir == null) {
            return null;
        }
        Plugin plugin = null;
        if (pluginFileDir.isDirectory()) {
            File[] pluginFiles = pluginFileDir.listFiles();
            if (ArrayUtils.isNotEmpty(pluginFiles)) {
                for (File f : pluginFiles) {
                    if (f.getName().equals("plugin.xml")) {
                        plugin = new Plugin();
                        InputStream inputStream = plugin.readEncryptXml(new FileInputStream(f));
                        XMLTools.readInputStreamXML(plugin, inputStream);
                        if (!plugin.isValidate()) {
                            return null;
                        }
                        inputStream.close();
                        break;
                    }
                }
            }
        }
        return plugin;
    }

    /**
     * 从选中的压缩文件中安装插件
     *
     * @param chosenFile 选择的压缩文件
     * @param after      操作完成事件
     * @throws Exception 安装失败则抛出异常
     */
    public static void installPluginFromDisk(File chosenFile, After after) throws Exception {
        Plugin plugin = readPlugin(chosenFile);
        installPluginFromUnzippedTempDir(FRContext.getCurrentEnv(), plugin, after);
    }

    /**
     * 从压缩文件中复制Restart*.class和restart.exe到bin目录下
     *
     * @param file   插件文件
     * @param plugin 插件
     * @throws Exception
     */
    public static void copyFilesToBinFolder(File file, Plugin plugin) throws Exception {
        File[] pluginFiles = file.listFiles();
        for (File restartFile : pluginFiles) {
            if (restartFile.getAbsolutePath().endsWith(".class")) {
                String installHome = StableUtils.getInstallHome();
                IOUtils.copy(restartFile, new File(StableUtils.pathJoin(new String[]{installHome, "bin"})));
            }
        }
    }

    /**
     * 从插件压缩包解压到得临时文件中安装插件
     *
     * @param env    报表运行环境
     * @param plugin 插件
     * @param after  操作完成事件
     * @throws Exception
     */
    public static void installPluginFromUnzippedTempDir(Env env, final Plugin plugin, final After after) throws Exception {
        validPlugin(plugin);
        if (plugin.isValidate()) {
            File file = getTempPluginFileDirectory();
            env.copyFilesToPluginAndLibFolder(file, plugin);
            copyFilesToBinFolder(file, plugin);
            env.movePluginEmbFile(file, plugin);
        }
        // 删除放解压文件的临时文件夹
        StableUtils.deleteFile(new File(TEMP_PATH));
        new SwingWorker<String, Void>() {

            @Override
            protected String doInBackground() throws Exception {
                return sendInstalledPluginInfo(plugin);
            }

            @Override
            protected void done() {
                try {
                    String text = get();
                    FRLogger.getLogger().info("plugin install:" + text);
                } catch (InterruptedException e) {
                    FRLogger.getLogger().error(e.getMessage(), e);
                } catch (ExecutionException e) {
                    FRLogger.getLogger().error(e.getMessage(), e);
                }
                if (after != null) {
                    after.done();
                }
            }
        }.execute();
    }

    private static void validPlugin(Plugin plugin) throws Exception {
        if (plugin == null) {
            throw new com.fr.plugin.PluginVerifyException(Inter.getLocText("FR-Designer-Plugin_Illegal_Plugin_Zip_Cannot_Be_Install"));
        }
        if (PluginLoader.getLoader().isInstalled(plugin)) {
            throw new com.fr.plugin.PluginVerifyException(Inter.getLocText("FR-Designer-Plugin_Has_Been_Installed"));
        }
        if (plugin.isJarExpired()) {
            String jarExpiredInfo = Inter.getLocText(new String[]{"FR-Designer-Plugin_Jar_Expired", ",", "FR-Designer-Plugin_Install_Failed", ",", "FR-Designer-Plugin_Please_Update_Jar", plugin.getRequiredJarTime()});
            FRLogger.getLogger().error(jarExpiredInfo);
            throw new com.fr.plugin.PluginVerifyException(jarExpiredInfo);
        }
        File fileToCheck = getTempPluginFileDirectory();
        File oldfile = new File(StableUtils.pathJoin(FRContext.getCurrentEnv().getPath(), ProjectConstants.PLUGINS_NAME, "plugin-" + plugin.getId()));
        if (!PluginManagerHelper.checkLic(plugin, fileToCheck)) {
            if (!PluginManagerHelper.checkLic(plugin, oldfile)) {//安装时,在安装目录下和压缩包里都没有才弹框
                String checkLicFail = Inter.getLocText("FR-Designer-PluginLicense_Check_Failed");
                FRLogger.getLogger().error(checkLicFail);
                throw new com.fr.plugin.PluginVerifyException(checkLicFail);
            }
        }
    }

    /**
     * 获取插件解压的临时文件夹
     *
     * @return 临时文件
     */
    public static File getTempPluginFileDirectory() {
        File file = new File(TEMP_PATH);
        if (file.isDirectory() && !file.getName().startsWith(".")) {
            File[] files = file.listFiles();
            if (ArrayUtils.isNotEmpty(files)) {
                for (File f : files) {
                    if (foundConfigFile(f)) {
                        return f;
                    }
                }
            }
        }
        return null;
    }

    private static boolean foundConfigFile(File dir) {
        if (!dir.isDirectory()) {
            return false;
        }
        File[] files = dir.listFiles();
        if (ArrayUtils.isNotEmpty(files)) {
            for (File f : files) {
                if ("plugin.xml".equals(f.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 从运行环境中卸载插件
     *
     * @param env    报表运行环境
     * @param plugin 插件
     * @return 返回没有删除掉的文件的集合
     * @throws Exception 卸载出错的时候抛出此异常
     */
    public static String[] uninstallPlugin(Env env, Plugin plugin) throws Exception {
        if (plugin == null || env == null) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
        PluginLoader.getLoader().deletePlugin(plugin);
        return env.deleteFileFromPluginAndLibFolder(plugin);
    }

    /**
     * 比较插件的版本，这里简单的比价字符串，不需要用数字作为标号
     * 版本号相同也认为是更新
     *
     * @param plugin    当前的插件
     * @param oldPlugin 老的插件
     * @return 当前插件比老的插件版本高则返回true，否则返回false
     */
    public static boolean isNewThan(Plugin plugin, Plugin oldPlugin) {
        return ComparatorUtils.compare(plugin.getVersion(), oldPlugin.getVersion()) >= 0;
    }

    private static String sendInstalledPluginInfo(final Plugin plugin) {
        if (StableUtils.isDebug()) {
            return "debug status";
        }
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("key", DesignerEnvManager.getEnvManager().getActivationKey());
        map.put("detail", plugin.toJSONObject().toString());
        map.put("build", GeneralUtils.readBuildNO());
        //第三个参数encode, nodejs服务器那边如果参数不encode, 带了空格会报错, 直接用urlconnection也是一样, jetty没能还原.
        HttpClient httpClient = new HttpClient(PluginConstants.PLUGIN_INSTALL_INFO, map, true);
        httpClient.setTimeout(TIME_OUT);
        httpClient.asGet();
        return httpClient.getResponseText();
    }

    private static final int TIME_OUT = 5000;
}