package com.fr.base.vcs;

import com.fr.design.base.mode.DesignModeContext;

/**
 * 兼容
 *
 * @deprecated user {@link com.fr.design.base.mode.DesignerMode} and {@link DesignModeContext} instead
 */
@Deprecated
public enum DesignerMode {

    NORMAL,
    VCS,
    AUTHORITY;


    public static boolean isVcsMode() {
        return DesignModeContext.isVcsMode();
    }


    public static void setMode(DesignerMode mode) {

        switch (mode) {
            case AUTHORITY:
                DesignModeContext.switchTo(com.fr.design.base.mode.DesignerMode.AUTHORITY);
                break;
            case VCS:
                DesignModeContext.switchTo(com.fr.design.base.mode.DesignerMode.VCS);
                break;
            case NORMAL:
            default:
                DesignModeContext.switchTo(com.fr.design.base.mode.DesignerMode.NORMAL);
        }


    }

    public static DesignerMode getMode() {
        switch (DesignModeContext.getMode()) {
            case VCS:
                return VCS;
            case AUTHORITY:
                return AUTHORITY;
            case NORMAL:
            default:
                return NORMAL;
        }
    }

    /**
     * 是否为权限编辑
     *
     * @return 是否为权限编辑
     */
    public static boolean isAuthorityEditing() {
        return DesignModeContext.isAuthorityEditing();
    }
}