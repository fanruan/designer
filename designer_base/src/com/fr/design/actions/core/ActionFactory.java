package com.fr.design.actions.core;

import com.fr.base.FRContext;
import com.fr.base.Utils;
import com.fr.design.actions.UpdateAction;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.menu.MenuKeySet;
import com.fr.design.selection.QuickEditor;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * 插入单元格元素和插入悬浮元素的一些集合方法
 */
public class ActionFactory {
    private static LinkedHashSet<Class<?>> actionClasses = new LinkedHashSet<>();
    private static LinkedHashSet<Class<?>> floatActionClasses = new LinkedHashSet<>();

    private ActionFactory() {
    }

    private static Map<Class, Class<? extends QuickEditor>> floatEditor = new HashMap<>();

    private static Class chartCollectionClass = null;

    private static Map<Class, Class<? extends QuickEditor>> cellEditor = new HashMap<>();

    private static UpdateAction chartPreStyleAction = null;

    private static UpdateAction chartMapEditorAction = null;

    /**
     * 待说明
     *
     * @param clazz  待说明
     * @param editor 待说明
     */
    public static void registerCellEditor(Class clazz, Class<? extends QuickEditor> editor) {
        cellEditor.put(clazz, editor);
    }


    /**
     * 待说明
     *
     * @param clazz  待说明
     * @param editor 待说明
     */
    public static void registerFloatEditor(Class clazz, Class<? extends QuickEditor> editor) {
        floatEditor.put(clazz, editor);
    }

    /**
     * 注册图表的 预定义样式.
     *
     * @param action 注册的图表预定义样式action
     */
    public static void registerChartPreStyleAction(UpdateAction action) {
        chartPreStyleAction = action;
    }

    /**
     * 返回 图表预定义样式Action
     */
    public static UpdateAction getChartPreStyleAction() {
        return chartPreStyleAction;
    }

    /**
     * 注册图表的 地图资源
     *
     * @param action 地图资源action
     */
    public static void registerChartMapEditorAction(UpdateAction action) {
        chartMapEditorAction = action;
    }

    public static UpdateAction getChartMapEditorAction() {
        return chartMapEditorAction;
    }

    /**
     * kunsnat: 图表注册 悬浮元素编辑器 , 因为ChartCollecion和ChartQuickEditor一个在Chart,一个在Designer, 所以分开注册.
     *
     * @param clazz 待说明
     */
    public static void registerChartCollection(Class clazz) {
        chartCollectionClass = clazz;
    }

    public static Class getChartCollectionClass() {
        return chartCollectionClass;
    }

    /**
     * kunsnat: 图表注册 悬浮元素编辑器 , 因为ChartCollection和ChartQuickEditor一个在Chart,一个在Designer, 所以分开注册.
     *
     * @param editor 待说明
     */
    public static void registerChartFloatEditorInEditor(Class<? extends QuickEditor> editor) {
        if (chartCollectionClass != null) {
            floatEditor.put(chartCollectionClass, editor);
        }
    }

    /**
     * kunsnat: 图表注册 悬浮元素编辑器 , 因为ChartCollecion和ChartQuickEditor一个在Chart,一个在Designer, 所以分开注册.
     *
     * @param editor 待说明
     */
    public static void registerChartCellEditorInEditor(Class<? extends QuickEditor> editor) {
        if (chartCollectionClass != null) {
            cellEditor.put(chartCollectionClass, editor);
        }
    }

    /**
     * 返回 悬浮元素选中的Editor
     */
    public static QuickEditor getFloatEditor(Class clazz) {
        return createEditor(clazz, floatEditor);
    }

    private static QuickEditor createEditor(Class clazz, Map<Class, Class<? extends QuickEditor>> editorMap) {
        Class<? extends QuickEditor> c = findQuickEditorClass(clazz, editorMap);
        if (c == null) {
            return null;
        }
        try {
            Constructor<? extends QuickEditor> constructor = c.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage(), e);
        }
        return null;
    }

    private static Class<? extends QuickEditor> findQuickEditorClass(Class clazz, Map<Class, Class<? extends QuickEditor>> editorMap) {
        Class<? extends QuickEditor> c = editorMap.get(clazz);
        if (c == null) {
            Class superClazz = clazz.getSuperclass();
            if (superClazz == null) {
                return null;
            }
            return findQuickEditorClass(superClazz, editorMap);
        }
        return c;
    }

    public static QuickEditor getCellEditor(Class clazz) {
        return createEditor(clazz, cellEditor);
    }

    public static UpdateAction createAction(Class clazz) {
        try {
            Constructor<? extends UpdateAction> c = clazz.getDeclaredConstructor();
            c.setAccessible(true);
            return c.newInstance();
        } catch (Exception e) {
            FRContext.getLogger().error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * peter:从Action来产生ToolTipText.
     *
     * @param action 动作
     * @return 字符
     */
    public static String createButtonToolTipText(Action action) {
        StringBuffer buttonToolTipTextBuf = new StringBuffer();

        //peter:把中文后面的(U),alt 快捷键的括号去掉,这个方法是临时的做法.
        String actionName = (String) action.getValue(Action.NAME);
        if (actionName.lastIndexOf("(") != -1) {
            buttonToolTipTextBuf.append(actionName.substring(0, actionName.lastIndexOf("(")));
        } else {
            buttonToolTipTextBuf.append(actionName);
        }

        //peter:产生快捷键的ToolTip.
        KeyStroke keyStroke = (KeyStroke) action.getValue(Action.ACCELERATOR_KEY);
        if (keyStroke != null) {
            buttonToolTipTextBuf.append(" (");
            buttonToolTipTextBuf.append(KeyEvent.getKeyModifiersText(keyStroke.getModifiers()));
            buttonToolTipTextBuf.append('+');
            buttonToolTipTextBuf.append(KeyEvent.getKeyText(keyStroke.getKeyCode()));
            buttonToolTipTextBuf.append(')');
        }

        return Utils.objectToString(buttonToolTipTextBuf);
    }

    /**
     * 纪录插入元素的种类
     *
     * @param cls 类型数组
     */
    public static void registerCellInsertActionClass(Class<?>[] cls) {
        if (cls != null) {
            Collections.addAll(actionClasses, cls);
        }
    }

    /**
     * 生成单元格插入相关的Action
     * 表单中报表块编辑需要屏蔽掉"插入子报表"
     *
     * @param cls 构造函数参数类型
     * @param obj 构造函数参数值
     * @return 相关Action组成的一个数组
     */
    public static UpdateAction[] createCellInsertAction(Class cls, Object obj) {
        List<UpdateAction> actions = new ArrayList<>();
        JTemplate jTemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
        for (Class<?> clazz : actionClasses) {
            if (clazz == null) {
                continue;
            }
            if (jTemplate.acceptToolbarItem(clazz)) {
                try {
                    Constructor<? extends UpdateAction> c = (Constructor<? extends UpdateAction>) clazz.getConstructor(cls);
                    actions.add(c.newInstance(obj));
                } catch (Exception e) {
                    FRContext.getLogger().error(e.getMessage(), e);
                }
            }
        }
        return actions.toArray(new UpdateAction[actions.size()]);
    }


    public static MenuKeySet[] createCellInsertActionName() {
        List<MenuKeySet> actionNames = new ArrayList<>();
        JTemplate jTemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
        for (Class<?> clazz : actionClasses) {
            if (clazz == null) {
                continue;
            }
            if (jTemplate.acceptToolbarItem(clazz)) {
                try {
                    Constructor<? extends UpdateAction> c = (Constructor<? extends UpdateAction>) clazz.getConstructor();
                    actionNames.add(c.newInstance().getMenuKeySet());
                } catch (Exception e) {
                    FRContext.getLogger().error(e.getMessage(), e);
                }
            }
        }
        return actionNames.toArray(new MenuKeySet[actionNames.size()]);
    }

    /**
     * 登记悬浮元素插入类型
     *
     * @param cls 插入类型数组
     */
    public static void registerFloatInsertActionClass(Class<?>[] cls) {
        if (cls != null) {
            Collections.addAll(floatActionClasses, cls);
        }
    }

    /**
     * 生成悬浮元素插入相关的Action
     *
     * @param cls 构造函数参数类型
     * @param obj 构造函数参数值
     * @return 相关Action组成的一个数组
     */
    public static UpdateAction[] createFloatInsertAction(Class cls, Object obj) {
        List<UpdateAction> actions = new ArrayList<>();
        for (Class<?> clazz : floatActionClasses) {
            if (clazz == null) {
                continue;
            }
            try {
                Constructor<? extends UpdateAction> c = (Constructor<? extends UpdateAction>) clazz.getConstructor(cls);
                actions.add(c.newInstance(obj));
            } catch (Exception e) {
                FRContext.getLogger().error(e.getMessage(), e);
            }
        }
        return actions.toArray(new UpdateAction[actions.size()]);
    }
}