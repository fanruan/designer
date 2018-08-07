package com.fr.design.mainframe.templateinfo;

import com.fr.stable.StringUtils;
import junit.framework.TestCase;

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

    private String parseVersion(String xmlDesignerVersion){
        String version = StringUtils.EMPTY;
        for(int i = 0; i < xmlDesignerVersion.length(); i++){
            //转为数字，A-Z从10开始，这里A要定义为0减10
            int number = Character.getNumericValue(xmlDesignerVersion.charAt(i)) - 10;
            version = version + number;
            if(i < xmlDesignerVersion.length() - 1){
                version = version + ".";
            }

        }

        return version;
    }
}

