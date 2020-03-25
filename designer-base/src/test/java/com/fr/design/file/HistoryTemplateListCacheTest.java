package com.fr.design.file;

import com.fr.design.mainframe.JTemplate;
import com.fr.file.FILE;
import com.fr.invoke.Reflect;
import junit.framework.TestCase;
import org.easymock.EasyMock;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author hades
 * @version 10.0
 * Created by hades on 2020/3/23
 */
public class HistoryTemplateListCacheTest extends TestCase {

    public void testContains() {
        JTemplate jTemplate = EasyMock.mock(JTemplate.class);
        FILE file = EasyMock.mock(FILE.class);
        EasyMock.expect(jTemplate.getEditingFILE()).andReturn(file).anyTimes();
        EasyMock.replay(file);
        EasyMock.replay(jTemplate);
        Assert.assertEquals(-1, HistoryTemplateListCache.getInstance().contains(file));
        Assert.assertEquals(-1, HistoryTemplateListCache.getInstance().contains(jTemplate));
        Reflect.on(HistoryTemplateListCache.getInstance()).set("historyList", new ArrayList<>(Arrays.asList(jTemplate)));
        Assert.assertEquals(0, HistoryTemplateListCache.getInstance().contains(file));
        Assert.assertEquals(0, HistoryTemplateListCache.getInstance().contains(jTemplate));
    }

}