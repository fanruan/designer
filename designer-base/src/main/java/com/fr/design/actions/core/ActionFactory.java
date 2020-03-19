package com.fr.design.actions.core;

import com.fr.base.Utils;
import com.fr.design.actions.UpdateAction;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.menu.MenuKeySet;
import com.fr.design.selection.QuickEditor;
import com.fr.log.FineLoggerFactory;

import javax.swing.Action;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 插入单元格元素和插入悬浮元素的一些集合方法
 *
 * @author null
 * @version 2017年11月17日14点39分
 */
public class ActionFactory {
    private static LinkedHashSet<Class<?>> actionClasses = new LinkedHashSet<>();
    private static LinkedHashSet<Class<?>> floatActionClasses = new LinkedHashSet<>();
    private static Class chartCollectionClass = null;
    /**
     * 无需每次实例化的悬浮元素编辑器
     */
    private static ConcurrentMap<Class, QuickEditor> floatEditor = new ConcurrentHashMap<>();
    /**
     * 无需每次实例化的单元格元素编辑器
     */
    private static ConcurrentMap<Class, QuickEditor> cellEditor = new ConcurrentHashMap<>();

    /**
     * 需要每次实例化的悬浮元素编辑器
     */
    private static ConcurrentMap<Class, Class<? extends QuickEditor>> floatEditorClass = new ConcurrentHashMap<>();
    /**
     * 需要每次实例化的单元格元素编辑器
     */
    private static ConcurrentMap<Class, Class<? extends QuickEditor>> cellEditorClass = new ConcurrentHashMap<>();

    private static UpdateAction chartPreStyleAction = null;
    private static UpdateAction chartEmptyDataStyleAction = null;
    private static UpdateAction chartMapEditorAction = null;

    private ActionFactory() {
    }


    /**
     * 元素编辑器释放模板对象
     */
    public static void editorRelease() {
        for (Map.Entry<Class, QuickEditor> entry : cellEditor.entrySet()) {
            entry.getValue().release();
        }
        for (Map.Entry<Class, QuickEditor> entry : floatEditor.entrySet()) {
            entry.getValue().release();
        }
    }


    /**
     * 注册无需每次实例化的单元格元素编辑器
     *
     * @param clazz  单元格属性类型
     * @param editor 单元格编辑器实例
     */
    public static void registerCellEditor(Class clazz, QuickEditor editor) {
        cellEditor.put(clazz, editor);
    }


    /**
     * 注册无需每次实例化的悬浮元素编辑器
     *
     * @param clazz  悬浮元素类型
     * @param editor 悬浮元素编辑器实例
     */
    public static void registerFloatEditor(Class clazz, QuickEditor editor) {
        floatEditor.put(clazz, editor);
    }

    /**
     * 注册需要每次实例化的单元格元素编辑器
     *
     * @param clazz       单元格属性类型
     * @param editorClass 单元格编辑器类
     */
    public static void registerCellEditorClass(Class clazz, Class<? extends QuickEditor> editorClass) {
        cellEditorClass.put(clazz, editorClass);
    }


    /**
     * 注册需要每次实例化的悬浮元素编辑器
     *
     * @param clazz       悬浮元素类型
     * @param editorClass 悬浮元素编辑器类
     */
    public static void registerFloatEditorClass(Class clazz, Class<? extends QuickEditor> editorClass) {
        floatEditorClass.put(clazz, editorClass);
    }


    /**
     * kunsnat: 图表注册 悬浮元素编辑器 , 因为ChartCollecion和ChartQuickEditor一个在Chart,一个在Designer, 所以分开注册.
     *
     * @param clazz 待说明
     */
    public static void registerChartCollection(Class clazz) {
        chartCollectionClass = clazz;
    }

    /**
     * 注册图表的 地图资源
     *
     * @param action 地图资源action
     */
    public static void registerChartMapEditorAction(UpdateAction action) {
        chartMapEditorAction = action;
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
     * 图表预定义样式Action
     *
     * @return 图表预定义样式Action
     */
    public static UpdateAction getChartPreStyleAction() {
        return chartPreStyleAction;
    }


    /**
     * 图表编辑器Action
     *
     * @return 图表编辑器Action
     */
    public static UpdateAction getChartMapEditorAction() {
        return chartMapEditorAction;
    }

    /**
     * 注册图表的 空数据提示样式.
     *
     * @param action 注册的图表空数据提示样式action
     */
    public static void registerChartEmptyDataStyleAction(UpdateAction action) {
        chartEmptyDataStyleAction = action;
    }

    /**
     * 图表空数据提示样式Action
     *
     * @return 图表空数据提示样式Action
     */
    public static UpdateAction getChartEmptyDataStyleAction() {
        return chartEmptyDataStyleAction;
    }


    /**
     * 获取图表集合类
     *
     * @return 获取图表集合类
     */
    public static Class getChartCollectionClass() {
        return chartCollectionClass;
    }


    /**
     * 选中的悬浮元素的编辑器
     *
     * @param clazz 选中的悬浮元素类型
     * @return 编辑器实例
     */
    public static QuickEditor getFloatEditor(Class clazz) {
        return createEditor(clazz, floatEditor, floatEditorClass);
    }

    /**
     * 选中的单元格元素编辑器
     *
     * @param clazz 选中的单元格元素类型
     * @return 编辑器实例
     */
    public static QuickEditor getCellEditor(Class clazz) {
        return createEditor(clazz, cellEditor, cellEditorClass);
    }

    public static UpdateAction createAction(Class clazz) {
        try {
            Constructor<? extends UpdateAction> c = clazz.getDeclaredConstructor();
            c.setAccessible(true);
            return c.newInstance();
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
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

    public static void referCellInsertActionClass(Class<?>[] cls) {
        if (cls != null) {
            actionClasses.clear();
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
                    FineLoggerFactory.getLogger().error(e.getMessage(), e);
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
                    FineLoggerFactory.getLogger().error(e.getMessage(), e);
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

    public static void referFloatInsertActionClass(Class<?>[] cls) {
        if (cls != null) {
            floatActionClasses.clear();
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
                FineLoggerFactory.getLogger().error(e.getMessage(), e);
            }
        }
        return actions.toArray(new UpdateAction[actions.size()]);
    }

    private static QuickEditor createEditor(Class clazz, Map<Class, QuickEditor> editorMap, Map<Class, Class<? extends QuickEditor>> editorClassMap) {
        QuickEditor c = findQuickEditor(clazz, editorMap);
        if (c == null) {
            Class<? extends QuickEditor> cClazz = findQuickEditorClass(clazz, editorClassMap);
            if (cClazz == null) {
                FineLoggerFactory.getLogger().error("No Such Editor");
                return null;
            }
            try {
                Constructor<? extends QuickEditor> constructor = cClazz.getDeclaredConstructor();
                constructor.setAccessible(true);
                return constructor.newInstance();
            } catch (Exception e) {
                FineLoggerFactory.getLogger().error(e.getMessage(), e);
            }
            return null;
        }
        return c;
    }

    private static QuickEditor findQuickEditor(Class clazz, Map<Class, QuickEditor> editorMap) {
        QuickEditor c = editorMap.get(clazz);
        if (c == null) {
            Class superClazz = clazz.getSuperclass();
            if (superClazz == null) {
                return null;
            }
            return findQuickEditor(superClazz, editorMap);
        }
        return c;
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
}