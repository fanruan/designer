package com.fr.design.update.ui.dialog;

import com.fr.design.RestartHelper;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.StableUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class RestoreResultDialogTest {

    @Test
    public void testDeletePreviousPropertyFile() {
        File moveFile = new File(RestartHelper.MOVE_FILE);
        File recordFile = new File(RestartHelper.RECORD_FILE);
        try {
            StableUtils.makesureFileExist(moveFile);
            StableUtils.makesureFileExist(recordFile);
            Assert.assertTrue(RestoreResultDialog.deletePreviousPropertyFile());
        } catch (IOException e) {
            FineLoggerFactory.getLogger().error(e.getMessage(),e);
        }
    }
}
