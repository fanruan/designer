package com.fr.design.fun;

import com.fr.design.actions.UpdateAction;
import com.fr.design.selection.QuickEditor;
import com.fr.stable.fun.mark.Mutable;


/**
 * Created by richie on 16/4/25.
 * 单元格元素和悬浮元素扩展,通过实现这个接口,可以在单元格中添加更多类型的元素.
 */
public interface ElementUIProvider extends Mutable {

    String MARK_STRING = "ElementUIProvider";

    int CURRENT_LEVEL = 2;

    /**
     * 单元格元素编辑器
     * @return 编辑器
     */
    Class<?> targetCellEditorClass();

    /**
     * 单元格内容
     * @return 单元格内容
     */
    Class<?> targetObjectClass();

    /**
     * 右侧的快速编辑器
     * @return 编辑器
     */
    Class<? extends QuickEditor> quickEditor();

    /**
     * 插件单元格元素
     * @return 插入操作类
     */
    Class<? extends UpdateAction> actionForInsertCellElement();

    /**
     * 插入悬浮元素
     * @return 插入操作类
     */
    Class<? extends UpdateAction> actionForInsertFloatElement();
}
