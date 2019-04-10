package com.fr.design.onlineupdate.push;

import com.fr.invoke.Reflect;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Created by plough on 2019/4/8.
 */
public class DesignerPushUpdateManagerTest {
    @Test
    public void testSingleton() {
        DesignerPushUpdateManager m1 = DesignerPushUpdateManager.getInstance();
        DesignerPushUpdateManager m2 = DesignerPushUpdateManager.getInstance();
        assertSame(m1, m2);
    }

    @Test
    public void testIsAutoPushUpdateSupported() {
        // 中文环境 + 本地设计 -> true
        DesignerPushUpdateManager pushUpdateManager = DesignerPushUpdateManager.getInstance();
        assertEquals(true, Reflect.on(pushUpdateManager).call("isAutoPushUpdateSupported", true, true).get());

        // 非中文环境 || 远程设计 -> false
        assertEquals(false, Reflect.on(pushUpdateManager).call("isAutoPushUpdateSupported", false, true).get());
        assertEquals(false, Reflect.on(pushUpdateManager).call("isAutoPushUpdateSupported", true, false).get());
        assertEquals(false, Reflect.on(pushUpdateManager).call("isAutoPushUpdateSupported", false, false).get());
    }

}
