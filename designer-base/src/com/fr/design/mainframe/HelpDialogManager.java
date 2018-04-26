package com.fr.design.mainframe;

/**
 * 帮助信息的面板由于需要滚动条所以采用了很挫的dialog做，dialog很多情况下不能主动关闭，这边控制一下
 * Coder: zack
 * Date: 2016/11/2
 * Time: 16:34
 */
public class HelpDialogManager {
    private static HelpDialogManager THIS;
    private HelpDialogHandler handler;

    private HelpDialogManager() {

    }

    public HelpDialogHandler getPane() {
        return handler;
    }

    public void setPane(HelpDialogHandler dialog) {
        if (dialog == this.handler) {
            return;
        }
        //只允许一个dialog存在
        if (this.handler != null) {
            handler.destroyHelpDialog();
        }
        this.handler = dialog;
    }

    public static HelpDialogManager getInstance() {
        if (THIS == null) {
            THIS = new HelpDialogManager();
        }
        return THIS;
    }
}
