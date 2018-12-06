package com.fr.design.bbs;

import com.fr.base.passport.FinePassportManager;
import com.fr.log.FineLoggerFactory;


/**
 * Created by ibm on 2017/8/21.
 */
public class BBSLoginUtils {

    public static void bbsLogin(final String username, final String password) {
        try {
            FinePassportManager.getInstance().login(username, password);
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
    }

    public static void bbsLogout() {
        FinePassportManager.getInstance().logout();
    }
}
