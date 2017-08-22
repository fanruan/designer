package com.fr.design.bbs;

import com.fr.base.ConfigManager;
import com.fr.base.FRContext;
import com.fr.stable.StringUtils;

/**
 * Created by ibm on 2017/8/21.
 */
public class BBSLoginUtils {

    public static void bbsLogin(String username, String password){
        try{
            ConfigManager.getProviderInstance().setBbsUsername(username);
            ConfigManager.getProviderInstance().setBbsPassword(password);
            FRContext.getCurrentEnv().writeResource(ConfigManager.getProviderInstance());
        }catch (Exception e){
            FRContext.getLogger().error(e.getMessage());
        }
    }

    public static void bbsLogout(){
        try{
            ConfigManager.getProviderInstance().setBbsUsername(StringUtils.EMPTY);
            ConfigManager.getProviderInstance().setBbsPassword(StringUtils.EMPTY);
            FRContext.getCurrentEnv().writeResource(ConfigManager.getProviderInstance());
        }catch (Exception e){
            FRContext.getLogger().error(e.getMessage());
        }
    }
}
