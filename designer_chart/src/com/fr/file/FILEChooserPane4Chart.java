package com.fr.file;

import com.fr.file.filter.ChooseFileFilter;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.stable.ProductConstants;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 7.1.1
 */
public class FILEChooserPane4Chart extends FILEChooserPane {

    private static final FILEChooserPane4Chart INSTANCE = new FILEChooserPane4Chart(true,true);

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

    protected void fileType() {
        String appName = ProductConstants.APP_NAME;
        if(ComparatorUtils.equals(suffix, ".crt")){
            this.addChooseFILEFilter(new ChooseFileFilter("crt", appName + Inter.getLocText(new String[]{"Utils-The-Chart", "FR-App-All_File"})));
            return;
        }
    }

    protected String getEnvProjectName(){
        return Inter.getLocText("FR-Chart-Env_Directory");
    }


}