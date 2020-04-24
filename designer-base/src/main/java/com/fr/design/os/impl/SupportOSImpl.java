package com.fr.design.os.impl;

import com.fr.base.FRContext;
import com.fr.general.CloudCenter;
import com.fr.general.GeneralContext;
import com.fr.json.JSON;
import com.fr.json.JSONFactory;
import com.fr.json.JSONObject;
import com.fr.stable.StringUtils;
import com.fr.stable.os.Arch;
import com.fr.stable.os.OperatingSystem;
import com.fr.stable.os.support.SupportOS;
import com.fr.workspace.WorkContext;

import java.util.Locale;

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
            boolean isLinux = OperatingSystem.isLinux();
            // 远程设计和非中文环境以及Linux环境，都不生效
            return isLocalEnv && !isLinux && isPushByConf();
        }

        private boolean isPushByConf() {
            String resp = CloudCenter.getInstance().acquireUrlByKind("update.push.conf");
            if (StringUtils.isEmpty(resp)) {
                return Locale.CHINA.equals(GeneralContext.getLocale());
            }
            JSONObject jo = JSONFactory.createJSON(JSON.OBJECT, resp);
            return jo.getBoolean(GeneralContext.getLocale().toString());
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
    },

    /**
     * mac下dock栏右键退出
     */
    DOCK_QUIT {
        @Override
        public boolean support() {
            return OperatingSystem.isMacos();
        }
    },

    NON_GUARDIAN_START {
        @Override
        public boolean support() {
            return OperatingSystem.isLinux() || Arch.getArch() == Arch.ARM || OperatingSystem.isMacos();
        }
    },

    DOCK_ICON {
        @Override
        public boolean support() {
            return OperatingSystem.isMacos();
        }
    },

    
    VM_OPTIONS_ADAPTER {
        @Override
        public boolean support() {
            return OperatingSystem.isWindows();
        }
    }

}
