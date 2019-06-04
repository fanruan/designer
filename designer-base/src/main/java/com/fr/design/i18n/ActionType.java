package com.fr.design.i18n;

/**
 * 不同语言环境下的action
 *
 * @author Hades
 * @date 2019/5/30
 */
public enum ActionType {

    /**
     * 视频教学
     */
    VIDEO("video"),

    /**
     * 激活码
     */
    ACTIVATION_CODE("activationCode"),

    /**
     * 帮助文档
     */
    HELP_DOCUMENT("helpDocument"),

    /**
     * 论坛
     */
    BBS("bbs");


    private String description;

    ActionType(String description) {
        this.description = description;
    }

}
