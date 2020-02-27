package com.fr.design.os.impl;

import com.fr.general.GeneralContext;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

import java.util.Locale;

/**
 * @author hades
 * @version 10.0
 * Created by hades on 2020/1/16
 */
public class SupportOSImplTest extends TestCase {

    @Test
    public void testAutoPush() {
        Assert.assertTrue(SupportOSImpl.AUTOPUSHUPDATE.support());
        GeneralContext.setLocale(Locale.TAIWAN);
        Assert.assertFalse(SupportOSImpl.AUTOPUSHUPDATE.support());
    }

}