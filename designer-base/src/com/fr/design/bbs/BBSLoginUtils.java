package com.fr.design.bbs;

import com.fr.config.Configuration;
import com.fr.config.MarketConfig;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.StringUtils;
import com.fr.transaction.Configurations;
import com.fr.transaction.Worker;

import java.util.List;

/**
 * Created by ibm on 2017/8/21.
 */
public class BBSLoginUtils {

    public static void bbsLogin(final String username, final String password) {
        try {
            Configurations.update(new Worker() {
                @Override
                public void run() {
                    MarketConfig.getInstance().setBbsUsername(username);
                    MarketConfig.getInstance().setBbsPassword(password);
                }
                @Override
                public Class<? extends Configuration>[] targets() {
                    return new Class[]{MarketConfig.class};
                }
            });

        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage());
        }
    }

    public static void bbsLogin(List<String> list) {
        try {
            final String uid = list.get(0);
            final String username = list.get(1);
            final String password = list.get(2);
            Configurations.update(new Worker() {
                @Override
                public void run() {
                    MarketConfig.getInstance().setBbsUsername(username);
                    MarketConfig.getInstance().setBbsPassword(password);
                    MarketConfig.getInstance().setBbsUid(Integer.parseInt(uid));
                    MarketConfig.getInstance().setInShowBBsName(username);
                }
                @Override
                public Class<? extends Configuration>[] targets() {
                    return new Class[]{MarketConfig.class};
                }
            });

        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage());
        }
    }

    public static void bbsLogout() {
        try {
            MarketConfig.getInstance().setBbsUsername(StringUtils.EMPTY);
            MarketConfig.getInstance().setBbsPassword(StringUtils.EMPTY);
            MarketConfig.getInstance().setBbsUid(0);
            MarketConfig.getInstance().setInShowBBsName(StringUtils.EMPTY);
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage());
        }
    }
}
