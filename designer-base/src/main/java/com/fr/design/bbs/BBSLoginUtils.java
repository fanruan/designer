package com.fr.design.bbs;

import com.fr.config.BBSAttr;
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
        final BBSAttr bbsAttr = new BBSAttr();
        bbsAttr.setBbsUsername(username);
        bbsAttr.setBbsPassword(password);
        try {
            Configurations.update(new Worker() {
                @Override
                public void run() {
                    MarketConfig.getInstance().setBBsAttr(bbsAttr);
                }
                @Override
                public Class<? extends Configuration>[] targets() {
                    return new Class[]{MarketConfig.class};
                }
            });

        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
    }

    public static void bbsLogin(List<String> list) {
        try {
            String uid = list.get(0);
            String username = list.get(1);
            String password = list.get(2);
            final BBSAttr bbsAttr = new BBSAttr();
            bbsAttr.setBbsUsername(username);
            bbsAttr.setBbsPassword(password);
            bbsAttr.setBbsUid(Integer.parseInt(uid));
            bbsAttr.setInShowBBsName(username);
            Configurations.update(new Worker() {
                @Override
                public void run() {
                    MarketConfig.getInstance().setBBsAttr(bbsAttr);
                }
                @Override
                public Class<? extends Configuration>[] targets() {
                    return new Class[]{MarketConfig.class};
                }
            });

        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
    }

    public static void bbsLogout() {
        final BBSAttr bbsAttr = new BBSAttr();
        bbsAttr.setBbsUsername(StringUtils.EMPTY);
        bbsAttr.setBbsPassword(StringUtils.EMPTY);
        bbsAttr.setBbsUid(0);
        bbsAttr.setInShowBBsName(StringUtils.EMPTY);
        try {
            MarketConfig.getInstance().setBBsAttr(bbsAttr);
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
    }
}
