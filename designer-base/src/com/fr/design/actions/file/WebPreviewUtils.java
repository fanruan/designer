package com.fr.design.actions.file;

import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.utils.DesignUtils;
import com.fr.file.FILE;
import com.fr.file.FileNodeFILE;
import com.fr.general.GeneralUtils;
import com.fr.general.Inter;
import com.fr.general.web.ParameterConstants;
import com.fr.stable.project.ProjectConstants;

import javax.swing.*;
import java.util.Map;

public final class WebPreviewUtils {

    public static void onWorkbookPreview(JTemplate<?, ?> jt) {
        actionPerformed(jt, jt.getPreviewType().parametersForPreview(), ParameterConstants.REPORTLET);
    }

    public static void onFormPreview(JTemplate<?, ?> jt) {
        actionPerformed(jt, null, ParameterConstants.FORMLET);
    }

    public static void actionPerformed(JTemplate<?, ?> jt, Map<String, Object> map, String actionType) {
        if (jt == null) {
            return;
        }

        DesignerContext.getDesignerFrame().refreshToolbar();

        jt.stopEditing();
        /*
		 * alex:如果没有保存,先保存到Env
		 * 
		 * 如果保存失败,不执行下面的WebPreview
		 */
        if (!jt.isSaved() && !jt.saveTemplate2Env()) {
            return;
        }

        FILE currentTemplate = jt.getEditingFILE();
        // carl:是否是保存在运行环境下的模板，不是就不能被预览
        if (currentTemplate instanceof FileNodeFILE) {
            browseUrl(currentTemplate, map, actionType, jt);
        } else {
            // 说明模板没有保存在报表运行环境下面,提示用户
            int selVal = JOptionPane.showConfirmDialog(DesignerContext.getDesignerFrame(), Inter.getLocText("Web_Preview_Message"),
                    Inter.getLocText("Preview_ToolTips"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);

            if (JOptionPane.OK_OPTION == selVal) {
                if (!jt.saveAsTemplate2Env()) {
                    return;
                }
                currentTemplate = jt.getEditingFILE();
                browseUrl(currentTemplate, map, actionType, jt);
            }
        }
    }

    private static void browseUrl(FILE currentTemplate, Map<String, Object> map, String actionType, JTemplate<?, ?> jt) {
        if (!(currentTemplate instanceof FileNodeFILE)) {
            return;
        }

        if (currentTemplate.exists()) {
            String path = currentTemplate.getPath();
            if (path.startsWith(ProjectConstants.REPORTLETS_NAME)) {
                path = path.substring(ProjectConstants.REPORTLETS_NAME.length() + 1);

                java.util.List<String> parameterNameList = new java.util.ArrayList<String>();
                java.util.List<String> parameterValueList = new java.util.ArrayList<String>();

                parameterNameList.add(actionType);
                parameterValueList.add(path);
                if (map != null) {
                    for (String key : map.keySet()) {
                        parameterNameList.add(key);
                        parameterValueList.add(GeneralUtils.objectToString(map.get(key)));
                    }
                }
                DesignUtils.visitEnvServerByParameters(parameterNameList.toArray(new String[parameterNameList.size()]), parameterValueList.toArray(new String[parameterValueList.size()]));
            }
        } else {
            int selVal = JOptionPane.showConfirmDialog(DesignerContext.getDesignerFrame(), Inter.getLocText("Web_Preview_Message"),
                    Inter.getLocText("Preview_ToolTips"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
            if (JOptionPane.OK_OPTION == selVal) {
                if (!jt.saveAsTemplate()) {
                    return;
                }
            }
        }
    }
}