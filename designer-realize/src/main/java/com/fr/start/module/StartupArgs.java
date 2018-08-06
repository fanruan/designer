package com.fr.start.module;

import com.fr.general.ComparatorUtils;

/**
 * Created by juhaoyu on 2018/1/8.
 * 封装启动参数
 */
public class StartupArgs {
    
    private final String[] args;
    
    public StartupArgs(String[] args) {
        
        this.args = args;
    }
    
    public String[] get() {
        
        return args;
    }
    
    /**
     * 是否是产品演示
     */
    public boolean isDemo() {
        
        if (args != null) {
            for (String arg : args) {
                if (ComparatorUtils.equals(arg, "demo")) {
                    return true;
                }
            }
        }
        return false;
    }
}

