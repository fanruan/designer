package com.fr.design.update.push;

import com.fr.design.DesignerEnvManager;
import com.fr.json.JSONObject;

/**
 * Created by plough on 2019/4/10.
 */
public class DesignerPushUpdateDialogTest {

    public static void main(String[] args) {
        DesignerEnvManager.getEnvManager().setOpenDebug(true);

        JSONObject jo = JSONObject.create();
        jo.put("version", "2019.03.06.04.02.43.6");
        jo.put("content", "设计器改进：去除右击弹框，让操作过程更流畅；增加报表块缩放功能，利于从全局角度整体设计报表\n插件重构：插件支持热部署，即装即用，不再需要重启服务器；\nsapbw：可用于bwcube和bwquery；\n私有云认证：可在客户本地部署私有云认证服务器，业务服务器可到此服务器进行认证；\n开放：打通简道云，可以在简道云里创建项目，并将数据同步到客户的私有库\nshould not display");
        jo.put("more", "http://baidu.com");
        jo.put("background", "http://updateten.finereport.com/fr.png");
        DesignerUpdateInfo mockUpdateInfo = new DesignerUpdateInfo("111.22.11", "2211.231.1", "11.23.1", jo);

        DesignerPushUpdateDialog.createAndShow(null, mockUpdateInfo);
    }
}
