package com.fr.design.os.impl;

import com.fr.log.FineLoggerFactory;
import com.fr.stable.ArrayUtils;
import com.fr.stable.StableUtils;
import com.fr.stable.os.OperatingSystem;
import com.fr.stable.os.support.OSBasedAction;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RestartAction implements OSBasedAction {
    @Override
    public void execute(Object... objects) {
        String[] filesToBeDelete = (String[])objects;
        String installHome = StableUtils.getInstallHome();
        try{
            if (OperatingSystem.isMacos()) {
               restartInMacOS(installHome, filesToBeDelete);
            } else if(OperatingSystem.isWindows()){
               restartInWindows(installHome, filesToBeDelete);
            }else{
               //增加一个Linux系统
              restartInLinux(installHome,filesToBeDelete);
            }
        }catch(Exception e){
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }

    }

    private static void restartInMacOS(String installHome, String[] filesToBeDelete) throws Exception {
        ProcessBuilder builder = new ProcessBuilder();
        List<String> commands = new ArrayList<String>();
        commands.add("open");
        commands.add(installHome + File.separator + "bin" + File.separator + "restart.app");
        if (ArrayUtils.isNotEmpty(filesToBeDelete)) {
            commands.add("--args");
            commands.add(StableUtils.join(filesToBeDelete, "+"));
        }
        builder.command(commands);
        builder.start();
    }

    private static void restartInWindows(String installHome, String[] filesToBeDelete) throws Exception {
        ProcessBuilder builder = new ProcessBuilder();
        List<String> commands = new ArrayList<String>();
        commands.add(installHome + File.separator + "bin" + File.separator + "restart.exe");
        if (ArrayUtils.isNotEmpty(filesToBeDelete)) {
            commands.add(StableUtils.join(filesToBeDelete, "+"));
        }
        builder.command(commands);
        builder.start();
    }

    private static void restartInLinux(String installHome, String[] filesToBeDelete) throws Exception {
        ProcessBuilder builder = new ProcessBuilder();
        List<String> commands = new ArrayList<String>();
        //现在先写的是restart.sh
        commands.add(installHome + File.separator + "bin" + File.separator + "restart.sh");
        if (ArrayUtils.isNotEmpty(filesToBeDelete)) {
            commands.add(StableUtils.join(filesToBeDelete, "+"));
        }
        builder.command(commands);
        builder.start();
    }







}
