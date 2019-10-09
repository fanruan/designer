package com.fr.design.os.impl;

import com.fr.design.os.SupportOS;
import com.fr.stable.os.Arch;
import com.fr.stable.os.OperatingSystem;

public enum SupportOSImpl implements SupportOS {
    //登录
    USERINFOPANE{
        public boolean support(){
            return Arch.getArch() != Arch.ARM;
        }
    },
    //透明度
    OPACITY{
        public boolean support(){
            return !OperatingSystem.isLinux();
        }
    },
    //FineUI选项
    FINEUI{
        public boolean support(){
            return !OperatingSystem.isLinux();
        }
    }

}
