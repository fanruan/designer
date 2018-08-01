package com.fr.design.actions.file;

import com.fr.base.extension.FileExtension;
import com.fr.base.vcs.DesignerMode;
import com.fr.design.fun.PreviewProvider;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.utils.DesignUtils;
import com.fr.file.FILE;
import com.fr.file.FileNodeFILE;
import com.fr.general.GeneralUtils;
import com.fr.general.web.ParameterConstants;
import com.fr.stable.project.ProjectConstants;
import com.fr.stable.web.AbstractWebletCreator;

import javax.swing.JOptionPane;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class WebPreviewUtils {

    public static void preview(JTemplate<?, ?> jt) {
        preview(jt, jt.getPreviewType());
    }

    @SuppressWarnings("unchecked")
    public static void preview(JTemplate<?, ?> jt, PreviewProvider provider) {
        String baseRoute = jt.route();
        actionPerformed(jt, baseRoute, provider == null ? Collections.EMPTY_MAP : provider.parametersForPreview(), ParameterConstants.VIEWLET);
    }

    private static void actionPerformed(JTemplate<?, ?> jt, String baseRoute, Map<String, Object> map, String actionType) {
        if (jt == null) {
            return;
        }

        if (map == null || map == Collections.EMPTY_MAP) {
            map = new HashMap<>();
        }
        if (DesignerMode.isVcsMode()) {
            map.put("mode", DesignerMode.getMode().toString());
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
            browseUrl(currentTemplate, baseRoute, map, actionType, jt);
        } else {
            // 说明模板没有保存在报表运行环境下面,提示用户
            int selVal = JOptionPane.showConfirmDialog(DesignerContext.getDesignerFrame(), com.fr.design.i18n.Toolkit.i18nText("Web_Preview_Message"),
                    com.fr.design.i18n.Toolkit.i18nText("Preview_ToolTips"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);

            if (JOptionPane.OK_OPTION == selVal) {
                if (!jt.saveAsTemplate2Env()) {
                    return;
                }
                currentTemplate = jt.getEditingFILE();
                browseUrl(currentTemplate, baseRoute, map, actionType, jt);
            }
        }
    }

    private static void browseUrl(FILE currentTemplate, String baseRoute, Map<String, Object> map, String actionType, JTemplate<?, ?> jt) {
        if (!(currentTemplate instanceof FileNodeFILE)) {
            return;
        }

        if (currentTemplate.exists()) {
            String path = currentTemplate.getPath();
            if (path.startsWith(ProjectConstants.REPORTLETS_NAME)) {
                path = path.substring(ProjectConstants.REPORTLETS_NAME.length() + 1);
            }

            java.util.List<String> parameterNameList = new java.util.ArrayList<String>();
            java.util.List<String> parameterValueList = new java.util.ArrayList<String>();

            // 暂时屏蔽cptx直接访问
            if (path.endsWith(FileExtension.CPTX.getSuffix())) {
                path = path.substring(0, path.length() - 1);
                parameterNameList.add(AbstractWebletCreator.FORMAT);
                parameterValueList.add(AbstractWebletCreator.X);
            }

            parameterNameList.add(actionType);
            parameterValueList.add(path);
            if (map != null) {
                for (String key : map.keySet()) {
                    parameterNameList.add(key);
                    parameterValueList.add(GeneralUtils.objectToString(map.get(key)));
                }
            }
            DesignUtils.visitEnvServerByParameters(baseRoute, parameterNameList.toArray(new String[parameterNameList.size()]), parameterValueList.toArray(new String[parameterValueList.size()]));
        } else {
            int selVal = JOptionPane.showConfirmDialog(DesignerContext.getDesignerFrame(), com.fr.design.i18n.Toolkit.i18nText("Web_Preview_Message"),
                    com.fr.design.i18n.Toolkit.i18nText("Preview_ToolTips"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
            if (JOptionPane.OK_OPTION == selVal) {
                if (!jt.saveAsTemplate()) {
                    return;
                }
            }
        }
    }
}
