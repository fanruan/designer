package com.fr.design.dcm;

import com.fr.decision.webservice.bean.BaseBean;
import com.fr.design.bridge.exec.JSBridge;
import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.JSObject;

/**
 * @author richie
 * @version 10.0
 * Created by richie on 2019-05-17
 * 桥接Java和JavaScript的类
 */
public class UniversalDcmBridge {

    public static UniversalDcmBridge getBridge(Browser browser) {
        return new UniversalDcmBridge(browser);
    }

    private JSObject window;

    private UniversalDcmBridge(Browser browser) {
        this.window = browser.executeJavaScriptAndReturnValue("window").asObject();
    }

    /**
     * 获取所有的数据连接
     * @return 数据连接集合
     */
    @JSBridge
    public BaseBean getConnections() {
        return null;
    }
}
