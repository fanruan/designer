package com.fr.design.mainframe.template.info;

import com.fr.invoke.Reflect;
import com.fr.stable.ProductConstants;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by plough on 2019/4/18.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(ProductConstants.class)
public class TemplateInfoCollectorTest {

    @Before
    public void setUp() {
        PowerMock.mockStatic(ProductConstants.class);
        EasyMock.expect(ProductConstants.getEnvHome()).andReturn("/Users/plough/.FineReport100").anyTimes();
        EasyMock.replay();
        PowerMock.replayAll();
    }

    // todo: 单元测试需要一般化，不能依赖特定电脑
    @Test
    public void testReadXML() {
        TemplateInfoCollector collector = TemplateInfoCollector.getInstance();
        assertEquals("2019-04-18", Reflect.on(collector).field("designerOpenDate").get());
        assertTrue(((Map)Reflect.on(collector).field("templateInfoMap").get()).size() > 0);
    }

    @Test
    public void testSend() {
        TemplateInfoCollector tic = TemplateInfoCollector.getInstance();
//        tic.sendTemplateInfo();
    }
}
