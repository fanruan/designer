package com.fr.file;

import com.fr.base.extension.FileExtension;
import com.fr.file.filter.ChooseFileFilter;
import com.fr.invoke.Reflect;
import com.fr.stable.StringUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by kerry on 2019-10-15
 */

public class FILEChooserPaneTest {
    @Test
    public void testAddChooseFileFilter() {
        FILEChooserPane chooserPane = Reflect.on(FILEChooserPane.class).field("INSTANCE").get();
        Reflect.on(chooserPane).set("suffix", ".cpt");
        String result1 = Reflect.on(chooserPane).call("calProperFileName", "WorkBook1.cpt", null).get();
        Assert.assertEquals("WorkBook1.cpt", result1);

        ChooseFileFilter chooseFileFilter1 = new ChooseFileFilter(FileExtension.CPT, StringUtils.EMPTY);
        String result2 = Reflect.on(chooserPane).call("calProperFileName", "WorkBook1.cpt", chooseFileFilter1).get();
        Assert.assertEquals("WorkBook1.cpt", result2);

        ChooseFileFilter chooseFileFilter2 = new ChooseFileFilter(FileExtension.CPTX, StringUtils.EMPTY);
        String result3 = Reflect.on(chooserPane).call("calProperFileName", "WorkBook1.cpt", chooseFileFilter2).get();
        Assert.assertEquals("WorkBook1.cptx", result3);

        ChooseFileFilter chooseFileFilter3 = new ChooseFileFilter(FileExtension.CPT, StringUtils.EMPTY);
        String result4 = Reflect.on(chooserPane).call("calProperFileName", "WorkBook1.cptx", chooseFileFilter3).get();
        Assert.assertEquals("WorkBook1.cpt", result4);

        ChooseFileFilter chooseFileFilter5 = new ChooseFileFilter(FileExtension.CPTX, StringUtils.EMPTY);
        String result5 = Reflect.on(chooserPane).call("calProperFileName", "WorkBook1.cptx", chooseFileFilter5).get();
        Assert.assertEquals("WorkBook1.cptx", result5);
    }

}
