package com.fr.design.mainframe.template.info;

import com.fr.config.MarketConfig;
import com.fr.general.ComparatorUtils;
import com.fr.general.GeneralUtils;
import com.fr.json.JSONObject;
import com.fr.stable.ProductConstants;
import org.easymock.EasyMock;
import org.powermock.api.easymock.PowerMock;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static org.junit.Assert.assertTrue;

/**
 * Created by plough on 2019/4/19.
 */
class TemplateInfoTestHelper {
    static void assertJsonStringEquals(String jo1, String jo2) {
        // HashMap 是无序的，所以不能直接比较它生成的 json 字符串
        assertTrue(ComparatorUtils.equals(new JSONObject(jo1), new JSONObject(jo2)));
    }

    private static void setFinalStatic(Field field, Object newValue) throws Exception {
        field.setAccessible(true);
        // remove final modifier from field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(null, newValue);
    }

    static void setUpMockForNewInstance() throws Exception {
        MarketConfig mockMarketConfig = EasyMock.mock(MarketConfig.class);
        EasyMock.expect(mockMarketConfig.getBbsUsername()).andReturn("plough").anyTimes();

        PowerMock.mockStatic(MarketConfig.class);
        EasyMock.expect(MarketConfig.getInstance()).andReturn(mockMarketConfig).anyTimes();

        PowerMock.mockStatic(GeneralUtils.class);
        EasyMock.expect(GeneralUtils.readBuildNO()).andReturn("不是安装版本").anyTimes();

        setFinalStatic(ProductConstants.class.getDeclaredField("VERSION"), "10.0");

        EasyMock.replay(mockMarketConfig);
        PowerMock.replayAll();
    }
}
