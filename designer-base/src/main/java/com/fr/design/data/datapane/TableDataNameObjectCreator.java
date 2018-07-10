package com.fr.design.data.datapane;

import com.fr.base.BaseUtils;
import com.fr.base.FRContext;
import com.fr.data.impl.DBTableData;
import com.fr.design.actions.tabledata.TableDataAction;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.controlpane.NameObjectCreator;
import com.fr.design.icon.WarningIcon;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.text.Collator;
import java.util.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 6.5.6
 * Date: 13-12-20
 * Time: 上午9:55
 */
public class TableDataNameObjectCreator extends NameObjectCreator {
    private boolean b = false;
    private Image menuImage;
    private String iconPath;
    private boolean isNeedInsertSeparator = false;
    private String prefix = "";
    private java.util.List<String> names = new ArrayList<String>();

    public TableDataNameObjectCreator(TableDataAction action) {
        super(action.getDisplayName(), action.getIconPath(), action.getTableDataClass(), action.getUpdateTableDataPaneClass());
        this.iconPath = action.getIconPath();
        menuImage = BaseUtils.readImage(iconPath);
        isNeedInsertSeparator = action.isNeedInsertSeparator();
        prefix = action.getPrefix();
    }

    public TableDataNameObjectCreator(TableDataAction action, Class clazz4Init) {
        super(action.getDisplayName(), action.getIconPath(), action.getTableDataClass(), clazz4Init, action.getUpdateTableDataPaneClass());
        isNeedInsertSeparator = action.isNeedInsertSeparator();
        prefix = action.getPrefix();
    }

    public TableDataNameObjectCreator(String menuName, String iconPath, Class clazz, Class<? extends BasicBeanPane> updatePane) {
        super(menuName, iconPath, clazz, updatePane);
        if (iconPath != null) {
            this.iconPath = iconPath;
            menuImage = BaseUtils.readImage(iconPath);
        }
    }

    public TableDataNameObjectCreator(String menuName, String prefix, String iconPath, Class clazz, Class<? extends BasicBeanPane> updatePane) {
        super(menuName, iconPath, clazz, updatePane);
        if (iconPath != null) {
            this.iconPath = iconPath;
            menuImage = BaseUtils.readImage(iconPath);
        }
        this.prefix = prefix;
    }

    public TableDataNameObjectCreator(String menuName, String iconPath, Class clazz, Class clazz4Init, Class<? extends BasicBeanPane> updatePane) {
        super(menuName, iconPath, clazz, clazz4Init, updatePane);
        if (iconPath != null) {
            this.iconPath = iconPath;
            menuImage = BaseUtils.readImage(iconPath);
        }
    }

    public TableDataNameObjectCreator(String menuName, String prefix, String iconPath, Class clazz, Class clazz4Init, Class<? extends BasicBeanPane> updatePane) {
        super(menuName, iconPath, clazz, clazz4Init, updatePane);
        if (iconPath != null) {
            this.iconPath = iconPath;
            menuImage = BaseUtils.readImage(iconPath);
        }
        this.prefix = prefix;
    }


    @Override
    protected void doSthChanged4Icon(Object ob) {
        if (ob instanceof DBTableData) {
            DBTableData db = (DBTableData) ob;
            b = StringUtils.isBlank(db.getQuery());
        }
    }

    /**
     * 取得菜单的图片
     *
     * @return 图片
     */
    public Icon menuIcon() {
        return createMenuIcon();
    }

    public String getIconPath() {
        return iconPath;
    }

    private Icon createMenuIcon() {
        return b ? new WarningIcon(this.menuImage) : this.menuIcon;
    }

    /**
     * 创建提示
     *
     * @return 提示
     */
    public String createTooltip() {
        return b ? Inter.getLocText("Connect_SQL_Cannot_Null") : null;
    }

    public String getPrefix() {
        return StringUtils.isNotBlank(prefix) ? prefix : menuName();
    }

    /**
     * 将names排序
     *
     * @return
     */
    public List<String> getNames() {
        String[] namearray = names.toArray(ArrayUtils.EMPTY_STRING_ARRAY);
        Arrays.sort(namearray, Collator.getInstance(java.util.Locale.CHINA));
        return Arrays.asList(namearray);
    }

    public Object createObject() {
        try {
            return clazzOfObject.newInstance();
        } catch (InstantiationException e) {
            try {
                return clazzOfInitCase.newInstance();
            } catch (InstantiationException | IllegalAccessException e1) {
                FRContext.getLogger().error(e.getMessage(), e);
            }
        } catch (IllegalAccessException e) {
            FRContext.getLogger().error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 加入名字
     *
     * @param names 名字
     */
    public void addNames(String names) {
        this.names.add(names);
    }

    /**
     * 清除
     */
    public void clear() {
        this.names.clear();
    }

    public boolean shouldInsertSeparator() {
        return false;
    }

    public boolean equals(Object obj) {
        return obj instanceof TableDataNameObjectCreator
                && ComparatorUtils.equals(menuName, ((TableDataNameObjectCreator) obj).menuName);
    }

    @Override
    public int hashCode() {
        return menuName == null ? 0 : menuName.hashCode();
    }
}