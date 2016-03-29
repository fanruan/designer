package com.fr.design.fun;

import com.fr.design.mainframe.JTemplate;
import com.fr.stable.fun.Level;

import java.util.Map;

/**
 * @author richie
 * @date 2015-03-19
 * @since 8.0
 * 自定义预览方式接口
 */
public interface PreviewProvider extends Level{

    String MARK_STRING = "PreviewProvider";

    int CURRENT_LEVEL = 1;


    /**
     * 下拉弹出菜单的名字
     * @return 弹出菜单名字
     */
    String nameForPopupItem();

    /**
     * 下拉弹出菜单的图标路径
     * @return 图标路径
     */
    String iconPathForPopupItem();

    /**
     * 大图标路径
     * @return 大图标路径
     */
    String iconPathForLarge();

    /**
     * 点击下拉菜单时触发的事件
     * @param jt 当前的模板对象
     */
    void onClick(JTemplate<?, ?> jt);

    /**
     * 用于标记预览类型的整数
     * @return 预览类型
     */
    int previewTypeCode();

    /**
     * 该种预览方式所携带的默认参数集合
     * @return 参数集合
     */
    Map<String, Object> parametersForPreview();

}