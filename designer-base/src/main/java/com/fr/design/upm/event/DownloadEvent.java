package com.fr.design.upm.event;

import com.fr.event.Event;

/**
 * @author richie
 * @version 10.0
 * Created by richie on 2019-04-12
 */
public enum  DownloadEvent implements Event<String> {

    SUCCESS, ERROR, UPDATE
}
