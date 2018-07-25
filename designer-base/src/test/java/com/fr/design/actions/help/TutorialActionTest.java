package com.fr.design.actions.help;

import com.fr.general.GeneralContext;
import com.fr.stable.ProductConstants;
import junit.framework.TestCase;

import java.util.Locale;
import java.util.UUID;

public class TutorialActionTest extends TestCase{

    public void testCreateKey() {
        TutorialAction action = new TutorialAction();
        GeneralContext.setLocale(Locale.US);
        String enKey = action.createDocKey();
        assertTrue(enKey.indexOf(Locale.US.toString()) != -1);
        assertTrue(enKey.indexOf(ProductConstants.MAIN_VERSION) != -1);

        GeneralContext.setLocale(Locale.CHINA);
        String zhKey = action.createDocKey();
        assertTrue(zhKey.indexOf(Locale.CHINA.toString()) != -1);

        Locale pt = new Locale("pt", "PT");
        GeneralContext.setLocale(pt);
        String ptKey = action.createDocKey();
        assertTrue(ptKey.indexOf(pt.toString()) != -1);

        GeneralContext.setLanguage(998);
        assertTrue(action.createDocKey().indexOf(Locale.CHINA.toString()) != -1);
    }

    public void testServerOnline() {
        TutorialAction action = new TutorialAction();
        assertFalse(action.isServerOnline(null));
        assertFalse(action.isServerOnline("中文"));
        assertTrue("百度暂时不会挂", action.isServerOnline("http://www.baidu.com"));
        assertTrue("https测试", action.isServerOnline("https://www.baidu.com"));
        assertFalse("连上是正常的", action.isServerOnline("https://www.fine-" + UUID.randomUUID() + ".com"));
    }
}
