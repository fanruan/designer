package com.fr.design.mainframe.alphafine;

import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.alphafine.component.AlphaFineDialog;
import com.fr.stable.StringUtils;

/**
 * Created by XiaXiang on 2017/5/8.
 */
public class AlphaFineHelper {

    /**
     * 弹出alphafine搜索面板
     */
    public static void showAlphaFineDialog(boolean forceOpen) {
        AlphaFineDialog dialog = new AlphaFineDialog(DesignerContext.getDesignerFrame(), forceOpen);
        dialog.setVisible(true);
    }

    /**
     * 获取文件名上级目录
     * @param text
     * @return
     */
    public static String findFolderName (String text) {
        return getSplitText(text, 2);
    }

    /**
     * 分割字符串，获取文件名，文件名上级目录等
     * @param text
     * @param index
     * @return
     */
    private static String getSplitText(String text, int index) {
        if (StringUtils.isNotBlank(text)) {
            String[] textArray = text.replaceAll("\\\\", "/").split("/");
            if (textArray != null && textArray.length > 1) {
                return textArray[textArray.length - index];
            }
        }
        return null;
    }

    /**
     * 获取文件名
     * @param text
     * @return
     */
    public static String findFileName (String text) {
        return getSplitText(text, 1);
    }




}
