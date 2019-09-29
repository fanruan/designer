package com.fr.design.update.ui.dialog;


import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class RestoreResultDialogTest {

    @Test
    public void testDeletePreviousPropertyFile() {
        IMocksControl control = EasyMock.createControl();
        File testRecordFile = control.createMock(File.class);
        File testMoveFile = control.createMock(File.class);
        EasyMock.expect(testRecordFile.getPath()).andReturn("move").anyTimes();
        EasyMock.expect(testMoveFile.getPath()).andReturn("record").anyTimes();
        control.replay();
        Assert.assertTrue(RestoreResultDialog.deletePreviousPropertyFile("move","record"));
    }
}
