package com.fr.start;


import com.fr.base.FRContext;
import com.fr.stable.OperatingSystem;
import com.fr.stable.StableUtils;

import java.io.IOException;

public class Demo {
    public static void main(String[] args) {
        String installHome = StableUtils.getInstallHome();
        if (installHome == null) {
            FRContext.getLogger().error("Can not find the install home, please check it.");
            return;
        }

        String executorPath;

        if (OperatingSystem.isMacOS()) {
            executorPath = StableUtils.pathJoin(installHome, "bin", "designer.app");
        } else {
            executorPath = StableUtils.pathJoin(installHome, "bin", "designer.exe demo");
        }
        if (!new java.io.File(executorPath).exists()) {
            FRContext.getLogger().error(executorPath + " can not be found.");
        }

        if (OperatingSystem.isMacOS()) {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command("open", "-a", executorPath, "--args", "demo");
            try {
                builder.start();
            } catch (IOException e) {
                FRContext.getLogger().error(e.getMessage(), e);
            }
        } else {
            // ProcessBuilder这种方式在window下报错：系统找不到指定文件
            Runtime rt = Runtime.getRuntime();
            try {
                rt.exec(executorPath);
            } catch (IOException e) {
                FRContext.getLogger().error(e.getMessage(), e);
            }
        }

        System.exit(0);
    }
}