package com.fr.design.mainframe.alphafine;

import com.fr.base.FRContext;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.alphafine.component.AlphaFineDialog;
import com.fr.stable.StableUtils;

import java.io.File;

/**
 * Created by XiaXiang on 2017/5/8.
 */
public class AlphaFineHelper {

    public static void showAlphaFineDialog() {
        AlphaFineDialog dialog = new AlphaFineDialog(DesignerContext.getDesignerFrame());
        dialog.setVisible(true);
    }

    public static File getInfoFile() {
        return new File(StableUtils.pathJoin(FRContext.getCurrentEnv().getPath(), AlphaFineConstants.SAVE_FILE_NAME));
    }

    public static String findFolderName (String text) {
        String[] textArray = text.split("/");
        if (textArray != null && textArray.length > 1) {
            return textArray[textArray.length - 2];

        }
        return null;
    }


}
