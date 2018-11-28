package com.fr.base.vcs;

import com.fr.design.base.mode.DesignModeContext;

/**
 * 兼容
 *
 * @deprecated user {@link com.fr.design.base.mode.DesignerMode} and {@link DesignModeContext} instead
 */
@Deprecated
public enum DesignerMode {

    NORMAL() {
        @Override
        public void doSwitch() {
            DesignModeContext.switchTo(com.fr.design.base.mode.DesignerMode.NORMAL);
        }
    },
    VCS() {
        @Override
        public void doSwitch() {
            DesignModeContext.switchTo(com.fr.design.base.mode.DesignerMode.VCS);
        }
    },
    AUTHORITY() {
        @Override
        public void doSwitch() {
            DesignModeContext.switchTo(com.fr.design.base.mode.DesignerMode.AUTHORITY);
        }
    };

    abstract void doSwitch();


    /**
     * @return 是否时版本控制模式
     * @deprecated use {@link DesignModeContext#isVcsMode()} instead
     */
    @Deprecated
    public static boolean isVcsMode() {
        return DesignModeContext.isVcsMode();
    }


    /**
     * 切换设计器模式
     *
     * @param mode mode
     * @deprecated use {@link DesignModeContext#switchTo(com.fr.design.base.mode.DesignerMode)} instead
     */
    @Deprecated
    public static void setMode(DesignerMode mode) {
        mode.doSwitch();
    }

    /**
     * @return 获取当前设计器模式
     * @deprecated use {@link DesignModeContext#getMode()} instead
     */
    @Deprecated
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
     * @deprecated use {@link DesignModeContext#isAuthorityEditing()} instead
     */
    @Deprecated
    public static boolean isAuthorityEditing() {
        return DesignModeContext.isAuthorityEditing();
    }
}