package com.fr.design.bbs;

import com.fr.base.FRContext;
import com.fr.config.ServerConfig;
import com.fr.stable.StringUtils;

import java.util.List;

/**
 * Created by ibm on 2017/8/21.
 */
public class BBSLoginUtils {

    public static void bbsLogin(String username, String password){
        try{
            ServerConfig.getInstance().setBbsUsername(username);
            ServerConfig.getInstance().setBbsPassword(password);
        }catch (Exception e){
            FRContext.getLogger().error(e.getMessage());
        }
    }

    public static void bbsLogin(List<String> list){
        try{
            String uid = list.get(0);
            String username = list.get(1);
            String password = list.get(2);
            ServerConfig.getInstance().setBbsUsername(username);
            ServerConfig.getInstance().setBbsPassword(password);
            ServerConfig.getInstance().setBbsUid(Integer.parseInt(uid));
            ServerConfig.getInstance().setInShowBBsName(username);
        }catch (Exception e){
            FRContext.getLogger().error(e.getMessage());
        }
    }

    public static void bbsLogout(){
        try{
            ServerConfig.getInstance().setBbsUsername(StringUtils.EMPTY);
            ServerConfig.getInstance().setBbsPassword(StringUtils.EMPTY);
            ServerConfig.getInstance().setBbsUid(0);
            ServerConfig.getInstance().setInShowBBsName(StringUtils.EMPTY);
        }catch (Exception e){
            FRContext.getLogger().error(e.getMessage());
        }
    }
}
