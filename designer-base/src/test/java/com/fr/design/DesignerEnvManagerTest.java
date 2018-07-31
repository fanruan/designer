package com.fr.design;

import junit.framework.TestCase;

import java.util.Locale;

public class DesignerEnvManagerTest extends TestCase{

    public void testGetLocale() {
        DesignerEnvManager envManager = new DesignerEnvManager();
        assertEquals("默认值", envManager.getLanguage(), Locale.SIMPLIFIED_CHINESE);

        envManager.setLanguage(Locale.US);
        assertEquals("上边界", envManager.getLanguage(), Locale.US);

        envManager.setLanguage(Locale.SIMPLIFIED_CHINESE);
        assertEquals("下边界", envManager.getLanguage(), Locale.SIMPLIFIED_CHINESE);

    }
}
