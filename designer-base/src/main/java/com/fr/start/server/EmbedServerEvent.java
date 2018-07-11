package com.fr.start.server;

import com.fr.event.Event;
import com.fr.event.Null;

/**
 * Created by juhaoyu on 2018/6/5.
 * 内置服务器事件
 */
public enum EmbedServerEvent implements Event<Null> {
    BeforeStart,
    AfterStart,
    BeforeStop,
    AfterStop
}
