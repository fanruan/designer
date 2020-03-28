package com.fr.file.filter;

import com.fr.base.extension.FileExtension;
import com.fr.stable.CoreConstants;
import com.fr.stable.StringUtils;
import junit.framework.TestCase;
import org.junit.Assert;


/**
 * @author hades
 * @version 10.0
 * Created by hades on 2020/3/18
 */
public class ChooseFileFilterTest extends TestCase {

    public void testGetExtensionString() {
        ChooseFileFilter chooseFileFilter = new ChooseFileFilter(FileExtension.XLSX, StringUtils.EMPTY);
        chooseFileFilter.addExtension(FileExtension.XLS.getExtension());
        Assert.assertEquals(CoreConstants.DOT + FileExtension.XLSX.getExtension(), chooseFileFilter.getExtensionString());
        Assert.assertEquals(CoreConstants.DOT + FileExtension.XLS.getExtension(), chooseFileFilter.getExtensionString(1));
        ChooseFileFilter chooseFileFilter1 = new ChooseFileFilter();
        Assert.assertEquals(StringUtils.EMPTY, chooseFileFilter1.getExtensionString());
        Assert.assertEquals(StringUtils.EMPTY, chooseFileFilter1.getExtensionString(1));

    }

    public void testGetExtensionCount() {
        ChooseFileFilter chooseFileFilter = new ChooseFileFilter(FileExtension.CPT, StringUtils.EMPTY);
        chooseFileFilter.addExtension(FileExtension.CPTX.getExtension());
        Assert.assertEquals(2, chooseFileFilter.getExtensionCount());
    }
}