package com.fr.design.i18n;

/**
 * 包装一些动作
 * @author Hades
 * @date 2019/6/4
 */
public interface Action {

    Action EMPTY_ACTION = new Action() {
        @Override
        public void todo() {
            // do nothing
        }
    };

    /**
     * 具体动作
     */
    void todo();
}
