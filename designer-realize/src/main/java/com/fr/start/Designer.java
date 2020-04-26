package com.fr.start;

import com.fr.design.os.impl.SupportOSImpl;
import com.fr.log.FineLoggerFactory;

/**
 * 设计器主进程入口(无缝更换升级jar包，若使用其他类作为入口，需要重新打包designer.exe等，升级后仍然走的原来逻辑)
 *
 * 设计器逻辑/UI调试 see MainDesigner/Designer4Debug
 *
 * @author hades
 * @date 2019/8/27
 */
public class Designer {

    public static void main(String[] args) {
        try {
            if (DesignerJavaRuntime.getInstance().isInValidVmOptions()
                    || SupportOSImpl.NON_GUARDIAN_START.support()) {
                runNonGuardianDesigner(args);
            } else {
                // 创建进程
                DesignerLauncher.getInstance().start(args);
            }
        } catch (Exception e) {
            runNonGuardianDesigner(args);
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
    }

    /**
     * 启动非守护设计器
     * @param args 参数
     */
    private static void runNonGuardianDesigner(String[] args) {
        MainDesigner.main(args);
    }
}
