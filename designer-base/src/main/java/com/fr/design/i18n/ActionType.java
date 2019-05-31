package com.fr.design.i18n;

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
