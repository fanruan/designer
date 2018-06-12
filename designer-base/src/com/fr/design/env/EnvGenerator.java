package com.fr.design.env;

import com.fr.base.Env;
import com.fr.core.env.EnvConfig;
import com.fr.core.env.impl.LocalEnvConfig;
import com.fr.dav.LocalEnv;
import com.fr.env.RemoteEnv;

/**
 * 根据配置生成运行环境
 */
public class EnvGenerator {
    public static Env generate(EnvConfig config) {
        Env env = null;
        if (config instanceof LocalEnvConfig) {
            env = new LocalEnv((LocalEnvConfig)config);
        } else if (config instanceof RemoteEnvConfig) {
            env = new RemoteEnv((RemoteEnvConfig) config);
        }
        return env;
    }
}
