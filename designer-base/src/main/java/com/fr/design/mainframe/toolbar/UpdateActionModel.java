package com.fr.design.mainframe.toolbar;

import com.fr.design.actions.UpdateAction;
import com.fr.stable.pinyin.PinyinFormat;
import com.fr.stable.pinyin.PinyinHelper;

/**
 * Created by XiaXiang on 2017/5/24.
 */

/**
 * action对象
 */
public class UpdateActionModel {
    private static final String SEPARATOR = "/";
    private String parentName;
    private String actionName;
    private String className;
    private String searchKey;
    private UpdateAction action;

    public UpdateActionModel(String parentName, UpdateAction action) {
        this.parentName = parentName;
        this.action = action;
        this.actionName = action.getName();
        setSearchKey(parentName, action);
    }

    /**
     * 获取搜索关键字，包括上级菜单名，菜单名，以及对应打开面板的所有文字信息（使其能够支持模糊搜索）
     * @param parentName
     * @param action
     */
    private void setSearchKey(String parentName, UpdateAction action) {
        if (actionName == null) {
            return;
        }
        String buffer = parentName +
                SEPARATOR +
                PinyinHelper.convertToPinyinString(parentName, "", PinyinFormat.WITHOUT_TONE) +
                SEPARATOR +
                PinyinHelper.getShortPinyin(parentName) +
                SEPARATOR +
                actionName +
                SEPARATOR +
                PinyinHelper.convertToPinyinString(actionName, "", PinyinFormat.WITHOUT_TONE) +
                SEPARATOR +
                PinyinHelper.getShortPinyin(actionName) +
                action.getSearchText();
        this.searchKey = buffer.toLowerCase();
    }

    /**
     * 获取上一层级菜单name
     * @return
     */
    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    /**
     * 获取action
     * @return
     */
    public UpdateAction getAction() {
        return action;
    }

    public void setAction(UpdateAction action) {
        this.action = action;
    }

    /**
     * 获取actionName
     * @return
     */
    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public String getClassName() {
        return getAction().getClass().getName();
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
