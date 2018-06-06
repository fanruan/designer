package com.fr.start.jni;

import com.fr.stable.ProductConstants;
import com.fr.stable.StableUtils;
import com.fr.start.SplashContext;
import com.fr.start.SplashStrategy;

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

    private static final String SPLASH_CACHE_NAME = "splash_10.gif";
    private static final String SPLASH_PATH = "/com/fr/base/images/oem/splash_10.gif";

    private SplashJNI jni;

    public SplashMac() {
        jni = new SplashJNI();
    }

    /**
     * 将jar中的资源拷贝到缓存文件夹
     *
     * @return 路径
     * @throws IOException 拷贝失败
     */
    private static String loadResFromJar() throws UnsatisfiedLinkError {
        File tempLib = null;
        try (InputStream inputStream = SplashContext.class.getResourceAsStream(SplashMac.SPLASH_PATH)) {
            tempLib = new File(StableUtils.pathJoin(ProductConstants.getEnvHome(), SPLASH_CACHE_NAME));
            byte[] buffer = new byte[1024];
            int read = -1;
            try (FileOutputStream fileOutputStream = new FileOutputStream(tempLib)) {
                while ((read = inputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, read);
                }
            }
            return tempLib.getAbsolutePath();
        } catch (IOException e) {
            tempLib.deleteOnExit();
            throw new UnsatisfiedLinkError("Unable to open " + SplashMac.SPLASH_PATH + " from jar file.");
        }
    }

    @Override
    public void show() {
        if (jni != null) {
            File splash = new File(StableUtils.pathJoin(ProductConstants.getEnvHome(), SPLASH_CACHE_NAME));
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
