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

/**
 * Created by plough on 2019/4/18.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(ProductConstants.class)
public class TemplateInfoCollectorTest {

    @Before
    public void setUp() {
        PowerMock.mockStatic(ProductConstants.class);

        String filePath = getClass().getResource("tpl.info").getPath();
        String dirPath = filePath.substring(0, filePath.indexOf("tpl.info"));
        EasyMock.expect(ProductConstants.getEnvHome()).andReturn(dirPath).anyTimes();

        EasyMock.replay();
        PowerMock.replayAll();
    }

    @Test
    public void testReadXML() {
        TemplateInfoCollector collector = TemplateInfoCollector.getInstance();
        assertEquals("2019-04-18", Reflect.on(collector).field("designerOpenDate").get());
        assertEquals(7, ((Map) Reflect.on(collector).field("templateInfoMap").get()).size());
    }
}
