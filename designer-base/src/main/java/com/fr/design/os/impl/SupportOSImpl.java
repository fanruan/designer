package com.fr.design.os.impl;

import com.fr.stable.os.Arch;
import com.fr.stable.os.OperatingSystem;
import com.fr.stable.os.support.SupportOS;
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
    }

}
