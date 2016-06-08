package com.fr.design.fun;

import com.fr.data.Verifier;
import com.fr.design.beans.BasicBeanPane;
import com.fr.stable.fun.Level;
import com.fr.stable.fun.Provider;

/**
 * Created by richie on 16/6/8.
 */
public interface VerifyDefineProvider extends Level, Provider {

    String MARK_STRING = "VerifyDefineProvider";

    int CURRENT_LEVEL = 1;

    Class<? extends Verifier> classForVerifier();

    Class<? extends BasicBeanPane> appearanceForVerifier();

    String nameForVerifier();

    String iconPath();
}
