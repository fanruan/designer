package com.fr.design.notification;

/**
 * 阅后即焚的消息提醒
 *
 * created by Harrison on 2020/03/16
 **/
public interface SnapChat {
    
    /**
     * 默认状态
     *
     * @return 默认状态
     */
    boolean defaultStatus();
    
    /**
     * 已读
     *
     * @return 是否为已读
     */
    boolean hasRead();
    
    /**
     * 标记为已读
     */
    void markRead();
    
    /**
     * 独一无二的标志
     *
     * @return 字符标志
     */
    SnapChatKey key();
}
