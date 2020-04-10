package com.fr.design.actions.file;

import com.fr.design.base.mode.DesignModeContext;
import com.fr.design.fun.PreviewProvider;
import com.fr.design.i18n.Toolkit;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.utils.DesignUtils;
import com.fr.file.FILE;
import com.fr.file.FileNodeFILE;
import com.fr.general.GeneralUtils;
import com.fr.general.web.ParameterConstants;
import com.fr.stable.project.ProjectConstants;
import com.fr.web.referrer.DesignSessionReferrer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static javax.swing.JOptionPane.OK_CANCEL_OPTION;
import static javax.swing.JOptionPane.OK_OPTION;
import static javax.swing.JOptionPane.WARNING_MESSAGE;
import static com.fr.design.dialog.FineJOptionPane.showConfirmDialog;

public final class WebPreviewUtils {

    public static void preview(JTemplate<?, ?> jt) {
        preview(jt, jt.getPreviewType());
    }

    public static void preview(JTemplate<?, ?> jt, PreviewProvider provider) {
        String baseRoute = jt.route();

        String previewType = ParameterConstants.VIEWLET;
        Map<String, Object> paraMap = new HashMap<>(getExtraPara());

        if (provider != null) {
            Map<String, Object> providerParaMap = provider.parametersForPreview();
            if (providerParaMap != null) {
                paraMap.putAll(providerParaMap);
            }
            previewType = provider.getActionType();
        }
        actionPerformed(jt, baseRoute, paraMap, previewType);
    }

    private static Map<String, Object> getExtraPara() {
        Map<String, Object> extraPara = new HashMap<>();
        if (DesignModeContext.isVcsMode()) {
            extraPara.put("mode", DesignModeContext.getMode().toString());

        }
        extraPara.putAll(new DesignSessionReferrer.Builder().referrerMap());

        return extraPara;
    }

    private static void actionPerformed(JTemplate<?, ?> jt, String baseRoute, Map<String, Object> map, String actionType) {
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
            browseUrl(currentTemplate, baseRoute, map, actionType, jt);
        } else {
            // 说明模板没有保存在报表运行环境下面,提示用户
            int selVal = showConfirmDialog(
                    DesignerContext.getDesignerFrame(),
                    Toolkit.i18nText("Fine-Design_Basic_Web_Preview_Message"),
                    Toolkit.i18nText("Fine-Design_Basic_Preview_Tool_Tips"),
                    OK_CANCEL_OPTION,
                    WARNING_MESSAGE);

            if (OK_OPTION == selVal) {
                if (!jt.saveAsTemplate2Env()) {
                    return;
                }
                currentTemplate = jt.getEditingFILE();
                browseUrl(currentTemplate, baseRoute, map, actionType, jt);
            }
        }
    }

    private static void browseUrl(FILE currentTemplate,
                                  String baseRoute,
                                  Map<String, Object> map,
                                  String actionType, JTemplate<?, ?> jt) {
        if (!(currentTemplate instanceof FileNodeFILE)) {
            return;
        }

        if (currentTemplate.exists()) {
            String path = currentTemplate.getPath();
            if (path.startsWith(ProjectConstants.REPORTLETS_NAME)) {
                path = path.substring(ProjectConstants.REPORTLETS_NAME.length() + 1);
            }

            List<String> parameterNameList = new ArrayList<>();
            List<String> parameterValueList = new ArrayList<>();

            parameterNameList.add(actionType);
            parameterValueList.add(path);
            if (map != null) {
                for (String key : map.keySet()) {
                    parameterNameList.add(key);
                    parameterValueList.add(GeneralUtils.objectToString(map.get(key)));
                }
            }
            DesignUtils.visitEnvServerByParameters(
                    baseRoute,
                    parameterNameList.toArray(new String[0]),
                    parameterValueList.toArray(new String[0])
            );
        } else {
            int selVal = showConfirmDialog(
                    DesignerContext.getDesignerFrame(),
                    Toolkit.i18nText("Fine-Design_Basic_Web_Preview_Message"),
                    Toolkit.i18nText("Fine-Design_Basic_Preview_Tool_Tips"),
                    OK_CANCEL_OPTION,
                    WARNING_MESSAGE
            );
            if (OK_OPTION == selVal) {
                jt.saveAsTemplate();
            }
        }
    }
}
