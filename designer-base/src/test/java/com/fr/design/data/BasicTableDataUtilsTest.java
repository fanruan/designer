package com.fr.design.data;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author hades
 * @version 10.0
 * Created by hades on 2020/4/27
 */
public class BasicTableDataUtilsTest extends TestCase {

    @Test
    public void testIsInValidName() {

        boolean result1 = BasicTableDataUtils.isInValidName("a.b");
        boolean result2 = BasicTableDataUtils.isInValidName("a.b.c");
        boolean result3 = BasicTableDataUtils.isInValidName("a..b");
        boolean result4 = BasicTableDataUtils.isInValidName("a.");
        boolean result5 = BasicTableDataUtils.isInValidName("a.b.");
        boolean result6 = BasicTableDataUtils.isInValidName("abc");
        boolean result7 = BasicTableDataUtils.isInValidName(".abc");
        boolean result8 = BasicTableDataUtils.isInValidName(".ab.c");
        boolean result9 = BasicTableDataUtils.isInValidName("ab.c.");
        boolean result10 = BasicTableDataUtils.isInValidName(".abc.");
        boolean result11 = BasicTableDataUtils.isInValidName(".ab.c.");
        boolean result12 = BasicTableDataUtils.isInValidName("..");

        Assert.assertTrue(result1);
        Assert.assertFalse(result2);
        Assert.assertFalse(result3);
        Assert.assertFalse(result4);
        Assert.assertTrue(result5);
        Assert.assertFalse(result6);
        Assert.assertTrue(result7);
        Assert.assertFalse(result8);
        Assert.assertTrue(result9);
        Assert.assertTrue(result10);
        Assert.assertFalse(result11);
        Assert.assertFalse(result12);
    }

}