package com.fr.design.gui.syntax.util;

import com.fr.stable.StringUtils;

/**
 * @author： Harrison
 * @date： 2018/08/29
 * @description: 为 RTextArea 类中的 Action.xxx 准备的国际化匹配文件
 **/
public enum RTextAreaActionI18nMappingUtil {

    Action_CollapseAllFolds("Action.CollapseAllFolds","Fine-Design_Basic_Action_CollapseAllFolds_Name","Fine-Design_Basic_Action_CollapseAllFolds_Mnemonic","Fine-Design_Basic_Action_CollapseAllFolds_Desc"),
    Action_CollapseCommentFolds("Action.CollapseCommentFolds","Fine-Design_Basic_Action_CollapseCommentFolds_Name","Fine-Design_Basic_Action_CollapseCommentFolds_Mnemonic","Fine-Design_Basic_Action_CollapseCommentFolds_Desc"),
    Action_Copy("Action.Copy","Fine-Design_Basic_Action_Copy_Name","Fine-Design_Basic_Action_Copy_Mnemonic","Fine-Design_Basic_Action_Copy_Desc"),
    Action_Cut("Action.Cut","Fine-Design_Basic_Action_Cut_Name","Fine-Design_Basic_Action_Cut_Mnemonic","Fine-Design_Basic_Action_Cut_Desc"),
    Action_Delete("Action.Delete","Fine-Design_Basic_Action_Delete_Name","Fine-Design_Basic_Action_Delete_Mnemonic","Fine-Design_Basic_Action_Delete_Desc"),
    Action_ExpandAllFolds("Action.ExpandAllFolds","Fine-Design_Basic_Action_ExpandAllFolds_Name","Fine-Design_Basic_Action_ExpandAllFolds_Mnemonic","Fine-Design_Basic_Action_ExpandAllFolds_Desc"),
    Action_Paste("Action.Paste","Fine-Design_Basic_Action_Paste_Name","Fine-Design_Basic_Action_Paste_Mnemonic","Fine-Design_Basic_Action_Paste_Desc"),
    Action_Redo("Action.Redo","Fine-Design_Basic_Action_Redo_Name","Fine-Design_Basic_Action_Redo_Mnemonic","Fine-Design_Basic_Action_Redo_Desc"),
    Action_SelectAll("Action.SelectAll","Fine-Design_Basic_Action_SelectAll_Name","Fine-Design_Basic_Action_SelectAll_Mnemonic","Fine-Design_Basic_Action_SelectAll_Desc"),
    Action_ToggleCurrentFold("Action.ToggleCurrentFold","Fine-Design_Basic_Action_ToggleCurrentFold_Name","Fine-Design_Basic_Action_ToggleCurrentFold_Mnemonic","Fine-Design_Basic_Action_ToggleCurrentFold_Desc"),
    Action_Undo("Action.Undo","Fine-Design_Basic_Action_Undo_Name","Fine-Design_Basic_Action_Undo_Mnemonic","Fine-Design_Basic_Action_Undo_Desc");

    private String actionRawKey;
    private String actionName;
    private String actionMnemonic;
    private String actionDesc;

    RTextAreaActionI18nMappingUtil(String actionRawKey, String actionName, String actionMnemonic, String actionDesc) {
        this.actionRawKey = actionRawKey;
        this.actionName = actionName;
        this.actionMnemonic = actionMnemonic;
        this.actionDesc = actionDesc;
    }

    public static String getActionName(String actionKey) {
        for (RTextAreaActionI18nMappingUtil mapping: RTextAreaActionI18nMappingUtil.values()) {
            if (StringUtils.equals(mapping.actionRawKey, actionKey)) {
                return mapping.actionName;
            }
        }
        return actionKey;
    }
    public static String getActionMnemonic(String actionKey) {
        for (RTextAreaActionI18nMappingUtil mapping: RTextAreaActionI18nMappingUtil.values()) {
            if (StringUtils.equals(mapping.actionRawKey, actionKey)) {
                return mapping.actionMnemonic;
            }
        }
        return actionKey;
    }
    public static String getActionDesc(String actionKey) {
        for (RTextAreaActionI18nMappingUtil mapping: RTextAreaActionI18nMappingUtil.values()) {
            if (StringUtils.equals(mapping.actionRawKey, actionKey)) {
                return mapping.actionDesc;
            }
        }
        return actionKey;
    }
}
