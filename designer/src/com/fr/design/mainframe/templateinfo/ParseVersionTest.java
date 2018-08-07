package com.fr.design.mainframe.templateinfo;

import com.fr.stable.StringUtils;
import junit.framework.TestCase;

import java.lang.reflect.Method;

/**
 * Created by XINZAI on 2018/8/7.
 */
public class ParseVersionTest extends TestCase {
    public void testParseVersion() throws Exception{
        assertEquals("10.0.0", parseVersion("KAA"));
        assertEquals("9.0.0", parseVersion("JAA"));
        assertEquals("8.0.0", parseVersion("IAA"));
        assertEquals("8.1.2", parseVersion("IBC"));
    }

    private String parseVersion(String xmlDesignerVersion) throws Exception{
        String version = StringUtils.EMPTY;
        try {
            Class reflect  = Class.forName("com.fr.design.mainframe.JTemplate");

            Method method = reflect.getDeclaredMethod("parseVersion", String.class);
            //取消访问私有方法的合法性检查
            method.setAccessible(true);

            version = (String) method.invoke(reflect, xmlDesignerVersion);

        }catch (Exception e){
            throw e;
        }

        return version;
    }

}

