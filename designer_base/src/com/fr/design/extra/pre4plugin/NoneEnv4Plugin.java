package com.fr.design.extra.pre4plugin;

/**
 * Created by hufan on 2016/9/1.
 */
public class NoneEnv4Plugin implements PreEnv4Plugin {
    @Override
    public boolean preOnline() {
        return true;
    }

    @Override
    public boolean checkEnv() {
        return true;
    }
}
