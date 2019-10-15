package com.fr.design.gui.itree.filetree;

import com.fr.base.extension.FileExtension;
import com.fr.base.io.BaseBook;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.mainframe.AbstractAppProvider;
import com.fr.design.mainframe.App;
import com.fr.design.mainframe.JTemplate;
import com.fr.file.FILE;
import com.fr.stable.fun.mark.Mutable;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by alex sung on 2019/7/25.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(ExtraDesignClassManager.class)
public class FileNodeConstantsTest {
    @Test
    public void supportFileTypesTest() {
        ExtraDesignClassManager extra = mockExtraDesignClassManager();
        Assert.assertEquals(1, extra.getArray(App.MARK_STRING).size());
        App app = (App) extra.getArray(App.MARK_STRING).iterator().next();
        Assert.assertEquals("cptx", app.defaultExtensions()[0]);
    }

    @Test
    public void testSupportFileTypesOrder() {
        ExtraDesignClassManager extra = mockExtraDesignClassManager();
        PowerMock.mockStatic(ExtraDesignClassManager.class);
        EasyMock.expect(ExtraDesignClassManager.getInstance()).andReturn(extra).once();
        PowerMock.replayAll();
        String[] fileTypes = FileNodeConstants.getSupportFileTypes();
        Assert.assertEquals("cptx", fileTypes[0]);
        Assert.assertEquals("cpt", fileTypes[1]);
    }

    private ExtraDesignClassManager mockExtraDesignClassManager() {
        ExtraDesignClassManager extra = EasyMock.mock(ExtraDesignClassManager.class);
        Set<Mutable> apps = new HashSet<Mutable>() {{
            add(new MockCptxApp());
        }};
        EasyMock.expect(extra.getArray(App.MARK_STRING)).andReturn(apps).anyTimes();
        EasyMock.replay(extra);
        return extra;
    }

    private class MockCptxApp extends AbstractAppProvider {
        @Override
        public String[] defaultExtensions() {
            return new String[]{FileExtension.CPTX.getExtension()};
        }

        @Override
        public JTemplate openTemplate(FILE tplFile) {
            return null;
        }

        @Override
        public BaseBook asIOFile(FILE tplFile) {
            return null;
        }
    }

}
