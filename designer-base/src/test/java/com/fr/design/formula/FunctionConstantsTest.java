package com.fr.design.formula;

import org.junit.Test;

import static junit.framework.Assert.fail;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

/**
 * Created by plough on 2018/12/7.
 */
public class FunctionConstantsTest {
    @Test
    public void testNewInstanceFail() throws Exception {
        try {
            FunctionConstants.class.newInstance();
            fail("Not allowed to instantiate FunctionConstants!");
        } catch (IllegalAccessException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testEmbedFuntionsAfterStaticInit() {
        NameAndTypeAndFunctionList[] embFunctionLists = FunctionConstants.EMBFUNCTIONS;
        // 一共有 8 个分类
        assertEquals(8, embFunctionLists.length);
        for (NameAndTypeAndFunctionList embFunctionsList : embFunctionLists) {
            // 每个分类下都有函数
            NameAndDescription[] nameAndDescriptions = embFunctionsList.getDescriptions();
            assertTrue(nameAndDescriptions.length > 0);
        }
    }

    @Test
    public void testCommonFuntionsAfterStaticInit() {
        NameAndFunctionList commonFunctionList = FunctionConstants.COMMON;
        assertEquals(9, commonFunctionList.getDescriptions().length);
    }
}
