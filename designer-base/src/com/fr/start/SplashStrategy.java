package com.fr.start;

/**
 * 启动动画策略接口
 *
 * @author vito
 * @date 2018/6/1
 */
public interface SplashStrategy {

    /**
     * 显示启动动画窗口
     */
    void show();

    /**
     * 隐藏启动动画窗口
     */
    void hide();

    /**
     * 设置模块加载信息
     *
     * @param text 更新的文字
     */
    void updateModuleLog(String text);

    /**
     * 设置感谢文字
     *
     * @param text 更新的文字
     */
    void updateThanksLog(String text);
}
