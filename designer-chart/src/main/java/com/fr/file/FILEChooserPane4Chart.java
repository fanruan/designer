package com.fr.file;

import com.fr.base.extension.FileExtension;
import com.fr.file.filter.ChooseFileFilter;
import com.fr.general.Inter;
import com.fr.stable.ProductConstants;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 7.1.1
 */
public class FILEChooserPane4Chart extends FILEChooserPane {

    private static final FILEChooserPane4Chart INSTANCE = new FILEChooserPane4Chart(true, true);

    /**
     * @param showEnv
     * @param showLoc
     * @return
     */
    public static FILEChooserPane4Chart getInstance(boolean showEnv, boolean showLoc) {
        INSTANCE.showEnv = showEnv;
        INSTANCE.showLoc = showLoc;
        INSTANCE.showWebReport = false;
        INSTANCE.setModelOfPlaceList();
        INSTANCE.removeAllFilter();
        return INSTANCE;
    }

    /**
     * @param showEnv
     * @param showLoc
     */
    public FILEChooserPane4Chart(boolean showEnv, boolean showLoc) {
        super(showEnv, showLoc);
    }

    @Override
    protected void fileType() {
        String appName = ProductConstants.APP_NAME;
        if (FileExtension.CRT.matchExtension(suffix)) {
            this.addChooseFILEFilter(new ChooseFileFilter(FileExtension.CRT, appName + Inter.getLocText(new String[]{"Utils-The-Chart", "FR-App-All_File"})));
            return;
        }
    }

    @Override
    protected String getEnvProjectName() {
        return Inter.getLocText("FR-Chart-Env_Directory");
    }


}