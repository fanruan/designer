package com.fr.design.fun.impl;

import com.fr.design.fun.ReportSupportedFileUIProvider;
import com.fr.design.mainframe.JTemplate;
import com.fr.file.FILEChooserPane;
import com.fr.stable.fun.impl.AbstractProvider;
import com.fr.stable.fun.mark.API;

import javax.swing.Icon;

/**
 * Created by kerry on 2019-10-14
 */
@API(level = ReportSupportedFileUIProvider.CURRENT_LEVEL)
public abstract class AbstractReportSupportedFileUIProvider extends AbstractProvider implements ReportSupportedFileUIProvider {
    @Override
    public void addChooseFileFilter(FILEChooserPane fileChooser, String suffix) {

    }

    @Override
    public Icon getFileIcon(String path, boolean isShowLock) {
        return null;
    }

    @Override
    public boolean saveToNewFile(String targetPath, JTemplate jTemplate) {
        return false;
    }

    @Override
    public int currentAPILevel() {
        return CURRENT_LEVEL;
    }

    @Override
    public String mark4Provider() {
        return getClass().getName();
    }
}
