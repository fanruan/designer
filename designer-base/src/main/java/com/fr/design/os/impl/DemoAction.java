package com.fr.design.os.impl;

import com.fr.log.FineLoggerFactory;
import com.fr.stable.StableUtils;
import com.fr.stable.os.OperatingSystem;
import com.fr.stable.os.support.OSBasedAction;

import java.io.IOException;
/**
 * @author pengda
 * @date 2019/10/9
 */
public class DemoAction implements OSBasedAction {

    @Override
    public void execute(Object... objects) {
        String installHome = StableUtils.getInstallHome();
        if (installHome == null) {
            FineLoggerFactory.getLogger().error("Can not find the install home, please check it.");
            return;
        }

        String executorPath;

        if (OperatingSystem.isMacos()) {
            executorPath = StableUtils.pathJoin(installHome, "bin", "designer.app");
        } else if(OperatingSystem.isWindows()){
            executorPath = StableUtils.pathJoin(installHome, "bin", "designer.exe demo");
        }else{
            executorPath = StableUtils.pathJoin(installHome, "bin", "designer.sh demo");
        }

        if (OperatingSystem.isMacos()) {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command("open", "-a", executorPath, "--args", "demo");
            try {
                builder.start();
            } catch (IOException e) {
                FineLoggerFactory.getLogger().error(e.getMessage(), e);
            }
        } else if(OperatingSystem.isWindows()){
            // ProcessBuilder这种方式在window下报错：系统找不到指定文件
            Runtime rt = Runtime.getRuntime();
            try {
                rt.exec(executorPath);
            } catch (IOException e) {
                FineLoggerFactory.getLogger().error(e.getMessage(), e);
            }
        }else{
            //先用和win一样的方式
            Runtime rt = Runtime.getRuntime();
            try {
                rt.exec(executorPath);
            } catch (IOException e) {
                FineLoggerFactory.getLogger().error(e.getMessage(), e);
            }
        }
    }
}
