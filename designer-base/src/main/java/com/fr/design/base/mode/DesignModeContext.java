package com.fr.design.base.mode;

import static com.fr.design.base.mode.DesignerMode.AUTHORITY;

public class DesignModeContext {

    private static DesignerMode mode = DesignerMode.NORMAL;

    public static void switchTo(DesignerMode mode) {
        DesignModeContext.mode = mode;
    }

    public static DesignerMode getMode() {
        return mode;
    }

    /**
     * 是否是版本控制模式
     *
     * @return 是否是版本控制模式
     */
    public static boolean isVcsMode() {
        return mode == DesignerMode.VCS;
    }

    /**
     * @return 是否是禁止拷贝剪切模式
     */
    public static boolean isBanCopyAndCut() {
        return mode == DesignerMode.BAN_COPY_AND_CUT;
    }


    /**
     * 是否为权限编辑
     *
     * @return 是否为权限编辑
     */
    public static boolean isAuthorityEditing() {
        return mode == AUTHORITY;
    }
}
