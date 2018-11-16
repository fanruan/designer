package com.fr.design.fun;

import com.fr.json.JSONObject;
import com.fr.stable.fun.mark.Mutable;

import java.util.Map;

public interface MessageObjectProvider extends Mutable {
    String MARK_STRING = "MessageObjectProvider";

    int CURRENT_LEVEL = 1;
    //消息类型
    int getMessageType();

    /**
     * 需要嵌入的组件内容
     * @return
     */
    Map<String,String> getValueEditorItems();

    /**
     * 实现发消息
     * @param messageJo
     */
    void sendMessage(JSONObject messageJo);
}
