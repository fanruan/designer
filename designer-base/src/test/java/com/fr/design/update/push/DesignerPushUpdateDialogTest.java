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
        jo.put("content", "test content");
        jo.put("more", "http://baidu.com");
        jo.put("background", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555043827901&di=fc266992abef5a7e13b4e0cb98975a75&imgtype=0&src=http%3A%2F%2Fi5.3conline.com%2Fimages%2Fpiclib%2F201203%2F20%2Fbatch%2F1%2F130280%2F1332249463721rez0li5fg0_medium.jpg");
        DesignerUpdateInfo mockUpdateInfo = new DesignerUpdateInfo("111.22.11", "2211.231.1", "11.23.1", jo);

        DesignerPushUpdateDialog.createAndShow(null, mockUpdateInfo);
    }
}
