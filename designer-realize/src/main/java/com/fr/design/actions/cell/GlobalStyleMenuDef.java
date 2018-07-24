package com.fr.design.actions.cell;

/**
 * richer:global style menu
 */

import com.fr.base.BaseUtils;
import com.fr.base.NameStyle;
import com.fr.config.ServerPreferenceConfig;
import com.fr.design.actions.SelectionListenerAction;
import com.fr.design.actions.UpdateAction;
import com.fr.design.gui.imenu.UIMenu;
import com.fr.design.mainframe.CellElementPropertyPane;
import com.fr.design.mainframe.ElementCasePane;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.menu.MenuDef;
import com.fr.design.selection.SelectionListener;
import com.fr.design.style.StylePane;

import com.fr.stable.StringUtils;
import com.fr.stable.pinyin.PinyinHelper;

import java.awt.*;
import java.awt.event.*;
import java.util.Iterator;

public class GlobalStyleMenuDef extends MenuDef {
    private static final int MAX_LENTH = 12;
    private ElementCasePane ePane;

    public GlobalStyleMenuDef(ElementCasePane ePane) {
        this.ePane = ePane;
        this.setMenuKeySet(KeySetUtils.GLOBAL_STYLE);
        this.setName(getMenuKeySet().getMenuKeySetName());
        this.setMnemonic(getMenuKeySet().getMnemonic());
        this.setIconPath("/com/fr/design/images/m_web/style.png");
    }

    protected ContainerListener getContainerListener() {
        return  containerListener;
    }

    private ContainerListener containerListener = new ContainerListener() {
        @Override
        public void componentAdded(ContainerEvent e) {

        }

        @Override
        public void componentRemoved(ContainerEvent e) {
            Component c = e.getChild();
            c.dispatchEvent(new MenuDeleteEvent(c));
        }
    };

    private class MenuDeleteEvent extends UpdateAction.ComponentRemoveEvent {

        public MenuDeleteEvent(Component source) {
            super(source);
        }

        @Override
        public void release(SelectionListener listener) {
            ePane.removeSelectionChangeListener(listener);
        }
    }


    /**
     * 更新菜单项
     */
    public void updateMenu() {
        UIMenu createdMenu = this.createJMenu();
        createdMenu.removeAll();
        Iterator iterator = ServerPreferenceConfig.getInstance().getStyleNameIterator();
        while (iterator.hasNext()) {
            String name = (String) iterator.next();
            NameStyle nameStyle = NameStyle.getInstance(name);
            UpdateAction.UseMenuItem useMenuItem =new GlobalStyleSelection(ePane, nameStyle).createUseMenuItem();
            useMenuItem.setNameStyle(nameStyle);
            createdMenu.add(useMenuItem);
        }
        createdMenu.addSeparator();
        createdMenu.add(new CustomStyleAction(com.fr.design.i18n.Toolkit.i18nText("FR-Engine_Custom")));
    }

    /**
     * 控制自定义样式名称的长度
     *
     * @param longName 名字
     * @return 控制之后的名字
     */
    public static String judgeChina(String longName) {

        //neil:bug 1623 控制自定义样式名称的长度，只显示前12个字符，每个英文算1个字符，每个汉字算2个字符
        Integer index = 0;
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < longName.length(); i++) {
            String bb = longName.substring(i, i + 1); //生成一个Pattern,同时编译一个正则表达式
            boolean cc = PinyinHelper.isChinese(bb.charAt(0));
            if (index == MAX_LENTH) {
                sBuffer.append("..");
                break;
            }
            if ((index == MAX_LENTH - 1 && cc)) {
                continue;
            }
            if (cc) {
                index = index + 2;
            } else {
                index = index + 1;
            }

            sBuffer.append(bb);
            if (index > MAX_LENTH) {
                sBuffer.append("..");
                break;
            }

        }

        return sBuffer.toString();

    }

    public static class CustomStyleAction extends UpdateAction {

        public CustomStyleAction(String name) {

            this.setName(name);
            this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_format/cell.png"));
        }

        /**
         * 动作
         *
         * @param e 事件
         */
        public void actionPerformed(ActionEvent e) {
            CellElementPropertyPane.getInstance().GoToPane(new String[]{com.fr.design.i18n.Toolkit.i18nText("FR-Engine_Style"), com.fr.design.i18n.Toolkit.i18nText("FR-Engine_Custom")});
        }

    }



    public static class GlobalStyleSelection extends SelectionListenerAction {

        private NameStyle nameStyle;

        public GlobalStyleSelection(ElementCasePane t, NameStyle nameStyle) {
            super(t);
            setName(StringUtils.EMPTY);
            //重复画了一次，不需要icon
//        	this.setName(nameStyle == null ? "" : nameStyle.getName());
//        	this.setSmallIcon(BaseUtils.readIcon("/com/fr/design/images/m_format/cell.png"));
            this.setNameStyle(nameStyle);
        }

        public NameStyle getNameStyle() {
            return this.nameStyle;
        }

        public void setNameStyle(NameStyle nameStyle) {
            this.nameStyle = nameStyle;
        }

        /**
         * 执行动作返回
         * @return 是返回true
         */
        public boolean executeActionReturnUndoRecordNeeded() {
            StylePane stylePane = new StylePane();
            if (StringUtils.isEmpty(this.getName())) {
                stylePane.setGlobalStyle(this.getNameStyle());
            } else {
                stylePane.setGlobalStyle(NameStyle.getInstance(this.getName()));
            }

            stylePane.updateGlobalStyle(getEditingComponent());
            CellElementPropertyPane.getInstance().GoToPane(com.fr.design.i18n.Toolkit.i18nText("FR-Designer-Widget_Style"));
            return true;
        }

        public UseMenuItem createUseMenuItem() {
            UseMenuItem useMenuItem = super.createUseMenuItem();
            SelectionListener listener = createSelectionListener();
            getEditingComponent().addSelectionChangeListener(listener);
            useMenuItem.setSelectionListener(listener);
            return useMenuItem;
        }

    }
}