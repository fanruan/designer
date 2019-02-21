package com.fr.start.jni;

import com.fr.log.FineLoggerFactory;
import com.fr.stable.ProductConstants;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;
import com.fr.start.SplashContext;
import com.fr.start.SplashStrategy;

import javax.swing.JFrame;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * mac上使用jni方式绘制gif。不使用javafx有两个原因：
 * 1.mac上javafx和swing同时启动会导致卡死；
 * 2.platform.exit会导致设计器崩溃
 *
 * @author vito
 * @see com.fr.start.fx.SplashFx
 */
public class SplashMac implements SplashStrategy {


    private SplashJNI jni;
    private static final int EXILE = 10000;

    public SplashMac() {
        jni = new SplashJNI();
    }

    /**
     * 将jar中的资源拷贝到缓存文件夹
     *
     * @return 路径
     */
    private static String loadResFromJar() {
        File tempLib = null;
        try (InputStream inputStream = SplashContext.class.getResourceAsStream(SplashContext.SPLASH_PATH)) {
            if (inputStream == null) {
                FineLoggerFactory.getLogger().error("Unable to copy " + SplashContext.SPLASH_PATH + " from jar file.");
                return StringUtils.EMPTY;
            }
            tempLib = new File(StableUtils.pathJoin(ProductConstants.getEnvHome(), SplashContext.SPLASH_CACHE_NAME));
            byte[] buffer = new byte[1024];
            int read = -1;
            try (FileOutputStream fileOutputStream = new FileOutputStream(tempLib)) {
                while ((read = inputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, read);
                }
            }
            return tempLib.getAbsolutePath();
        } catch (IOException e) {
            if (tempLib != null) {
                tempLib.deleteOnExit();
            }
            // 直接抛异常
            throw new RuntimeException("Unable to copy " + SplashContext.SPLASH_PATH + " from jar file.");
        }
    }

    @Override
    public void show() {
        if (jni != null) {
            // mac下安装版模糊的hack
            JFrame jFrame = new JFrame();
            jFrame.setLocation(EXILE, EXILE);
            jFrame.setVisible(true);
            jFrame.setVisible(false);
            File splash = new File(StableUtils.pathJoin(ProductConstants.getEnvHome(), SplashContext.SPLASH_CACHE_NAME));
            String path = splash.exists() ? splash.getAbsolutePath() : loadResFromJar();
            jni.show(path);
        }
    }

    @Override
    public void hide() {
        if (jni != null) {
            jni.hide();
            jni = null;
        }
    }

    @Override
    public void updateModuleLog(String text) {
        if (jni != null) {
            jni.updateModuleLog(text);
        }

    }

    @Override
    public void updateThanksLog(String text) {
        if (jni != null) {
            jni.updateThanksLog(text);
        }
    }
}
