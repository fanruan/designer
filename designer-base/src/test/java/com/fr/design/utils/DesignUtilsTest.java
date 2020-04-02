package com.fr.design.utils;


import com.fr.general.ComparatorUtils;
import junit.framework.TestCase;
import org.junit.Test;

import java.net.ServerSocket;

/**
 * @author hades
 * @version 10.0
 * Created by hades on 2020/1/10
 */
public class DesignUtilsTest extends TestCase {

    @Test
    public void testIsPortOccupied() {
        assertFalse(DesignUtils.isPortOccupied());
        try {
            if (ComparatorUtils.equals("true", System.getProperty("debug"))) {
                new ServerSocket(DesignerPort.getInstance().getDebugMessagePort());
            } else {
                new ServerSocket(DesignerPort.getInstance().getMessagePort());
            }
        } catch (Exception ignore) {
            System.exit(0);
        }
        assertTrue(DesignUtils.isPortOccupied());
    }

}