package com.fr.design.mainframe.alphafine;

import com.fr.design.DesignerEnvManager;
import com.fr.design.actions.help.alphafine.AlphafineConfigManager;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.alphafine.cell.model.NoResultModel;
import com.fr.design.mainframe.alphafine.component.AlphaFineDialog;
import com.fr.general.Inter;
import com.fr.general.ProcessCanceledException;
import com.fr.stable.StringUtils;

/**
 * Created by XiaXiang on 2017/5/8.
 */
public class AlphaFineHelper {
    public static final NoResultModel noResultModel = new NoResultModel(Inter.getLocText("FR-Designere_AlphaFine_NoResult"));
    public static final NoResultModel noConnectionModel = new NoResultModel(Inter.getLocText("FR-Designer_ConnectionFailed"));

    /**
     * 弹出alphafine搜索面板
     */
    public static void showAlphaFineDialog(boolean forceOpen) {
        AlphaFineDialog dialog = new AlphaFineDialog(DesignerContext.getDesignerFrame(), forceOpen);
        final AlphafineConfigManager manager = DesignerEnvManager.getEnvManager().getAlphafineConfigManager();
        manager.setNeedRemind(false);
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

    /**
     * 中断当前线程的搜索
     */
    public static void checkCancel() {
        if (Thread.interrupted()) {
            throw new ProcessCanceledException();
        }
    }




}
