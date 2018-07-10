package com.fr.start.jni;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Splash JNI调用。jni类改名或者移包之后
 * 必须重新编译动态库
 *
 * @author vito
 * @date 2018/6/4
 */
public class SplashJNI {

    static {
        try {
            System.setProperty("java.library.path", ".");
            System.loadLibrary("splash");
        } catch (UnsatisfiedLinkError e) {
            loadLibraryFromJar("/com/fr/start/jni/splash.dylib");
        }
    }

    /**
     * 显示启动动画窗口
     */
    public native void show(String path);

    /**
     * 隐藏启动动画窗口
     */
    public native void hide();

    /**
     * 设置模块加载信息
     */
    public native void updateModuleLog(String text);

    /**
     * 设置感谢文字
     */
    public native void updateThanksLog(String text);

    /**
     * 从jar中加载动态库
     *
     * @param path 路径，如/com/a/b
     * @throws UnsatisfiedLinkError 没有找到合适的动态库
     */
    private static void loadLibraryFromJar(String path) throws UnsatisfiedLinkError {
        try (InputStream inputStream = SplashJNI.class.getResourceAsStream(path)) {
            File tempLib = File.createTempFile(path, "");

            byte[] buffer = new byte[1024];
            int read = -1;

            try (FileOutputStream fileOutputStream = new FileOutputStream(tempLib)) {
                while ((read = inputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, read);
                }
            }

            System.load(tempLib.getAbsolutePath());
        } catch (Exception e) {
            throw new UnsatisfiedLinkError("Unable to open " + path + " from jar file.");
        }
    }
}
