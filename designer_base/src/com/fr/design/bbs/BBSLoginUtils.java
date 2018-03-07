package com.fr.design.bbs;

import com.fr.base.FRContext;
import com.fr.config.MarketConfig;
import com.fr.stable.StringUtils;

import java.util.List;

/**
 * Created by ibm on 2017/8/21.
 */
public class BBSLoginUtils {

    public static void bbsLogin(String username, String password) {
        try {
            MarketConfig.getInstance().setBbsUsername(username);
            MarketConfig.getInstance().setBbsPassword(password);
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage());
        }
    }

    public static void bbsLogin(List<String> list) {
        try {
            String uid = list.get(0);
            String username = list.get(1);
            String password = list.get(2);
            MarketConfig.getInstance().setBbsUsername(username);
            MarketConfig.getInstance().setBbsPassword(password);
            MarketConfig.getInstance().setBbsUid(Integer.parseInt(uid));
            MarketConfig.getInstance().setInShowBBsName(username);
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage());
        }
    }

    public static void bbsLogout() {
        try {
            MarketConfig.getInstance().setBbsUsername(StringUtils.EMPTY);
            MarketConfig.getInstance().setBbsPassword(StringUtils.EMPTY);
            MarketConfig.getInstance().setBbsUid(0);
            MarketConfig.getInstance().setInShowBBsName(StringUtils.EMPTY);
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage());
        }
    }
}
