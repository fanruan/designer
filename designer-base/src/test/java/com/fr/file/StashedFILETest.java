package com.fr.file;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

public class StashedFILETest {

    /**
     * 用于测试暂存文件未保存时的提示
     */
    @Test
    public void testToString() {
        FILE file = EasyMock.mock(FILE.class);
        String name = "getA.cpt";
        EasyMock.expect(file.getName()).andReturn(name).once();
        EasyMock.replay(file);
        FILE stashedFILE = new StashedFILE(file, new byte[0]);
        String expectString = FILEFactory.MEM_PREFIX + name;
        Assert.assertEquals(expectString, stashedFILE + "");
        EasyMock.verify(file);
    }
}
