package com.fr.design.bbs;

import com.fr.base.ConfigManager;
import com.fr.base.FRContext;
import com.fr.stable.StringUtils;

import java.util.List;

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

    public static void bbsLogin(List<String> list){
        try{
            String uid = list.get(0);
            String username = list.get(1);
            String password = list.get(2);
            ConfigManager.getProviderInstance().setBbsUsername(username);
            ConfigManager.getProviderInstance().setBbsPassword(password);
            ConfigManager.getProviderInstance().setBbsUid(Integer.parseInt(uid));
            ConfigManager.getProviderInstance().setInShowBBsName(username);
            FRContext.getCurrentEnv().writeResource(ConfigManager.getProviderInstance());
        }catch (Exception e){
            FRContext.getLogger().error(e.getMessage());
        }
    }

    public static void bbsLogout(){
        try{
            ConfigManager.getProviderInstance().setBbsUsername(StringUtils.EMPTY);
            ConfigManager.getProviderInstance().setBbsPassword(StringUtils.EMPTY);
            ConfigManager.getProviderInstance().setBbsUid(0);
            ConfigManager.getProviderInstance().setInShowBBsName(StringUtils.EMPTY);
            FRContext.getCurrentEnv().writeResource(ConfigManager.getProviderInstance());
        }catch (Exception e){
            FRContext.getLogger().error(e.getMessage());
        }
    }
}
