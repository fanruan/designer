package com.fr.design.gui.controlpane.shortcutfactory;

import com.fr.base.BaseUtils;
import com.fr.design.actions.UpdateAction;
import com.fr.design.gui.controlpane.NameableCreator;
import com.fr.design.gui.controlpane.ShortCut4JControlPane;
import com.fr.design.gui.controlpane.ShortCutListenerProvider;
import com.fr.design.menu.ShortCut;

import java.awt.event.ActionEvent;

/**
 * 生成 ShortCut 的抽象工厂
 * Created by plough on 2018/8/2.
 */
public abstract class AbstractShortCutFactory {
    protected ShortCutListenerProvider listener;

    AbstractShortCutFactory(ShortCutListenerProvider listener) {
        setListener(listener);
    }

    /**
     * 生成一组默认的 ShortCut
     */
    abstract public ShortCut4JControlPane[] createShortCuts();

    abstract public ShortCut createAddItemUpdateAction(NameableCreator[] creator);

    abstract public ShortCut createAddItemMenuDef(NameableCreator[] creator);

    public ShortCut4JControlPane addItemShortCut() {
        ShortCut addItemShortCut;
        NameableCreator[] creators = listener.creators();
        if (creators.length == 1) {
            addItemShortCut = createAddItemUpdateAction(creators);
        } else {
            addItemShortCut = createAddItemMenuDef(creators);
        }
        return new AbsoluteEnableShortCut(addItemShortCut);
    }

    public ShortCut4JControlPane removeItemShortCut() {
        return new NormalEnableShortCut(new RemoveItemAction());
    }

    public ShortCut4JControlPane copyItemShortCut() {
        return new NormalEnableShortCut(new CopyItemAction());
    }

    public ShortCut4JControlPane moveUpItemShortCut() {
        return new NormalEnableShortCut(new MoveUpItemAction());
    }

    public ShortCut4JControlPane moveDownItemShortCut() {
        return new NormalEnableShortCut(new MoveDownItemAction());
    }

    public ShortCut4JControlPane sortItemShortCut() {
        return new NormalEnableShortCut(new SortItemAction());
    }

    public void setListener(ShortCutListenerProvider listener) {
        this.listener = listener;
    }

    private class AbsoluteEnableShortCut extends ShortCut4JControlPane {
        AbsoluteEnableShortCut(ShortCut shortCut) {
            this.shortCut = shortCut;
        }

        /**
         * 检查是否可用
         */
        @Override
        public void checkEnable() {
            this.shortCut.setEnabled(true);
        }
    }

    private class NormalEnableShortCut extends ShortCut4JControlPane {
        NormalEnableShortCut(ShortCut shortCut) {
            this.shortCut = shortCut;
        }

        /**
         * 检查是否可用
         */
        @Override
        public void checkEnable() {
            this.shortCut.setEnabled(listener.isItemSelected());
        }
    }

    /*
     * 移除item
     */
    private class RemoveItemAction extends UpdateAction {
        RemoveItemAction() {
            this.setName(com.fr.design.i18n.Toolkit.i18nText(("Fine-Design_Basic_Action_Remove")));
            this.setMnemonic('R');
            this.setSmallIcon(BaseUtils
                    .readIcon("/com/fr/base/images/cell/control/remove.png"));
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            listener.onRemoveItem();
        }
    }

    /*
     * CopyItem
     */
    private class CopyItemAction extends UpdateAction {
        CopyItemAction() {
            this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Action_Copy"));
            this.setMnemonic('C');
            this.setSmallIcon(BaseUtils
                    .readIcon("/com/fr/design/images/m_edit/copy.png"));
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            listener.onCopyItem();
        }
    }

    /*
     * 上移Item
     */
    private class MoveUpItemAction extends UpdateAction {
        MoveUpItemAction() {
            this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Utils_Move_Up"));
            this.setMnemonic('U');
            this.setSmallIcon(BaseUtils
                    .readIcon("/com/fr/design/images/control/up.png"));
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            listener.onMoveUpItem();
        }
    }

    /*
     * 下移Item
     */
    private class MoveDownItemAction extends UpdateAction {
        MoveDownItemAction() {
            this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Utils_Move_Down"));
            this.setMnemonic('D');
            this.setSmallIcon(BaseUtils
                    .readIcon("/com/fr/design/images/control/down.png"));
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            listener.onMoveDownItem();
        }
    }

    private class SortItemAction extends UpdateAction {
        private boolean isAtoZ = false;

        SortItemAction() {
            this.setName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Action_Sort"));
            this.setMnemonic('S');
            this.setSmallIcon(BaseUtils
                    .readIcon("/com/fr/design/images/control/sortAsc.png"));
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            listener.onSortItem(isAtoZ);
        }
    }
}
