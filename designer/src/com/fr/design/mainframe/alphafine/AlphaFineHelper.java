package com.fr.design.mainframe.alphafine;

import com.fr.base.FRContext;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.alphafine.cell.cellModel.*;
import com.fr.design.mainframe.alphafine.component.AlphaFineDialog;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;

import java.io.File;

/**
 * Created by XiaXiang on 2017/5/8.
 */
public class AlphaFineHelper {

    public static void showAlphaFineDialog() {
        AlphaFineDialog dialog = new AlphaFineDialog(DesignerContext.getDesignerFrame());
        dialog.setVisible(true);
    }

    public static String findFolderName (String text) {
        return getSplitText(text, 2);
    }

    private static String getSplitText(String text, int index) {
        if (StringUtils.isNotBlank(text)) {
            String[] textArray = text.split("/");
            if (textArray != null && textArray.length > 1) {
                return textArray[textArray.length - index];
            }
        }
        return null;
    }

    public static String findFileName (String text) {
        return getSplitText(text, 1);
    }




}
