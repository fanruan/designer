package com.fr.start.module;

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
}

