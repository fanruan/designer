package com.fr.design.os.impl;

import com.fr.base.FRContext;
import com.fr.general.GeneralContext;
import com.fr.stable.os.Arch;
import com.fr.stable.os.OperatingSystem;
import com.fr.stable.os.support.SupportOS;
import com.fr.workspace.WorkContext;

/**
 * @author pengda
 * @date 2019/10/9
 */
public enum SupportOSImpl implements SupportOS {

    /**
     * ARM下屏蔽登录
     */
    USERINFOPANE{
        public boolean support(){
            return Arch.getArch() != Arch.ARM;
        }
    },
    /**
     * Linux系统屏蔽透明度
     */
    OPACITY{
        public boolean support(){
            return !OperatingSystem.isLinux();
        }
    },
    /**
     * Linux系统屏蔽FineUI选项
     */
    FINEUI{
        public boolean support(){
            return !OperatingSystem.isLinux();
        }
    },
    /**
     * 自动更新推送
     */
    AUTOPUSHUPDATE{
        @Override
        public boolean support() {
            boolean isLocalEnv = WorkContext.getCurrent().isLocal();
            boolean isChineseEnv = GeneralContext.isChineseEnv();
            boolean isLinux = OperatingSystem.isLinux();
            // 远程设计和非中文环境以及Linux环境，都不生效
            return isLocalEnv && isChineseEnv && !isLinux;
        }
    },
    /**
     * BBS窗口
     */
    BBSDIALOG{
        @Override
        public boolean support() {
            return FRContext.isChineseEnv() && !OperatingSystem.isMacos() && Arch.getArch() != Arch.ARM;
        }
    }

}
