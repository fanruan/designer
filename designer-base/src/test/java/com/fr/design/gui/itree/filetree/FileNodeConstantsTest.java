package com.fr.design.gui.itree.filetree;

import com.fr.base.extension.FileExtension;
import com.fr.decision.extension.report.ReportSupportedFileProvider;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.fun.ReportSupportedFileUIProvider;
import com.fr.design.fun.impl.AbstractReportSupportedFileUIProvider;
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
        Assert.assertEquals(1, extra.getArray(ReportSupportedFileUIProvider.XML_TAG).size());
        ReportSupportedFileUIProvider option = (ReportSupportedFileUIProvider) extra.getArray(ReportSupportedFileUIProvider.XML_TAG).iterator().next();
        Assert.assertEquals(FileExtension.CPTX, option.getSupportedFile().getFileExtensions()[0]);
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
        Set<Mutable> options = new HashSet<Mutable>() {{
            add(new MockNewTemplateFileOption());
        }};
        EasyMock.expect(extra.getArray(ReportSupportedFileUIProvider.XML_TAG)).andReturn(options).anyTimes();
        EasyMock.replay(extra);
        return extra;
    }

    private class MockNewTemplateFileOption extends AbstractReportSupportedFileUIProvider {

        @Override
        public ReportSupportedFileProvider getSupportedFile() {
            ReportSupportedFileProvider supportedFileProvider = EasyMock.mock(ReportSupportedFileProvider.class);
            EasyMock.expect(supportedFileProvider.getFileExtensions()).andReturn(new FileExtension[]{FileExtension.CPTX}).anyTimes();
            EasyMock.replay(supportedFileProvider);
            return supportedFileProvider;
        }


    }

}
