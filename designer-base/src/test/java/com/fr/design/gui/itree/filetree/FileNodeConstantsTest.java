package com.fr.design.gui.itree.filetree;

import com.fr.base.extension.FileExtension;
import com.fr.report.ExtraReportClassManager;
import com.fr.report.fun.ReportSupportedFileProvider;
import com.fr.report.fun.impl.AbstractReportSupportedFileProvider;
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
@PrepareForTest(ExtraReportClassManager.class)
public class FileNodeConstantsTest {
    @Test
    public void supportFileTypesTest() {
        ExtraReportClassManager extra = mockExtraReportClassManager();
        Assert.assertEquals(1, extra.getArray(ReportSupportedFileProvider.XML_TAG).size());
        ReportSupportedFileProvider option = (ReportSupportedFileProvider) extra.getArray(ReportSupportedFileProvider.XML_TAG).iterator().next();
        Assert.assertEquals(FileExtension.CPTX, option.getFileExtensions()[0]);
    }

    @Test
    public void testSupportFileTypesOrder() {
        ExtraReportClassManager extra = mockExtraReportClassManager();
        PowerMock.mockStatic(ExtraReportClassManager.class);
        EasyMock.expect(ExtraReportClassManager.getInstance()).andReturn(extra).once();
        PowerMock.replayAll();
        String[] fileTypes = FileNodeConstants.getSupportFileTypes();
        Assert.assertEquals("cptx", fileTypes[0]);
        Assert.assertEquals("cpt", fileTypes[1]);
    }

    private ExtraReportClassManager mockExtraReportClassManager() {
        ExtraReportClassManager extra = EasyMock.mock(ExtraReportClassManager.class);
        Set<Mutable> options = new HashSet<Mutable>() {{
            add(new MockNewTemplateFileOption());
        }};
        EasyMock.expect(extra.getArray(ReportSupportedFileProvider.XML_TAG)).andReturn(options).anyTimes();
        EasyMock.replay(extra);
        return extra;
    }

    private class MockNewTemplateFileOption extends AbstractReportSupportedFileProvider {

        @Override
        public FileExtension[] getFileExtensions() {
            return new FileExtension[]{FileExtension.CPTX
            };
        }

    }

}
