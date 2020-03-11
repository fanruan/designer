package com.fr.start;

import com.fr.general.ComparatorUtils;
import com.fr.process.engine.core.AbstractJavaRuntime;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;
import com.fr.stable.os.OperatingSystem;

import java.util.Set;

/**
 * 设计器Java运行环境
 *
 * @author hades
 * @version 10.0
 * Created by hades on 2019/9/22
 */
public class DesignerJavaRuntime extends AbstractJavaRuntime {

    private static final String DOT = ".";
    private static final String REMOTE_DEBUG = "-agentlib:jdwp=transport=dt_socket";
    private static final String INSTALL4J = ".install4j";
    private static final String JAVA_EXEC = "java";
    private static final String WIN_JRE_BIN = StableUtils.pathJoin("jre", "bin");
    private static final String MAC_JRE_BIN = StableUtils.pathJoin("jre.bundle", "Contents", "Home", "jre", "bin");
    private static final String[] DEBUG_OPTIONS = new String[]{"-Dfile.encoding=UTF-8", "-Xmx2048m"};

    private static final DesignerJavaRuntime INSTANCE = new DesignerJavaRuntime();

    public static DesignerJavaRuntime getInstance() {
        return INSTANCE;
    }

    /**
     * 远程调试不走启动守护
     * @return
     */
    public boolean isInValidVmOptions() {
        String[] options = getJvmOptions();
        for (String op : options) {
            if (op.startsWith(REMOTE_DEBUG)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getJavaExec() {
        String installHome = StableUtils.getInstallHome();
        if (!isInstallVersion()) {
            return JAVA_EXEC;
        }
        if (OperatingSystem.isWindows()) {
            return StableUtils.pathJoin(installHome, WIN_JRE_BIN, JAVA_EXEC);
        }
        if (OperatingSystem.isMacos()) {

            return StableUtils.pathJoin(installHome, INSTALL4J, MAC_JRE_BIN, JAVA_EXEC);
        }
        if (OperatingSystem.isUnix()) {
            return StableUtils.pathJoin(installHome, WIN_JRE_BIN, JAVA_EXEC);

        }
        return StringUtils.EMPTY;
    }

    private boolean isInstallVersion() {
        return !ComparatorUtils.equals(StableUtils.getInstallHome(), DOT);
    }


    /**
     * 非安装版本需要添加下内存参数
     * 工程中可根据需要修改
     *
     * @return 参数
     */
    @Override
    public String[] getJvmOptions() {
        if (isInstallVersion()) {
            return super.getJvmOptions();
        } else {
            return DEBUG_OPTIONS;
        }

    }
}
