package com.fr.design.update.push;

import com.fr.design.event.DesignerOpenedListener;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.DesignerFrame;
import com.fr.invoke.Reflect;
import com.fr.stable.StringUtils;
import org.easymock.EasyMock;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Created by plough on 2019/4/8.
 */
@RunWith(value = PowerMockRunner.class)
@PrepareForTest(DesignerContext.class)
public class DesignerPushUpdateManagerTest {

    @BeforeClass
    public static void setUp() {
        DesignerFrame mockFrame = EasyMock.mock(DesignerFrame.class);
        mockFrame.addDesignerOpenedListener(EasyMock.anyObject(DesignerOpenedListener.class));
        EasyMock.replay(mockFrame);

        PowerMock.mockStatic(DesignerContext.class);
        EasyMock.expect(DesignerContext.getDesignerFrame()).andReturn(mockFrame).anyTimes();
        PowerMock.replayAll();
    }

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

    @Test
    public void testSkipCurrentPushVersion() {
        DesignerPushUpdateManager pushUpdateManager = DesignerPushUpdateManager.getInstance();

        // 1. updateInfo 为 null 的情况
        pushUpdateManager.skipCurrentPushVersion();
        assertEquals(StringUtils.EMPTY, DesignerPushUpdateConfigManager.getInstance().getLastIgnoredVersion());


        // 2. updateInfo 有值的情况
        final String PUSH_VERSION = "stable-2019.02.03.12.44.22";
        DesignerUpdateInfo mockInfo = EasyMock.mock(DesignerUpdateInfo.class);
        EasyMock.expect(mockInfo.getPushVersion()).andReturn(PUSH_VERSION).anyTimes();
        Reflect.on(pushUpdateManager).set("updateInfo", mockInfo);
        EasyMock.replay(mockInfo);

        pushUpdateManager.skipCurrentPushVersion();
        assertEquals(PUSH_VERSION, DesignerPushUpdateConfigManager.getInstance().getLastIgnoredVersion());
    }

}
