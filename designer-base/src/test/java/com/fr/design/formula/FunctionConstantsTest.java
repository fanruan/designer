package com.fr.design.formula;

import com.fr.general.GeneralUtils;
import com.fr.invoke.Reflect;
import com.fr.log.FineLoggerFactory;
import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

/**
 * Created by plough on 2018/12/7.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(GeneralUtils.class)
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
    public void testEmbedFunctionsAfterStaticInit() {
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
    public void testCommonFunctionsAfterStaticInit() {
        NameAndFunctionList commonFunctionList = FunctionConstants.COMMON;
        assertEquals(9, commonFunctionList.getDescriptions().length);
    }

    @Test
    public void testIsCustomFormulaPathRunWithCode() {
        // mock 的 GeneralUtils.readBuildNO()).andReturn("不是安装版本").anyTimes()
        // 会影响 FunctionConstants.class 静态代码块 loadEmbededFunctions 执行，先加载这个类，执行静态代码块
        FineLoggerFactory.getLogger().info(String.valueOf(FunctionConstants.EMBFUNCTIONS.length));

        PowerMock.mockStatic(GeneralUtils.class);
        EasyMock.expect(GeneralUtils.readBuildNO()).andReturn("不是安装版本").anyTimes();
        PowerMock.replayAll();

        String classFilePath = "/Users/plough/.m2/repository/com/fr/core/fine-core/10.0-RELEASE-SNAPSHOT/fine-core-10.0-RELEASE-20181211.024527-499.jar!/com/fr/function";
        assertFalse(Reflect.on(FunctionConstants.class).call("isCustomFormulaPath", classFilePath).<Boolean>get());

        classFilePath = "/Users/plough/work/new_10_release_finereport/engine-settings/env/webroot/WEB-INF/classes/com/fr/function";
        assertFalse(Reflect.on(FunctionConstants.class).call("isCustomFormulaPath", classFilePath).<Boolean>get());
    }

    @Test
    public void testIsCustomFormulaPathRunWithJar() {
        // mock 的 GeneralUtils.readBuildNO()).andReturn("不是安装版本").anyTimes()
        // 会影响 FunctionConstants.class 静态代码块 loadEmbededFunctions 执行，先加载这个类，执行静态代码块
        FineLoggerFactory.getLogger().info(String.valueOf(FunctionConstants.EMBFUNCTIONS.length));

        PowerMock.mockStatic(GeneralUtils.class);
        EasyMock.expect(GeneralUtils.readBuildNO()).andReturn("Build#release-2018.12.10.12.11.09.95").anyTimes();
        PowerMock.replayAll();

        String classFilePath = "file:/Applications/FineReport_10.0_12_10/webapps/webroot/WEB-INF/lib/fine-report-engine-10.0.jar!/com/fr/function";
        assertFalse(Reflect.on(FunctionConstants.class).call("isCustomFormulaPath", classFilePath).<Boolean>get());

        classFilePath = "/Applications/FineReport_10.0_12_10/webapps/webroot/WEB-INF/classes/com/fr/function";
        assertTrue(Reflect.on(FunctionConstants.class).call("isCustomFormulaPath", classFilePath).<Boolean>get());
    }
}
