package com.fr.design.upm;

import com.fr.design.upm.event.DownloadEvent;
import com.fr.event.EventDispatcher;

/**
 * @author richie
 * @version 10.0
 * Created by richie on 2019-04-12
 * 桥接Java和JavaScript的类
 */
public class UPMBridge {

    private static UPMBridge bridge = new UPMBridge();

    public static UPMBridge getBridge() {
        return bridge;
    }

    public void startDownload() {
        // do something.....
        EventDispatcher.fire(DownloadEvent.FINISH, "start");
    }



    public void closeWindow() {
        UPM.closeWindow();
    }

}
